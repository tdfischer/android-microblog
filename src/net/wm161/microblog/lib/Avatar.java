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
		if (m_bitmap != null)
			m_bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		BitmapDrawable d = new BitmapDrawable(in);
		m_bitmap = d.getBitmap();
	}
}
