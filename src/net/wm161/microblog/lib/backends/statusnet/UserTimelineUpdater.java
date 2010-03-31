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

import net.wm161.microblog.lib.APIRequest;
import net.wm161.microblog.lib.Timeline;
import net.wm161.microblog.lib.User;
import net.wm161.microblog.lib.API.TimelineType;

public class UserTimelineUpdater extends TimelineUpdater {
	
	@Override
	protected String getPath() {
		return "statuses/user_timeline/"+m_user.getId();
	}

	public UserTimelineUpdater(Statusnet statusnet, APIRequest req, User user, Timeline timeline) {
		super(statusnet, req, TimelineType.User, timeline);
		m_user = user;
	}

	private User m_user;
}
