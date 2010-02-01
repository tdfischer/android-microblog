package net.wm161.microblog;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map.Entry;

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
		ERROR_INTERNAL
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
		URL url;
		try {
			url = new URL(m_account.getBase()+"/"+path+".json");
		} catch (MalformedURLException e) {
			setError(ErrorType.ERROR_INTERNAL);
			throw new APIException();
		}
		
		return getData(url);
	}
	
	protected String getData(URL location) throws APIException {
		Log.d("APIRequest", "Downloading "+location);
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) location.openConnection();
			connection.setAllowUserInteraction(true);
		} catch (IOException e) {
			setError(ErrorType.ERROR_CONNECTION_FAILED);
			throw new APIException();
		}
		connection.setUseCaches(true);
		
		String auth = m_account.getUser()+":"+m_account.getPassword();
		String encoding = Base64.encodeBytes(auth.getBytes());
		connection.setRequestProperty("Authorization", "Basic " + encoding);
		connection.setDoInput(true);
		if (!m_params.isEmpty()) {
			try {
				connection.setRequestMethod("POST");
			} catch (ProtocolException e2) {
				setError(ErrorType.ERROR_INTERNAL);
				throw new APIException();
			}
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
			OutputStreamWriter out = null;
			try {
				out = new OutputStreamWriter(connection.getOutputStream());
			} catch (IOException e1) {
				setError(ErrorType.ERROR_CONNECTION_FAILED);
				throw new APIException();
			}
			int last = m_params.size();
			int num = 0;
			for (Entry<String, Object> item : m_params.entrySet()) {
				num++;
				try {
					Object value = item.getValue();
					out.write(item.getKey()+"=");
					if (value != null)
						out.write(URLEncoder.encode(value.toString()));
					if (num != last)
						out.write("&");
				} catch (IOException e) {
					setError(ErrorType.ERROR_CONNECTION_BROKEN);
					throw new APIException();
				}
			}
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				setError(ErrorType.ERROR_CONNECTION_BROKEN);
				throw new APIException();
			}
		}
		InputStreamReader in = null;
		int status;
		try {
			connection.connect();
			in = new InputStreamReader(connection.getInputStream());
			status = connection.getResponseCode();
			setStatusCode(status);
		} catch (IOException e) {
			setError(ErrorType.ERROR_CONNECTION_FAILED);
			throw new APIException();
		}
		if (status >= 300 || status < 200) {
			setError(ErrorType.ERROR_SERVER);
			Log.w("APIRequest", "Server code wasn't 2xx, got "+m_status);
			throw new APIException();
		}
		
		int totalSize = connection.getContentLength();
		
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
