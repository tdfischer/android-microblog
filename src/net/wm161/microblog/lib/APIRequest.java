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

import android.os.AsyncTask;
import android.util.Log;
import net.wm161.microblog.R;

public abstract class APIRequest extends AsyncTask<Void, APIProgress, Boolean> {
	
	private ErrorType m_error;
	private ProgressHandler m_progress;
	protected API m_api;
	
	public enum ErrorType {
		ERROR_NONE,
		ERROR_CONNECTION_FAILED,
		ERROR_CONNECTION_BROKEN,
		ERROR_PARSE,
		ERROR_SERVER,
		ERROR_INTERNAL,
		ERROR_ATTACHMENT_NOT_FOUND
	}
	
	public APIRequest(API api) {
		super();
		m_api = api;
		m_error = ErrorType.ERROR_NONE;
	}
	
	public void setError(ErrorType e) {
		m_error = e;
	}
	
	public ErrorType getError() {
		return m_error;
	}
	
	public int getErrorString(ErrorType e) {
		switch (e) {
		case ERROR_NONE:
			return R.string.no_error_;
		case ERROR_CONNECTION_FAILED:
			return R.string.connection_failed_;
		case ERROR_CONNECTION_BROKEN:
			return R.string.connection_interrupted_;
		case ERROR_PARSE:
			return R.string.recieved_unintelligible_gibberish_;
		case ERROR_SERVER:
			return R.string.server_error_;
		case ERROR_INTERNAL:
			return R.string.internal_error_please_send_a_bug_report_;
		case ERROR_ATTACHMENT_NOT_FOUND:
			return R.string.attachment_not_found_;
		default:
			return -1;
		}
	}
	
	
	public void setProgressHandler(ProgressHandler progress) {
		m_progress = progress;
	}

	@Override
	protected abstract Boolean doInBackground(Void... arg0);
	
	
	@Override
	protected void onPreExecute() {
		if (m_progress != null)
			m_progress.starting();
	}
	
	@Override
	protected void onProgressUpdate(APIProgress... progress) {
		if (m_progress != null)
			m_progress.updated(progress[0]);
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		if (!result) {
			Log.w("APIRequest", "Execution failed: "+getError());
			if (m_progress != null)
				m_progress.error(getError(), getErrorString(getError()));
		}
		if (m_progress != null)
			m_progress.finished(result);
	}
	
	public void publishProgress(APIProgress progress) {
		super.publishProgress(progress);
	}

}
