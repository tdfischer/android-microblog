package net.wm161.microblog.lib;



public class DestroyFavoriteRequest extends APIRequest {

	private Object m_id;

	public DestroyFavoriteRequest(Account account, ProgressHandler progress, long id) {
		super(account, progress);
		m_id = id;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			setParameter("id", m_id);
			getData("/favorites/destroy");
		} catch (APIException e) {
			return false;
		}
		return true;
	}
}
