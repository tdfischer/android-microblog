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
