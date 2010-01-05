package net.wm161.microblog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;

import android.content.Context;
import android.util.Log;

public class StatusCache {
	private Account m_account;
	private Context m_cxt;
	private LinkedHashMap<Long, Status> m_cache;
	public static final int CACHE_LIMIT = 360000;
	public static final int CACHE_MIN_SIZE = 40;
	public static final int CACHE_MAX_SIZE = 200;
	
	class CacheEntry implements Comparable<CacheEntry> {
		Status m_status;
		Date m_date;
		public CacheEntry(Status s, Date d) {
			m_status = s;
			m_date = d;
		}

		@Override
		public int compareTo(CacheEntry another) {
			if (another.m_status.id() == m_status.id())
				return 0;
			if (another.m_date.before(m_date))
				return -1;
			return 1;
		}
	}

	public StatusCache(Account account, Context cxt) {
		m_account = account;
		m_cxt = cxt;
		m_cache = new LinkedHashMap<Long, Status>();
	}
	
	public void shrink() {
		Log.w("StatusCache", "Shrinking down because of memory. We've got "+m_cache.size()+" items.");
		for (Iterator<Status> it = m_cache.values().iterator();it.hasNext();) {
			if (m_cache.size() > CACHE_MIN_SIZE)
				it.remove();
			else
				break;
		}
	}
	
	public void trim() {
		for (Iterator<Status> it = m_cache.values().iterator();it.hasNext();) {
			if (m_cache.size() > CACHE_MAX_SIZE)
				it.remove();
			else
				break;
		}
	}
	
	public Status get(long id) {
		if (m_cache.containsKey(id))
			return m_cache.get(id);
		File cache = m_cxt.getCacheDir();
		File cacheFile = new File(cache.toString()+File.separator+m_account.getGuid()+id);
		Date now;
		if (cacheFile.exists()) {
			now = new Date();
			if (now.getTime()-cacheFile.lastModified() > CACHE_LIMIT) {
				cacheFile.delete();
				Log.d("StatusCache", "Expired: "+id);
				return null;
			}
			try {
				Status s;
				s = (Status) (new ObjectInputStream(new FileInputStream(cacheFile))).readObject();
				Log.d("StatusCache", "HIT: "+id);
				m_cache.put(s.id(), s);
				trim();
				return s;
			} catch (OptionalDataException e) {
			} catch (StreamCorruptedException e) {
			} catch (FileNotFoundException e) {
			} catch (ClassNotFoundException e) {
			} catch (IOException e) {
			}
		}
		Log.d("StatusCache", "MISS: "+id);
		return null;
	}
	
	public boolean put(Status s) {
		m_cache.put(s.id(), s);
		trim();
		File cache = m_cxt.getCacheDir();
		if (!cache.exists())
			cache.mkdir();
		File cacheFile = new File(cache.toString()+File.separator+m_account.getGuid()+s.id());
		if (!cacheFile.exists())
			try {
				cacheFile.createNewFile();
			} catch (IOException e) {
				return false;
			}
		try {
			(new ObjectOutputStream(new FileOutputStream(cacheFile))).writeObject(s);
			Log.d("StatusCache", "Cached "+s.id());
			return true;
		} catch (FileNotFoundException e) {
			Log.e("StatusCache", "Failed to cache "+s.id()+": "+e.getMessage());
			return false;
		} catch (IOException e) {
			Log.e("StatusCache", "Failed to cache "+s.id()+": "+e.getMessage());
			return false;
		}
	}
}
