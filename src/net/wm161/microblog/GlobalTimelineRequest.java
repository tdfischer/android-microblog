package net.wm161.microblog;

import java.net.MalformedURLException;

public abstract class GlobalTimelineRequest extends StatusListRequest {

	public GlobalTimelineRequest(Account account, ProgressHandler progress, DataCache<Long, net.wm161.microblog.Status> cache) {
		super(account, progress, cache);
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
