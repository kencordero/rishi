package com.kentheken.rishi;

import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

public class TTSEngine implements OnInitListener {
	private static TextToSpeech mTTS;
	
	public TTSEngine(Context c) {
		if (mTTS == null)
			mTTS = new TextToSpeech(c, this);		
	}
	
	public void stop() {
		if (mTTS != null) {
			mTTS.stop();
		}
	}
	
	public void onInit(int status) { }
	
	public void speak(Locale locale, String textToSpeak) {
		stop();
		
		mTTS.setLanguage(locale);
		mTTS.speak(textToSpeak, TextToSpeech.QUEUE_ADD, null);
	}
	
	public void shutdown() {
		mTTS.shutdown();
	}
}
