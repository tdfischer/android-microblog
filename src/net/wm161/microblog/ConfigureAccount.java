package net.wm161.microblog;

import net.wm161.microblog.lib.API;
import net.wm161.microblog.lib.APIConfiguration;
import net.wm161.microblog.lib.APIManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ConfigureAccount extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String apiName = intent.getStringExtra("api");
		API api = APIManager.getAPI(apiName);
		APIConfiguration conf = api.configuration();
		setContentView(R.layout.edit_account);
		LinearLayout layout = (LinearLayout) findViewById(R.id.content);
		for(String key : conf.keys()) {
			Object defaultValue = conf.defaultValue(key);
			View setting;
			if (defaultValue instanceof Boolean) {
				CheckBox box = new CheckBox(this);
				box.setChecked((Boolean) defaultValue);
				setting = box;
				box.setText(conf.name(key));
			} else if (defaultValue instanceof CharSequence) {
				EditText edit = new EditText(this);
				edit.setText((CharSequence) defaultValue);
				TextView label = new TextView(this);
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
	}

}
