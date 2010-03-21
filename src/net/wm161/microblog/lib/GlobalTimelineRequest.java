package net.wm161.microblog.lib;

public class GlobalTimelineRequest extends TimelineUpdateRequest {

	public GlobalTimelineRequest(API api, Timeline timeline) {
		super(api, timeline);
	}

	@Override
	protected Boolean doInBackground(Void... arg0) {
		try {
			return m_api.updateGlobalTimeline(this, m_timeline);
		} catch (APIException e) {
			return false;
		}
	}

}
