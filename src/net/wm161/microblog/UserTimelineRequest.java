package net.wm161.microblog;

import java.net.MalformedURLException;

import android.util.Log;

public abstract class UserTimelineRequest extends StatusListRequest {
	
	private String m_user;
		
	public UserTimelineRequest(Account account, ProgressHandler progress, DataCache<Long, net.wm161.microblog.Status> cache, String user) {
		super(account, progress, cache);
		m_user = user;
	}
	
	public UserTimelineRequest(Account account, ProgressHandler progress, DataCache<Long, net.wm161.microblog.Status> cache, User user) {
		super(account, progress, cache);
		m_user = String.valueOf(user.getId());
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			if (getStatuses("statuses/user_timeline/"+m_user))
				return true;
			return false;
		} catch (MalformedURLException e) {
			Log.e("UserTimelineRequest", "Malformed URL!");
			return false;
		} catch (APIException e) {
			return false;
		}
	}

}
