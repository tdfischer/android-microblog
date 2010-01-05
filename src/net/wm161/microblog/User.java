package net.wm161.microblog;

import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;

public class User implements Serializable {
	private static final long serialVersionUID = -5767166001263426558L;
	
	private Avatar m_avatar;
	private long m_id;
	private String m_name;
	private String m_screenName;
	private String m_location;
	private String m_description;
	private String m_url;
	private int m_subscribers;
	private int m_subscriptions;
	private int m_statuses;
	private int m_backgroundColor;
	private String m_backgroundImage;
	private int m_sidebarBackground;
	
	
	public User(JSONObject user) {
		
		try {
			m_id = user.getLong("id");
		} catch (JSONException e2) {
		}
		
		try {
			m_name = user.getString("name");
		} catch (JSONException e2) {
		}
		
		try {
			m_screenName = user.getString("screen_name");
		} catch (JSONException e2) {
		}
		
		try {
			m_location = user.getString("location");
		} catch (JSONException e2) {
		}
		
		try {
			m_description = user.getString("description");
		} catch (JSONException e2) {
		}
		
		try {
			m_url = user.getString("url");
		} catch (JSONException e5) {
		}
		
		try {
			m_subscribers = user.getInt("followers_count");
		} catch (JSONException e4) {
		}
		
		try {
			m_subscriptions = user.getInt("friends_count");
		} catch (JSONException e3) {
		}
		
		try {
			m_statuses = user.getInt("statuses_count");
		} catch (JSONException e2) {
		}
		
		try {
			m_avatar = new Avatar(new URL(user.getString("profile_image_url"))); 
		} catch (Exception e2) {
			m_avatar = null;
		}

		try {
			m_backgroundColor = Integer.parseInt(user.getString("profile_background_color"), 16);
			if (m_backgroundColor == 0)
				m_backgroundColor = android.R.color.background_dark;
		} catch (Exception e) {
			m_backgroundColor = android.R.color.background_dark;
		}
		
		try {
			m_backgroundImage = user.getString("profile_background_image_url");
		} catch (Exception e) {
		}
		

		try {
			m_sidebarBackground = Integer.parseInt(user.getString("profile_sidebar_fill_color"), 16);
			m_sidebarBackground +=0xff000000;
			if (m_sidebarBackground == 0)
				m_sidebarBackground = android.R.color.background_light;
		} catch (Exception e) {
			m_sidebarBackground = android.R.color.background_light;
		}
		
		m_backgroundColor = android.R.color.background_dark;
	}

	public long getId() {
		return m_id;
	}

	public String getName() {
		return m_name;
	}

	public String getScreenName() {
		return m_screenName;
	}

	public String getLocation() {
		return m_location;
	}

	public String getDescription() {
		return m_description;
	}

	public Uri getUrl() {
		return Uri.parse(m_url);
	}

	public int getSubscribers() {
		return m_subscribers;
	}

	public int getSubscriptions() {
		return m_subscriptions;
	}

	public int getStatuses() {
		return m_statuses;
	}

	public Avatar getAvatar() {
		return m_avatar;
	}
	
	public BackgroundColorDrawable getBackgroundColor() {
		return new BackgroundColorDrawable(m_backgroundColor, true);
	}
	
	public Drawable getBackgroundImage() {
		URLConnection c;
		try {
			c = (new URL(m_backgroundImage)).openConnection();
			c.setUseCaches(true);
			return new BitmapDrawable(c.getInputStream());
		} catch (Exception e) {
			return new BitmapDrawable();
		}
	}
	
	public Drawable getBackground() {
		Drawable[] layers = {getBackgroundColor(), getBackgroundImage()};
		return new LayerDrawable(layers);
	}
	
	public Drawable getSidebarBackground() {
		return new ColorDrawable(m_sidebarBackground);
	}
	
}
