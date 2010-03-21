package net.wm161.microblog;

import net.wm161.microblog.lib.API;
import net.wm161.microblog.lib.APIManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class AccountTypePicker extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		
		Gallery g = (Gallery) findViewById(R.id.types);
		g.setAdapter(new APIListAdapter(this));
		g.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String api = (String) view.getTag();
				API apiInstance = APIManager.getAPI(api);
				((TextView)findViewById(R.id.name)).setText(apiInstance.getName());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				((TextView)findViewById(R.id.name)).setText("");
			}
		});
		
		Button next = (Button) findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Gallery g = (Gallery) findViewById(R.id.types);
				String api = (String) g.getSelectedView().getTag();
				Intent configure = new Intent(AccountTypePicker.this, ConfigureAccount.class);
				configure.putExtra("api", api);
				startActivity(configure);
			}
		});
	}
}
