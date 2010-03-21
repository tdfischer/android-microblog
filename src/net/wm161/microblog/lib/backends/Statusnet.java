package net.wm161.microblog.lib.backends;

import net.wm161.microblog.R;
import net.wm161.microblog.lib.API;
import net.wm161.microblog.lib.APIConfiguration;
import net.wm161.microblog.lib.APIManager;
import net.wm161.microblog.lib.APIRegistration;

public class Statusnet extends Twitter {
	
	public Statusnet() {
		m_config = new StatusnetConfig();
	}
	
	@Override
	public String getDescription() {
		return "Statusnet is an open source and free software platform for microblogging that powers popular sites such as Identi.ca, TWiT Army, and Bleeper.";
	}

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
		return R.drawable.logo;
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
