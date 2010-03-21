package net.wm161.microblog;

import java.util.EnumSet;

import net.wm161.microblog.lib.ActivityProgressHandler;
import net.wm161.microblog.lib.OnNewUserHandler;
import net.wm161.microblog.lib.User;
import net.wm161.microblog.lib.UserRequest;
import net.wm161.microblog.lib.UserTimelineRequest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewUser extends TimelineActivity {
	private User m_user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_user);
		Intent intent = getIntent();
		String user = intent.getStringExtra("user");
		if (user == null)
			user = String.valueOf(intent.getLongExtra("user", 0L));
		ViewStub stub = (ViewStub) findViewById(R.id.load_stub);
		stub.inflate();
		UserRequest req = new UserRequest(getAPI(), user);
		req.setProgressHandler(new ActivityProgressHandler(this));

		req.setOnNewUserHandler(new OnNewUserHandler() {

			@Override
			public void onNewUser(User user) {
				m_user = user;
				refresh();
				LinearLayout area = (LinearLayout) findViewById(R.id.dent_area);
				area.setVisibility(View.VISIBLE);
				ViewStub stub = (ViewStub) findViewById(R.id.user_area);
				stub.inflate();
				LinearLayout userBox = (LinearLayout) findViewById(R.id.user_area);
				
				//getWindow().setBackgroundDrawable(user.getBackground());
				
				userBox.setBackgroundDrawable(user.getSidebarBackground());
				
				setTitle(getAPI().getAccount().getName()+" - User "+user.getScreenName());
				TextView screenName = (TextView) findViewById(R.id.screenname);
				screenName.setText(user.getScreenName());
				TextView name = (TextView) findViewById(R.id.name);
				name.setText(user.getScreenName());
				TextView location = (TextView) findViewById(R.id.location);
				location.setText(user.getLocation());
				TextView subscriptions = (TextView) findViewById(R.id.subscriptions);
				subscriptions.setText(user.getSubscriptions()+" subscriptions");
				TextView statuses = (TextView) findViewById(R.id.statuses);
				statuses.setText(user.getStatuses()+" statuses");
				TextView subscribers = (TextView) findViewById(R.id.subscribers);
				subscribers.setText(user.getSubscribers()+ " subscribers");
				
				ImageView avatar = (ImageView) findViewById(R.id.avatar);
				//TODO: avatar cache!
				avatar.setImageDrawable(user.getAvatar().getBitmap());
				
				TextView url = (TextView) findViewById(R.id.url);
				if (user.getUrl() != null)
					url.setText(user.getUrl().toString());
				EnumSet<TimelineAdapter.Options> options = EnumSet.of(TimelineAdapter.Options.DEFAULT, TimelineAdapter.Options.NO_USER);
				getStatusList().setOptions(options);
				setListAdapter(getStatusList());
				//setEmptyView(getLayoutInflater().inflate(R.layout.loading, null));
			}
		});
		req.execute();
	}
	@Override
	public void refresh() {
		if (m_user != null) {
			UserTimelineRequest timelineReq = new UserTimelineRequest(getAPI(), m_user, m_timeline);
			timelineReq.execute();
		}
	}
}
