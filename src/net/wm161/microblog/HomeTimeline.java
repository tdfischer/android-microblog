package net.wm161.microblog;


public class HomeTimeline extends TimelineActivity {

    public void refresh() {
    	APIRequest timelineReq = new HomeTimelineRequest(getAccount(), new ActivityProgressHandler(this), getAccount().getStatusCache()) {

			@Override
			public void onNewStatus(net.wm161.microblog.Status s) {
				getStatusList().addStatus(s);
			}
    		
    	};
    	timelineReq.execute();
    }
}