package net.wm161.microblog.lib;

public class ReplyTimelineUpdateRequest extends TimelineUpdateRequest {

	public ReplyTimelineUpdateRequest(API api, Timeline timeline) {
		super(api, timeline);
	}

	@Override
	protected Boolean doInBackground(Void... arg0) {
		try {
			return m_api.updateReplyTimeline(this, m_timeline);
		} catch (APIException e) {
			return false;
		}
	}

}
