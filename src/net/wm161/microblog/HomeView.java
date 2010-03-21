package net.wm161.microblog;

import net.wm161.microblog.lib.API;
import net.wm161.microblog.lib.APIManager;
import net.wm161.microblog.lib.Account;
import net.wm161.microblog.lib.ActivityProgressHandler;
import net.wm161.microblog.lib.Attachment;
import net.wm161.microblog.lib.PublishUpdateRequest;
import net.wm161.microblog.lib.Status;
import net.wm161.microblog.lib.backends.Twitter;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class HomeView extends TabActivity implements OnClickListener {

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			m_attachment = new Attachment(getContentResolver(), data.getData());
			TextView attachmentName = (TextView)findViewById(R.id.attachment);
			attachmentName.setText(m_attachment.name());
			LinearLayout attachmentGroup = (LinearLayout)findViewById(R.id.attachmentArea);
			attachmentGroup.setVisibility(View.VISIBLE);
		}
	}
	
	private Attachment m_attachment = null;

	private MicroblogAccount m_account;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.home);
		TabHost tabs = getTabHost();
		//Intent i = getIntent();
		Preferences preferences = ((MicroblogApp)getApplication()).getPreferences();
		//m_account = preferences.getAccount(i.getStringExtra("account"));
		m_account = (MicroblogAccount) preferences.getDefaultAccount();
		
		Intent home = new Intent(this, HomeTimeline.class);
		home.putExtra("account", m_account.getGuid());
		tabs.addTab(tabs.newTabSpec("user").setContent(home).setIndicator("Home"));
		
		Intent timeline = new Intent(this, GlobalTimeline.class);
		timeline.putExtra("account", m_account.getGuid());
		tabs.addTab(tabs.newTabSpec("global").setContent(timeline).setIndicator("Global Timeline"));
		
		Button sendButton = (Button) findViewById(R.id.send);
		sendButton.setOnClickListener(this);
		
		Button unattach = (Button) findViewById(R.id.unattach);
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
			status.setAttachment(m_attachment);
		status.setSource("Android Microblog");
		
		//TODO: Different APIs
		//FIXME: Copied from TimelineActivity.java
		APIManager.getAPI(m_account.getAPI());
		API api = new Twitter();
		api.setAccount(m_account);
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
					m_attachment = new Attachment(getContentResolver(), uri);
					TextView attachmentName = (TextView)findViewById(R.id.attachment);
					attachmentName.setText(m_attachment.name());
					LinearLayout attachmentGroup = (LinearLayout)findViewById(R.id.attachmentArea);
					attachmentGroup.setVisibility(View.VISIBLE);
				}
			}
		}
	}

}
