package net.wm161.microblog;

import net.wm161.microblog.lib.Account;
import net.wm161.microblog.lib.ActivityProgressHandler;
import net.wm161.microblog.lib.FileAttachment;
import net.wm161.microblog.lib.UpdateRequest;
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
			m_attachment = new FileAttachment(getContentResolver(), data.getData());
			TextView attachmentName = (TextView)findViewById(R.id.attachment);
			attachmentName.setText(m_attachment.name());
			LinearLayout attachmentGroup = (LinearLayout)findViewById(R.id.attachmentArea);
			attachmentGroup.setVisibility(View.VISIBLE);
		}
	}
	
	private FileAttachment m_attachment = null;

	private Account m_account;

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
		m_account = preferences.getDefaultAccount();
		
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
		String status = statusEdit.getText().toString();
		UpdateRequest req = new UpdateRequest(m_account, new ActivityProgressHandler(this), status) {

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				if (result) {
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
			protected void onPreExecute() {
				super.onPreExecute();
				findViewById(R.id.input).setEnabled(false);
				findViewById(R.id.send).setEnabled(false);
			}
		};
		if (m_attachment != null)
			req.setParameter("media", m_attachment);
		req.setParameter("source", "Android Microblog");
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
				m_attachment = new FileAttachment(getContentResolver(), uri);
				TextView attachmentName = (TextView)findViewById(R.id.attachment);
				attachmentName.setText(m_attachment.name());
				LinearLayout attachmentGroup = (LinearLayout)findViewById(R.id.attachmentArea);
				attachmentGroup.setVisibility(View.VISIBLE);
			}
		}
	}

}
