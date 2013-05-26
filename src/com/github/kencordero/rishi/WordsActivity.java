package com.github.kencordero.rishi;

import java.io.InputStream;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kencordero.rishi.SimpleGestureFilter.SimpleGestureListener;

public class WordsActivity extends Activity implements SimpleGestureListener {
	protected AssetManager _assets;
	protected ResourceBundle _rb;
	protected Random _random;
	private String[] _files;
	private String _currentFileName;
	private int _currentFileNumber;
	private SimpleGestureFilter _detector;
	private String _localeId;
	private String _imageFolderName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_words);
		Bundle bundle = getIntent().getExtras();
		int folderResId = bundle.getInt("folder");
		_imageFolderName = getString(folderResId).toLowerCase(Locale.ENGLISH);
		ActionBar ab = getActionBar();
		ab.setTitle(folderResId);
		findImages();
		_detector = new SimpleGestureFilter(this, this);
		_currentFileNumber = 0;
		_random = new Random();	
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadImage();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		_localeId = preferences.getString(SettingsFragment.KEY_PREF_LANGUAGE, "0");
		Configuration config = getBaseContext().getResources().getConfiguration();
		config.locale = new Locale(_localeId);
		getBaseContext().getResources().updateConfiguration(config, null);		
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
		this._detector.onTouchEvent(me);
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

	public void onImageClick(View view) {
		// Set textview
		int resId = 0;
		String displayName = _currentFileName.replace(".jpg", "");
		try {
			resId = R.string.class.getField(displayName).getInt(null);
			setViewText(R.id.txtView_Words, resId);
		} catch (Exception e) {
			throwError(e);
		}
	}

	public void onTextClick(View view) {
		MediaPlayer mp = new MediaPlayer();
		AssetFileDescriptor afd;
		try {
			afd = _assets.openFd("audio/"
					+ _currentFileName.replace(".jpg", ".ogg"));
			mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
					afd.getLength());
			mp.prepare();
			afd.close();
			mp.start();
		} catch (Exception e) {
			throwError(e);
		}
	}

	private void findImages() {
		_assets = getAssets();
		try {
			_files = _assets.list(_imageFolderName);
		} catch (Exception e) {
			throwError(e);
		}
	}

	private void loadImage() {
		_currentFileName = _files[_currentFileNumber];
		InputStream stream = null;
		try {
			stream = _assets.open(_imageFolderName + "/" + _currentFileName);
		} catch (Exception e) {
			throwError(e);
		}
		Drawable img = Drawable.createFromStream(stream, _currentFileName);
		ImageView iv = (ImageView) findViewById(R.id.imgView_Words);
		iv.setImageDrawable(img);
		setViewText(R.id.txtView_Words, "");
	}

	private void loadNextImage() {
		_currentFileNumber = (++_currentFileNumber) % (_files.length);
		loadImage();
	}

	private void loadPreviousImage() {
		_currentFileNumber = (--_currentFileNumber) % (_files.length);
		if (_currentFileNumber < 0)
			_currentFileNumber = _files.length - 1;
		loadImage();
	}

	private void loadRandomImage() {
		int randInt;
		do {
			randInt = _random.nextInt(_files.length);
		} while (randInt == _currentFileNumber);
		_currentFileNumber = randInt;
		loadImage();
	}

	private void setViewText(int viewId, int resId) {
		TextView tv = (TextView) findViewById(viewId);
		tv.setText(resId);
	}

	private void setViewText(int viewId, String text) {
		TextView tv = (TextView) findViewById(viewId);
		tv.setText(text);
	}

	private void throwError(Exception e) {
		Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
		e.printStackTrace();
	}
}
