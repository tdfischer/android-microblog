package net.wm161.microblog.lib;



import org.json.JSONException;
import org.json.JSONObject;

public class StatusRequest extends APIRequest {

	private long m_id;
	private net.wm161.microblog.lib.Status m_status;
	private DataCache<Long, net.wm161.microblog.lib.Status> m_cache;
	
	public StatusRequest(Account account, ProgressHandler progress, DataCache<Long, net.wm161.microblog.lib.Status> cache, long id) {
		super(account, progress);
		m_id = id;
		m_cache = cache;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			try {
				m_status = m_cache.get(m_id);
			} catch (ClassCastException e) {
				m_status = null;
			}
			if (m_status == null) {
				m_status = new net.wm161.microblog.lib.Status(new JSONObject(getData("/statuses/show/"+m_id)));
				m_cache.put(m_id, m_status);
			}
		} catch (JSONException e) {
			setError(ErrorType.ERROR_PARSE);
			return false;
		} catch (APIException e) {
			return false;
		}
		return true;
	}
	
	public net.wm161.microblog.lib.Status status() {
		return m_status;
	}

}
