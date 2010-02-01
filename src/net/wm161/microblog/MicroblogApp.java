package net.wm161.microblog;

import android.app.Application;

public class MicroblogApp extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		m_prefs = new Preferences(this);
		m_cache = new CacheManager(this);
	}

	private Preferences m_prefs;
	private CacheManager m_cache;
	
	@Override
	public void onLowMemory() {
		m_cache.shrink();
	}
	
	public CacheManager getCache() {
		return m_cache;
	}
	
	public Preferences getPreferences() {
		return m_prefs;
	}
}
