package com.kentheken.rishi;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

import java.util.Locale;

public class TTSEngine {
	private static final String TAG = "TTSEngine";
	private static TextToSpeech mTTS;

    private static TTSEngine sEngine;
    private Language mCurrentLanguage;

    public Language getCurrentLanguage() {
        return mCurrentLanguage;
    }

    public enum Language { ENGLISH, MARATHI, SPANISH }

    private TTSEngine(Context c) {
        if (mTTS == null) {
			mTTS = new TextToSpeech(c, new OnInitListener() {
				@Override
				public void onInit(int status) {
					mTTS.setLanguage(Locale.ENGLISH);
                    mCurrentLanguage = Language.ENGLISH;
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

	public void setLanguage(Language language) {
        if (language != mCurrentLanguage) {
            switch (language) {
                case ENGLISH:
                    mTTS.setLanguage(Locale.ENGLISH);
                    mCurrentLanguage = Language.ENGLISH;
                    break;
                case MARATHI:
                    mTTS.setLanguage(new Locale("hi"));
                    mCurrentLanguage = Language.MARATHI;
                    break;
                case SPANISH:
                    mTTS.setLanguage(new Locale("es"));
                    mCurrentLanguage = Language.SPANISH;
                    break;
            }
        }
	}
	
	public void shutdown() {
		mTTS.shutdown();
	}
}
