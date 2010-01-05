package net.wm161.microblog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;


public class StatusListAdapter extends BaseAdapter implements ListAdapter {
	private ArrayList<Status> m_statuses;
	private Account m_account;
	private EnumSet<Options> m_options;
	private MicroblogApp m_app;
	private long m_last = 0L;
	
	enum Options {
		DEFAULT,
		NO_USER
	}

	public StatusListAdapter(Account account, Context cxt) {
		m_statuses = new ArrayList<Status>();
		m_account = account;
		m_options = EnumSet.of(Options.DEFAULT);
		m_app = (MicroblogApp) cxt.getApplicationContext();
	}
	
	public long getLastId() {
		return m_last;
	}
	
	@Override
	public int getCount() {
		return m_statuses.size();
	}
	
	@Override
	public boolean hasStableIds() {
		return true;
	}
	
	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}
	
	@Override
	public int getItemViewType(int position) {
		return 0;
	}
	
	@Override
	public int getViewTypeCount() {
		return 1;
	}
	
	@Override
	public boolean isEmpty() {
		return m_statuses.isEmpty();
	}

	@Override
	public Object getItem(int position) {
		return m_statuses.get(position);
	}

	@Override
	public long getItemId(int position) {
		return m_statuses.get(position).id();
	}	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View dentView;
		if (convertView == null) {
			LayoutInflater inflate = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			dentView = inflate.inflate(R.layout.status, null);
		} else {
			dentView = convertView;
		}
		Status status = m_statuses.get(position);
		
		TextView text = (TextView) dentView.findViewById(R.id.text);
		TextView stamp = (TextView) dentView.findViewById(R.id.timestamp);
		text.setText(status.text());
		
		String timestamp;
		Date statusDate = status.getDate();
		Date now = new Date();
		int days = now.getDay() - statusDate.getDay();
		int hours = now.getHours() - statusDate.getHours();
		int minutes = now.getMinutes() - statusDate.getMinutes();
		int seconds = now.getSeconds() - statusDate.getSeconds();
		if (days > 0)
			timestamp = now.toLocaleString();
		else if (hours > 0)
			timestamp = hours+" hours ago";
		else if (minutes > 0)
			timestamp = minutes+" minutes ago";
		else
			timestamp = seconds+" seconds ago";
		stamp.setText(timestamp);
		
		if (!m_options.contains(Options.NO_USER)) {
			ImageView avatarView = (ImageView) dentView.findViewById(R.id.avatar);
			TextView name = (TextView) dentView.findViewById(R.id.name);
		
			DataCache<Long, Avatar> avatarCache = m_app.getAvatarCache(m_account);
			Avatar avatar = avatarCache.get(status.getUser().getId());
			if (avatar == null) {
				avatar = status.getUser().getAvatar();
				avatarCache.put(status.getUser().getId(), avatar);
			}
			avatarView.setImageDrawable(avatar.getBitmap());
			m_account.addLinks(text);
			name.setText(status.getUser().getName());
		} else {
			LinearLayout user = (LinearLayout) dentView.findViewById(R.id.user);
			user.setVisibility(View.GONE);
		}
		
		return dentView;
	}

	public void clear() {
		m_statuses.clear();
		notifyDataSetInvalidated();
	}

	public void addStatus(Status status) {
		if (!m_statuses.contains(status)) {
			m_statuses.add(status);
			if (status.id() > m_last)
				m_last = status.id();
			notifyDataSetChanged();
		}
	}

	public void setStatuses(ArrayList<Status> statuses) {
		m_statuses = statuses;
		notifyDataSetInvalidated();
	}

	public Account getAccount() {
		return m_account;
	}

	public void setOptions(EnumSet<Options> options) {
		m_options = options;
	}

	public EnumSet<Options> getOptions() {
		return m_options;
	}

}
