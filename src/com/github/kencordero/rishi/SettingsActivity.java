package com.github.kencordero.rishi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {
	public static final String KEY_PREF_LANG = "pref_language";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Display settings fragment as main
		getFragmentManager().beginTransaction()
		.replace(android.R.id.content, new SettingsFragment())
		.commit();
	}
	
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals(KEY_PREF_LANG)) {
			Preference languagePref = findPreference(key);
			//Set summary to value
			languagePref.setSummary(sharedPreferences.getString(key, ""));
		}
	}
}
