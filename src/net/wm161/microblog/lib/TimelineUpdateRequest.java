package net.wm161.microblog.lib;

public abstract class TimelineUpdateRequest extends APIRequest {
	
	
	public TimelineUpdateRequest(API api, Timeline timeline) {
		super(api);
		m_timeline = timeline;
	}

	protected Timeline m_timeline;
	
	public Timeline getTimeline() {
		return m_timeline;
	}
}
