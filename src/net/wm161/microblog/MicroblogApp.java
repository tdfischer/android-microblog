package net.wm161.microblog;

import net.wm161.microblog.lib.CacheManager;
import android.app.Application;

public class MicroblogApp extends Application {
	private CacheManager m_cache;
	private Preferences m_prefs;

	@Override
	public void onCreate() {
		super.onCreate();
		m_cache = new CacheManager(this);
		m_prefs = new Preferences(this);
	}
	
	@Override
	public void onLowMemory() {
		m_cache.shrink();
	}
	
	public CacheManager getCacheManager() {
		return m_cache;
	}
	
	public Preferences getPreferences() {
		return m_prefs;
	}
}
