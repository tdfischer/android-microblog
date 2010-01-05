package net.wm161.microblog;

import java.net.MalformedURLException;

import android.app.Activity;
import android.util.Log;

public class UserTimelineRequest extends StatusListRequest {
	
	private String m_user;
		
	public UserTimelineRequest(Account account, Activity activity, StatusListAdapter statusListAdapter, String user) {
		super(account, activity, statusListAdapter);
		m_user = user;
	}
	
	public UserTimelineRequest(Account account, Activity activity, StatusListAdapter statusListAdapter, User user) {
		super(account, activity, statusListAdapter);
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
