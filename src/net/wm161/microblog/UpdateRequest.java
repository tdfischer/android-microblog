package net.wm161.microblog;


public class UpdateRequest extends APIRequest {

	private String m_status;
	public UpdateRequest(Account account, ProgressHandler progress, String status) {
		super(account, progress);
		m_status = status;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			setParameter("status", m_status);
			getData("statuses/update");
		} catch (APIException e) {
			return false;
		}
		return true;
	}
}
