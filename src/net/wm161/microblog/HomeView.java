package net.wm161.microblog;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

public class HomeView extends TabActivity implements OnClickListener {

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
		req.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add("Accounts").setIntent(new Intent(this, AccountList.class));
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
		super.onNewIntent(intent);
		String text = intent.getStringExtra(Intent.EXTRA_TEXT);
		EditText edit = (EditText) findViewById(R.id.input);
		edit.setText(text);
	}

}
