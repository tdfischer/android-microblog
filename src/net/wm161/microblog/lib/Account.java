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