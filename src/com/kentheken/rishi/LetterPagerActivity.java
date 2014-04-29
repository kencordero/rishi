package com.kentheken.rishi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class LetterPagerActivity extends FragmentActivity {
	private ViewPager mViewPager;
	private ArrayList<String> mAlphabet;
	private String mLocaleId;
	private Locale mLocale;
	private boolean mIsUpperCase;
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			getActionBar().setTitle(R.string.activity_letters_name);
		mIsUpperCase = true;
		
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
				if (!mIsUpperCase && !mLocaleId.equals("mr"))
					letter = letter.toLowerCase(mLocale);					
				return LetterFragment.newInstance(letter);
			}

			@Override
			public int getCount() {
				return mAlphabet.size();
			}			
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.letters, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem switchCase = menu.findItem(R.id.action_switch_case);
		switchCase.setVisible(!mLocaleId.equals("mr"));
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_random:
			Collections.shuffle(mAlphabet);			
			return true;
		case R.id.action_settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_switch_case:
			mIsUpperCase = !mIsUpperCase;
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
}
