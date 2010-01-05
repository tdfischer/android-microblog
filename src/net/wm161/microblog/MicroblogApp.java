package net.wm161.microblog;

import java.util.HashMap;

import android.app.Application;

public class MicroblogApp extends Application {
	public MicroblogApp() {
		super();
		m_statuscache = new HashMap<Account, DataCache<Long, Status>>();
		m_avatarCache = new HashMap<Account, DataCache<Long, Avatar>>();
	}

	private HashMap<Account, DataCache<Long, Status>> m_statuscache;
	private HashMap<Account, DataCache<Long, Avatar>> m_avatarCache;
	
	public DataCache<Long, Status> getStatusCache(Account acct) {
		if (!m_statuscache.containsKey(acct))
			m_statuscache.put(acct, new DataCache<Long, Status>(acct, this));
		return m_statuscache.get(acct);
	}
	
	public DataCache<Long, Avatar> getAvatarCache(Account acct) {
		if (!m_avatarCache.containsKey(acct))
			m_avatarCache.put(acct, new DataCache<Long, Avatar>(acct, this));
		return m_avatarCache.get(acct);
	}

	@Override
	public void onLowMemory() {
		for(DataCache<Long, Status> c : m_statuscache.values()) {
			c.shrink();
		}
	}
}
