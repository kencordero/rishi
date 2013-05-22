package com.github.kencordero.rishi;

import android.app.ListActivity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SettingsActivity extends ListActivity {
	Resources res = getResources();
	String locales[] = res.getStringArray(R.array.locales);
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Resources res = getResources();
		String locales[] = res.getStringArray(R.array.locales);
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, locales));
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		String localeName = locales[position];
		//TODO set locale
		
	}
}
