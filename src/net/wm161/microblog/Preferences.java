package net.wm161.microblog;

import java.util.UUID;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
		private static Preferences m_instance;
		SharedPreferences m_prefs;
		
		private Preferences(Context cxt) {
			m_prefs = cxt.getSharedPreferences("Microblogging.Main", Context.MODE_PRIVATE);
		}
		
		public static Preferences getPreferences(Context cxt) {
			if (m_instance == null)
				m_instance = new Preferences(cxt);
			return m_instance;
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
		
		public Account[] getAccounts() {
			String accountGUIDs = m_prefs.getString("accounts", null);
			if (accountGUIDs == null)
				return new Account[] {};
			String[] guids = accountGUIDs.split(",");
			Account[] accounts = new Account[guids.length];
			for(int i = 0;i<guids.length;i++) {
				accounts[i] = new Account(this, guids[i]);
			}
			return accounts;
		}

		public Account getAccount(String guid) {
			for(Account acct : getAccounts())
				if (acct.getGuid().equals(guid))
					return acct;
			return null;
		}

		public Account getNewAccount() {
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
