package com.kentheken.rishi;

import android.app.Activity;
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
	private TextView mTextView;
	private OnFragmentInteractionListener mListener;

	public interface OnFragmentInteractionListener {
		void onAlphabetUpdated();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mListener = (OnFragmentInteractionListener)activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

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
		String letter = getArguments().getString(EXTRA_LETTER);
        mTextView.setText(letter);
        mTextView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				// TODO setup TTS
			}
		});
    }

	public void setLetter(String letter) {
		mTextView.setText(letter);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_letter, parent, false);
		mTextView = (TextView)view.findViewById(R.id.txtView_Letters);
		return view;
	}
	
	public static LetterFragment newInstance(String letter) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(EXTRA_LETTER, letter);
		
		LetterFragment fragment = new LetterFragment();
		fragment.setArguments(bundle);
		
		return fragment;
	}

}
