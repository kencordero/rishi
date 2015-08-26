package com.kentheken.rishi;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

public class LetterFragment extends Fragment {
	public static final String TAG = LetterFragment.class.getSimpleName();
	private static final String EXTRA_LETTER = "com.kentheken.rishi.letter";

	@Override
	public void onResume() {
		super.onResume();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		String mLocaleId = preferences.getString(SettingsFragment.KEY_PREF_LANGUAGE, "0");
		Locale mLocale = new Locale(mLocaleId);
		Configuration config = getActivity().getBaseContext().getResources().getConfiguration();
		config.locale = mLocale;
		getActivity().getBaseContext().getResources().updateConfiguration(config, null);
		if (mLocaleId.equals("mr"))
			mLocale = new Locale("hi");
        setLetter();
	}

    private void setLetter() {
		String mLetter = getArguments().getString(EXTRA_LETTER);
		TextView mTextView = (TextView) getView().findViewById(R.id.txtView_Letters);
        mTextView.setText(mLetter);
        mTextView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				// TODO setup TTS
			}
		});
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_letter, parent, false);
	}
	
	public static LetterFragment newInstance(String letter) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(EXTRA_LETTER, letter);
		
		LetterFragment fragment = new LetterFragment();
		fragment.setArguments(bundle);
		
		return fragment;
	}

}
