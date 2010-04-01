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

import java.util.EnumSet;

public abstract class API {
	
	public static final int VERSION = 1;
		
	public enum Feature {
		Timeline,
	}
	
	public enum TimelineType {
		Home,
		Public,
		User,
		Replies
	}
	private Account m_account;
	
	public void setAccount(Account account) {
		m_account = account;
	}
	
	public Account getAccount() {
		return m_account;
	}
	
	public abstract EnumSet<TimelineType> supportedTimelines();
	public abstract boolean updateGlobalTimeline(APIRequest request, Timeline timeline) throws APIException;
	public abstract boolean updateHomeTimeline(APIRequest request, Timeline timeline) throws APIException;
	public abstract boolean updateUserTimeline(User user, APIRequest request, Timeline timeline) throws APIException;
	public abstract Boolean updateReplyTimeline(APIRequest replyTimelineUpdateRequest, Timeline timeline) throws APIException;
	public abstract User getUser(String user, APIRequest request);
	public abstract net.wm161.microblog.lib.Status getStatus(long status, APIRequest request);
	public abstract boolean sendUpdate(net.wm161.microblog.lib.Status status, APIRequest request);
	public abstract boolean favorite(net.wm161.microblog.lib.Status status, APIRequest request);
	public abstract boolean unfavorite(net.wm161.microblog.lib.Status status, APIRequest request);
	public abstract int getIcon();
	public abstract APIConfiguration configuration();
	public abstract int getName();
	public abstract int getVersion();
}
