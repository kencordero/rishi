package com.github.kencordero.rishi;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	public static final String KEY_PREF_LANGUAGE = "pref_language";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		ListPreference languagePref = (ListPreference) findPreference(KEY_PREF_LANGUAGE);
		languagePref.setSummary(languagePref.getEntry());
	}

	@Override
	public void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPrefences, String key) {
		if (key.equals(KEY_PREF_LANGUAGE)) {
			ListPreference languagePref = (ListPreference) findPreference(key);
			languagePref.setSummary(languagePref.getEntry());
		}
	}

}
