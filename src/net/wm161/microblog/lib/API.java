package net.wm161.microblog.lib;

import java.util.EnumSet;

public abstract class API {
	
	public static final int VERSION = 1;
		
	public enum Feature {
		Timeline,
	}
	
	public enum TimelineType {
		Home,
		Public,
		User
	}
	private Account m_account;
	
	public void setAccount(Account account) {
		m_account = account;
	}
	
	public Account getAccount() {
		return m_account;
	}
	
	public abstract EnumSet<TimelineType> supportedTimelines();
	public abstract boolean updateGlobalTimeline(APIRequest request, Timeline timeline) throws APIException;
	public abstract boolean updateHomeTimeline(APIRequest request, Timeline timeline) throws APIException;
	public abstract boolean updateUserTimeline(User user, APIRequest request, Timeline timeline) throws APIException;
	public abstract User getUser(String user, APIRequest request);
	public abstract net.wm161.microblog.lib.Status getStatus(long status, APIRequest request);
	public abstract boolean sendUpdate(net.wm161.microblog.lib.Status status, APIRequest request);
	public abstract boolean favorite(net.wm161.microblog.lib.Status status, APIRequest request);
	public abstract boolean unfavorite(net.wm161.microblog.lib.Status status, APIRequest request);
	public abstract int getIcon();
	public abstract APIConfiguration configuration();
	public abstract String getName();
	public abstract int getVersion();
}
