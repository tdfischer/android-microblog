package net.wm161.microblog;

import net.wm161.microblog.lib.Account;



/**
 * Accounts manage authentication, identify 'sources' of data, and manage
 * account-specific caches.
 */
public class MicroblogAccount extends Account {
	private Preferences m_prefs;
	String m_guid;

	public MicroblogAccount(MicroblogApp app, String guid) {
		super(app.getCache());
		m_guid = guid;
		m_prefs = app.getPreferences();
	}

	@Override
	public String getUser() {
		return m_prefs.m_prefs.getString(m_guid + ".user", null);
	}

	@Override
	public String getPassword() {
		return m_prefs.m_prefs.getString(m_guid + ".password", null);
	}

	@Override
	public String getBase() {
		return m_prefs.m_prefs.getString(m_guid + ".baseurl", null);
	}

	@Override
	public String getName() {
		return m_prefs.m_prefs.getString(m_guid + ".name", null);
	}

	public void setUser(String user) {
		m_prefs.m_prefs.edit().putString(m_guid + ".user", user).commit();
	}

	public void setPassword(String password) {
		m_prefs.m_prefs.edit().putString(m_guid + ".password", password)
				.commit();
	}

	public void setBase(String baseurl) {
		m_prefs.m_prefs.edit().putString(m_guid + ".baseurl", baseurl).commit();
	}

	public void setName(String name) {
		m_prefs.m_prefs.edit().putString(m_guid + ".name", name).commit();
	}

	@Override
	public String getGuid() {
		return m_guid;
	}

	@Override
	public String toString() {
		return getName();
	}
}
