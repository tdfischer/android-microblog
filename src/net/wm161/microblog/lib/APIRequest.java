package net.wm161.microblog.lib;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map.Entry;



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

import android.os.AsyncTask;
import android.util.Log;

public abstract class APIRequest extends AsyncTask<Void, APIProgress, Boolean> {
	
	protected Account m_account;
	private ErrorType m_error;
	private ProgressHandler m_progress;
	private HashMap<String, Object> m_params;
	private int m_status;
	public class APIException extends Exception {
		private static final long serialVersionUID = -4526739499995112944L;
	}
	
	public enum ErrorType {
		ERROR_NONE,
		ERROR_CONNECTION_FAILED,
		ERROR_CONNECTION_BROKEN,
		ERROR_PARSE,
		ERROR_SERVER,
		ERROR_INTERNAL,
		ERROR_ATTACHMENT_NOT_FOUND
	}
	
	public APIRequest(Account account, ProgressHandler activity) {
		super();
		m_account = account;
		m_error = ErrorType.ERROR_NONE;
		m_progress = activity;
		m_params = new HashMap<String, Object>();
	}
	
	public void setParameter(String key, Object param) {
		m_params.put(key, param);
	}
	
	protected void setError(ErrorType e) {
		m_error = e;
	}
	
	public ErrorType getError() {
		return m_error;
	}
	
	public String getErrorString(ErrorType e) {
		switch (e) {
		case ERROR_NONE:
			return "No Error.";
		case ERROR_CONNECTION_FAILED:
			return "Connection failed.";
		case ERROR_CONNECTION_BROKEN:
			return "Connection interrupted.";
		case ERROR_PARSE:
			return "Recieved unintelligible gibberish.";
		case ERROR_SERVER:
			return "Server error: "+m_status;
		case ERROR_INTERNAL:
			return "Internal error. Please send a bug report.";
		case ERROR_ATTACHMENT_NOT_FOUND:
			return "Attachment not found.";
		default:
			return null;
		}
	}
	
	@Override
	protected void onPreExecute() {
		m_progress.starting();
	}
	
	@Override
	protected void onProgressUpdate(APIProgress... progress) {
		m_progress.updated(progress[0]);
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		if (!result) {
			Log.w("APIRequest", "Execution failed: "+getError()+" ("+getStatusCode()+")");
			m_progress.error(getError(), getErrorString(getError()));
		}
		m_progress.finished();
	}
	
	protected String getData(String path) throws APIException {
		URI uri;
		try {
			uri = new URI(m_account.getBase()+"/"+path+".json");
		} catch (URISyntaxException e) {
			setError(ErrorType.ERROR_INTERNAL);
			throw new APIException();
		}
		
		return getData(uri);
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
	
	protected String getData(URI location) throws APIException {
		Log.d("APIRequest", "Downloading "+location);
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(location);
		client.addRequestInterceptor(preemptiveAuth, 0);
		
		client.getParams().setBooleanParameter("http.protocol.expect-continue", false); 
		
		if (!m_params.isEmpty()) {
			MultipartEntity params = new MultipartEntity();
			for (Entry<String, Object> item : m_params.entrySet()) {
				Object value = item.getValue();
				ContentBody data;
				if (value instanceof FileAttachment) {
					FileAttachment attachment = (FileAttachment) value;
					try {
						data = new InputStreamBody(attachment.getStream(), attachment.contentType(), attachment.name());
						Log.d("APIRequest", "Found a "+attachment.contentType()+" attachment named "+attachment.name());
					} catch (FileNotFoundException e) {
						setError(ErrorType.ERROR_ATTACHMENT_NOT_FOUND);
						throw new APIException();
					}
				} else {
					try {
						data = new StringBody(value.toString());
					} catch (UnsupportedEncodingException e) {
						setError(ErrorType.ERROR_INTERNAL);
						throw new APIException();
					}
				}
				params.addPart(item.getKey(), data);
			}
			post.setEntity(params);
		}
		
		UsernamePasswordCredentials creds = new UsernamePasswordCredentials(m_account.getUser(), m_account.getPassword());
		client.getCredentialsProvider().setCredentials(AuthScope.ANY, creds);
		
		HttpResponse req;
		try {
			req = client.execute(post);
		} catch (ClientProtocolException e3) {
			setError(ErrorType.ERROR_CONNECTION_BROKEN);
			throw new APIException();
		} catch (IOException e3) {
			setError(ErrorType.ERROR_CONNECTION_FAILED);
			throw new APIException();
		}
		
		InputStream result;
		try {
			result = req.getEntity().getContent();
		} catch (IllegalStateException e1) {
			setError(ErrorType.ERROR_INTERNAL);
			throw new APIException();
		} catch (IOException e1) {
			setError(ErrorType.ERROR_CONNECTION_BROKEN);
			throw new APIException();
		}
		
		InputStreamReader in = null;
		int status;
		in = new InputStreamReader(result);
		status = req.getStatusLine().getStatusCode();
		Log.d("APIRequest", "Got status code of "+status);
		setStatusCode(status);
		if (status >= 300 || status < 200) {
			setError(ErrorType.ERROR_SERVER);
			Log.w("APIRequest", "Server code wasn't 2xx, got "+m_status);
			throw new APIException();
		}
		
		int totalSize = -1;
		if (req.containsHeader("Content-length"))
			totalSize = Integer.parseInt(req.getFirstHeader("Content-length").getValue());
		
		char[] buffer = new char[1024];
		StringBuilder contents = new StringBuilder();
		try {
			int size = 0;
			while ((totalSize > 0 && size < totalSize) || totalSize == -1) {
				int readSize = in.read(buffer);
				size+=readSize;
				if (readSize == -1)
					break;
				if (totalSize>=0)
					publishProgress(new APIProgress((size/totalSize)*5000));
				contents.append(buffer, 0, readSize);
			}
		} catch (IOException e) {
			setError(ErrorType.ERROR_CONNECTION_BROKEN);
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
