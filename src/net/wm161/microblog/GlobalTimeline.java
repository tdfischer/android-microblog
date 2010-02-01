package net.wm161.microblog;


public class GlobalTimeline extends TimelineActivity {
    
    public void refresh() {
    	APIRequest timelineReq = new GlobalTimelineRequest(getAccount(), new ActivityProgressHandler(this), getAccount().getStatusCache()) {

			@Override
			public void onNewStatus(net.wm161.microblog.Status s) {
				getStatusList().addStatus(s);
			}
    		
    	};
    	timelineReq.execute();
    }
}