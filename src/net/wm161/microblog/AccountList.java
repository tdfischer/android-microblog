package net.wm161.microblog;

import net.wm161.microblog.lib.Account;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;

public class AccountList extends ListActivity implements OnItemClickListener, OnClickListener {

	private ArrayAdapter<MicroblogAccount> m_accounts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Microblog - Accounts");
		setContentView(R.layout.accounts);
		getListView().setEmptyView(findViewById(R.id.empty));
		Account[] accounts = ((MicroblogApp)getApplication()).getPreferences().getAccounts();
		if (accounts.length == 1) {
			HomeView.show(this, accounts[0]);
		}
		Button addButton = (Button)findViewById(R.id.add_account);
		addButton.setOnClickListener(this);
		getListView().setOnItemClickListener(this);
		registerForContextMenu(getListView());
	}
	
	public void refresh() {
		MicroblogAccount[] accounts = ((MicroblogApp)getApplication()).getPreferences().getAccounts();
		m_accounts = new ArrayAdapter<MicroblogAccount>(this, R.layout.account,R.id.name, accounts);
		setListAdapter(m_accounts);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		HomeView.show(this, (Account)getListAdapter().getItem(position));
	}

	@Override
	public void onClick(View v) {
		onNewAccount();
	}
	
	public void onNewAccount() {
		Intent i = new Intent(this, EditAccount.class);
		startActivity(i);
	}

	@Override
	protected void onResume() {
		super.onResume();
		refresh();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		Account account = m_accounts.getItem(info.position);
		Intent editIntent = new Intent(this, EditAccount.class);
		editIntent.putExtra("account", account.getGuid());
		menu.add("Edit").setIntent(editIntent);
	}
}
