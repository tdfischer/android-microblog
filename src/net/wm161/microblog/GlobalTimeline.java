package net.wm161.microblog;

import net.wm161.microblog.lib.GlobalTimelineRequest;


public class GlobalTimeline extends TimelineActivity {
	
    public void refresh() {
    	GlobalTimelineRequest timelineReq = new GlobalTimelineRequest(getAPI(), m_timeline);
    	timelineReq.execute();
    }
}