package net.wm161.microblog.lib;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.regex.Pattern;

import android.widget.TextView;

public abstract class Account extends Authenticator {

	protected DataCache<Long, Status> m_statusCache;

	public abstract String toString();

	public abstract String getName();

	public abstract String getBase();

	public abstract String getPassword();

	public abstract String getUser();

	public Account(CacheManager cache) {
		super();
		m_statusCache = cache.getStatusCache(this);
	}

	public DataCache<Long, Status> getStatusCache() {
		//TODO: Is this /really/ the account's job?
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