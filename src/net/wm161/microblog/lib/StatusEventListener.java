package net.wm161.microblog.lib;

import net.wm161.microblog.ViewStatus;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class StatusEventListener implements OnItemClickListener {
	
	Activity m_activity;
	private Account m_account;
	public StatusEventListener(Activity cxt, Account account) {
		m_activity = cxt;
		m_account = account;
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
		Intent viewIntent = new Intent(m_activity, ViewStatus.class);
		viewIntent.putExtra("account", m_account.getGuid());
		viewIntent.putExtra("status", id);
		Log.d("GlobalTimeline", "Starting "+viewIntent);
		m_activity.startActivity(viewIntent);
	}

}