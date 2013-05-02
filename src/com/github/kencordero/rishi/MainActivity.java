package com.github.kencordero.rishi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onButtonClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.btnSelectAnimals:
			intent = new Intent(this, WordsActivity.class);
			intent.putExtra("folder", R.string.words_animals_name);
			break;
		case R.id.btnSelectFoods:
			intent = new Intent(this, WordsActivity.class);
			intent.putExtra("folder", R.string.words_foods_name);
			break;
		case R.id.btnSelectColors:
			intent = new Intent(this, ColorsActivity.class);
			break;
		case R.id.btnSelectLetters:
			intent = new Intent(this, LettersActivity.class);
			break;
		case R.id.btnSelectNumbers:
			intent = new Intent(this, NumbersActivity.class);
			break;
		case R.id.btnSelectShapes:
			intent = new Intent(this, ShapesActivity.class);
			break;
		}
		startActivity(intent);
	}
}
