package net.wm161.microblog.lib.backends.twitter;

import java.util.Date;

import net.wm161.microblog.lib.Status;
import net.wm161.microblog.lib.User;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONStatus extends Status {

	private static final long serialVersionUID = 2232111122304335417L;

	public JSONStatus(JSONObject status) throws JSONException {
		setUser(new User(status.getJSONObject("user")));
		setText(status.getString("text"));
		setId(status.getLong("id"));
		setDate(new Date(status.getString("created_at")));
		setFavorited(status.getString("favorited").equalsIgnoreCase("true"));
	}

}
