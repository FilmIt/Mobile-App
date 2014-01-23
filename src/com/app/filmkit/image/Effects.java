package com.app.filmkit.image;

import android.graphics.Bitmap;

public interface Effects {
	public Bitmap Flea(Bitmap image);
	
	public Bitmap Tint(Bitmap image, int intensity);
	
	public Bitmap GrayScale(Bitmap image);
	
	public Bitmap Pixelate(Bitmap photo, int intensity);//range from 0 to 100
	
	public Bitmap Invert(Bitmap image);
	
	public Bitmap changeContrast(Bitmap image, float intensity);
	
	public Bitmap changeBrightness(Bitmap image, int intensity);
	public Bitmap changeOpacity(Bitmap image, int intensity); //range from 0 to 255
}
