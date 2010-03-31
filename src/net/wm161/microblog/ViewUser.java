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

import net.wm161.microblog.lib.ActivityProgressHandler;
import net.wm161.microblog.lib.OnNewUserHandler;
import net.wm161.microblog.lib.User;
import net.wm161.microblog.lib.UserRequest;
import net.wm161.microblog.lib.UserTimelineRequest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewUser extends TimelineActivity {
	private User m_user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_user);
		Intent intent = getIntent();
		String user = intent.getStringExtra("user");
		if (user == null)
			user = String.valueOf(intent.getLongExtra("user", 0L));
		ViewStub stub = (ViewStub) findViewById(R.id.load_stub);
		stub.inflate();
		UserRequest req = new UserRequest(getAPI(), user);
		req.setProgressHandler(new ActivityProgressHandler(this));

		req.setOnNewUserHandler(new OnNewUserHandler() {

			@Override
			public void onNewUser(User user) {
				m_user = user;
				refresh();
				LinearLayout area = (LinearLayout) findViewById(R.id.dent_area);
				area.setVisibility(View.VISIBLE);
				ViewStub stub = (ViewStub) findViewById(R.id.user_area);
				stub.inflate();
				LinearLayout userBox = (LinearLayout) findViewById(R.id.user_area);
				
				//getWindow().setBackgroundDrawable(user.getBackground());
				
				userBox.setBackgroundDrawable(user.getSidebarBackground());
				
				setTitle(getAPI().getAccount().getName()+" - User "+user.getScreenName());
				TextView screenName = (TextView) findViewById(R.id.screenname);
				screenName.setText(user.getScreenName());
				TextView name = (TextView) findViewById(R.id.name);
				name.setText(user.getScreenName());
				TextView location = (TextView) findViewById(R.id.location);
				location.setText(user.getLocation());
				TextView subscriptions = (TextView) findViewById(R.id.subscriptions);
				subscriptions.setText(user.getSubscriptions()+" subscriptions");
				TextView statuses = (TextView) findViewById(R.id.statuses);
				statuses.setText(user.getStatuses()+" statuses");
				TextView subscribers = (TextView) findViewById(R.id.subscribers);
				subscribers.setText(user.getSubscribers()+ " subscribers");
				
				ImageView avatar = (ImageView) findViewById(R.id.avatar);
				//TODO: avatar cache!
				avatar.setImageDrawable(user.getAvatar().getBitmap());
				
				TextView url = (TextView) findViewById(R.id.url);
				if (user.getUrl() != null)
					url.setText(user.getUrl().toString());
				//EnumSet<TimelineAdapter.Options> options = EnumSet.of(TimelineAdapter.Options.DEFAULT, TimelineAdapter.Options.NO_USER);
				//getStatusList().setOptions(options);
				//setListAdapter(getStatusList());
				//setEmptyView(getLayoutInflater().inflate(R.layout.loading, null));
				findViewById(android.R.id.empty).setVisibility(View.GONE);
			}
		});
		req.execute();
	}
	@Override
	public void refresh() {
		if (m_user != null) {
			UserTimelineRequest timelineReq = new UserTimelineRequest(getAPI(), m_user, m_timeline);
			timelineReq.setProgressHandler(new ActivityProgressHandler(this));
			timelineReq.execute();
		}
	}
}
