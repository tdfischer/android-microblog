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

package net.wm161.microblog;

import net.wm161.microblog.lib.API;
import net.wm161.microblog.lib.Status;
import net.wm161.microblog.lib.Timeline;
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

	private TimelineAdapter m_statusList;
	private MicroblogAccount m_account;
	protected Timeline m_timeline;
	
	public TimelineAdapter getStatusList() {
		return m_statusList;
	}
	
	public API getAPI() {
		API api = m_account.getAPIInstance();
		return api;
	}

	public void setStatusList(TimelineAdapter statusList) {
		m_statusList = statusList;
        getListView().setAdapter(m_statusList);
	}

	public void setAccount(MicroblogAccount account) {
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
        
        m_timeline = new Timeline();
        setStatusList(new TimelineAdapter(m_timeline, this));
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
		}).setIcon(android.R.drawable.ic_menu_view);
		return true;
	}
	
}
