package net.wm161.microblog;

import org.json.JSONException;
import org.json.JSONObject;

public class UserRequest extends APIRequest {
	private String m_user;
	private User m_result;

	public UserRequest(Account account, ProgressHandler progress, String user) {
		super(account, progress);
		m_user = user;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		JSONObject user = null;
		try {
			user = new JSONObject(getData("/users/show/"+m_user));
		} catch (JSONException e) {
			setError(ErrorType.ERROR_PARSE);
			return false;
		} catch (APIException e) {
			return false;
		}
		try {
			m_result = new User(user);
		} catch (JSONException e) {
			setError(ErrorType.ERROR_PARSE);
			return false;
		}
		return true;
	}
	
	public User getUser() {
		return m_result;
	}

}
