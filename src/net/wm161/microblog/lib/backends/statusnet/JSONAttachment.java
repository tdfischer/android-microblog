package net.wm161.microblog.lib.backends.statusnet;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import net.wm161.microblog.lib.Attachment;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONAttachment extends Attachment {

	private URL m_url;
	private String m_type;

	public JSONAttachment(JSONObject attachment) throws JSONException, MalformedURLException {
		m_url = new URL(attachment.getString("url"));
		m_type = attachment.getString("mimetype");
	}

	@Override
	public String contentType() {
		return m_type;
	}

	@Override
	public InputStream getStream() throws IOException {
		return m_url.openStream();
	}

	@Override
	public String name() {
		return m_url.toString();
	}

	@Override
	public String toString() {
		return m_url.toString();
	}

}
