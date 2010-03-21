package net.wm161.microblog.lib.backends;

import net.wm161.microblog.lib.APIConfiguration;

public class StatusnetConfig extends APIConfiguration {

	@Override
	public Object defaultValue(String key) {
		if (key.equals("server"))
			return "identi.ca";
		if (key.equals("path"))
			return "/api/";
		if (key.equals("https"))
			return true;
		throw new UnsupportedOperationException("Unsupported key "+key);
	}

	@Override
	public String[] keys() {
		String[] ret = {"server", "path", "https"};
		return ret;
	}

	@Override
	public String name(String key) {
		if (key.equals("server"))
			return "Server";
		if (key.equals("path"))
			return "Path";
		if (key.equals("https"))
			return "Use HTTPS";
		throw new UnsupportedOperationException("Unsupported key "+key);
	}

}
