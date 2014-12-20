package com.kentheken.rishi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

public class FlashCardPagerActivity extends FragmentActivity implements FlashCardFragment.Callbacks {
    private static final String TAG = "FlashCardPagerActivity";
	private String mFolderName;
	private ArrayList<String> mFiles;
    private ViewPager mViewPager;
    private FlashCardFragment.Locale mLocale;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)			
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");

		setContentView(R.layout.activity_flashcard);
		mViewPager = (ViewPager)findViewById(R.id.viewPager);
        mLocale = FlashCardFragment.Locale.ENGLISH; // default to English
		
		getFileList();
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			getActionBar().setTitle(mFolderName);
		RadioButton radioButtonEnglish = (RadioButton)findViewById(R.id.optEnglish);
		radioButtonEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Locale: English");
                mLocale = FlashCardFragment.Locale.ENGLISH;
                speakText();
            }
        });
		RadioButton radioButtonMarathi = (RadioButton)findViewById(R.id.optMarathi);
		radioButtonMarathi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Locale: Marathi");
                mLocale = FlashCardFragment.Locale.MARATHI;
                speakText();
            }
        });
		RadioButton radioButtonSpanish = (RadioButton)findViewById(R.id.optSpanish);
		radioButtonSpanish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Locale: Spanish");
                mLocale = FlashCardFragment.Locale.SPANISH;
                speakText();
            }
        });
		
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

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) { }

            @Override
            public void onPageSelected(int i) {
                FlashCardFragment fragment = (FlashCardFragment)mViewPager.getAdapter().instantiateItem(mViewPager, i);
                fragment.clearText();
            }

            @Override
            public void onPageScrollStateChanged(int i) { }
        });
	}

    public void speakText() {
        FlashCardFragment fragment = (FlashCardFragment)mViewPager.getAdapter().instantiateItem(mViewPager, mViewPager.getCurrentItem());
        fragment.setText();
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.alternate, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected");
		switch (item.getItemId()) {
			/*case R.id.action_random:
				Collections.shuffle(mFiles);
				return true;*/
			case R.id.action_settings:
				Intent intent = new Intent(this, SettingsActivity.class);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void getFileList() {
        Log.i(TAG, "getFileList");
		AssetManager am = getAssets();		
		int resId = getIntent().getExtras().getInt(MainActivity.EXTRA_FOLDER_NAME);
		mFolderName = getString(resId);
		try {
			mFiles = new ArrayList<>(Arrays.asList(am.list(mFolderName.toLowerCase(java.util.Locale.US))));
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

    // FlashCardFragment.Callbacks
    @Override
    public FlashCardFragment.Locale onsetText() {

        if (mLocale == null)
            mLocale = FlashCardFragment.Locale.ENGLISH;
        return mLocale;
    }
}
