package net.wm161.microblog.lib;

public class PublishUpdateRequest extends APIRequest {

	private net.wm161.microblog.lib.Status m_status;

	public PublishUpdateRequest(API api, net.wm161.microblog.lib.Status status) {
		super(api);
		m_status = status;
	}

	@Override
	protected Boolean doInBackground(Void... arg0) {
		return m_api.sendUpdate(m_status, this);
	}

}
