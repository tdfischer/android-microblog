package net.wm161.microblog.lib;

public class DestroyFavoriteRequest extends APIRequest {

	private net.wm161.microblog.lib.Status m_status;

	public DestroyFavoriteRequest(API api, net.wm161.microblog.lib.Status status) {
		super(api);
		m_status = status;
	}

	@Override
	protected Boolean doInBackground(Void... arg0) {
		return m_api.unfavorite(m_status, this);
	}

}
