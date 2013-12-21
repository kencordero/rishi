package com.github.kencordero.rishi;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {
	public static final String EXTRA_FOLDER_NAME = "folder_name";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		Intent intent = null;
		switch (v.getId()) {
			case R.id.btnSelectAnimals:
				intent = new Intent(this, WordsActivity.class);
				intent.putExtra(EXTRA_FOLDER_NAME, R.string.words_animals_name);
				break;
			case R.id.btnSelectFoods:
				intent = new Intent(this, WordsActivity.class);
				intent.putExtra(EXTRA_FOLDER_NAME, R.string.words_foods_name);
				break;
			case R.id.btnSelectColors:
				intent = new Intent(this, WordsActivity.class);
				intent.putExtra(EXTRA_FOLDER_NAME, R.string.activity_colors_name);
				break;
			case R.id.btnSelectLetters:
				intent = new Intent(this, LettersActivity.class);			
				break;
			case R.id.btnSelectNumbers:			
				intent = new Intent(this, WordsActivity.class);
				intent.putExtra(EXTRA_FOLDER_NAME, R.string.activity_numbers_name);
				break;
			case R.id.btnSelectShapes:
				intent = new Intent(this, WordsActivity.class);
				intent.putExtra(EXTRA_FOLDER_NAME, R.string.activity_shapes_name);
				break;
		}
		startActivity(intent);
	}
}
