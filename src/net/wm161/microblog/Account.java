package net.wm161.microblog;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.regex.Pattern;

import android.text.util.Linkify;
import android.widget.TextView;


/**
 * Accounts manage authentication, identify 'sources' of data, and manage account-specific caches. 
 */
public class Account extends Authenticator {
	private Preferences m_prefs;
	private String m_guid; 
	
	public Account(MicroblogApp app, String guid) {
		m_guid = guid;
		m_prefs = app.getPreferences();
		m_statusCache = app.getCache().getStatusCache(this);
	}
	
	private DataCache<Long, Status> m_statusCache;
	public DataCache<Long, Status> getStatusCache() {
		return m_statusCache;
	}
	
	public String getUser() {
		return m_prefs.m_prefs.getString(m_guid+".user", null);
	}
	
	public void addLinks(TextView view) {
		Pattern p = Pattern.compile("@(\\^S+) *$");
		Linkify.addLinks(view, p, "microblog://"+m_guid+"/user/");
	}

	public String getPassword(){ 
		return m_prefs.m_prefs.getString(m_guid+".password", null);
	}
	
	public String getBase() {
		return m_prefs.m_prefs.getString(m_guid+".baseurl", null);
	}
	
	public String getName() {
		return m_prefs.m_prefs.getString(m_guid+".name", null);
	}
	
	public void setUser(String user) {
		m_prefs.m_prefs.edit().putString(m_guid+".user", user).commit();
	}
	
	public void setPassword(String password) {
		m_prefs.m_prefs.edit().putString(m_guid+".password", password).commit();
	}
	
	public void setBase(String baseurl) {
		m_prefs.m_prefs.edit().putString(m_guid+".baseurl", baseurl).commit();
	}
	
	public void setName(String name) {
		m_prefs.m_prefs.edit().putString(m_guid+".name", name).commit();
	}

	public String getGuid() {
		return m_guid;
	}
	
	public String toString() {
		return getName();
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(getUser(), getPassword().toCharArray());
	}
}
