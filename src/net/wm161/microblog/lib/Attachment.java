package net.wm161.microblog.lib;

import java.io.IOException;
import java.io.InputStream;

public abstract class Attachment {

	public Attachment() {
		super();
	}

	public abstract InputStream getStream() throws IOException;

	public abstract String contentType();

	public abstract String toString();

	public abstract String name();

}