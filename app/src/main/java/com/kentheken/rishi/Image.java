package com.kentheken.rishi;

public class Image {
	public enum Category {
		FOOD, ANIMAL, COLOR, SHAPE, LETTER, NUMBER		
	}
	
	private final String mTitle;
	private final String mFileName;
	private final Category mCategory;
	
	public Image(String title, String fileName, Category category)  {
		mTitle = title;
		mFileName = fileName;
		mCategory = category;
	}
	
	public String getTitle() {
		return mTitle;
	}
	
	public String getFileName() {
		return mFileName;
	}
	
	public Category getCategory() {
		return mCategory;
	}
}
