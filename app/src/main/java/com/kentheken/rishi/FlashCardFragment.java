package com.kentheken.rishi;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

public class FlashCardFragment extends Fragment {
    private static final String TAG = "FlashCardFragment";
	public static final String EXTRA_FOLDER = "com.kentheken.rishi.folder";
	public static final String EXTRA_FILENAME = "com.kentheken.rishi.filename";
	private SQLiteDatabase mDatabase;

    public void clearText() {
        mTextView.setText("");
    }

    public enum Locale { ENGLISH, MARATHI, SPANISH }
	private TextView mTextView;
	private String mFileName;
	private String mText;
	private TTSEngine mTTS;
	private Locale mLocale;
    private Callbacks mCallbacks;

    public interface Callbacks {
        Locale onsetText();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
	
	@Override
	public void onResume() {
		super.onResume();
		DatabaseOpenHelper dbHelper = new DatabaseOpenHelper(getActivity());
		mDatabase = dbHelper.openDatabase();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mDatabase.close();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		mTTS = new TTSEngine(getActivity());
		View v = inflater.inflate(R.layout.fragment_flashcard, parent, false);

		ImageView imageView = (ImageView) v.findViewById(R.id.imgView_Words);
		mTextView = (TextView) v.findViewById(R.id.txtView_Words);

		String folder = getArguments().getString(EXTRA_FOLDER).toLowerCase(java.util.Locale.US);
		mFileName = getArguments().getString(EXTRA_FILENAME);

		InputStream stream = null;
		try {
			stream = getActivity().getAssets().open(folder + "/" + mFileName);
		} catch (Exception e) {
			throwError(e);
		}
		Drawable img = Drawable.createFromStream(stream, mFileName);
		imageView.setImageDrawable(img);
		imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "ImageView click");
                setText();
            }
        });
		mTextView.setText("");
		mTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                Log.i(TAG, "TextView click");
                setText();
			}
		});		
		return v;
	}
	
	public void setText() {
        Locale locale = mCallbacks.onsetText();
        if (locale.equals(mLocale) && mText != null) {
            Log.i(TAG, "setText: already cached");
            mTextView.setText(mText);
        }
        else {
            mLocale = locale;
            if (mLocale == Locale.MARATHI)
                mTextView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "DroidHindi.ttf"));
            else
                mTextView.setTypeface(Typeface.DEFAULT);
            mText = getText();
            Log.i(TAG, "setText: " + mText);

            mTextView.setText(mText);
        }
		speakText();
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
					
	private void speakText() {
        Log.i(TAG, "speakText - Locale: " + mLocale.toString());
        Log.i(TAG, "speakText - Text: " + mText);
		if (mText != null && mLocale != null)
			mTTS.speak(mLocale, mText);
	}

	private void throwError(Exception e) {
		Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
		e.printStackTrace();
	}

    private String getLocaleCode(Locale lId) {
        switch (lId) {
            case ENGLISH:
                return "en";
            case MARATHI:
                return "mr";
            case SPANISH:
                return "es";
            default:
                return "";
        }
    }
	
	private String getText() {
		String languageCode = getLocaleCode(mLocale);
		Cursor cursor = mDatabase.rawQuery("SELECT display_name " +
		"FROM imagelocale INNER JOIN image " +
		"ON imagelocale.image_id = image._id " +
		"INNER JOIN locale ON imagelocale.locale_id = locale._id " +
		"WHERE file_name = ? AND code = ?", new String[] {mFileName, languageCode});
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