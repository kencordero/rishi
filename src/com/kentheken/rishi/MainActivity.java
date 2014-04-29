package com.kentheken.rishi;

import com.kentheken.rishi.R;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {
	public static final String EXTRA_FOLDER_NAME = "folder_name";
	private TTSEngine mTTS;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTTS = new TTSEngine(this);
		setContentView(R.layout.activity_main);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onButtonClick(View v) {		
		Class<?> cls = null;
		int resId = -1;
		switch (v.getId()) {
			case R.id.btnSelectAnimals:
				cls = FlashCardPagerActivity.class;
				resId = R.string.words_animals_name;
				break;
			case R.id.btnSelectFoods:
				cls = FlashCardPagerActivity.class;
				resId = R.string.words_foods_name;
				break;
			case R.id.btnSelectColors:
				cls = FlashCardPagerActivity.class;
				resId = R.string.activity_colors_name;
				break;
			case R.id.btnSelectLetters:
				cls = LetterPagerActivity.class;				
				break;
			case R.id.btnSelectNumbers:
				cls = FlashCardPagerActivity.class;
				resId = R.string.activity_numbers_name;
				break;
			case R.id.btnSelectShapes:
				cls = FlashCardPagerActivity.class;
				resId = R.string.activity_shapes_name;
				break;
		}
		Intent intent = new Intent(this, cls);
		if (resId > -1)
			intent.putExtra(EXTRA_FOLDER_NAME, resId);
		startActivity(intent);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mTTS.stop();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mTTS.shutdown();
	}
}
