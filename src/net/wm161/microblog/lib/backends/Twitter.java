package net.wm161.microblog.lib.backends;

import net.wm161.microblog.R;
import net.wm161.microblog.lib.API;
import net.wm161.microblog.lib.APIConfiguration;
import net.wm161.microblog.lib.APIManager;
import net.wm161.microblog.lib.APIRegistration;

public class Twitter extends Statusnet {
	TwitterConfig m_config = new TwitterConfig();
	
	public static void register() {
		APIManager.registerAPI(new APIRegistration("twitter") {
			
			@Override
			public API construct() {
				return new Twitter();
			}
		});
	}

	@Override
	public int getIcon() {
		return R.drawable.twittericon;
	}

	@Override
	public String getName() {
		return "Twitter";
	}

	@Override
	public APIConfiguration configuration() {
		return m_config;
	}

}
