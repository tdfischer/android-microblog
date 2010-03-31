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

package net.wm161.microblog.lib.backends.statusnet;

import java.net.URL;

import net.wm161.microblog.lib.Avatar;
import net.wm161.microblog.lib.User;

import org.json.JSONException;
import org.json.JSONObject;


public class JSONUser extends User {
	
	private static final long serialVersionUID = 4109817236138709089L;

	public JSONUser(JSONObject user) throws JSONException {
		setId(user.getLong("id"));
		setName(user.getString("name"));
		setScreenName(user.getString("screen_name"));
		setLocation(user.getString("location"));
		setDescription(user.getString("description"));
		setUrl(user.getString("url"));
		setSubscribers(user.getInt("followers_count"));
		setSubscriptions(user.getInt("friends_count"));
		setStatuses(user.getInt("statuses_count"));
		
		try {
			setAvatar(new Avatar(new URL(user.getString("profile_image_url")))); 
		} catch (Exception e2) {
			setAvatar(null);
		}

		try {
			int background = Integer.parseInt(user.getString("profile_background_color"), 16);
			if (background == 0)
				setBackgroundColor(android.R.color.background_dark);
		} catch (Exception e) {
			setBackgroundColor(android.R.color.background_dark);
		}
		
		setBackgroundImage(user.getString("profile_background_image_url"));
		

		int sidebar;
		try {
			sidebar = Integer.parseInt(user.getString("profile_sidebar_fill_color"), 16);
			sidebar+=0xff000000;
			if (sidebar == 0)
				setSidebarBackground(android.R.color.background_light);
		} catch (Exception e) {
			setSidebarBackground(android.R.color.background_light);
		}
		
		//FIXME: Why?
		setBackgroundColor(android.R.color.background_dark);
	}
	
}
