package net.wm161.microblog;

import android.app.Activity;

public class DestroyFavoriteRequest extends APIRequest {

	private Object m_id;

	public DestroyFavoriteRequest(Account account, Activity activity, long id) {
		super(account, activity);
		m_id = id;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			setParameter("id", m_id);
			getData("/favorites/destroy");
		} catch (APIException e) {
			return false;
		}
		return true;
	}
}
