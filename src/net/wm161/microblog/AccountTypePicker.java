/**
 *  This file is part of android-microblog
 *  Copyright (C) 2010 Trever Fischer <tdfischer@fedoraproject.org>
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package net.wm161.microblog;

import net.wm161.microblog.lib.API;
import net.wm161.microblog.lib.APIManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
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
				if (view == null) {
					onNothingSelected(parent);
				} else {
					String api = (String) view.getTag();
					API apiInstance = APIManager.getAPI(api);
					((TextView)findViewById(R.id.name)).setText(apiInstance.getName());
				}
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
				startActivityForResult(configure, 0);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			String guid = data.getStringExtra("account");
			MicroblogAccount account = new MicroblogAccount((MicroblogApp)getApplication(), guid);
			HomeView.show(this, account);
		}
	}
}
