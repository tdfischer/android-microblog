package net.wm161.microblog.lib;

import java.util.HashMap;

import android.util.Log;

import net.wm161.microblog.lib.backends.Statusnet;
import net.wm161.microblog.lib.backends.Twitter;

public class APIManager {
	private static HashMap<String, APIRegistration> m_apis = new HashMap<String, APIRegistration>();
	
	static {
		Twitter.register();
		Statusnet.register();
	}
	
	public static String[] getAPIs() {
		return (String[]) m_apis.keySet().toArray();
	}
	
	public static void registerAPI(APIRegistration api) {
		Log.d("APIManager", "New API: "+api.name);
		m_apis.put(api.name, api);
	}
	
	public static API getAPI(String key) {
		return m_apis.get(key).construct();
	}
}