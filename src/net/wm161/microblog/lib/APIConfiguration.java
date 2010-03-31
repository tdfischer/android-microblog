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

import java.util.HashMap;

public abstract class APIConfiguration {
	private HashMap<String, Object> m_values;
	public abstract String[] keys();
	public abstract String name(String key);
	
	public APIConfiguration() {
		m_values = new HashMap<String, Object>();
		for(String key : keys()) {
			set(key, defaultValue(key));
		}
	}
	
	public Object value(String key) {
		return m_values.get(key);
	}
	
	public void set(String key, Object value) {
		m_values.put(key, value);
	}
	
	public abstract Object defaultValue(String key);
}
