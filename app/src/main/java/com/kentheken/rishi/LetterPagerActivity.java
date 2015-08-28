package com.kentheken.rishi;

import android.annotation.TargetApi;
import android.content.Intent;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

public class LetterPagerActivity extends FragmentActivity
		implements LetterFragment.OnFragmentInteractionListener {
    private static final String TAG = LetterPagerActivity.class.getSimpleName();
	private ArrayList<String> mAlphabet;
	private String mLocaleId;
	private Locale mLocale;
	private boolean mIsUpperCase;
	private ViewPager mViewPager;
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && getActionBar() != null)
			getActionBar().setTitle(R.string.activity_letters_name);
		mIsUpperCase = true;
		
		//grab locale from settings
		mLocaleId = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(SettingsFragment.KEY_PREF_LANGUAGE, "0");
		Configuration config = getBaseContext().getResources().getConfiguration();
		mLocale = new Locale(mLocaleId);
		config.locale = mLocale;
		getBaseContext().getResources().updateConfiguration(config, null);
		Resources res = getResources();
		if (mLocaleId.equals("mr"))		
			mLocale = new Locale("hi");	
		
		int arrayResId = R.array.letters;
		mAlphabet = new ArrayList<>(Arrays.asList(res.getStringArray(arrayResId)));
		
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
        Log.i(TAG, "onOptionsItemSelected");
		switch(item.getItemId()) {
		case R.id.action_random:
			Collections.shuffle(mAlphabet);
			onAlphabetUpdated();
			return true;
		case R.id.action_settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_switch_case:
			mIsUpperCase = !mIsUpperCase;
			onAlphabetUpdated();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onAlphabetUpdated() {
		int currentItem = mViewPager.getCurrentItem();
		LetterFragment currentFragment = (LetterFragment)mViewPager.getAdapter()
				.instantiateItem(mViewPager, currentItem);
		currentFragment.setLetter(mAlphabet.get(currentItem));

		if (currentItem > 0) {
			LetterFragment previousFragment = (LetterFragment)mViewPager.getAdapter()
					.instantiateItem(mViewPager, currentItem - 1);
			previousFragment.setLetter(mAlphabet.get(currentItem - 1));
		}

		if (currentItem + 1 < mViewPager.getAdapter().getCount()) {
			LetterFragment nextFragment = (LetterFragment)mViewPager.getAdapter()
					.instantiateItem(mViewPager, currentItem + 1);
			nextFragment.setLetter(mAlphabet.get(currentItem + 1));
		}

	}
}
