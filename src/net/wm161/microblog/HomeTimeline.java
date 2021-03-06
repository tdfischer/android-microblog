package net.wm161.microblog;

import net.wm161.microblog.lib.ActivityProgressHandler;
import net.wm161.microblog.lib.HomeTimelineUpdateRequest;


public class HomeTimeline extends TimelineActivity {

    public void refresh() {
    	HomeTimelineUpdateRequest timelineReq = new HomeTimelineUpdateRequest(getAPI(), m_timeline);
    	timelineReq.setProgressHandler(new ActivityProgressHandler(this));
    	timelineReq.execute();
    }
}