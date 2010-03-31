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

import net.wm161.microblog.lib.Avatar;
import net.wm161.microblog.lib.CacheManager;
import net.wm161.microblog.lib.DataCache;
import net.wm161.microblog.lib.Status;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StatusView extends LinearLayout {

	private View m_dentView;
	private GeocodeTask m_geocode = null;

	public StatusView(Context context, Status status) {
		super(context);
		LayoutInflater inflate = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		m_dentView = inflate.inflate(R.layout.status, null);
		m_dentView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		addView(m_dentView);
		setStatus(status);
	}

	public void setStatus(Status status) {
		if (m_geocode != null)
			m_geocode.cancel(true);
		TextView text = (TextView) m_dentView.findViewById(R.id.text);
		TextView details = (TextView) m_dentView.findViewById(R.id.details);
		text.setText(status.text());
		if (status.getLocation() == null) {
			details.setText(status.getTimestamp());
		} else {
			m_geocode = new GeocodeTask(getContext(), status, details) {

				@Override
				protected void onPostExecute(String result) {
					super.onPostExecute(result);
					m_geocode = null;
				}
				
			};
			m_geocode.execute();
		}
		
		ImageView avatarView = (ImageView) m_dentView.findViewById(R.id.avatar);
		TextView name = (TextView) m_dentView.findViewById(R.id.name);
	
		//FIXME: More than just the default account
		DataCache<Long, Avatar> avatarCache = ((MicroblogApp)getContext().getApplicationContext()).getCacheManager().getCache(((MicroblogApp)getContext().getApplicationContext()).getPreferences().getDefaultAccount(), CacheManager.CacheType.Avatar);
		
		Avatar avatar = null;
		//FIXME: Why does this need to be done here, and not in get()?
		try {
			avatar = avatarCache.get(status.getUser().getId());
		} catch (ClassCastException e) {
		}
		if (avatar == null) {
			avatar = status.getUser().getAvatar();
			avatarCache.put(status.getUser().getId(), avatar);
		}
		avatarView.setImageDrawable(avatar.getBitmap());
		//m_timeline.addLinks(text);
		name.setText(status.getUser().getScreenName());
		
		if (status.hasAttachments()) {
			findViewById(R.id.has_attachment).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.has_attachment).setVisibility(View.GONE);
		}
	}

}
