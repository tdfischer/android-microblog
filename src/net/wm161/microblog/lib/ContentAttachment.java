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

package net.wm161.microblog.lib;


import java.io.FileNotFoundException;
import java.io.InputStream;

import android.content.ContentResolver;
import android.net.Uri;

public class ContentAttachment extends Attachment {
	private ContentResolver m_resolver;
	Uri m_uri;
	
	public ContentAttachment(ContentResolver resolver, Uri uri) {
		m_uri = uri;
		m_resolver = resolver;
	}
	
	@Override
	public String contentType() {
		return m_resolver.getType(m_uri);
	}
	
	@Override
	public InputStream getStream() throws FileNotFoundException {
		return m_resolver.openInputStream(m_uri);
	}

	@Override
	public String name() {
		return m_uri.toString();
	}

	@Override
	public String toString() {
		return m_uri.toString();
	}
}
