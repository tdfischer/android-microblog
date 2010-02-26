package net.wm161.microblog;

import net.wm161.microblog.lib.APIRequest;
import net.wm161.microblog.lib.ActivityProgressHandler;
import net.wm161.microblog.lib.HomeTimelineRequest;


public class HomeTimeline extends TimelineActivity {

    public void refresh() {
    	APIRequest timelineReq = new HomeTimelineRequest(getAccount(), new ActivityProgressHandler(this), getAccount().getStatusCache()) {

			@Override
			public void onNewStatus(net.wm161.microblog.lib.Status s) {
				getStatusList().addStatus(s);
			}
    		
    	};
    	timelineReq.execute();
    }
}