package net.wm161.microblog.lib;


public class StatusRequest extends APIRequest {

	private net.wm161.microblog.lib.Status m_status;
	OnNewStatusHandler m_handler;

	public StatusRequest(API api, long status) {
		super(api);
		m_status = new net.wm161.microblog.lib.Status();
		m_status.setId(status);
		m_handler = null;
	}

	@Override
	protected Boolean doInBackground(Void... arg0) {
		m_status = m_api.getStatus(m_status.id(), this);
		return m_status != null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (result && m_handler != null) {
			m_handler.onNewStatus(m_status);
		}
	}

	public void setOnNewStatusHandler(OnNewStatusHandler onNewStatusHandler) {
		m_handler = onNewStatusHandler;
	}

}
