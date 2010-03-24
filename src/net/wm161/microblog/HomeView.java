package net.wm161.microblog;

import java.io.IOException;
import java.util.List;

import net.wm161.microblog.lib.API;
import net.wm161.microblog.lib.Account;
import net.wm161.microblog.lib.ActivityProgressHandler;
import net.wm161.microblog.lib.Attachment;
import net.wm161.microblog.lib.ContentAttachment;
import net.wm161.microblog.lib.OnNewUserHandler;
import net.wm161.microblog.lib.PublishUpdateRequest;
import net.wm161.microblog.lib.Status;
import net.wm161.microblog.lib.User;
import net.wm161.microblog.lib.UserRequest;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class HomeView extends TabActivity implements OnClickListener, LocationListener {

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			m_attachment = new ContentAttachment(getContentResolver(), data.getData());
			TextView attachmentName = (TextView)findViewById(R.id.attachment);
			attachmentName.setText(m_attachment.name());
			LinearLayout attachmentGroup = (LinearLayout)findViewById(R.id.attachmentArea);
			attachmentGroup.setVisibility(View.VISIBLE);
		}
	}
	
	private Attachment m_attachment = null;

	private MicroblogAccount m_account;

	private Location m_location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.home);
		TabHost tabs = getTabHost();
		Preferences preferences = ((MicroblogApp)getApplication()).getPreferences();
		if (preferences.getAccounts().length == 0) {
			Intent newAccount = new Intent(this, AccountTypePicker.class);
			startActivity(newAccount);
			finish();
		} else {
			//TODO: Multiple accounts
			//Intent i = getIntent();
			//m_account = preferences.getAccount(i.getStringExtra("account"));
			m_account = (MicroblogAccount) preferences.getDefaultAccount();
			
			Resources res = getResources();
			
			UserRequest req = new UserRequest(m_account.getAPIInstance(), m_account.getUser());
			req.setOnNewUserHandler(new OnNewUserHandler() {
				
				@Override
				public void onNewUser(User user) {
					ImageView icon = (ImageView) getTabWidget().getChildTabViewAt(0).findViewById(android.R.id.icon);
					icon.setImageDrawable(user.getAvatar().getBitmap());
				}
			});
			req.execute();
			
			Intent home = new Intent(this, HomeTimeline.class);
			home.putExtra("account", m_account.getGuid());
			tabs.addTab(tabs.newTabSpec("user").setContent(home).setIndicator("Home"));
			
			Intent timeline = new Intent(this, GlobalTimeline.class);
			timeline.putExtra("account", m_account.getGuid());
			tabs.addTab(tabs.newTabSpec("global").setContent(timeline).setIndicator("Global Timeline", res.getDrawable(m_account.getAPIInstance().getIcon())));
			
			Intent replies = new Intent(this, ReplyTimeline.class);
			replies.putExtra("account", m_account.getGuid());
			tabs.addTab(tabs.newTabSpec("replies").setContent(replies).setIndicator("Replies", res.getDrawable(android.R.drawable.sym_call_incoming)));
			
			Button sendButton = (Button) findViewById(R.id.send);
			sendButton.setOnClickListener(this);
			
			Button unattach = (Button) findViewById(R.id.unattach);
			//TODO: Fix this to get a better image for 'unattach' 
			//unattach.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.btn_minus));
			unattach.setText("Remove");
			unattach.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					m_attachment = null;
					LinearLayout attachmentGroup = (LinearLayout)findViewById(R.id.attachmentArea);
					attachmentGroup.setVisibility(View.GONE);
				}
			});
			
			EditText edit = (EditText) findViewById(R.id.input);
			edit.setOnClickListener(new OnClickListener() {
	
				@Override
				public void onClick(View arg0) {
					takeKeyEvents(true);
					arg0.requestFocus();
				}
				
			});
			setDefaultKeyMode(DEFAULT_KEYS_DISABLE);
		}
	}
	
	public static void show(Context cxt, Account account) {
		Intent i = new Intent(cxt, HomeView.class);
		i.putExtra("account", account.getGuid());
		cxt.startActivity(i);
	}
	
	@Override
	public void onClick(View v) {
		EditText statusEdit = (EditText) findViewById(R.id.input);
		Status status = new Status();
		status.setText(statusEdit.getText().toString());
		if (m_attachment != null)
			status.addAttachment(m_attachment);
		status.setLocation(m_location);
		status.setSource("Android Microblog");
		
		API api = m_account.getAPIInstance();
		PublishUpdateRequest req = new PublishUpdateRequest(api, status);
		req.setProgressHandler(new ActivityProgressHandler(this) {

			@Override
			public void finished(Boolean success) {
				super.finished(success);
				if (success) {
					EditText input = (EditText) findViewById(R.id.input);
					input.setText("");
					Toast.makeText(HomeView.this, "Update sent.", Toast.LENGTH_SHORT).show();
					m_attachment = null;
					LinearLayout attachmentGroup = (LinearLayout)findViewById(R.id.attachmentArea);
					attachmentGroup.setVisibility(View.GONE);
				}
				findViewById(R.id.input).setEnabled(true);
				findViewById(R.id.send).setEnabled(true);
			}

			@Override
			public void starting() {
				super.starting();
				findViewById(R.id.input).setEnabled(false);
				findViewById(R.id.send).setEnabled(false);
			}
		});
		req.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add("Accounts").setIntent(new Intent(this, AccountList.class)).setIcon(android.R.drawable.ic_menu_preferences);
		menu.add("Attach Picture").setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				Intent picker =  new Intent(Intent.ACTION_PICK);
				picker.setType("image/*");
				startActivityForResult(picker, 0);
				return true;
			}
			
		}).setIcon(android.R.drawable.ic_menu_add);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.isPrintingKey()) {
			EditText edit = (EditText) findViewById(R.id.input);
			edit.requestFocus();
			edit.dispatchKeyEvent(event);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		Log.d("HomeView", "New intent!");
		super.onNewIntent(intent);
		if (intent.getAction() != null) {
			if (intent.getAction().equals(Intent.ACTION_SEND)) {
				if (intent.hasExtra(Intent.EXTRA_TEXT)) {
					String text = intent.getStringExtra(Intent.EXTRA_TEXT);
					EditText edit = (EditText) findViewById(R.id.input);
					edit.setText(text);
				}
				for(String s : intent.getExtras().keySet()) {
					Log.d("HomeView", "Got extra "+s);
				}
				if (intent.hasExtra(Intent.EXTRA_STREAM)) {
					Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
					Log.d("HomeView", "Sending image "+uri);
					m_attachment = new ContentAttachment(getContentResolver(), uri);
					TextView attachmentName = (TextView)findViewById(R.id.attachment);
					attachmentName.setText(m_attachment.name());
					LinearLayout attachmentGroup = (LinearLayout)findViewById(R.id.attachmentArea);
					attachmentGroup.setVisibility(View.VISIBLE);
				}
			}
		}
	}
	
	private void updateLocation(Location location) {
		m_location = location;
		Geocoder encoder = new Geocoder(this, getResources().getConfiguration().locale);
		Address addr = null;
		try {
			List<Address> addresses = encoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
			if (addresses.size() > 0)
				addr = addresses.get(0);
		} catch (IOException e) {
			return;
		}
		TextView locationText = (TextView) findViewById(R.id.location);
		locationText.setText(addr.getLocality());
	}

	@Override
	public void onLocationChanged(Location location) {
		updateLocation(location);
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		LocationManager locations = (LocationManager) getSystemService(LOCATION_SERVICE);
		locations.removeUpdates(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		LocationManager locations = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setSpeedRequired(false);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		String provider = locations.getBestProvider(criteria, true);
		locations.requestLocationUpdates(provider, 60000, 20, this);
		updateLocation(locations.getLastKnownLocation(provider));
	}

}
