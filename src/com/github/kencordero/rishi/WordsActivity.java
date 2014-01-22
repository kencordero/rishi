package com.github.kencordero.rishi;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kencordero.rishi.SimpleGestureFilter.SimpleGestureListener;

public class WordsActivity extends Activity implements SimpleGestureListener, OnInitListener, OnClickListener {
	private static final String BUNDLE_FILE_KEY = "currentFileNumber";
	private static final String BUNDLE_LOCALE_KEY = "currentLocale";
	
	protected AssetManager mAssets;	
	private Locale mLocale;
	private Random mRandom;
	private String[] mFiles;
	private ArrayList<String> mFileList;
	private String mCurrentFileName;
	private int mCurrentFileIdx;
	private SimpleGestureFilter mDetector;
	private String mLocaleId;
	private int mFolderResId;
	private String mImageFolderName;
	private TextToSpeech mTTS;
	private int mResId;
	private ImageView mImageView;
	private TextView mTextView;

	@TargetApi(11)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_words);
		Bundle bundle = getIntent().getExtras();
		mFolderResId = bundle.getInt(MainActivity.EXTRA_FOLDER_NAME);
		mImageFolderName = getString(mFolderResId).toLowerCase(Locale.ENGLISH);
		
		//setup action bar		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar ab = getActionBar();
			ab.setTitle(mFolderResId);
			ab.setDisplayHomeAsUpEnabled(true);
		}
		
		findImages();
		mDetector = new SimpleGestureFilter(this, this);
		mCurrentFileIdx = 0;
		mRandom = new Random();	
		mTTS = new TextToSpeech(this, this);
		mImageView = (ImageView) findViewById(R.id.imgView_Words);
		mImageView.setOnClickListener(this);
		mTextView = (TextView) findViewById(R.id.txtView_Words);
		mTextView.setOnClickListener(this);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle bundle) {
		bundle.putInt(BUNDLE_FILE_KEY , mCurrentFileIdx);
		bundle.putString(BUNDLE_LOCALE_KEY, mLocaleId);
	}
	
	protected void onRestoreInstanceState(Bundle bundle) {
		mCurrentFileIdx = bundle.getInt(BUNDLE_FILE_KEY);
		mLocaleId = bundle.getString(BUNDLE_LOCALE_KEY);
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadImage();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		mLocaleId = preferences.getString(SettingsFragment.KEY_PREF_LANGUAGE,  "0");
		Configuration config = getBaseContext().getResources().getConfiguration();
		mLocale = new Locale(mLocaleId);
		config.locale = mLocale;
		getBaseContext().getResources().updateConfiguration(config, null);
		if (mLocaleId.equals("mr")) //There's no speech engine for Marathi			
			mLocale = new Locale("hi");					
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.alternate, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_random:
			loadRandomImage();
			return true;
		case R.id.action_settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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
			loadNextImage();
			break;
		case SimpleGestureFilter.SWIPE_RIGHT:
			loadPreviousImage();
			break;
		}
	}
	
	private void displayText() {
		if (mTextView.getText().length() == 0) {
			String displayName = mCurrentFileName.replace(".jpg", "");
			try {
				mResId = R.string.class.getField(displayName).getInt(null);			
				mTextView.setText(mResId);
			} catch (Exception e) {
				throwError(e);
			}
		}
	}
	
	private void speakText() {
		if (mResId > 0) {
			mTTS.setLanguage(mLocale);
			mTTS.speak(getString(mResId), TextToSpeech.QUEUE_ADD, null);
		}
	}

	private void findImages() {
		mAssets = getAssets();
		try {
			mFiles = mAssets.list(mImageFolderName);
			mFileList = new ArrayList<String>(Arrays.asList(mFiles));
			if (mFolderResId != R.string.activity_letters_name && 
				mFolderResId != R.string.activity_numbers_name)
				Collections.shuffle(mFileList);
		} catch (Exception e) {
			throwError(e);
		}
	}

	private void loadImage() {
		//mCurrentFileName = mFiles[mCurrentFileIdx];
		mCurrentFileName = mFileList.get(mCurrentFileIdx);
		InputStream stream = null;
		try {
			stream = mAssets.open(mImageFolderName + "/" + mCurrentFileName);
		} catch (Exception e) {
			throwError(e);
		}
		Drawable img = Drawable.createFromStream(stream, mCurrentFileName);
		//ImageView iv = (ImageView) findViewById(R.id.imgView_Words);
		mImageView.setImageDrawable(img);				
		mTextView.setText("");
		// not resetting mResId causes tts to speak previous word
		mResId = -1; 
	}

	private void loadNextImage() {
		mCurrentFileIdx = (++mCurrentFileIdx) % (mFiles.length);
		loadImage();
	}

	private void loadPreviousImage() {
		mCurrentFileIdx = (--mCurrentFileIdx) % (mFiles.length);
		if (mCurrentFileIdx < 0)
			mCurrentFileIdx = mFiles.length - 1;
		loadImage();
	}

	private void loadRandomImage() {
		int randInt;
		do {
			randInt = mRandom.nextInt(mFiles.length);
		} while (randInt == mCurrentFileIdx);
		mCurrentFileIdx = randInt;
		loadImage();
	}

	private void throwError(Exception e) {
		Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
		e.printStackTrace();
	}

	@Override
	public void onInit(int arg0) {}

	@Override
	protected void onPause() {
		super.onPause();
		mTTS.stop();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgView_Words:
			displayText();
			speakText();			
			break;
		case R.id.txtView_Words:
			speakText();			
			break;
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mTTS.shutdown();
	}
}
