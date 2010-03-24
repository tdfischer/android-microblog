package net.wm161.microblog;

import java.io.IOException;

import net.wm161.microblog.lib.Attachment;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class AttachmentAdapter extends BaseAdapter {
	
	private Attachment[] m_attachments;
	private Context m_cxt;

	public AttachmentAdapter(Context cxt, Attachment[] attachments) {
		m_attachments = attachments.clone();
		m_cxt = cxt;
	}

	@Override
	public int getCount() {
		return m_attachments.length;
	}

	@Override
	public Object getItem(int pos) {
		return m_attachments[pos];
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View recycleView, ViewGroup root) {
		ImageView icon = new ImageView(m_cxt);
		icon.setScaleType(ImageView.ScaleType.FIT_START);
		icon.setMaxHeight(64);
		icon.setMaxWidth(64);
		icon.setMinimumHeight(64);
		icon.setMinimumWidth(64);
		try {
			icon.setImageDrawable(new BitmapDrawable(m_attachments[pos].getStream()));
		} catch (IOException e) {
			icon.setImageResource(android.R.drawable.ic_menu_delete);
		}
		icon.setBackgroundResource(android.R.drawable.gallery_thumb);
		return icon;
	}

}
