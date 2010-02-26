package net.wm161.microblog.lib;

import java.net.MalformedURLException;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public abstract class StatusListRequest extends APIRequest {
	DataCache<Long, net.wm161.microblog.lib.Status> m_cache;
	
	public StatusListRequest(Account account, ProgressHandler progress, DataCache<Long, net.wm161.microblog.lib.Status> cache) {
		super(account, progress);
		assert(cache != null);
		m_cache = cache;
	}
	
	protected void onPostExecute(Boolean success) {
		super.onPostExecute(success);
	}

	protected boolean getStatuses(String path) throws APIException, MalformedURLException {
		Long biggest = m_cache.biggestKey();
		if (biggest != null)
			setParameter("since_id", m_cache.biggestKey());
		JSONArray data = null;
		String strdata = getData(path);
		try {
			data = new JSONArray(strdata);
		} catch (JSONException e) {
			Log.e("StatusListRequest", "Parse error: "+e.getMessage());
			Log.e("StatusListRequest", "Original data: "+strdata);
			setError(ErrorType.ERROR_PARSE);
			throw new APIException();
		}
		//m_statuses.clear();
		Integer i;
		for(i=0;i<data.length();i++) {
			try {
				Float progress = 5000+(5000)*(i.floatValue()/(float)data.length());
				JSONObject dent = data.getJSONObject(i);
				long id = dent.getLong("id");
				net.wm161.microblog.lib.Status status = null;
				try {
					status = m_cache.get(id);
				} catch (ClassCastException e) {
				}
				if (status == null) {
					status = new net.wm161.microblog.lib.Status(dent);
					m_cache.put(id, status);
				}
				publishProgress(new Progress(progress.intValue(), status));
			} catch (JSONException e) {
				Log.w("StatusListRequest", "JSONException while listing statuses: "+e.getMessage());
			}
		}
		return true;
	}
	
	private class Progress extends APIProgress {
		private net.wm161.microblog.lib.Status m_status;

		public Progress(Integer progress, net.wm161.microblog.lib.Status status) {
			super(progress);
			m_status = status;
		}
	}

	@Override
	protected void onProgressUpdate(APIProgress... progress) {
		super.onProgressUpdate(progress);
		if (progress[0] instanceof Progress) {
			net.wm161.microblog.lib.Status status = ((Progress)progress[0]).m_status;
			onNewStatus(status);
		}
	}
	
	public abstract void onNewStatus(net.wm161.microblog.lib.Status s); 

}
