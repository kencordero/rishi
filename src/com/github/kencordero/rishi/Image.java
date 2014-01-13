package com.github.kencordero.rishi;

public class Image {
	public enum Category {
		FOOD, ANIMAL, COLOR, SHAPE, LETTER, NUMBER		
	}
	
	private String mTitle;
	private String mFileName;
	private Category mCategory;
	
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
