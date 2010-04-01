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
import java.util.ArrayList;
import java.util.Date;
import net.wm161.microblog.R;

import android.content.Context;
import android.location.Location;

public class Status implements Comparable<Status>, Serializable {

	private static final long serialVersionUID = -8916839596710394496L;
	private User m_user;
	private String m_text;
	private long m_id;
	private Date m_date;
	private boolean m_favorited;
	private ArrayList<Attachment> m_attachments;
	private String m_source;
	private double m_latitude;
	private double m_longitude;
	private boolean m_hasLocation = false;
	
	public Status() {
		super();
		m_attachments = new ArrayList<Attachment>();
	}
	
	public String getTimestamp(Context cxt) {
		String timestamp;
		Date statusDate = getDate();
		Date now = new Date();
		int days = now.getDay() - statusDate.getDay();
		int hours = now.getHours() - statusDate.getHours();
		int minutes = now.getMinutes() - statusDate.getMinutes();
		int seconds = now.getSeconds() - statusDate.getSeconds();
		if (days > 5)
			timestamp = now.toLocaleString();
		else if (days > 0)
			timestamp = cxt.getString(R.string._days_ago, days);
		else if (hours > 0)
			timestamp = cxt.getString(R.string._hours_ago, hours);
		else if (minutes > 0)
			timestamp = cxt.getString(R.string._minutes_ago, minutes);
		else
			timestamp = cxt.getString(R.string._seconds_ago, seconds);
		return timestamp;
	}
	
	public String getText() {
		return m_text;
	}

	public void setText(String text) {
		m_text = text;
	}

	public long getId() {
		return m_id;
	}

	public void setId(long id) {
		m_id = id;
	}

	public void setUser(User user) {
		m_user = user;
	}

	public void setDate(Date date) {
		m_date = date;
	}

	public void setFavorited(boolean favorited) {
		m_favorited = favorited;
	}

	public boolean isFavorited() {
		return m_favorited;
	}

	public User getUser() {
		return m_user;
	}

	public String text() {
		return m_text;
	}

	public long id() {
		return m_id;
	}

	public Date getDate() {
		return m_date;
	}

	public void addAttachment(Attachment attachment) {
		m_attachments.add(attachment);
	}
	
	public boolean hasAttachments() {
		return !m_attachments.isEmpty();
	}

	public Attachment getAttachment(int id) {
		return m_attachments.get(id);
	}
	
	public Attachment[] getAttachments() {
		Attachment[] ret = new Attachment[m_attachments.size()];
		return m_attachments.toArray(ret);
	}

	public void setSource(String source) {
		m_source = source;
	}

	public String getSource() {
		return m_source;
	}

	@Override
	public int compareTo(Status another) {
		if (id() == another.id())
			return 0;
		if (id() < another.id())
			return 1;
		return -1;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Status) {
			if (((Status)o).id() == id())
				return true;
		}
		return false;
	}

	public void setLocation(Location location) {
		if (m_hasLocation = (location != null)) {
			m_latitude = location.getLatitude();
			m_longitude = location.getLongitude();
		}
	}

	public Location getLocation() {
		if (m_hasLocation) {
			Location ret = new Location("");
			ret.setLongitude(m_longitude);
			ret.setLatitude(m_latitude);
			return ret;
		} else {
			return null;
		}
	}

}