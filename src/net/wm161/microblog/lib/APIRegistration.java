package net.wm161.microblog.lib;

public abstract class APIRegistration {
	public APIRegistration(String name) {
		this.name = name;
	}
	
	public abstract API construct();
	public String name;
}
