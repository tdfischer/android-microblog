package net.wm161.microblog.lib;

import java.io.Serializable;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.regex.Pattern;

import android.widget.TextView;

public abstract class Account extends Authenticator {

	private CacheManager m_cache;

	public String toString() {
		return getName();
	}

	public abstract String getName();

	public abstract String getPassword();

	public abstract String getUser();

	public Account(CacheManager cache) {
		super();
		m_cache = cache;
	}
	
	public <K extends Comparable<K>, V extends Serializable> DataCache<K, V> getCache(CacheManager.CacheType type) {
		return m_cache.getCache(this, type);
	}

	public void addLinks(TextView view) {
		Pattern.compile("@(\\^S+) *$");
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(getUser(), getPassword()
				.toCharArray());
	}

	public abstract String getGuid();

}