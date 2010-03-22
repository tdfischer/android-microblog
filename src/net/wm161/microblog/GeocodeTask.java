package net.wm161.microblog;

import java.io.IOException;
import java.util.List;

import net.wm161.microblog.lib.DataCache;
import net.wm161.microblog.lib.Status;
import net.wm161.microblog.lib.CacheManager.CacheType;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class GeocodeTask extends AsyncTask<Void, Void, String> {

	private Context m_cxt;
	private Location m_location;
	private TextView m_text;

	public GeocodeTask(Context cxt, Location location, TextView text) {
		m_cxt = cxt;
		m_location = location;
		m_text = text;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		if (m_location != null ) {
			Geocoder decoder = new Geocoder(m_cxt, m_cxt.getResources().getConfiguration().locale);
			Log.d("TimelineAdapter", "Looking up location for "+m_location.getLatitude()+","+m_location.getLongitude());
			List<Address> locations = null;
			try {
				locations = decoder.getFromLocation(m_location.getLatitude(), m_location.getLongitude(), 1);
			} catch (IOException e) {
				Log.d("TimelineAdapter", "Couldn't geocode location");
			}
			if (locations != null && locations.size() > 0) {
				DataCache<Double, String> geocache = ((MicroblogApp)m_cxt.getApplicationContext()).getCacheManager().getCache(((MicroblogApp)m_cxt.getApplicationContext()).getPreferences().getDefaultAccount(), CacheType.Geocode);
				String location = locations.get(0).getLocality()+", "+locations.get(0).getCountryName();
				geocache.put(m_location.getLatitude()+2*m_location.getLongitude(), location);
				return location;
			}
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		if (result != null)
			m_text.setText(m_text.getText()+", from "+result);
	}

}
