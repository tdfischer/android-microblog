package net.wm161.microblog;

import java.util.Date;

import net.wm161.microblog.lib.APIManager;
import net.wm161.microblog.lib.APIRequest;
import net.wm161.microblog.lib.ActivityProgressHandler;
import net.wm161.microblog.lib.CreateFavoriteRequest;
import net.wm161.microblog.lib.DestroyFavoriteRequest;
import net.wm161.microblog.lib.OnNewStatusHandler;
import net.wm161.microblog.lib.Status;
import net.wm161.microblog.lib.StatusRequest;
import net.wm161.microblog.lib.backends.Twitter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewStatus extends Activity {

	private MicroblogAccount m_account;
	private net.wm161.microblog.lib.Status m_status = null;
	private Twitter m_api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Preferences prefs = ((MicroblogApp)getApplication()).getPreferences();
        m_account = prefs.getAccount(getIntent().getStringExtra("account"));
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.loading);
        
		//FIXME: Copied from TimelineActivity
		APIManager.getAPI(m_account.getAPI());
		m_api = new Twitter();
		m_api.setAccount(m_account);
        
        StatusRequest req = new StatusRequest(m_api, getIntent().getLongExtra("status", 0));
        req.setProgressHandler(new ActivityProgressHandler(this));
        req.setOnNewStatusHandler(new OnNewStatusHandler() {

			@Override
			public void onNewStatus(Status status) {
				m_status = status;
				setContentView(R.layout.view_status);
				ImageView avatar = (ImageView) findViewById(R.id.avatar);
				avatar.setImageDrawable(status.getUser().getAvatar().getBitmap());
				TextView user = (TextView) findViewById(R.id.name);
				user.setText(status.getUser().getScreenName());
				TextView text = (TextView) findViewById(R.id.text);
				text.setText(status.text());
				TextView stamp = (TextView) findViewById(R.id.stamp);
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
				LinearLayout userBox = (LinearLayout) findViewById(R.id.user);
				userBox.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent view = new Intent(ViewStatus.this, ViewUser.class);
						view.putExtra("account", m_account.getGuid());
						view.putExtra("user", m_status.getUser().getId());
						startActivity(view);
					}
					
				});
				//getWindow().setBackgroundDrawable(status().getUser().getBackground());
			}
        	
        });
        req.execute();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_TEXT, "@"+m_status.getUser().getScreenName()+" "+m_status.text());
		shareIntent.setType("text/plain");
		menu.add("Share").setIntent(Intent.createChooser(shareIntent, "")); 
		menu.add("Favorite").setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				if (m_status != null) {
					APIRequest req;
					if (m_status.isFavorited()) {
						req = new DestroyFavoriteRequest(m_api, m_status);
						req.setProgressHandler(new ActivityProgressHandler(ViewStatus.this) {
							@Override
							public void finished(Boolean result) {
								super.finished(result);
								if (result) {
									Toast.makeText(ViewStatus.this, "Un-favorited.", Toast.LENGTH_SHORT).show();
								}
							}
						});
					} else {
						req = new CreateFavoriteRequest(m_api, m_status);
						req.setProgressHandler(new ActivityProgressHandler(ViewStatus.this) {
							@Override
							public void finished(Boolean result) {
								super.finished(result);
								if (result) {
									Toast.makeText(ViewStatus.this, "Favorited!", Toast.LENGTH_SHORT).show();
								}
							}
						});
					}
					req.execute();
				}
				return true;
			}
			
		});
		return true;
	}
}
