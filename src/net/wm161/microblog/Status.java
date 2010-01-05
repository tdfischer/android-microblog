package net.wm161.microblog;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class Status implements Comparable<Status>, Serializable {
	private static final long serialVersionUID = 1772677324524066760L;
	private User m_user;
	private String m_text;
	private long m_id;
	private Date m_date;
	private boolean m_favorited;
	
	public Status(JSONObject status) throws JSONException {
		m_user = new User(status.getJSONObject("user"));
		m_text = status.getString("text");
		m_id = status.getLong("id");
		m_date = new Date(status.getString("created_at"));
		m_favorited = status.getString("favorited").equalsIgnoreCase("true");
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

	@Override
	public int compareTo(Status another) {
		if (id() == another.id())
			return 0;
		if (id() > another.id())
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
	
}
