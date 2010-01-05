package net.wm161.microblog;

import java.net.MalformedURLException;

import android.app.Activity;

public class GlobalTimelineRequest extends StatusListRequest {

	public GlobalTimelineRequest(Account account, Activity activity, StatusListAdapter statusListAdapter) {
		super(account, activity, statusListAdapter);
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			if (getStatuses("statuses/public_timeline"))
				return true;
			return false;
		} catch (MalformedURLException e) {
			return false;
		} catch (APIException e) {
			return false;
		}
	}

}
