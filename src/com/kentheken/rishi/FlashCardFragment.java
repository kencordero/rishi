package com.kentheken.rishi;

import java.io.InputStream;
import java.util.Locale;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FlashCardFragment extends Fragment {
	public static final String EXTRA_FOLDER = "com.kentheken.rishi.folder";
	public static final String EXTRA_FILENAME = "com.kentheken.rishi.filename";

	private ImageView mImageView;
	private TextView mTextView;
	private String mFileName;
	private int mResId;
	private TTSEngine mTTS;
	private String mLocaleId;
	private Locale mLocale;
	
	@Override
	public void onResume() {
		super.onResume();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		mLocaleId = preferences.getString(SettingsFragment.KEY_PREF_LANGUAGE,  "0");
		Configuration config = getActivity().getBaseContext().getResources().getConfiguration();
		mLocale = new Locale(mLocaleId);
		config.locale = mLocale;
		getActivity().getBaseContext().getResources().updateConfiguration(config, null);
		if (mLocaleId.equals("mr")) //There's no speech engine for Marathi			
			mLocale = new Locale("hi");		
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		mTTS = new TTSEngine(getActivity());
		View v = inflater.inflate(R.layout.activity_words, parent, false);

		mImageView = (ImageView) v.findViewById(R.id.imgView_Words);
		mTextView = (TextView) v.findViewById(R.id.txtView_Words);

		String folder = getArguments().getString(EXTRA_FOLDER);
		mFileName = getArguments().getString(EXTRA_FILENAME);

		InputStream stream = null;
		try {
			stream = getActivity().getAssets().open(folder + "/" + mFileName);
		} catch (Exception e) {
			throwError(e);
		}
		Drawable img = Drawable.createFromStream(stream, mFileName);
		mImageView.setImageDrawable(img);
		mImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayText();
				speakText();
			}
		});
		mTextView.setText("");
		mTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				speakText();
			}
		});
		mResId = -1;		
		return v;
	}

	public static FlashCardFragment newInstance(String folderName,
			String fileName) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(EXTRA_FOLDER, folderName);
		bundle.putSerializable(EXTRA_FILENAME, fileName);

		FlashCardFragment fragment = new FlashCardFragment();
		fragment.setArguments(bundle);

		return fragment;
	}

	private void displayText() {
		if (mTextView.getText().length() == 0) {
			String displayName = mFileName.replace(".jpg", "");
			try {
				mResId = R.string.class.getField(displayName).getInt(null);
				mTextView.setText(mResId);
			} catch (Exception e) {
				throwError(e);
			}
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_random:
				return true;
			case R.id.action_settings:
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	

	private void speakText() {
		if (mResId > 0)
			mTTS.speak(mLocale, getString(mResId));					
	}

	private void throwError(Exception e) {
		Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
		e.printStackTrace();
	}
}
