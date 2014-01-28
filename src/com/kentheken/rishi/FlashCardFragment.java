package com.kentheken.rishi;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class FlashCardFragment extends Fragment {
	public static final String EXTRA_FOLDER = "com.kentheken.rishi.folder";
	public static final String EXTRA_FILENAME = "com.kentheken.rishi.filename";
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String folder = getArguments().getString(EXTRA_FOLDER);
		String fileName = getArguments().getString(EXTRA_FILENAME);
		
	}
	
	public static FlashCardFragment newInstance(String folderName, String fileName) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(EXTRA_FOLDER, folderName);
		bundle.putSerializable(EXTRA_FILENAME, fileName);
		
		FlashCardFragment fragment = new FlashCardFragment();
		fragment.setArguments(bundle);
		
		return fragment;
		
	}
}
