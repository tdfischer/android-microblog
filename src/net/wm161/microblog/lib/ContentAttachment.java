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
