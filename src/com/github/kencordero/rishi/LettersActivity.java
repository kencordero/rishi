package com.github.kencordero.rishi;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;

import com.github.kencordero.rishi.SimpleGestureFilter.SimpleGestureListener;

public class LettersActivity extends Activity implements SimpleGestureListener {
	private static final String BUNDLE_INDEX_KEY = "currentIndex";
	private static final String BUNDLE_LOCALE_KEY = "currentLocale";
	public final String TAG = "LettersActivity";
	
	private int _currentIdx;
	private SimpleGestureFilter _detector;
	private String _localeId;
	private String[] _letters;
	private ArrayList<String> _alphabet;
	private TextView _textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_letters);
		ActionBar ab = getActionBar();
		ab.setTitle(R.string.activity_letters_name);
		_currentIdx = 0;
		_detector = new SimpleGestureFilter(this, this);
		Resources res = getResources();
		String[] _letters = res.getStringArray(R.array.letters);
		_textView = (TextView) findViewById(R.id.txtView_Letters);		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		setLetter();
	}
	
	private void setLetter() {
		_textView.setText(_letters[_currentIdx]);
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
}
