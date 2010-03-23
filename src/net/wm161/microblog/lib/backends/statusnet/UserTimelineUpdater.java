package net.wm161.microblog.lib.backends.statusnet;

import net.wm161.microblog.lib.APIRequest;
import net.wm161.microblog.lib.Timeline;
import net.wm161.microblog.lib.User;
import net.wm161.microblog.lib.API.TimelineType;

public class UserTimelineUpdater extends TimelineUpdater {
	
	@Override
	protected String getPath() {
		return "statuses/user_timeline/"+m_user.getId();
	}

	public UserTimelineUpdater(Statusnet statusnet, APIRequest req, User user, Timeline timeline) {
		super(statusnet, req, TimelineType.User, timeline);
		m_user = user;
	}

	private User m_user;
}
