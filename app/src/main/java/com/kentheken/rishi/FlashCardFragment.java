package com.kentheken.rishi;

import android.app.Activity;
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
import java.util.Locale;

public class FlashCardFragment extends Fragment {
    private static final String TAG = "FlashCardFragment";
	public static final String EXTRA_FOLDER = "com.kentheken.rishi.folder";
	public static final String EXTRA_FILENAME = "com.kentheken.rishi.filename";

    private DatabaseOpenHelper mDbHelper;

    public void clearText() {
        mTextView.setText("");
    }

	private TextView mTextView;
	private String mFileName;
	private String mText;
	private TTSEngine mTTS;
	private TTSEngine.Language mLanguage;
    private Callbacks mCallbacks;

    public interface Callbacks {
        TTSEngine.Language onSetText();
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
	}
	
	@Override
	public void onPause() {
		super.onPause();
        DatabaseOpenHelper.get(getActivity()).close();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_flashcard, parent, false);

		ImageView imageView = (ImageView) v.findViewById(R.id.imgView_Words);
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
        TTSEngine.Language locale = mCallbacks.onSetText();
        if (locale.equals(mLanguage) && mText != null) {
            Log.i(TAG, "setText: already cached");
            mTextView.setText(mText);
        }
        else {
            mLanguage = locale;
            if (mLanguage == TTSEngine.Language.MARATHI)
                mTextView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "DroidHindi.ttf"));
            else
                mTextView.setTypeface(Typeface.DEFAULT);
            mText = DatabaseOpenHelper.get(getActivity()).getText(mFileName, mLanguage);
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
        Log.i(TAG, "speakText - Locale: " + mLanguage.toString());
        Log.i(TAG, "speakText - Text: " + mText);
		if (mText != null && mLanguage != null)
			mTTS.speak(mText);
	}

	private void throwError(Exception e) {
		Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
		e.printStackTrace();
	}
}
