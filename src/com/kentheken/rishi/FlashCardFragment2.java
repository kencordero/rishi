package com.kentheken.rishi;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class FlashCardFragment2 extends Fragment {
	private static final String TAG = "com.kentheken.rishi.FlashCardFragment2";
	public static final String EXTRA_FOLDER = "com.kentheken.rishi.folder";
	public static final String EXTRA_FILENAME = "com.kentheken.rishi.filename";
	private DatabaseOpenHelper dbHelper;
	private SQLiteDatabase mSqliteDb;
	
	private ImageView mImageView;
	private TextView mTextView;
	private String mFileName;
	private int mResId;	
	private TTSEngine mTTS;
	private String mLocaleId;
	private Locale mLocale;
	private RadioButton mRadioButton1;
	private RadioButton mRadioButton2;
	private RadioButton mRadioButton3;
	
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
		dbHelper = new DatabaseOpenHelper(getActivity());
		mSqliteDb = dbHelper.openDataBase();			
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mSqliteDb.close();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		mTTS = new TTSEngine(getActivity());
		View v = inflater.inflate(R.layout.activity_words_v2, parent, false);

		mImageView = (ImageView) v.findViewById(R.id.imgView_Words);
		mTextView = (TextView) v.findViewById(R.id.txtView_Words);

		String folder = getArguments().getString(EXTRA_FOLDER).toLowerCase(Locale.US);
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
				//displayText();
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
		mRadioButton1 = (RadioButton)v.findViewById(R.id.opt1);
		mRadioButton1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (((RadioButton)v).isChecked())
				{
					mLocaleId = "en";
					mTextView.setTypeface(Typeface.DEFAULT);
					mTextView.setText(getText());
				}	
			}
		});
		mRadioButton2 = (RadioButton)v.findViewById(R.id.opt2);
		mRadioButton2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (((RadioButton)v).isChecked())
				{
					mLocaleId = "mr";
					mTextView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "DroidHindi.ttf"));
					mTextView.setText(getText());
				}	
			}
		});
		mRadioButton3 = (RadioButton)v.findViewById(R.id.opt3);
		mRadioButton3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (((RadioButton)v).isChecked())
				{					
					mLocaleId = "es";
					mTextView.setTypeface(Typeface.DEFAULT);
					mTextView.setText(getText());
				}	
			}
		});
		mResId = -1;
		return v;
	}

	public static FlashCardFragment2 newInstance(String folderName,
			String fileName) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(EXTRA_FOLDER, folderName);
		bundle.putSerializable(EXTRA_FILENAME, fileName);

		FlashCardFragment2 fragment = new FlashCardFragment2();
		fragment.setArguments(bundle);

		return fragment;
	}
					
	private void speakText() {
		if (mResId > 0)
			mTTS.speak(mLocale, getString(mResId));					
	}

	private void throwError(Exception e) {
		Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
		e.printStackTrace();
	}
	
	private String getText() {
		Cursor cursor = mSqliteDb.rawQuery("SELECT display_name " +
		"FROM imagelocale INNER JOIN image " +
		"ON imagelocale.image_id = image._id " +
		"INNER JOIN locale ON imagelocale.locale_id = locale._id " +
		"WHERE file_name = ? AND code = ?", new String[] {mFileName, mLocaleId});
		String display_name = "No translation found";
		if (cursor != null) {
			try {
				cursor.moveToFirst();
				do {
					display_name = cursor.getString(cursor.getColumnIndex("display_name"));
				} while (cursor.moveToNext());
			} finally {
				cursor.close();				
			}
		}
		return display_name;
	}
}
