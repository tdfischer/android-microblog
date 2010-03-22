package net.wm161.microblog;

import net.wm161.microblog.lib.API;
import net.wm161.microblog.lib.APIConfiguration;
import net.wm161.microblog.lib.APIManager;
import net.wm161.microblog.lib.Account;



/**
 * Accounts manage authentication, identify 'sources' of data, and manage
 * account-specific caches.
 */
public class MicroblogAccount extends Account {
	private Preferences m_prefs;
	String m_guid;
	API m_api = null;

	public MicroblogAccount(MicroblogApp app, String guid) {
		super(app.getCacheManager());
		m_guid = guid;
		m_prefs = app.getPreferences();
	}
	
	public String getAPI() {
		return getString("api");
	}
	
	public API getAPIInstance() {
		if (m_api == null) {
			m_api = APIManager.getAPI(getAPI());
			m_api.setAccount(this);
			APIConfiguration conf = m_api.configuration();
			for(String key : conf.keys()) {
				Object type = conf.defaultValue(key);
				Object value = null;
				if (type instanceof Boolean)
					value = getAPIBoolean(key);
				if (type instanceof String)
					value = getAPIString(key);
				conf.set(key, value);
			}
		}
		return m_api;
	}
	
	public String getGuid() {
		return m_guid;
	}

	@Override
	public String getUser() {
		return getString("user");
	}

	@Override
	public String getPassword() {
		return getString("password");
	}

	@Override
	public String getName() {
		return getString("name");
	}

	public void setUser(String user) {
		set("user", user);
	}

	public void setPassword(String password) {
		set("password", password);
	}

	public void setName(String name) {
		set("name", name);
	}
	
	public void setAPI(String api) {
		set("api", api);
		m_api = null;
	}
	
	public void setAPIValue(String key, String value) {
		set("api."+key, value);
	}
	
	public String getAPIString(String key) {
		return getString("api."+key);
	}
	
	public int getAPIVersion() {
		return getInt("apiversion");
	}
	
	public void setAPIVersion(int version) {
		set("apiversion", version);
	}
	
	public boolean getAPIBoolean(String key) {
		return getBoolean("api."+key);
	}
	
	public void setAPIValue(String key, boolean value) {
		set("api."+key, value);
	}
	
	private void set(String key, String value) {
		m_prefs.m_prefs.edit().putString(m_guid+"."+key, value).commit();
	}
	
	private void set(String key, boolean value) {
		m_prefs.m_prefs.edit().putBoolean(m_guid+"."+key, value).commit();
	}
	
	private void set(String key, int value) {
		m_prefs.m_prefs.edit().putInt(m_guid+"."+key, value).commit();
	}
	
	private boolean getBoolean(String key) {
		return m_prefs.m_prefs.getBoolean(key, false);
	}
	
	private String getString(String key) {
		return m_prefs.m_prefs.getString(m_guid+"."+key, null);
	}
	
	private int getInt(String key) {
		return m_prefs.m_prefs.getInt(m_guid+"."+key, 0);
	}
}
