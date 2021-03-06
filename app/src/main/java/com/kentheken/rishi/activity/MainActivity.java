package com.kentheken.rishi.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;

import com.kentheken.rishi.DatabaseOpenHelper;
import com.kentheken.rishi.R;
import com.kentheken.rishi.TTSEngine;

public class MainActivity extends Activity {
	public static final String TAG = MainActivity.class.getSimpleName();
	public static final String EXTRA_FOLDER_NAME = "folder_name";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TTSEngine.get(this); // initializes TTS engine
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
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
	public void onDestroy() {
		super.onDestroy();
		TTSEngine.get(this).shutdown();
		DatabaseOpenHelper.get(this).close();
	}
}
