package net.wm161.microblog;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Status implements Comparable<Status>, Serializable, Parcelable {
	private static final long serialVersionUID = 1772677324524066760L;
	private User m_user;
	private String m_text;
	private long m_id;
	private Date m_date;
	private boolean m_favorited;
	

    public static final Parcelable.Creator<Status> CREATOR = new Parcelable.Creator<Status>() {
        public Status createFromParcel(Parcel in) {
            return new Status(in);
        }

        public Status[] newArray(int size) {
            return new Status[size];
        }
    };
	
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

	@Override
	public int describeContents() {
		return 0;
	}
	
	public Status(Parcel in) {
		m_id = in.readLong();
		m_date = new Date(in.readLong());
		if (in.readByte() == (byte)1)
			m_favorited = true;
		else
			m_favorited = false;
		m_text = in.readString();
		//m_user = in.readLong();
	}
	
	/*private User m_user;
	private String m_text;
	private long m_id;
	private Date m_date;
	private boolean m_favorited;*/

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeLong(m_id);
		dest.writeLong(m_date.getTime());
		if (m_favorited)
			dest.writeByte((byte)1);
		else
			dest.writeByte((byte)0);
		dest.writeString(m_text);
		dest.writeLong(m_user.getId());
	}
	
}
