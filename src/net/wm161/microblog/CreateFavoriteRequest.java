package net.wm161.microblog;


public class CreateFavoriteRequest extends APIRequest {

	private Object m_id;

	public CreateFavoriteRequest(Account account, ProgressHandler progress, long id) {
		super(account, progress);
		m_id = id;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			setParameter("id", m_id);
			getData("/favorites/create");
		} catch (APIException e) {
			return false;
		}
		return true;
	}

}
