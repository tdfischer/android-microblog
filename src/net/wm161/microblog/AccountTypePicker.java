package net.wm161.microblog;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Gallery;

public class AccountTypePicker extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Gallery g = (Gallery) findViewById(R.id.types);
	}
}
