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

import java.io.IOException;
import java.util.List;

import net.wm161.microblog.lib.DataCache;
import net.wm161.microblog.lib.CacheManager.CacheType;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.TextView;

public class GeocodeTask extends AsyncTask<Void, Void, String> {

	private Context m_cxt;
	private net.wm161.microblog.lib.Status m_status;
	private TextView m_text;
	private boolean m_canceled;

	public GeocodeTask(Context cxt, net.wm161.microblog.lib.Status status, TextView text) {
		m_cxt = cxt;
		m_status = status;
		m_text = text;
		m_canceled = false;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		if (m_status != null && m_status.getLocation() != null) {
			DataCache<Double, String> geocache = ((MicroblogApp)m_cxt.getApplicationContext()).getCacheManager().getCache(((MicroblogApp)m_cxt.getApplicationContext()).getPreferences().getDefaultAccount(), CacheType.Geocode);
			String location = geocache.get(m_status.getLocation().getLatitude()+2*m_status.getLocation().getLongitude());
			if (location != null)
				return location;
			Geocoder decoder = new Geocoder(m_cxt, m_cxt.getResources().getConfiguration().locale);
			List<Address> locations = null;
			try {
				locations = decoder.getFromLocation(m_status.getLocation().getLatitude(), m_status.getLocation().getLongitude(), 1);
			} catch (IOException e) {
			}
			if (locations != null && locations.size() > 0) {
				//FIXME: Make this translatable
				location = locations.get(0).getLocality()+", "+locations.get(0).getCountryName();
				geocache.put(m_status.getLocation().getLatitude()+2*m_status.getLocation().getLongitude(), location);
				return location;
			}
			geocache.put(m_status.getLocation().getLatitude()+2*m_status.getLocation().getLongitude(), "");
		}
		return null;
	}
	
	public void cancel() {
		cancel(true);
		m_canceled = true;
	}
	
	public boolean isCanceled() {
		return m_canceled;
	}
	
	@Override
	protected void onPostExecute(String result) {
		if (!m_canceled) {
			if (result != null && result.length()>0) {
				m_text.setText(m_cxt.getString(R.string._from_, m_status.getTimestamp(m_cxt), result));
			} else {
				m_text.setText(m_status.getTimestamp(m_cxt));
			}
		}
	}

}
