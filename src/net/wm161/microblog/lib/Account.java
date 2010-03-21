package net.wm161.microblog.lib;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.regex.Pattern;

import android.widget.TextView;

public abstract class Account extends Authenticator {

	protected DataCache<Long, Status> m_statusCache;

	public String toString() {
		return getName();
	}

	public abstract String getName();

	public abstract String getPassword();

	public abstract String getUser();

	public Account(CacheManager cache) {
		super();
		m_statusCache = cache.getStatusCache(this);
	}

	public DataCache<Long, Status> getStatusCache() {
		return m_statusCache;
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