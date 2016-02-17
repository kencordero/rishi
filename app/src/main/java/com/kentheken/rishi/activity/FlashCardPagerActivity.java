package com.kentheken.rishi.activity;

import android.annotation.TargetApi;
import android.app.ActionBar;
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
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.kentheken.rishi.R;
import com.kentheken.rishi.TTSEngine;
import com.kentheken.rishi.fragment.FlashCardFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FlashCardPagerActivity extends FragmentActivity {
    private static final String TAG = FlashCardPagerActivity.class.getSimpleName();
	private String mFolderName;
	private ArrayList<String> mFiles;
    private ViewPager mViewPager;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)			
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");

		setContentView(R.layout.activity_flashcard);
		mViewPager = (ViewPager)findViewById(R.id.viewPager);

		getFileList();
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		ActionBar actionBar = getActionBar();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && actionBar != null)
			actionBar.setTitle(mFolderName);
		RadioButton radioButtonEnglish = (RadioButton)findViewById(R.id.optEnglish);
		radioButtonEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Locale: English");
				TTSEngine.get(getApplicationContext()).setLanguage(TTSEngine.LANGUAGE_ENGLISH);
                setText();
            }
        });
		RadioButton radioButtonMarathi = (RadioButton)findViewById(R.id.optMarathi);
		radioButtonMarathi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Locale: Marathi");
				TTSEngine.get(getApplicationContext()).setLanguage(TTSEngine.LANGUAGE_MARATHI);
                setText();
            }
        });
		RadioButton radioButtonSpanish = (RadioButton)findViewById(R.id.optSpanish);
		radioButtonSpanish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Locale: Spanish");
				TTSEngine.get(getApplicationContext()).setLanguage(TTSEngine.LANGUAGE_SPANISH);
                setText();
            }
        });

		//set radio button
		int lang = TTSEngine.get(this).getCurrentLanguage();
		switch (lang) {
			case TTSEngine.LANGUAGE_MARATHI:
				radioButtonMarathi.toggle();
				break;
			case TTSEngine.LANGUAGE_SPANISH:
				radioButtonSpanish.toggle();
				break;
			default:
				//English is selected by default via XML
				break;
		}
		
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

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int i, float v, int i2) {
			}

			@Override
			public void onPageSelected(int i) {
				FlashCardFragment fragment = (FlashCardFragment) mViewPager.getAdapter()
						.instantiateItem(mViewPager, i);
				fragment.clearText();
			}

			@Override
			public void onPageScrollStateChanged(int i) {
			}
		});
	}

    private void setText() {
        FlashCardFragment fragment = (FlashCardFragment)mViewPager.getAdapter()
                .instantiateItem(mViewPager, mViewPager.getCurrentItem());
        fragment.setText();
    }
	
	private void getFileList() {
        Log.i(TAG, "getFileList");
		AssetManager am = getAssets();		
		int resId = getIntent().getExtras().getInt(MainActivity.EXTRA_FOLDER_NAME);
		mFolderName = getString(resId);
		try {
			mFiles = new ArrayList<>
                    (Arrays.asList(am.list(mFolderName.toLowerCase(java.util.Locale.US))));
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
