package net.wm161.microblog.lib;

public class HomeTimelineUpdateRequest extends TimelineUpdateRequest {

	public HomeTimelineUpdateRequest(API api, Timeline timeline) {
		super(api, timeline);
	}

	@Override
	protected Boolean doInBackground(Void... arg0) {
		try {
			return m_api.updateHomeTimeline(this, m_timeline);
		} catch (APIException e) {
			return false;
		}
	}

}
