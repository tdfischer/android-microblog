package net.wm161.microblog;

import net.wm161.microblog.lib.ActivityProgressHandler;
import net.wm161.microblog.lib.ReplyTimelineUpdateRequest;

public class ReplyTimeline extends TimelineActivity {

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		ReplyTimelineUpdateRequest timelineReq = new ReplyTimelineUpdateRequest(getAPI(), m_timeline);
    	timelineReq.setProgressHandler(new ActivityProgressHandler(this));
    	timelineReq.execute();
	}

}
