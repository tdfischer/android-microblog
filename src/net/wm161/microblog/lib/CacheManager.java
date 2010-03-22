package net.wm161.microblog.lib;

import java.io.Serializable;
import java.util.HashMap;



import android.content.Context;

public class CacheManager {
	public enum CacheType {
		Avatar,
		Status
	};
	private HashMap<Account, DataCache<Long, Avatar>> m_avatarCache;
	private HashMap<Account, DataCache<Long, Status>> m_statuscache;
	private HashMap<Account, HashMap<CacheType, DataCache<? extends Comparable<?>, ? extends Serializable>>> m_caches;
	private Context m_cxt;

	public CacheManager(Context cxt) {
		m_cxt = cxt;
		m_caches = new HashMap<Account, HashMap<CacheType, DataCache<? extends Comparable<?>, ? extends Serializable>>>();
		m_statuscache = new HashMap<Account, DataCache<Long, Status>>();
		m_avatarCache = new HashMap<Account, DataCache<Long, Avatar>>();
	}
	
	public <K extends Comparable<K>, V extends Serializable> DataCache<K, V> getCache(Account acct, CacheType type) {
		if (!m_caches.containsKey(acct)) {
			m_caches.put(acct, new HashMap<CacheType, DataCache<? extends Comparable<?>, ? extends Serializable>>());
		}
		if (!m_caches.get(acct).containsKey(type)) {
			m_caches.get(acct).put(type, new DataCache<K, V>(acct, m_cxt, type.name()));
		}
		return (DataCache<K, V>) m_caches.get(acct).get(type);
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
