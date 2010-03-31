/**
 *  This file is part of android-microblog
 *  Copyright (C) 2010 Trever Fischer <tdfischer@fedoraproject.org>
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package net.wm161.microblog.lib;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.lang.ref.SoftReference;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;



import android.content.Context;
import android.util.Log;

public class DataCache<K extends Comparable<K>, T extends Serializable> {
	private Account m_account;
	private Context m_cxt;
	private String m_name;
	private LinkedHashMap<K, SoftReference<T>> m_cache;
	public static final int CACHE_LIMIT = 360000;
	public static final int CACHE_MIN_SIZE = 40;
	public static final int CACHE_MAX_SIZE = 200;

	public DataCache(Account account, Context cxt, String name) {
		m_account = account;
		m_cxt = cxt;
		m_cache = new LinkedHashMap<K, SoftReference<T>>();
		m_name = name;
	}
	
	public void shrink() {
		Log.w("DataCache", "Shrinking down because of memory. We've got "+m_cache.size()+" items.");
		for (Iterator<SoftReference<T>> it = m_cache.values().iterator();it.hasNext();) {
			if (m_cache.size() > CACHE_MIN_SIZE)
				it.remove();
			else
				break;
		}
	}
	
	public void trim() {
		for (Iterator<SoftReference<T>> it = m_cache.values().iterator();it.hasNext();) {
			if (m_cache.size() > CACHE_MAX_SIZE)
				it.remove();
			else
				break;
		}
	}
	
	public void clear() {
		m_cache.clear();
		File cache = m_cxt.getCacheDir();
		FileFilter filter = new FileFilter() {
			
			@Override
			public boolean accept(File arg0) {
				return (arg0.getName().startsWith(m_account.getGuid()+m_name));
			}
		};
		for(File file: cache.listFiles(filter)) {
			Log.d("DataCache", "Deleting cache "+file.getAbsolutePath());
			file.delete();
		}
	}
	

	@SuppressWarnings("unchecked")
	public T get(K id) {
		if (m_cache.containsKey(id))
			if (m_cache.get(id).get() != null)
				return m_cache.get(id).get();
			else
				m_cache.remove(id);
		File cache = m_cxt.getCacheDir();
		File cacheFile = new File(cache.toString()+File.separator+m_account.getGuid()+m_name+id);
		Date now;
		if (cacheFile.exists()) {
			now = new Date();
			if (now.getTime()-cacheFile.lastModified() > CACHE_LIMIT) {
				cacheFile.delete();
				return null;
			}
			try {
				T s;
				s = (T) (new ObjectInputStream(new FileInputStream(cacheFile))).readObject();
				m_cache.put(id, new SoftReference<T>(s));
				trim();
				return s;
			} catch (OptionalDataException e) {
			} catch (StreamCorruptedException e) {
			} catch (FileNotFoundException e) {
			} catch (ClassNotFoundException e) {
			} catch (IOException e) {
			} catch (ClassCastException e) {
				cacheFile.delete();
				Log.w("DataCache", "CORRUPT: "+id);
				return null;
			}
		}
		return null;
	}
	
	public boolean put(K key, T data) {
		m_cache.put(key, new SoftReference<T>(data));
		trim();
		File cache = m_cxt.getCacheDir();
		if (!cache.exists())
			cache.mkdir();
		File cacheFile = new File(cache.toString()+File.separator+m_account.getGuid()+m_name+key);
		if (!cacheFile.exists())
			try {
				cacheFile.createNewFile();
			} catch (IOException e) {
				return false;
			}
		try {
			(new ObjectOutputStream(new FileOutputStream(cacheFile))).writeObject(data);
			return true;
		} catch (FileNotFoundException e) {
			Log.e("DataCache", "Failed to cache "+key+": "+e.getMessage());
			return false;
		} catch (IOException e) {
			Log.e("DataCache", "Failed to cache "+key+": "+e.getMessage());
			return false;
		}
	}
	
	public K biggestKey() {
		K biggest = null;
		for(K key : m_cache.keySet()) {
			if (biggest == null)
				biggest = key;
			if (key.compareTo(biggest) < 0)
				biggest = key;
		}
		
		return biggest;
	}
}
