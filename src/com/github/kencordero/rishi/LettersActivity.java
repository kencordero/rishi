package com.github.kencordero.rishi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioManager;
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
	
	private int _currentIdx;
	private SimpleGestureFilter _detector;
	private String _localeId;
	private Locale _locale;
	private String[] _letters;
	private ArrayList<String> _alphabet;
	private TextToSpeech _tts;	
	private TextView _textView;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		try {
		setContentView(R.layout.activity_letters);
		ActionBar ab = getActionBar();
		ab.setTitle(R.string.activity_letters_name);
		ab.setDisplayHomeAsUpEnabled(true);
		_currentIdx = 0;
		_detector = new SimpleGestureFilter(this, this);
		_textView = (TextView) findViewById(R.id.txtView_Letters);
		_textView.setOnClickListener(this);
		_tts = new TextToSpeech(this, this);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		} catch (Exception e) {
			throwError(e);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		try {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		_localeId = preferences.getString(SettingsFragment.KEY_PREF_LANGUAGE, "0");
		Configuration config = getBaseContext().getResources().getConfiguration();
		_locale = new Locale(_localeId);
		config.locale = _locale;
		getBaseContext().getResources().updateConfiguration(config, null);
		Resources res = getResources();
		if (_localeId.equals("mr"))
			_locale = new Locale("hi");
		_letters = res.getStringArray(R.array.letters);
		_alphabet = new ArrayList<String>(Arrays.asList(_letters));
		setLetter();
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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_random:
			//randomLetter();
			Collections.shuffle(_alphabet);
			nextLetter();
			return true;
		case R.id.action_settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_switch_case:
			_letters = getResources().getStringArray(R.array.letters_lower);
			_alphabet = new ArrayList<String>(Arrays.asList(_letters));
			setLetter();
			return true;
		default:
			return super.onOptionsItemSelected(item);			
		}
	}
	
	private void speakText() {
		try {
			_tts.setLanguage(_locale);		
			_tts.speak(_alphabet.get(_currentIdx).toString(), TextToSpeech.QUEUE_ADD, null);
		} catch (Exception e) {
			throwError(e);
		}
	}
	
	private void setLetter() {
		String letter = _alphabet.get(_currentIdx);
		_textView.setText(letter);		
	}
	
	protected void onSaveInstanceState(Bundle bundle) {
		bundle.putInt(BUNDLE_INDEX_KEY, _currentIdx);
		bundle.putString(BUNDLE_LOCALE_KEY, _localeId);
	}
	
	protected void onRestoreInstanceState(Bundle bundle) {
		_currentIdx = bundle.getInt(BUNDLE_INDEX_KEY);
		_localeId = bundle.getString(BUNDLE_LOCALE_KEY);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent me) {
		this._detector.onTouchEvent(me);
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
		_currentIdx = (++_currentIdx) % (_letters.length);
		setLetter();
	}
	
	private void previousLetter() {
		_currentIdx = (--_currentIdx) % (_letters.length);
		if (_currentIdx < 0)
			_currentIdx = _letters.length - 1;
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
		_tts.stop();
	}
	
	private void throwError(Exception e) {
		Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
		e.printStackTrace();
	}
}
