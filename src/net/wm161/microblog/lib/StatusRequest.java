/**
 *  This file is part of android-microblog
 *  Copyright (C) 2010 Trever Fischer <tdfischer@fedoraproject.org>
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

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
