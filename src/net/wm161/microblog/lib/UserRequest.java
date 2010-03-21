package net.wm161.microblog.lib;


public class UserRequest extends APIRequest {

	private User m_user;
	private String m_username;
	private OnNewUserHandler m_handler;

	public UserRequest(API api, String user) {
		super(api);
		m_username = user;
	}

	@Override
	protected Boolean doInBackground(Void... arg0) {
		m_user = m_api.getUser(m_username, this);
		return m_user != null;
	}

	public void setOnNewUserHandler(OnNewUserHandler onNewUserHandler) {
		m_handler = onNewUserHandler;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (m_handler != null)
			m_handler.onNewUser(m_user);
	}

}
