package com.kentheken.rishi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

import android.content.Intent;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class FlashCardPagerActivity extends FragmentActivity {
	private ViewPager mViewPager;
	private String mFolderName;
	private ArrayList<String> mFiles;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		
		setContentView(mViewPager);
		getFileList();
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {

			@Override
			public Fragment getItem(int pos) {
				String imgFilename = mFiles.get(pos);
				return FlashCardFragment.newInstance(mFolderName, imgFilename);
			}

			@Override
			public int getCount() {
				return mFiles.size();
			}			
		});
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
				Collections.shuffle(mFiles);
				return true;
			case R.id.action_settings:
				Intent intent = new Intent(this, SettingsActivity.class);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void getFileList() {
		AssetManager am = getAssets();		
		int resId = getIntent().getExtras().getInt(MainActivity.EXTRA_FOLDER_NAME);
		mFolderName = getString(resId).toLowerCase(Locale.US);
		try {
			mFiles = new ArrayList<String>(Arrays.asList(am.list(mFolderName)));
			if (resId != R.string.activity_numbers_name)
				Collections.shuffle(mFiles);
		} catch (Exception e) {
			throwError(e);
		}		
	}
	
	private void throwError(Exception e) {
		Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
		e.printStackTrace();
	}

	
}
