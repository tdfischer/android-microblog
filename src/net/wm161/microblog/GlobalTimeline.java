package net.wm161.microblog;

import net.wm161.microblog.lib.APIRequest;
import net.wm161.microblog.lib.ActivityProgressHandler;
import net.wm161.microblog.lib.GlobalTimelineRequest;


public class GlobalTimeline extends TimelineActivity {
    
    public void refresh() {
    	APIRequest timelineReq = new GlobalTimelineRequest(getAccount(), new ActivityProgressHandler(this), getAccount().getStatusCache()) {

			@Override
			public void onNewStatus(net.wm161.microblog.lib.Status s) {
				getStatusList().addStatus(s);
			}
    		
    	};
    	timelineReq.execute();
    }
}