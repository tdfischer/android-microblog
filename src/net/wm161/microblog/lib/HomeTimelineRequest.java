package net.wm161.microblog.lib;

import java.net.MalformedURLException;



public abstract class HomeTimelineRequest extends StatusListRequest {

	public HomeTimelineRequest(Account account, ProgressHandler progress, DataCache<Long, net.wm161.microblog.lib.Status> cache) {
		super(account, progress, cache);
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
