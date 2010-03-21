package net.wm161.microblog.lib;

public class CreateFavoriteRequest extends APIRequest {

	private net.wm161.microblog.lib.Status m_status;

	public CreateFavoriteRequest(API api, net.wm161.microblog.lib.Status status) {
		super(api);
		m_status = status;
	}

	@Override
	protected Boolean doInBackground(Void... arg0) {
		return m_api.favorite(m_status, this);
	}

}
