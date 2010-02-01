package net.wm161.microblog;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;

public abstract class TimelineActivity extends ListActivity {

	private StatusListAdapter m_statusList;
	private Account m_account;
	
	public StatusListAdapter getStatusList() {
		return m_statusList;
	}

	public void setStatusList(StatusListAdapter statusList) {
		m_statusList = statusList;
        getListView().setAdapter(m_statusList);
	}

	public Account getAccount() {
		return m_account;
	}

	public void setAccount(Account account) {
		m_account = account;
	}

	private StatusEventListener m_events;
	private int m_interval = 1000*60*5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        MicroblogApp app = (MicroblogApp) getApplication();
		Preferences prefs = app.getPreferences();
        m_account = prefs.getAccount(getIntent().getStringExtra("account"));
        setTitle(m_account.getName());
        m_events = new StatusEventListener(this, m_account);
        ListView dents = getListView();
		dents.setOnItemClickListener(m_events);
		registerForContextMenu(dents);
        dents.setEmptyView(getLayoutInflater().inflate(R.layout.loading, null));
        setStatusList(new StatusListAdapter(m_account, this));
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		int id = info.position;
		Status status = (Status) getStatusList().getItem(id);
		
		//Intent statusView = new Intent(this, )
		//menu.add("View Status").setIntent(new Intent(Intent.ACTION_VIEW, location));
		
		Intent userView = new Intent(this, ViewUser.class);
		userView.putExtra("account", m_account.getGuid());
		userView.putExtra("user", status.getUser().getId());
		//menu.add("View "+status.getUser().getName()).setIntent(userView);
		
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		String text; 
		String user;
		user = (String) status.getUser().getScreenName();
		text = (String) status.text();
		text = "@"+user+" "+text;
		shareIntent.putExtra(Intent.EXTRA_TEXT, text);
		shareIntent.setType("text/plain");
		menu.add("Share").setIntent(Intent.createChooser(shareIntent, ""));
	}
	
	public abstract void refresh();
	
    /* Update Timer */
    private Handler m_handler = new Handler();
    private Runnable m_refresher = new Runnable() {

		@Override
		public void run() {
			if (m_interval > 0)
				m_handler.postDelayed(this, m_interval);
			refresh();
		}
    	
    };
    
	@Override
	protected void onPause() {
		super.onPause();
		pauseUpdates();
	}

	@Override
	protected void onResume() {
		super.onResume();
		resumeUpdates();
	}
	
	public void pauseUpdates() {
		m_handler.removeCallbacks(m_refresher);
	}
	
	public void resumeUpdates() {
		m_handler.postDelayed(m_refresher, 0);
	}

	public void setInterval(int interval) {
		m_interval = interval;
	}

	public int getInterval() {
		return m_interval;
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		getListView().setAdapter(m_statusList);
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		super.setContentView(view, params);
		getListView().setAdapter(m_statusList);
	}

	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		getListView().setAdapter(m_statusList);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add("Update").setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				refresh();
				return true;
			}
		});
		return true;
	}
	
}
