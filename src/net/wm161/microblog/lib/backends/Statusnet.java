package net.wm161.microblog.lib.backends;

import net.wm161.microblog.lib.API;
import net.wm161.microblog.lib.APIConfiguration;
import net.wm161.microblog.lib.APIManager;
import net.wm161.microblog.lib.APIRegistration;

public class Statusnet extends Twitter {
	StatusnetConfig m_config = new StatusnetConfig();
	
	public static void register() {
		APIManager.registerAPI(new APIRegistration("statusnet") {
			
			@Override
			public API construct() {
				return new Statusnet();
			}
		});
	}

	@Override
	public int getIcon() {
		// TODO Auto-generated method stub
		return super.getIcon();
	}

	@Override
	public String getName() {
		return "StatusNet";
	}

	@Override
	public APIConfiguration configuration() {
		return m_config;
	}

}
