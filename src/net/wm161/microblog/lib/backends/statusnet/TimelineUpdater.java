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

import net.wm161.microblog.lib.APIException;
import net.wm161.microblog.lib.APIProgress;
import net.wm161.microblog.lib.APIRequest;
import net.wm161.microblog.lib.CacheManager;
import net.wm161.microblog.lib.DataCache;
import net.wm161.microblog.lib.Timeline;
import net.wm161.microblog.lib.API.TimelineType;
import net.wm161.microblog.lib.APIRequest.ErrorType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class TimelineUpdater extends HTTPAPIRequest {
	DataCache<Long, net.wm161.microblog.lib.Status> m_cache;
	private TimelineType m_type;
	private Timeline m_timeline;
	
	public TimelineUpdater(Statusnet statusnet, APIRequest req, TimelineType type, Timeline timeline) {
		super(statusnet, req);
		m_type = type;
		m_timeline = timeline;
		m_cache = statusnet.getAccount().getCache(CacheManager.CacheType.Status);
	}
	
	protected String getPath() {
		switch(m_type) {
			case Home:
				return "statuses/friends_timeline";
			case Public:
				return "statuses/public_timeline";
			case Replies:
				return "statuses/mentions";
		}
		throw new UnsupportedOperationException("Unknown timeline type: "+m_type.toString());
	}

	public boolean update() throws APIException, MalformedURLException {
		Long biggest = m_cache.biggestKey();
		if (biggest != null)
			setParameter("since_id", m_cache.biggestKey());
		JSONArray data = null;
		String strdata = getData(getPath());
		try {
			data = new JSONArray(strdata);
		} catch (JSONException e) {
			Log.e("StatusListRequest", "Parse error: "+e.getMessage());
			Log.e("StatusListRequest", "Original data: "+strdata);
			setError(ErrorType.ERROR_PARSE);
			throw new APIException();
		}
		//TODO: Investigate if we still need this clear commented out
		//m_statuses.clear();
		Integer i;
		for(i=0;i<data.length();i++) {
			try {
				Float progress = 5000+(5000)*(i.floatValue()/(float)data.length());
				JSONObject dent = data.getJSONObject(i);
				long id = dent.getLong("id");
				net.wm161.microblog.lib.Status status = null;
				try {
					status = m_cache.get(id);
				} catch (ClassCastException e) {
				}
				if (status == null) {
					status = new JSONStatus(dent);
					m_cache.put(id, status);
				}
				publishProgress(new Progress(progress.intValue(), status));
			} catch (JSONException e) {
				Log.w("StatusListRequest", "JSONException while listing statuses: "+e.getMessage());
			}
		}
		return true;
	}
	
	private class Progress extends APIProgress {
		private net.wm161.microblog.lib.Status m_status;

		public Progress(Integer progress, net.wm161.microblog.lib.Status status) {
			super(progress);
			m_status = status;
		}
	}

	@Override
	protected void publishProgress(APIProgress progress) {
		super.publishProgress(progress);
		if (progress instanceof Progress) {
			net.wm161.microblog.lib.Status status = ((Progress)progress).m_status;
			m_timeline.add(status);
		}
	}
}
