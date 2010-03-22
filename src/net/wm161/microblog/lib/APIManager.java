package net.wm161.microblog.lib;

import java.util.HashMap;

import android.util.Log;

import net.wm161.microblog.lib.backends.statusnet.Statusnet;
import net.wm161.microblog.lib.backends.statusnet.Twitter;

public class APIManager {
	private static HashMap<String, APIRegistration> m_apis = new HashMap<String, APIRegistration>();
	
	static {
		Twitter.register();
		Statusnet.register();
	}
	
	public static String[] getAPIs() {
		String[] ret = new String[m_apis.size()];
		return m_apis.keySet().toArray(ret);
	}
	
	public static void registerAPI(APIRegistration api) {
		Log.d("APIManager", "New API: "+api.name);
		m_apis.put(api.name, api);
	}
	
	public static API getAPI(String key) {
		return m_apis.get(key).construct();
	}
}
