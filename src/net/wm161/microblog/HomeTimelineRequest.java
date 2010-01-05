package net.wm161.microblog;

import java.net.MalformedURLException;

import android.app.Activity;

public class HomeTimelineRequest extends StatusListRequest {

	public HomeTimelineRequest(Account account, Activity activity, StatusListAdapter statusListAdapter) {
		super(account, activity, statusListAdapter);
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			if (getStatuses("statuses/friends_timeline"))
				return true;
			return false;
		} catch (MalformedURLException e) {
			return false;
		} catch (APIException e) {
			return false;
		}
	}

}
