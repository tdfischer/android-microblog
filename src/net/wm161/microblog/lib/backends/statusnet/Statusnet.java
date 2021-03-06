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
import java.util.EnumSet;

import net.wm161.microblog.R;
import net.wm161.microblog.lib.API;
import net.wm161.microblog.lib.APIConfiguration;
import net.wm161.microblog.lib.APIException;
import net.wm161.microblog.lib.APIManager;
import net.wm161.microblog.lib.APIRegistration;
import net.wm161.microblog.lib.APIRequest;
import net.wm161.microblog.lib.Status;
import net.wm161.microblog.lib.Timeline;
import net.wm161.microblog.lib.User;
import net.wm161.microblog.lib.APIRequest.ErrorType;

import org.json.JSONException;
import org.json.JSONObject;

public class Statusnet extends API {
	
	public Statusnet() {
		m_config = new StatusnetConfig();
	}

	StatusnetConfig m_config = new StatusnetConfig();
	
	public static void register() {
		APIManager.registerAPI(new APIRegistration("statusnet") {
			
			@Override
			public API construct() {
				return new Statusnet();
			}
		});
	}

	@Override
	public int getIcon() {
		return R.drawable.logo;
	}

	@Override
	public int getName() {
		return R.string.statusnet;
	}

	@Override
	public APIConfiguration configuration() {
		return m_config;
	}
	
	@Override
	public EnumSet<TimelineType> supportedTimelines() {
		return EnumSet.of(TimelineType.Home, TimelineType.Public, TimelineType.User);
	}
	
	@Override
	public User getUser(String user, APIRequest request) {
		HTTPAPIRequest req = new HTTPAPIRequest(this, request);
		try {
			return new JSONUser(new JSONObject(req.getData("/users/show/"+user)));
		} catch (JSONException e) {
			request.setError(ErrorType.ERROR_PARSE);
			return null;
		} catch (APIException e) {
			return null;
		}
	}

	@Override
	public boolean updateGlobalTimeline(APIRequest request, Timeline timeline) throws APIException {
		TimelineUpdater req = new TimelineUpdater(this, request, TimelineType.Public, timeline);
		try {
			return req.update();
		} catch (MalformedURLException e) {
			throw new APIException();
		}
	}

	@Override
	public boolean updateHomeTimeline(APIRequest request, Timeline timeline) throws APIException {
		TimelineUpdater req = new TimelineUpdater(this, request, TimelineType.Home, timeline);
		try {
			return req.update();
		} catch (MalformedURLException e) {
			throw new APIException();
		}
	}

	@Override
	public boolean updateUserTimeline(User user, APIRequest request, Timeline timeline) throws APIException {
		UserTimelineUpdater req = new UserTimelineUpdater(this, request, user, timeline);
		try {
			return req.update();
		} catch (MalformedURLException e) {
			throw new APIException();
		}
	}
	
	@Override
	public Boolean updateReplyTimeline(APIRequest request, Timeline timeline) throws APIException {
		TimelineUpdater req = new TimelineUpdater(this, request, TimelineType.Replies, timeline);
		try {
			return req.update();
		} catch (MalformedURLException e) {
			throw new APIException();
		}
	}
	
	@Override
	public boolean sendUpdate(Status update, APIRequest request) {
		HTTPAPIRequest req = new HTTPAPIRequest(this, request);
		req.setParameter("status", update.getText());
		if (update.hasAttachments())
			req.setParameter("media", update.getAttachment(0));
		req.setParameter("source", update.getSource());
		req.setParameter("status", update.getText());
		if (update.getLocation() != null) {
			req.setParameter("lat", update.getLocation().getLatitude());
			req.setParameter("long", update.getLocation().getLongitude());
		}
		try {
			req.getData("statuses/update");
			return true;
		} catch (APIException e) {
			return false;
		}
	}

	@Override
	public boolean favorite(Status status, APIRequest request) {
		HTTPAPIRequest req = new HTTPAPIRequest(this, request);
		req.setParameter("id", status.id());
		try {
			req.getData("favorites/create");
		} catch (APIException e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean unfavorite(Status status, APIRequest request) {
		HTTPAPIRequest req = new HTTPAPIRequest(this, request);
		req.setParameter("id", status.id());
		try {
			req.getData("favorites/destroy");
		} catch (APIException e) {
			return false;
		}
		return true;
	}

	@Override
	public Status getStatus(long id, APIRequest request) {
		HTTPAPIRequest req = new HTTPAPIRequest(this, request);
		try {
			return new JSONStatus(new JSONObject(req.getData("/statuses/show/"+id)));
		} catch (JSONException e) {
			request.setError(ErrorType.ERROR_PARSE);
			return null;
		} catch (APIException e) {
			return null;
		}
	}
	
	@Override
	public int getVersion() {
		return 1;
	}

}
