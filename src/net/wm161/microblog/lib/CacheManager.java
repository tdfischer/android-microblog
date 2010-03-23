package net.wm161.microblog.lib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

import android.content.Context;
import android.util.Log;

public class CacheManager {
	public static final int CACHE_VERSION = 1;
	
	public enum CacheType {
		Avatar,
		Status,
		//FIXME: This isn't really library-specific.
		Geocode
	};
	private HashMap<Account, HashMap<CacheType, DataCache<? extends Comparable<?>, ? extends Serializable>>> m_caches;
	private Context m_cxt;

	public CacheManager(Context cxt) {
		m_cxt = cxt;
		m_caches = new HashMap<Account, HashMap<CacheType, DataCache<? extends Comparable<?>, ? extends Serializable>>>();
		File cacheVersion = new File(m_cxt.getCacheDir()+File.pathSeparator+"version");
		int version = CACHE_VERSION;
		try {
			Scanner scanner = new Scanner(cacheVersion);
			version = scanner.nextInt();
			if (version != CACHE_VERSION) {
				Log.i("CacheManager", "Old version of cache detected. Cleaning up...");
				updateVersion();
			}
		} catch (FileNotFoundException e) {
			updateVersion();
		} catch (NoSuchElementException e) {
			updateVersion();
		}
	}
	
	private void updateVersion() {
		clear();
		File cacheVersion = new File(m_cxt.getCacheDir()+File.pathSeparator+"version");
		FileWriter writer;
		try {
			writer = new FileWriter(cacheVersion);
			writer.write(String.valueOf(CACHE_VERSION));
			Log.i("CacheManager", "Cache updated to version "+CACHE_VERSION);
		} catch (IOException e) {
			Log.e("CacheManager", "Couldn't create cache version file.");
		}
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
		for(HashMap<CacheType, DataCache<? extends Comparable<?>, ? extends Serializable>> cacheList : m_caches.values()) {
			for(DataCache<? extends Comparable<?> , ? extends Serializable> cache : cacheList.values()) {
				cache.shrink();
			}
		}
	}
	
	public void clear() {
		for(HashMap<CacheType, DataCache<? extends Comparable<?>, ? extends Serializable>> cacheList : m_caches.values()) {
			for(DataCache<? extends Comparable<?> , ? extends Serializable> cache : cacheList.values()) {
				cache.clear();
			}
		}
		File cache = m_cxt.getCacheDir();
		for(File file: cache.listFiles()) {
			Log.d("CacheManager", "Deleting cache "+file.getAbsolutePath());
			file.delete();
		}
	}
}
