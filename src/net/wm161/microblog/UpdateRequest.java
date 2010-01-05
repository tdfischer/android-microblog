package net.wm161.microblog;

import android.app.Activity;

public class UpdateRequest extends APIRequest {

	private String m_status;
	public UpdateRequest(Account account, Activity activity, String status) {
		super(account, activity);
		m_status = status;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			setParameter("status", m_status);
			getData("statuses/update");
		} catch (APIException e) {
			return false;
		}
		return true;
	}
}
