package net.wm161.microblog;

import net.wm161.microblog.lib.API;
import net.wm161.microblog.lib.APIManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.GridView;
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
		View tile;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) m_cxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			tile = inflater.inflate(R.layout.api_tile, null);
			tile.setLayoutParams(new Gallery.LayoutParams(128, 128));
			((LinearLayout)tile.findViewById(R.id.tile)).setBackgroundResource(android.R.drawable.btn_default);
		} else {
			tile = convertView;
		}
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
