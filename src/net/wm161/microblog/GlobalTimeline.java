package net.wm161.microblog;

import net.wm161.microblog.lib.ActivityProgressHandler;
import net.wm161.microblog.lib.GlobalTimelineUpdateRequest;


public class GlobalTimeline extends TimelineActivity {
	
    public void refresh() {
    	GlobalTimelineUpdateRequest timelineReq = new GlobalTimelineUpdateRequest(getAPI(), m_timeline);
    	timelineReq.setProgressHandler(new ActivityProgressHandler(this));
    	timelineReq.execute();
    }
}