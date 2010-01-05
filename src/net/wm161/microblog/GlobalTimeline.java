package net.wm161.microblog;


public class GlobalTimeline extends TimelineActivity {
    
    public void refresh() {
    	APIRequest timelineReq = new GlobalTimelineRequest(getAccount(), this, getStatusList());
    	timelineReq.execute();
    }
}