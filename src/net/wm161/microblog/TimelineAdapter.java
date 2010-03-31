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

import java.util.EnumSet;

import net.wm161.microblog.lib.Account;
import net.wm161.microblog.lib.OnNewStatusHandler;
import net.wm161.microblog.lib.Status;
import net.wm161.microblog.lib.Timeline;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;


public class TimelineAdapter extends BaseAdapter implements ListAdapter {
	private Timeline m_timeline;
	private EnumSet<Options> m_options;
	private MicroblogApp m_app;
	public enum Options {
		DEFAULT,
		NO_USER
	}
	
	private Handler m_handler = new Handler();
    private Runnable m_refresher = new Runnable() {

		@Override
		public void run() {
			notifyDataSetChanged();
		}
    	
    };

	public TimelineAdapter(Timeline timeline, Context cxt) {
		m_timeline = timeline;
		m_timeline.setOnNewStatusHandler(new OnNewStatusHandler() {
			
			@Override
			public void onNewStatus(Status s) {
				m_handler.post(m_refresher);
			}
		});
		m_options = EnumSet.of(Options.DEFAULT);
		m_app = (MicroblogApp) cxt.getApplicationContext();
	}
	
	@Override
	public int getCount() {
		return m_timeline.size();
	}
	
	@Override
	public boolean hasStableIds() {
		return true;
	}
	
	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}
	
	@Override
	public int getItemViewType(int position) {
		return 0;
	}
	
	@Override
	public int getViewTypeCount() {
		return 1;
	}
	
	@Override
	public boolean isEmpty() {
		return m_timeline.isEmpty();
	}

	@Override
	public Object getItem(int position) {
		return m_timeline.get(position);
	}

	@Override
	public long getItemId(int position) {
		return m_timeline.get(position).id();
	}	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		StatusView dentView;
		Status status = m_timeline.get(position);
		if (convertView == null) {
			dentView = new StatusView(m_app, status);
		} else {
			dentView = (StatusView)convertView;
		}
		dentView.setStatus(status);
		return dentView;
	}

	public void clear() {
		m_timeline.clear();
		notifyDataSetInvalidated();
	}

	public Account getAccount() {
		//FIXME: More than just the default
		return m_app.getPreferences().getDefaultAccount();
	}

	public void setOptions(EnumSet<Options> options) {
		m_options = options;
	}

	public EnumSet<Options> getOptions() {
		return m_options;
	}

}
