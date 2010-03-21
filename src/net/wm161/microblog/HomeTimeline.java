package net.wm161.microblog;

import net.wm161.microblog.lib.HomeTimelineRequest;


public class HomeTimeline extends TimelineActivity {

    public void refresh() {
    	HomeTimelineRequest timelineReq = new HomeTimelineRequest(getAPI(), m_timeline);
    	timelineReq.execute();
    }
}