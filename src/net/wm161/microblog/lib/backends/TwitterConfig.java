package net.wm161.microblog.lib.backends;


public class TwitterConfig extends StatusnetConfig {

	@Override
	public String[] keys() {
		String[] ret = {"https"};
		return ret;
	}

	@Override
	public Object value(String key) {
		if (key == "path")
			return "";
		if (key == "server")
			return "twitter.com";
		return super.value(key);
	}

}
