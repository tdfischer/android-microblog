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

import net.wm161.microblog.lib.APIRequest.ErrorType;
import android.app.Activity;
import android.widget.Toast;

public class ActivityProgressHandler implements ProgressHandler {

	Activity m_activity;
	
	public ActivityProgressHandler(Activity activity) {
		m_activity = activity;
	}
	
	@Override
	public void error(ErrorType errorType, String errorString) {
		Toast.makeText(m_activity, errorString, Toast.LENGTH_LONG).show();
	}

	@Override
	public void finished(Boolean success) {
		m_activity.setProgressBarVisibility(false);
	}

	@Override
	public void starting() {
		m_activity.setProgress(0);
		m_activity.setProgressBarIndeterminate(true);
		m_activity.setProgressBarVisibility(true);
	}

	@Override
	public void updated(APIProgress progress) {
		m_activity.setProgressBarIndeterminate(false);
		m_activity.setProgress(progress.getProgress());
	}

}
