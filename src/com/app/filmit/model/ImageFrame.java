package com.app.filmit.model;

import android.graphics.Bitmap;

public class ImageFrame {
	private Bitmap bitmap;
	private int id;
	
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}

}
