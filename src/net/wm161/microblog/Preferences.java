package net.wm161.microblog;

import java.util.UUID;

import net.wm161.microblog.lib.Account;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.util.Log;

public class Preferences {
		SharedPreferences m_prefs;
		MicroblogApp m_app;
		public final int VERSION = 4;

		public Preferences(Context cxt) {
			m_prefs = cxt.getSharedPreferences("Microblogging.Main", Context.MODE_PRIVATE);
			m_app = (MicroblogApp)cxt.getApplicationContext();
			upgrade();
		}
		
		public boolean upgrade() {
			int configVersion;
			while((configVersion = m_prefs.getInt("version", 0)) < VERSION) {
				Log.d("Preferences", "Current version: " + configVersion);
				if (!upgrade(configVersion))
					return false;
			}
			return true;
		}
		
		private void writeVersion(int version) {
			m_prefs.edit().putInt("version", version).commit();
			Log.d("Preferences", "Version set to "+version);
		}
		
		//TODO: Test this with the data from the market version
		public boolean upgrade(int old) {
			Log.d("Preferences", "Upgrading to "+VERSION+" from "+old);
			switch(old) {
				case 0:
					if (m_prefs.contains("accounts"))
						writeVersion(3);
					else
						writeVersion(VERSION);
					break;
				case 3:
					//In version 0.1 (serial 3), accounts stored a URL in baseurl.
					String accountGUIDs = m_prefs.getString("accounts", null);
					String[] guids = accountGUIDs.split(",");
					for(String acct : guids) {
						String baseurl = m_prefs.getString(acct+".baseurl", "");
						Uri base = Uri.parse(baseurl);
						
						Editor editor = m_prefs.edit();
						editor.putString(acct+".api", "statusnet");
						editor.putInt(acct+".apiversion", 1);
						editor.remove(acct+".baseurl");
						
						String server = base.getHost();
						String path = base.getPath();
						boolean https = base.getScheme().equals("https");
						
						editor.putString(acct+".api.server", server);
						editor.putString(acct+".api.path", path);
						editor.putBoolean(acct+".api.https", https);
						
						editor.commit();
					}
					writeVersion(4);
					break;
			}
			return true;
		}

		public void setDefaultAccount(String guid) {
			m_prefs.edit().putString("defaultAccount", guid);
		}

		public Account getDefaultAccount() {
			String defaultAccount = m_prefs.getString("defaultAccount", null);
			if (defaultAccount == null) {
				Account[] accounts = getAccounts();
				if (accounts.length == 0)
					return null;
				setDefaultAccount(accounts[0].getGuid());
				return accounts[0];
			} else {
				return getAccount(defaultAccount);
			}
		}
		
		public MicroblogAccount[] getAccounts() {
			String accountGUIDs = m_prefs.getString("accounts", null);
			if (accountGUIDs == null)
				return new MicroblogAccount[] {};
			String[] guids = accountGUIDs.split(",");
			MicroblogAccount[] accounts = new MicroblogAccount[guids.length];
			for(int i = 0;i<guids.length;i++) {
				accounts[i] = new MicroblogAccount(m_app, guids[i]);
			}
			return accounts;
		}

		public MicroblogAccount getAccount(String guid) {
			for(MicroblogAccount acct : getAccounts())
				if (acct.getGuid().equals(guid))
					return acct;
			return null;
		}

		public MicroblogAccount getNewAccount() {
			String accountGUIDs = m_prefs.getString("accounts", null);
			String newID = UUID.randomUUID().toString();
			if (accountGUIDs == null) {
				accountGUIDs = newID;
			} else {
				accountGUIDs += ","+newID;
			}
			m_prefs.edit().putString("accounts", accountGUIDs).commit();
			return getAccount(newID);
		}
}
