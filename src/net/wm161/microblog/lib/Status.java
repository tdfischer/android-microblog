package net.wm161.microblog.lib;

import java.io.Serializable;
import java.util.Date;

import android.location.Location;

public class Status implements Comparable<Status>, Serializable {

	private static final long serialVersionUID = -8916839596710394496L;
	private User m_user;
	private String m_text;
	private long m_id;
	private Date m_date;
	private boolean m_favorited;
	private Attachment m_attachment = null;
	private String m_source;
	private double m_latitude;
	private double m_longitude;
	private boolean m_hasLocation = false;
	
	public Status() {
		super();
	}
	
	public String getTimestamp() {
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
			timestamp = days+" days ago";
		else if (hours > 0)
			timestamp = hours+" hours ago";
		else if (minutes > 0)
			timestamp = minutes+" minutes ago";
		else
			timestamp = seconds+" seconds ago";
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

	public void setAttachment(Attachment attachment) {
		m_attachment = attachment;
	}
	
	public boolean hasAttachment() {
		return m_attachment != null;
	}

	public Attachment getAttachment() {
		return m_attachment;
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