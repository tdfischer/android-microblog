package net.wm161.microblog.lib;

import java.util.HashMap;

public abstract class APIConfiguration {
	private HashMap<String, Object> m_values;
	public abstract String[] keys();
	public abstract String name(String key);
	
	public APIConfiguration() {
		m_values = new HashMap<String, Object>();
		for(String key : keys()) {
			set(key, defaultValue(key));
		}
	}
	
	public Object value(String key) {
		return m_values.get(key);
	}
	
	public void set(String key, Object value) {
		m_values.put(key, value);
	}
	
	public abstract Object defaultValue(String key);
}
