package net.wm161.microblog;

import net.wm161.microblog.lib.API;
import net.wm161.microblog.lib.APIConfiguration;
import net.wm161.microblog.lib.APIManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ConfigureAccount extends Activity {

	private MicroblogAccount m_account;
	private API m_api;
	private String m_apiName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if (intent.hasExtra("account")) {
			String account = intent.getStringExtra("account");
			Preferences prefs = ((MicroblogApp)getApplication()).getPreferences();
			m_account = prefs.getAccount(account);
			m_apiName = m_account.getAPI();
		} else {
			m_apiName = intent.getStringExtra("api");
		}
		m_api = APIManager.getAPI(m_apiName);
		APIConfiguration conf = m_api.configuration();
		setContentView(R.layout.edit_account);
		LinearLayout layout = (LinearLayout) findViewById(R.id.content);
		for(String key : conf.keys()) {
			Object defaultValue = conf.defaultValue(key);
			View setting;
			if (defaultValue instanceof Boolean) {
				CheckBox box = new CheckBox(this);
				if (m_account == null) {
					box.setChecked((Boolean) defaultValue);
				} else {
					box.setChecked(m_account.getAPIBoolean(key));
				}
				box.setTag(key);
				setting = box;
				box.setText(conf.name(key));
			} else if (defaultValue instanceof CharSequence) {
				EditText edit = new EditText(this);
				if (m_account == null) {
					edit.setText((CharSequence) defaultValue);
				} else {
					edit.setText(m_account.getAPIString(key));
				}
				TextView label = new TextView(this);
				edit.setTag(key);
				label.setText(conf.name(key));
				LinearLayout settinglayout = new LinearLayout(this);
				settinglayout.setOrientation(LinearLayout.VERTICAL);
				settinglayout.addView(label);
				settinglayout.addView(edit);
				setting = settinglayout;
			} else {
				continue;
			}
			
			layout.addView(setting);
		}
		
		if (m_account != null) {
			EditText input = (EditText) findViewById(R.id.name);
			input.setText(m_account.getName());
			input = (EditText) findViewById(R.id.username);
			input.setText(m_account.getUser());
			input = (EditText) findViewById(R.id.password);
			input.setText(m_account.getPassword());
		}
		
		Button save = (Button) findViewById(R.id.save);
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				save();
			}
		});
	}
	
	private void save() {
		APIConfiguration conf = m_api.configuration();
		MicroblogAccount account;
		if (m_account == null) {
			Preferences prefs = ((MicroblogApp)getApplication()).getPreferences();
			account = prefs.getNewAccount();
		} else {
			account = m_account;
		}
		LinearLayout layout = (LinearLayout) findViewById(R.id.content);
		for(String key : conf.keys()) {
			View item = layout.findViewWithTag(key);
			Object defaultValue = conf.defaultValue(key);
			if (defaultValue instanceof Boolean) {
				CheckBox box = (CheckBox) item;
				account.setAPIValue(key, box.isChecked());
			} else if (defaultValue instanceof String) {
				EditText edit = (EditText) item;
				account.setAPIValue(key, edit.getText().toString());
			}
		}
		
		EditText input = (EditText) findViewById(R.id.name);
		account.setName(input.getText().toString());
		input = (EditText) findViewById(R.id.password);
		account.setPassword(input.getText().toString());
		input = (EditText) findViewById(R.id.username);
		account.setUser(input.getText().toString());
		account.setAPI(m_apiName);
		
		Intent result = new Intent();
		result.putExtra("account", account.getGuid());
		setResult(Activity.RESULT_OK, result);
		finish();
	}

}
