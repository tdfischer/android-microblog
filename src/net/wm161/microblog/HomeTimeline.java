package net.wm161.microblog;


public class HomeTimeline extends TimelineActivity {

    public void refresh() {
    	APIRequest timelineReq = new HomeTimelineRequest(getAccount(), this, getStatusList());
    	timelineReq.execute();
    }
}