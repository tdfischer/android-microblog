package net.wm161.microblog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EditAccount extends Activity implements OnClickListener {

	private MicroblogAccount m_account;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_account);
		Intent i = getIntent();
		Preferences preferences = ((MicroblogApp)getApplication()).getPreferences();
		m_account = preferences.getAccount(i.getStringExtra("account"));
		if (m_account != null) {
			//EditText input = (EditText) findViewById(R.id.baseurl);
			//input.setText(m_account.getBase());
			EditText input = (EditText) findViewById(R.id.name);
			input.setText(m_account.getName());
			input = (EditText) findViewById(R.id.username);
			input.setText(m_account.getUser());
			input = (EditText) findViewById(R.id.password);
			input.setText(m_account.getPassword());
		}
		Button saveButton = (Button) findViewById(R.id.save);
		saveButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Preferences prefs = ((MicroblogApp)getApplication()).getPreferences();
		if (m_account == null)
			m_account = prefs.getNewAccount();
		//EditText input = (EditText) findViewById(R.id.baseurl);
		//m_account.setBase(input.getText().toString());
		EditText input = (EditText) findViewById(R.id.name);
		m_account.setName(input.getText().toString());
		input = (EditText) findViewById(R.id.password);
		m_account.setPassword(input.getText().toString());
		input = (EditText) findViewById(R.id.username);
		m_account.setUser(input.getText().toString());
		finish();
	}
}
