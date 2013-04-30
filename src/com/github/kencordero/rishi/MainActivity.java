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
		Intent intent = new Intent(this, WordsActivity.class);
		switch (v.getId()) {
		case R.id.btnSelectAnimals:
			intent.putExtra("folder", R.string.words_animals_name);
			break;
		case R.id.btnSelectFoods:
			intent.putExtra("folder", R.string.words_foods_name);
			break;
		}
		startActivity(intent);
	}
}
