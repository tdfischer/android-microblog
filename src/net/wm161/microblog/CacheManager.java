package net.wm161.microblog;

import java.util.HashMap;

import android.content.Context;

public class CacheManager {
	private HashMap<Account, DataCache<Long, Avatar>> m_avatarCache;
	private HashMap<Account, DataCache<Long, Status>> m_statuscache;
	private Context m_cxt;

	public CacheManager(Context cxt) {
		m_cxt = cxt;
		m_statuscache = new HashMap<Account, DataCache<Long, Status>>();
		m_avatarCache = new HashMap<Account, DataCache<Long, Avatar>>();
	}
	
	public DataCache<Long, Status> getStatusCache(Account acct) {
		if (!m_statuscache.containsKey(acct))
			m_statuscache.put(acct, new DataCache<Long, Status>(acct, m_cxt));
		return m_statuscache.get(acct);
	}
	
	public DataCache<Long, Avatar> getAvatarCache(Account acct) {
		if (!m_avatarCache.containsKey(acct))
			m_avatarCache.put(acct, new DataCache<Long, Avatar>(acct, m_cxt));
		return m_avatarCache.get(acct);
	}

	public void shrink() {
		for(DataCache<Long, Status> c : m_statuscache.values()) {
			c.shrink();
		}
		
		for (DataCache<Long, Avatar> c: m_avatarCache.values()) {
			c.shrink();
		}
	}

}
