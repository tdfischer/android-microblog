package net.wm161.microblog.lib;


public class UserTimelineRequest extends TimelineUpdateRequest {

	private User m_user;

	public UserTimelineRequest(API api, User user, Timeline timeline) {
		super(api, timeline);
		m_user = user;
	}

	@Override
	protected Boolean doInBackground(Void... arg0) {
		try {
			return m_api.updateUserTimeline(m_user, this, m_timeline);
		} catch (APIException e) {
			return false;
		}
	}

}
