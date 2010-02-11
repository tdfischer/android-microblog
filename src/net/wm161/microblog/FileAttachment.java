package net.wm161.microblog;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.content.ContentResolver;
import android.net.Uri;

public class FileAttachment {
	private ContentResolver m_resolver;
	private Uri m_uri;
	
	public FileAttachment(ContentResolver resolver, Uri uri) {
		m_uri = uri;
		m_resolver = resolver;
	}
	
	public String contentType() {
		return m_resolver.getType(m_uri);
	}
	
	public InputStream getStream() throws FileNotFoundException {
		return m_resolver.openInputStream(m_uri);
	}
	
	public String name() {
		return m_uri.toString();
	}
	
	public String toString() {
		return m_uri.toString();
	}
}
