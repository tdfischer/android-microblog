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
import net.wm161.microblog.lib.APIRequest;
import net.wm161.microblog.lib.ActivityProgressHandler;
import net.wm161.microblog.lib.CreateFavoriteRequest;
import net.wm161.microblog.lib.DataCache;
import net.wm161.microblog.lib.DestroyFavoriteRequest;
import net.wm161.microblog.lib.OnNewStatusHandler;
import net.wm161.microblog.lib.Status;
import net.wm161.microblog.lib.StatusRequest;
import net.wm161.microblog.lib.CacheManager.CacheType;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewStatus extends Activity {

	private MicroblogAccount m_account;
	private net.wm161.microblog.lib.Status m_status = null;
	private API m_api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Preferences prefs = ((MicroblogApp)getApplication()).getPreferences();
        m_account = prefs.getAccount(getIntent().getStringExtra("account"));
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.loading);
        
		m_api = m_account.getAPIInstance();
        
        StatusRequest req = new StatusRequest(m_api, getIntent().getLongExtra("status", 0));
        req.setProgressHandler(new ActivityProgressHandler(this));
        req.setOnNewStatusHandler(new OnNewStatusHandler() {

			@Override
			public void onNewStatus(Status status) {
				m_status = status;
				setContentView(R.layout.view_status);
				ImageView avatar = (ImageView) findViewById(R.id.avatar);
				avatar.setImageDrawable(status.getUser().getAvatar().getBitmap());
				TextView user = (TextView) findViewById(R.id.name);
				user.setText(status.getUser().getScreenName());
				TextView text = (TextView) findViewById(R.id.text);
				text.setText(status.text());
				TextView details = (TextView) findViewById(R.id.details);
				Gallery attachments = (Gallery)findViewById(R.id.attachments);
				attachments.setAdapter(new AttachmentAdapter(ViewStatus.this, status.getAttachments()));
				if (status.getLocation() == null) {
					details.setText(status.getTimestamp(ViewStatus.this));
				} else {
					DataCache<Double, String> geocache = ((MicroblogApp)getApplication()).getCacheManager().getCache(((MicroblogApp)getApplication()).getPreferences().getDefaultAccount(), CacheType.Geocode);
					String location;
					//FIXME: Replace this with the code from the timeline adapter
					if ((location = geocache.get(status.getLocation().getLatitude()+2*status.getLocation().getLongitude())) == null) {
						details.setText(status.getTimestamp(ViewStatus.this));
						GeocodeTask task = new GeocodeTask(((MicroblogApp)getApplication()), status, details);
						task.execute();
					} else {
						details.setText(getString(R.string._from_, status.getTimestamp(ViewStatus.this), location));
					}
				}
				
				LinearLayout userBox = (LinearLayout) findViewById(R.id.user);
				userBox.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent view = new Intent(ViewStatus.this, ViewUser.class);
						view.putExtra("account", m_account.getGuid());
						view.putExtra("user", m_status.getUser().getId());
						startActivity(view);
					}
					
				});
				//getWindow().setBackgroundDrawable(status().getUser().getBackground());
			}
        	
        });
        req.execute();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		//FIXME: This formatting should be handled by the API
		shareIntent.putExtra(Intent.EXTRA_TEXT, "@"+m_status.getUser().getScreenName()+" "+m_status.text());
		shareIntent.setType("text/plain");
		menu.add(R.string.share).setIntent(Intent.createChooser(shareIntent, "")).setIcon(android.R.drawable.ic_menu_share); 
		menu.add(R.string.favorite).setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				if (m_status != null) {
					APIRequest req;
					if (m_status.isFavorited()) {
						req = new DestroyFavoriteRequest(m_api, m_status);
						req.setProgressHandler(new ActivityProgressHandler(ViewStatus.this) {
							@Override
							public void finished(Boolean result) {
								super.finished(result);
								if (result) {
									Toast.makeText(ViewStatus.this, R.string.un_favorited_, Toast.LENGTH_SHORT).show();
								}
							}
						});
					} else {
						req = new CreateFavoriteRequest(m_api, m_status);
						req.setProgressHandler(new ActivityProgressHandler(ViewStatus.this) {
							@Override
							public void finished(Boolean result) {
								super.finished(result);
								if (result) {
									Toast.makeText(ViewStatus.this, R.string.favorited_, Toast.LENGTH_SHORT).show();
								}
							}
						});
					}
					req.execute();
				}
				return true;
			}
			
		}).setIcon(android.R.drawable.btn_star);
		return true;
	}
}
