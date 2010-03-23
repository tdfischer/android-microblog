package net.wm161.microblog.lib.backends.statusnet;

import java.util.Date;

import net.wm161.microblog.lib.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;

public class JSONStatus extends Status {

	private static final long serialVersionUID = 2232111122304335417L;

	public JSONStatus(JSONObject status) throws JSONException {
		setUser(new JSONUser(status.getJSONObject("user")));
		setText(status.getString("text"));
		setId(status.getLong("id"));
		setDate(new Date(status.getString("created_at")));
		setFavorited(status.getString("favorited").equalsIgnoreCase("true"));
		if (!status.isNull("geo")) {
			JSONObject geo = status.getJSONObject("geo");
			if (geo.getString("type").equals("Point")) {
				Location loc = new Location("");
				//TODO: Make sure this gets re-reversed once the twitter api gets fixed
				JSONArray coordinates = geo.getJSONArray("coordinates");
				loc.setLatitude(coordinates.getDouble(0));
				loc.setLongitude(coordinates.getDouble(1));
				setLocation(loc);
			}
		}
	}
}
