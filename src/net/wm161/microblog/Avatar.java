package net.wm161.microblog;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class Avatar implements Serializable {

	private static final long serialVersionUID = 5430749539823927013L;
	private Bitmap m_bitmap;
	private URL m_url;
	
	public Avatar(URL location) {
		m_url = location;
	}

	public BitmapDrawable getBitmap() {
		if (m_bitmap == null) {
			URLConnection c;
			try {
				c = m_url.openConnection();
				c.setUseCaches(true);
				BitmapDrawable d= new BitmapDrawable(c.getInputStream());
				m_bitmap = d.getBitmap();
				return d;
			} catch (IOException e) {
				BitmapDrawable b = new BitmapDrawable();
				m_bitmap = b.getBitmap();
				return b;
			}
		}
		return new BitmapDrawable(m_bitmap);
	}
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		getBitmap();
		m_bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		BitmapDrawable d = new BitmapDrawable(in);
		m_bitmap = d.getBitmap();
	}


}
