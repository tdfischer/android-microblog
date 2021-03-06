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

import android.util.Log;

import net.wm161.microblog.lib.backends.statusnet.Statusnet;
import net.wm161.microblog.lib.backends.statusnet.Twitter;

public class APIManager {
	private static HashMap<String, APIRegistration> m_apis = new HashMap<String, APIRegistration>();
	
	static {
		Twitter.register();
		Statusnet.register();
	}
	
	public static String[] getAPIs() {
		String[] ret = new String[m_apis.size()];
		return m_apis.keySet().toArray(ret);
	}
	
	public static void registerAPI(APIRegistration api) {
		Log.d("APIManager", "New API: "+api.name);
		m_apis.put(api.name, api);
	}
	
	public static API getAPI(String key) {
		return m_apis.get(key).construct();
	}
}
