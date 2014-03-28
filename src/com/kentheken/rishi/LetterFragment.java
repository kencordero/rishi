package com.kentheken.rishi;

import java.util.Locale;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LetterFragment extends Fragment {
	public static final String EXTRA_LETTER = "com.kentheken.rishi.letter";
	
	private TextView mTextView;
	private String mLetter;
	private TTSEngine mTTS;
	private String mLocaleId;
	private Locale mLocale;
	
	@Override
	public void onResume() {
		super.onResume();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		mLocaleId = preferences.getString(SettingsFragment.KEY_PREF_LANGUAGE,  "0");
		mLocale = new Locale(mLocaleId);
		Configuration config = getActivity().getBaseContext().getResources().getConfiguration();
		config.locale = mLocale;
		getActivity().getBaseContext().getResources().updateConfiguration(config, null);
		if (mLocaleId.equals("mr"))
			mLocale = new Locale("hi");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_letters, parent, false);
		
		mLetter = getArguments().getString(EXTRA_LETTER);
		mTextView = (TextView)v.findViewById(R.id.txtView_Letters);
		mTextView.setText(mLetter);
		mTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mTTS.speak(mLocale, mLetter);
			}			
		});
		
		return v;
	}
	
	public static LetterFragment newInstance(String letter) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(EXTRA_LETTER, letter);
		
		LetterFragment fragment = new LetterFragment();
		fragment.setArguments(bundle);
		
		return fragment;
	}

}
