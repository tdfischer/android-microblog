package net.wm161.microblog.lib.backends.statusnet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map.Entry;

import net.wm161.microblog.lib.APIConfiguration;
import net.wm161.microblog.lib.APIException;
import net.wm161.microblog.lib.APIProgress;
import net.wm161.microblog.lib.APIRequest;
import net.wm161.microblog.lib.Attachment;
import net.wm161.microblog.lib.APIRequest.ErrorType;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import android.util.Log;

public class HTTPAPIRequest {
	
	protected Statusnet m_api;
	private HashMap<String, Object> m_params;
	private int m_status;
	private APIRequest m_request;

	public HTTPAPIRequest(Statusnet api, APIRequest req) {
		super();
		m_api = api;
		m_request = req;
		m_params = new HashMap<String, Object>();
	}
	
	protected APIRequest getRequest() {
		return m_request;
	}
	
	public void setParameter(String key, Object param) {
		m_params.put(key, param);
	}
	
	protected void setError(ErrorType type) {
		m_request.setError(type);
	}
	
	protected void publishProgress(APIProgress progress) {
		m_request.publishProgress(progress);
	}
	
	HttpRequestInterceptor preemptiveAuth = new HttpRequestInterceptor() {

		@Override
		public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
			AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);
	        CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(
	                ClientContext.CREDS_PROVIDER);
	        HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
	        
	        if (authState.getAuthScheme() == null) {
	            AuthScope authScope = new AuthScope(targetHost.getHostName(), targetHost.getPort());
	            Credentials creds = credsProvider.getCredentials(authScope);
	            if (creds != null) {
	                authState.setAuthScheme(new BasicScheme());
	                authState.setCredentials(creds);
	            }
	        }
		}
	};
	
	public String getData(String path) throws APIException {
		APIConfiguration conf = m_api.configuration();
		String server = (String) conf.value("server");
		String apiPath = (String) conf.value("path");
		Boolean https = (Boolean) conf.value("https");
		String scheme = (https) ? "https" : "http";
		
		URI location = URI.create(scheme+"://"+server+"/"+apiPath+"/"+path+".json");
		return getData(location);
	}
	
	protected String getData(URI location) throws APIException {
		Log.d("HTTPAPIRequest", "Downloading "+location);
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(location);
		client.addRequestInterceptor(preemptiveAuth, 0);
		
		client.getParams().setBooleanParameter("http.protocol.expect-continue", false); 
		
		if (!m_params.isEmpty()) {
			MultipartEntity params = new MultipartEntity();
			for (Entry<String, Object> item : m_params.entrySet()) {
				Object value = item.getValue();
				ContentBody data;
				if (value instanceof Attachment) {
					Attachment attachment = (Attachment) value;
					try {
						data = new InputStreamBody(attachment.getStream(), attachment.contentType(), attachment.name());
						Log.d("HTTPAPIRequest", "Found a "+attachment.contentType()+" attachment named "+attachment.name());
					} catch (FileNotFoundException e) {
						getRequest().setError(ErrorType.ERROR_ATTACHMENT_NOT_FOUND);
						throw new APIException();
					} catch (IOException e) {
						getRequest().setError(ErrorType.ERROR_ATTACHMENT_NOT_FOUND);
						throw new APIException();
					}
				} else {
					try {
						data = new StringBody(value.toString());
					} catch (UnsupportedEncodingException e) {
						getRequest().setError(ErrorType.ERROR_INTERNAL);
						throw new APIException();
					}
				}
				params.addPart(item.getKey(), data);
			}
			post.setEntity(params);
		}
		
		UsernamePasswordCredentials creds = new UsernamePasswordCredentials(m_api.getAccount().getUser(), m_api.getAccount().getPassword());
		client.getCredentialsProvider().setCredentials(AuthScope.ANY, creds);
		
		HttpResponse req;
		try {
			req = client.execute(post);
		} catch (ClientProtocolException e3) {
			getRequest().setError(ErrorType.ERROR_CONNECTION_BROKEN);
			throw new APIException();
		} catch (IOException e3) {
			getRequest().setError(ErrorType.ERROR_CONNECTION_FAILED);
			throw new APIException();
		}
		
		InputStream result;
		try {
			result = req.getEntity().getContent();
		} catch (IllegalStateException e1) {
			getRequest().setError(ErrorType.ERROR_INTERNAL);
			throw new APIException();
		} catch (IOException e1) {
			getRequest().setError(ErrorType.ERROR_CONNECTION_BROKEN);
			throw new APIException();
		}
		
		InputStreamReader in = null;
		int status;
		in = new InputStreamReader(result);
		status = req.getStatusLine().getStatusCode();
		Log.d("HTTPAPIRequest", "Got status code of "+status);
		setStatusCode(status);
		if (status >= 300 || status < 200) {
			getRequest().setError(ErrorType.ERROR_SERVER);
			Log.w("HTTPAPIRequest", "Server code wasn't 2xx, got "+m_status);
			throw new APIException();
		}
		
		int totalSize = -1;
		if (req.containsHeader("Content-length"))
			totalSize = Integer.parseInt(req.getFirstHeader("Content-length").getValue());
		
		char[] buffer = new char[1024];
		//2^17 = 131072.
		StringBuilder contents = new StringBuilder(131072);
		try {
			int size = 0;
			while ((totalSize > 0 && size < totalSize) || totalSize == -1) {
				int readSize = in.read(buffer);
				size+=readSize;
				if (readSize == -1)
					break;
				if (totalSize>=0)
					getRequest().publishProgress(new APIProgress((size/totalSize)*5000));
				contents.append(buffer, 0, readSize);
			}
		} catch (IOException e) {
			getRequest().setError(ErrorType.ERROR_CONNECTION_BROKEN);
			throw new APIException();
		}
		return contents.toString();
	}

	private void setStatusCode(int status) {
		m_status = status;
	}
	
	public int getStatusCode() {
		return m_status;
	}

}
