package com.kentheken.rishi;

import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

public class TTSEngine implements OnInitListener {
	private static TextToSpeech mTTS_en;
	private static TextToSpeech mTTS_mr;
	private static TextToSpeech mTTS_es;
	
	public TTSEngine(Context c) {
		if (mTTS_en == null) {
			mTTS_en = new TextToSpeech(c, this);
			mTTS_en.setLanguage(Locale.ENGLISH);
		}
		if (mTTS_mr == null) {
			mTTS_mr = new TextToSpeech(c, this);
			mTTS_mr.setLanguage(new Locale("hi"));
		}
		if (mTTS_es == null) {
			mTTS_es = new TextToSpeech(c, this);
			mTTS_es.setLanguage(new Locale("es"));
		}
	}
	
	public void stop() {
		if (mTTS_en != null)
			mTTS_en.stop();
		if (mTTS_mr != null)
			mTTS_mr.stop();
		if (mTTS_es != null)
			mTTS_es.stop();
	}
	
	public void onInit(int status) { }
	
	public void speak(FlashCardFragment2.LocaleId lId, String textToSpeak) {
		stop();
		TextToSpeech tts = null;
		switch (lId) {
		case ENGLISH:			
			tts = mTTS_en;
			break;
		case MARATHI:
			tts = mTTS_mr;
			break;
		case SPANISH:
			tts = mTTS_es;
			break;
		}
		tts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null);
		
	}
	
	public void shutdown() {
		mTTS_en.shutdown();
		mTTS_mr.shutdown();
		mTTS_es.shutdown();
	}
}
