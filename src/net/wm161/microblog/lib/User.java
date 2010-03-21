package net.wm161.microblog.lib;

import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;

public class User implements Serializable {
	private static final long serialVersionUID = -4848495105280531372L;
	private Avatar m_avatar;
	private int m_backgroundColor;

	private String m_backgroundImage;

	private String m_description;

	private long m_id;

	private String m_location;

	private String m_name;

	private String m_screenName;

	private int m_sidebarBackground;

	private int m_statuses;

	private int m_subscribers;

	private int m_subscriptions;

	private String m_url;

	public User() {
		super();
	}

	public Avatar getAvatar() {
		return m_avatar;
	}
	public Drawable getBackground() {
		Drawable[] layers = {getBackgroundColor(), getBackgroundImage()};
		return new LayerDrawable(layers);
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
	public String getDescription() {
		return m_description;
	}
	public long getId() {
		return m_id;
	}
	public String getLocation() {
		return m_location;
	}
	public String getName() {
		return m_name;
	}
	public String getScreenName() {
		return m_screenName;
	}
	public Drawable getSidebarBackground() {
		return new ColorDrawable(m_sidebarBackground);
	}
	public int getStatuses() {
		return m_statuses;
	}
	public int getSubscribers() {
		return m_subscribers;
	}

	public int getSubscriptions() {
		return m_subscriptions;
	}

	public Uri getUrl() {
		return Uri.parse(m_url);
	}

	public void setAvatar(Avatar avatar) {
		m_avatar = avatar;
	}

	public void setBackgroundColor(int backgroundColor) {
		m_backgroundColor = backgroundColor;
	}

	public void setBackgroundImage(String backgroundImage) {
		m_backgroundImage = backgroundImage;
	}

	public void setDescription(String description) {
		m_description = description;
	}

	public void setId(long id) {
		m_id = id;
	}

	public void setLocation(String location) {
		m_location = location;
	}

	public void setName(String name) {
		m_name = name;
	}

	public void setScreenName(String screenName) {
		m_screenName = screenName;
	}

	public void setSidebarBackground(int sidebarBackground) {
		m_sidebarBackground = sidebarBackground;
	}

	public void setStatuses(int statuses) {
		m_statuses = statuses;
	}

	public void setSubscribers(int subscribers) {
		m_subscribers = subscribers;
	}

	public void setSubscriptions(int subscriptions) {
		m_subscriptions = subscriptions;
	}

	public void setUrl(String url) {
		m_url = url;
	}

}