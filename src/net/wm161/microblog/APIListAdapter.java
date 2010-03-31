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

package net.wm161.microblog;

import net.wm161.microblog.lib.API;
import net.wm161.microblog.lib.APIManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class APIListAdapter extends BaseAdapter {
	
	public String[] m_apis = APIManager.getAPIs();
	private Context m_cxt;

	public APIListAdapter(Context cxt) {
		m_cxt = cxt;
	}
	
	@Override
	public int getCount() {
		return m_apis.length;
	}

	@Override
	public Object getItem(int arg0) {
		return m_apis[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) m_cxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View tile = inflater.inflate(R.layout.api_tile, null);
		tile.setLayoutParams(new Gallery.LayoutParams(128, 128));
		((LinearLayout)tile.findViewById(R.id.tile)).setBackgroundResource(android.R.drawable.btn_default);
		API apiInstance = APIManager.getAPI(m_apis[position]);
		ImageView icon = ((ImageView)tile.findViewById(R.id.icon));
		TextView label = ((TextView)tile.findViewById(R.id.label));
		icon.setImageResource(apiInstance.getIcon());
		icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
		label.setText(apiInstance.getName());
		tile.setTag(m_apis[position]);
		return tile;
	}

}
