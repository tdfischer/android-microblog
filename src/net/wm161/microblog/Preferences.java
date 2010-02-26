package net.wm161.microblog;

import java.util.UUID;

import net.wm161.microblog.lib.Account;


import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
		SharedPreferences m_prefs;
		MicroblogApp m_app;

		public Preferences(Context cxt) {
			m_prefs = cxt.getSharedPreferences("Microblogging.Main", Context.MODE_PRIVATE);
			m_app = (MicroblogApp)cxt.getApplicationContext();
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
