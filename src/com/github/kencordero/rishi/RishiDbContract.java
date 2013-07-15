package com.github.kencordero.rishi;

import android.provider.BaseColumns;

public final class RishiDbContract {

	public RishiDbContract() {}
	
	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
	
	public static abstract class Locale implements BaseColumns {
		public static final String TABLE_NAME = "locale";
		public static final String COLUMN_NAME_NAME = "name";
		private static final String SQL_CREATE_ENTRIES = 
			"CREATE TABLE " + TABLE_NAME + " (" + 
			_ID + " INTEGER PRIMARY KEY," + 
			COLUMN_NAME_NAME + TEXT_TYPE + " )";
		private static final String SQL_DELETE_ENTRIES =
			"DROP TABLE IF EXISTS " + TABLE_NAME;
	}
	
	public static abstract class Category implements BaseColumns {
		public static final String TABLE_NAME = "category";
		public static final String COLUMN_NAME_NAME = "name";
		private static final String SQL_CREATE_ENTRIES =
			"CREATE TABLE " + TABLE_NAME + " (" + 
			_ID + " INTEGER PRIMARY KEY," + 
			COLUMN_NAME_NAME + TEXT_TYPE + " )";
		private static final String SQL_DELETE_ENTRIES =
			"DROP TABLE IF EXISTS " + TABLE_NAME;
		
	}
	
	public static abstract class Image implements BaseColumns {
		public static final String TABLE_NAME = "image";		
		public static final String COLUMN_NAME_FILE_NAME = "file_name";
		public static final String COLUMN_NAME_CATEGORY = "category";
		private static final String SQL_CREATE_ENTRIES =
			"CREATE TABLE " + TABLE_NAME + " (" + 
			_ID + " INTEGER PRIMARY KEY," + 
			COLUMN_NAME_FILE_NAME + TEXT_TYPE + COMMA_SEP +
			COLUMN_NAME_CATEGORY + TEXT_TYPE + " )";
		private static final String SQL_DELETE_ENTRIES =
			"DROP TABLE IF EXISTS " + TABLE_NAME;
	}
	
	public static abstract class Color implements BaseColumns {
		public static final String TABLE_NAME = "color";
		public static final String COLUMN_NAME_RGB_VALUE = "rgb_value";
		private static final String SQL_CREATE_ENTRIES =
			"CREATE TABLE " + TABLE_NAME + " (" +
			_ID + " INTEGER PRIMARY KEY," +
			COLUMN_NAME_RGB_VALUE + TEXT_TYPE + " )";
		private static final String SQL_DELETE_ENTRIES =
			"DROP TABLE IF EXISTS " + TABLE_NAME;
	}
	
	public static abstract class ImageLocale implements BaseColumns {
		public static final String TABLE_NAME = "image_locale";
		public static final String COLUMN_NAME_IMAGE = "image";
		public static final String COLUMN_NAME_LOCALE = "locale";
		public static final String COLUMN_NAME_DISPLAY_NAME = "display_name";
	}
	
	public static abstract class ColorLocale implements BaseColumns {
		public static final String TABLE_NAME = "color_locale";
		public static final String COLUMN_NAME_COLOR = "color";
		public static final String COLUMN_NAME_LOCALE = "locale";
		public static final String COLUMN_NAME_DISPLAY_NAME = "display_name";
	}

}
