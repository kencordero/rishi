package com.github.kencordero.rishi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kencordero.rishi.SimpleGestureFilter.SimpleGestureListener;

public class LettersActivity extends Activity implements SimpleGestureListener, OnInitListener, OnClickListener {
	private static final String BUNDLE_INDEX_KEY = "currentIndex";
	private static final String BUNDLE_LOCALE_KEY = "currentLocale";
	public final String TAG = "LettersActivity";
	
	private int mCurrentIdx;
	private SimpleGestureFilter mDetector;
	private String mLocaleId;
	private Locale mLocale;
	private int mArrayResId;
	private String[] mLetters;
	private ArrayList<String> mAlphabet;
	private TextToSpeech mTTS;	
	private TextView mTextView;
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		try {
		setContentView(R.layout.activity_letters);
		
		//setup action bar
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar ab = getActionBar();
			ab.setTitle(R.string.activity_letters_name);
			ab.setDisplayHomeAsUpEnabled(true);				
		}
		
		mCurrentIdx = 0;
		mDetector = new SimpleGestureFilter(this, this);
		mTextView = (TextView) findViewById(R.id.txtView_Letters);
		mTextView.setOnClickListener(this);
		mTTS = new TextToSpeech(this, this);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		} catch (Exception e) {
			throwError(e);
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onResume() {
		super.onResume();
		
		try {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		mLocaleId = preferences.getString(SettingsFragment.KEY_PREF_LANGUAGE, "0");
		Configuration config = getBaseContext().getResources().getConfiguration();
		mLocale = new Locale(mLocaleId);
		config.locale = mLocale;
		getBaseContext().getResources().updateConfiguration(config, null);
		Resources res = getResources();
		if (mLocaleId.equals("mr"))		
			mLocale = new Locale("hi");	
		mArrayResId = R.array.letters;
		mLetters = res.getStringArray(mArrayResId);
		mAlphabet = new ArrayList<String>(Arrays.asList(mLetters));
		setLetter();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			invalidateOptionsMenu();
		} catch (Exception e) {
			throwError(e);
		}
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
		switch (item.getItemId()) {
		case R.id.action_random:
			//randomLetter();
			Collections.shuffle(mAlphabet);
			nextLetter();
			return true;
		case R.id.action_settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_switch_case:
			if (mArrayResId == R.array.letters)
				mArrayResId = R.array.letters_lower;
			else
				mArrayResId = R.array.letters;
			mLetters = getResources().getStringArray(mArrayResId);
			mAlphabet = new ArrayList<String>(Arrays.asList(mLetters));
			setLetter();
			return true;
		default:
			return super.onOptionsItemSelected(item);			
		}
	}
	
	private void speakText() {
		try {
			mTTS.setLanguage(mLocale);		
			mTTS.speak(mAlphabet.get(mCurrentIdx).toString(), TextToSpeech.QUEUE_ADD, null);
		} catch (Exception e) {
			throwError(e);
		}
	}
	
	private void setLetter() {
		String letter = mAlphabet.get(mCurrentIdx);
		mTextView.setText(letter);		
	}
	
	protected void onSaveInstanceState(Bundle bundle) {
		bundle.putInt(BUNDLE_INDEX_KEY, mCurrentIdx);
		bundle.putString(BUNDLE_LOCALE_KEY, mLocaleId);
	}
	
	protected void onRestoreInstanceState(Bundle bundle) {
		mCurrentIdx = bundle.getInt(BUNDLE_INDEX_KEY);
		mLocaleId = bundle.getString(BUNDLE_LOCALE_KEY);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent me) {
		mDetector.onTouchEvent(me);
		return super.dispatchTouchEvent(me);
	}


	@Override
	public void onSwipe(int direction) {
		switch (direction) {
		case SimpleGestureFilter.SWIPE_LEFT:
			nextLetter();
			break;
		case SimpleGestureFilter.SWIPE_RIGHT:
			previousLetter();
			break;
		}		
	}
	
	private void nextLetter() {
		mCurrentIdx = (++mCurrentIdx) % (mLetters.length);
		setLetter();
	}
	
	private void previousLetter() {
		mCurrentIdx = (--mCurrentIdx) % (mLetters.length);
		if (mCurrentIdx < 0)
			mCurrentIdx = mLetters.length - 1;
		setLetter();
	}
	
	@Override
	public void onClick(View v) {
		try {
		switch (v.getId()) {
		case R.id.txtView_Letters:
			speakText();
			break;
		}
		} catch (Exception e) {
			throwError(e);
		}
		
	}

	@Override
	public void onInit(int arg0) {}
	
	@Override
	protected void onPause() {
		super.onPause();
		mTTS.stop();
	}
	
	private void throwError(Exception e) {
		Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
		e.printStackTrace();
	}
}
