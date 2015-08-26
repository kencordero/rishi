package com.kentheken.rishi;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

import java.util.Locale;

public class TTSEngine {
	private static final String TAG = TTSEngine.class.getSimpleName();
	private static TextToSpeech mTTS;

    private static TTSEngine sEngine;
    private int mCurrentLanguage;

    public int getCurrentLanguage() {
        return mCurrentLanguage;
    }

    public static final int LANGUAGE_ENGLISH = 0;
    public static final int LANGUAGE_MARATHI = 1;
    public static final int LANGUAGE_SPANISH = 2;

    private TTSEngine(Context c) {
        if (mTTS == null) {
			mTTS = new TextToSpeech(c, new OnInitListener() {
				@Override
				public void onInit(int status) {
					mTTS.setLanguage(Locale.ENGLISH);
                    mCurrentLanguage = LANGUAGE_ENGLISH;
					Log.d(TAG, "English TTS initialized");
				}				
			});
		}
	}

    public static TTSEngine get(Context c) {
        if (sEngine == null) {
            sEngine = new TTSEngine(c.getApplicationContext());
        }
        return sEngine;
    }
	
	public void stop() {
		if (mTTS != null)
			mTTS.stop();
	}		
	
	public void speak(String textToSpeak) {
        //TODO be sure to set language when option is set
		stop();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTTS.speak(textToSpeak, TextToSpeech.QUEUE_ADD, null, "");
        }
		else {
            mTTS.speak(textToSpeak, TextToSpeech.QUEUE_ADD, null);
        }
	}

	public void setLanguage(int language) {
        if (language != mCurrentLanguage) {
            mCurrentLanguage = language;
            switch (language) {
                case LANGUAGE_ENGLISH:
                    mTTS.setLanguage(Locale.ENGLISH);
                    break;
                case LANGUAGE_MARATHI:
                    mTTS.setLanguage(new Locale("hi"));
                    break;
                case LANGUAGE_SPANISH:
                    mTTS.setLanguage(new Locale("es"));
                    break;
            }
        }
	}
	
	public void shutdown() {
		mTTS.shutdown();
	}
}
