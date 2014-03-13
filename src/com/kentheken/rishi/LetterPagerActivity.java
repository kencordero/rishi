package com.kentheken.rishi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class LetterPagerActivity extends FragmentActivity {
	private ViewPager mViewPager;
	private ArrayList<String> mAlphabet;
	private String mLocaleId;
	private Locale mLocale;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);		
		
		//grab locale from settings
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		mLocaleId = preferences.getString(SettingsFragment.KEY_PREF_LANGUAGE, "0");
		Configuration config = getBaseContext().getResources().getConfiguration();
		mLocale = new Locale(mLocaleId);
		config.locale = mLocale;
		getBaseContext().getResources().updateConfiguration(config, null);
		Resources res = getResources();
		if (mLocaleId.equals("mr"))		
			mLocale = new Locale("hi");	
		
		int arrayResId = R.array.letters;
		mAlphabet = new ArrayList<String>(Arrays.asList(res.getStringArray(arrayResId)));
		
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			@Override
			public Fragment getItem(int pos) {
				String letter = mAlphabet.get(pos);
				return LetterFragment.newInstance(letter);
			}

			@Override
			public int getCount() {
				return mAlphabet.size();
			}			
		});
	}
	
}
