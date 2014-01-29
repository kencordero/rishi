package com.kentheken.rishi;

import android.os.Bundle;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LetterFragment extends Fragment 
	implements OnInitListener {
	public static final String EXTRA_LETTER = "com.kentheken.rishi.letter";
	
	private TextView mTextView;
	private String mLetter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_letters, parent, false);
		
		mLetter = getArguments().getString(EXTRA_LETTER);
		mTextView = (TextView)v.findViewById(R.id.txtView_Letters);
		mTextView.setText(mLetter);
		mTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO
			}			
		});
		
		return v;
	}
	
	@Override
	public void onInit(int arg0) {}
	
	public static LetterFragment newInstance(String letter) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(EXTRA_LETTER, letter);
		
		LetterFragment fragment = new LetterFragment();
		fragment.setArguments(bundle);
		
		return fragment;
	}

}
