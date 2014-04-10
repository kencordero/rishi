package com.kentheken.rishi;

import java.io.InputStream;
import java.util.Locale;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class FlashCardFragment2 extends Fragment {
	public static final String EXTRA_FOLDER = "com.kentheken.rishi.folder";
	public static final String EXTRA_FILENAME = "com.kentheken.rishi.filename";
	private DatabaseOpenHelper mDbHelper;
	private SQLiteDatabase mDatabase;
	
	private ImageView mImageView;
	private TextView mTextView;
	private String mFileName;
	private String mText;
	private TTSEngine mTTS;
	private Locale mLocale;
	private RadioButton mRadioButton1;
	private RadioButton mRadioButton2;
	private RadioButton mRadioButton3;
	
	@Override
	public void onResume() {
		super.onResume();
		mDbHelper = new DatabaseOpenHelper(getActivity());
		mDatabase = mDbHelper.openDatabase();			
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mDatabase.close();
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
					setText("en");				
			}
		});
		mRadioButton2 = (RadioButton)v.findViewById(R.id.opt2);
		mRadioButton2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (((RadioButton)v).isChecked())
					setText("mr");
			}
		});
		mRadioButton3 = (RadioButton)v.findViewById(R.id.opt3);
		mRadioButton3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (((RadioButton)v).isChecked())
					setText("es");
			}
		});
		return v;
	}
	
	public void setText(String localeId) {
		if (localeId.equals("mr")) {
			mTextView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "DroidHindi.ttf"));
			mLocale = new Locale("hi"); // no Marathi locale exists
		}
		else {
			mTextView.setTypeface(Typeface.DEFAULT);
			mLocale = new Locale(localeId);
		}
		mText = getText(localeId);
		mTextView.setText(mText);
		speakText();
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
		if (mText != null && mLocale != null)
			mTTS.speak(mLocale, mText);					
	}

	private void throwError(Exception e) {
		Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
		e.printStackTrace();
	}
	
	private String getText(String localeId) {
		Cursor cursor = mDatabase.rawQuery("SELECT display_name " +
		"FROM imagelocale INNER JOIN image " +
		"ON imagelocale.image_id = image._id " +
		"INNER JOIN locale ON imagelocale.locale_id = locale._id " +
		"WHERE file_name = ? AND code = ?", new String[] {mFileName, localeId});
		String displayName = null;
		if (cursor != null) {
			try {
				cursor.moveToFirst();
				do {
					displayName = cursor.getString(cursor.getColumnIndex("display_name"));
				} while (cursor.moveToNext());
			} finally {
				cursor.close();				
			}
		}
		return displayName;
	}
}
