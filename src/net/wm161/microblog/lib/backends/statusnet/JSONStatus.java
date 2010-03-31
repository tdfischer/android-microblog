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

import java.net.MalformedURLException;
import java.util.Date;

import net.wm161.microblog.lib.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;

public class JSONStatus extends Status {

	private static final long serialVersionUID = 2232111122304335417L;

	public JSONStatus(JSONObject status) throws JSONException {
		setUser(new JSONUser(status.getJSONObject("user")));
		setText(status.getString("text"));
		setId(status.getLong("id"));
		setDate(new Date(status.getString("created_at")));
		setFavorited(status.getString("favorited").equalsIgnoreCase("true"));
		if (status.has("attachments")) {
			JSONArray attachments = status.getJSONArray("attachments");
			for(int i = 0;i<attachments.length();i++) {
				try {
					addAttachment(new JSONAttachment(attachments.getJSONObject(i)));
				} catch (MalformedURLException e) {
				}
			}
		}
		if (!status.isNull("geo")) {
			JSONObject geo = status.getJSONObject("geo");
			if (geo.getString("type").equals("Point")) {
				Location loc = new Location("");
				//TODO: Make sure this gets re-reversed once the twitter api gets fixed
				JSONArray coordinates = geo.getJSONArray("coordinates");
				loc.setLatitude(coordinates.getDouble(0));
				loc.setLongitude(coordinates.getDouble(1));
				setLocation(loc);
			}
		}
	}
}
