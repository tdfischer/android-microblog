package net.wm161.microblog;

import android.app.Activity;
import android.widget.Toast;
import net.wm161.microblog.APIRequest.ErrorType;

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
	public void finished() {
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
