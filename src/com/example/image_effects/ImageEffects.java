package com.example.image_effects;

import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.Menu;

public class ImageEffects implements Effects {

	@Override
	public Bitmap Flea(Bitmap image) {
	    // get image size
	    int width = image.getWidth();
	    int height = image.getHeight();
	    int[] pixels = new int[width * height];
	    // get pixel array from source
	    image.getPixels(pixels, 0, width, 0, 0, width, height);
	    // a random object
	    Random random = new Random();
	 
	    int index = 0;
	    // iteration through pixels
	    for(int y = 0; y < height; ++y) {
	        for(int x = 0; x < width; ++x) {
	            // get current index in 2D-matrix
	            index = y * width + x;
	            // get random color
	            int randColor = Color.rgb(random.nextInt(0xFF),
	                    random.nextInt(0xFF), random.nextInt(0xFF));
	            // OR
	            pixels[index] |= randColor;
	        }
	    }
	    // output bitmap
	    Bitmap bmOut = Bitmap.createBitmap(width, height, image.getConfig());
	    bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
	    return bmOut;
	}

	@Override
	public Bitmap Tint(Bitmap image, int intensity)
	{
		int width = image.getWidth();
        int height = image.getHeight();
 
        int[] pix = new int[width * height];
        image.getPixels(pix, 0, width, 0, 0, width, height);
 
        int RY, GY, BY, RYY, GYY, BYY, R, G, B, Y;
        double angle = (3.14 * (double)intensity) / 180;
        
        int S = (int)(256 * Math.sin(angle));
        int C = (int)(256 * Math.cos(angle));
 
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                int index = y * width + x;
                int r = ( pix[index] >> 16 ) & 0xff;
                int g = ( pix[index] >> 8 ) & 0xff;
                int b = pix[index] & 0xff;
                RY = ( 70 * r - 59 * g - 11 * b ) / 100;
                GY = (-30 * r + 41 * g - 11 * b ) / 100;
                BY = (-30 * r - 59 * g + 89 * b ) / 100;
                Y  = ( 30 * r + 59 * g + 11 * b ) / 100;
                RYY = ( S * BY + C * RY ) / 256;
                BYY = ( C * BY - S * RY ) / 256;
                GYY = (-51 * RYY - 19 * BYY ) / 100;
                R = Y + RYY;
                R = ( R < 0 ) ? 0 : (( R > 255 ) ? 255 : R );
                G = Y + GYY;
                G = ( G < 0 ) ? 0 : (( G > 255 ) ? 255 : G );
                B = Y + BYY;
                B = ( B < 0 ) ? 0 : (( B > 255 ) ? 255 : B );
                pix[index] = 0xff000000 | (R << 16) | (G << 8 ) | B;
            }
         
        Bitmap outBitmap = Bitmap.createBitmap(width, height, image.getConfig());    
        outBitmap.setPixels(pix, 0, width, 0, 0, width, height);
        
        pix = null;
        
        return outBitmap;
	}

	@Override
	public Bitmap GrayScale(Bitmap image)
	{
		// constant factors
	    final double GS_RED = 0.299;
	    final double GS_GREEN = 0.587;
	    final double GS_BLUE = 0.114;
	 
	    // create output bitmap
	    Bitmap bmOut = Bitmap.createBitmap(image.getWidth(), image.getHeight(), image.getConfig());
	    // pixel information
	    int A, R, G, B;
	    int pixel;
	 
	    // get image size
	    int width = image.getWidth();
	    int height = image.getHeight();
	 
	    // scan through every single pixel
	    for(int x = 0; x < width; ++x) {
	        for(int y = 0; y < height; ++y) {
	            // get one pixel color
	            pixel = image.getPixel(x, y);
	            // retrieve color of all channels
	            A = Color.alpha(pixel);
	            R = Color.red(pixel);
	            G = Color.green(pixel);
	            B = Color.blue(pixel);
	            // take conversion up to one single value
	            R = G = B = (int)(GS_RED * R + GS_GREEN * G + GS_BLUE * B);
	            // set new pixel color to output bitmap
	            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
	        }
	    }
	 
	    // return final image
	    return bmOut;
	}

	@Override
	public Bitmap Pixelate(Bitmap photo, int intensity)//range from 0 to 100
	{
		int width = photo.getWidth();
        int height = photo.getHeight();
        int max;
        
        if(height>width)
        	max=height;
        else
        	max=width;
		intensity=100-intensity;
        
        if(intensity>max)
        	intensity=max;
        
        if(intensity<6)
        	intensity=6;   

         Bitmap temp=Bitmap.createScaledBitmap(photo, intensity, intensity, true);
         return Bitmap.createScaledBitmap(temp, width, height, true);
	}

	@Override
	public Bitmap Invert(Bitmap image) {
		 
        //size of input image
        int width = image.getWidth();
        int height = image.getHeight();
 
        int[] pixels = new int[width*height];
        int index = 0;
        image.getPixels(pixels, 0, width, 0, 0, width, height);
        Bitmap returnBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        int A,R,G,B;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                //A = (pixels[index] >> 24) & 0xFF;
                R = (pixels[index] >> 16) & 0xFF;
                G = (pixels[index] >> 8) & 0xFF;
                B = pixels[index] & 0xFF;

                //A+=intensity;
                B=255-B;
                G=255-G;
                R=255-R;
                if(R > 255) { R = 255; }
                else if(R < 0) { R = 0; }

                if(G > 255) { G = 255; }
                else if(G < 0) { G = 0; }

                if(B > 255) { B = 255; }
                else if(B < 0) { B = 0; }
                
                pixels[index] = 0xff000000 | (R << 16) | (G << 8) | B;
                                ++index;
                 }
        }
        returnBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return returnBitmap;
	}

	@Override
	public Bitmap changeContrast(Bitmap image, float intensity) {
		int width = image.getWidth();
        int height = image.getHeight();
 
        int[] pixels = new int[width*height];
        int index = 0;
        double contrast = Math.pow((100 + intensity) / 100, 2);
        
        image.getPixels(pixels, 0, width, 0, 0, width, height);
        Bitmap returnBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        int A,R,G,B;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                //A = (pixels[index] >> 24) & 0xFF;
                R = (pixels[index] >> 16) & 0xFF;
                G = (pixels[index] >> 8) & 0xFF;
                B = pixels[index] & 0xFF;

                //A+=intensity;
                B = (int)(((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                G = (int)(((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                R = (int)(((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(R > 255) { R = 255; }
                else if(R < 0) { R = 0; }

                if(G > 255) { G = 255; }
                else if(G < 0) { G = 0; }

                if(B > 255) { B = 255; }
                else if(B < 0) { B = 0; }
                
                pixels[index] = 0xff000000 | (R << 16) | (G << 8) | B;
                                ++index;
                 }
        }
        returnBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return returnBitmap;
	}

	@Override
	public Bitmap changeBrightness(Bitmap image, int intensity) {
		 
        //size of input image
        int width = image.getWidth();
        int height = image.getHeight();
 
        int[] pixels = new int[width*height];
        int index = 0;
        image.getPixels(pixels, 0, width, 0, 0, width, height);
        Bitmap returnBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        int A,R,G,B;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                //A = (pixels[index] >> 24) & 0xFF;
                R = (pixels[index] >> 16) & 0xFF;
                G = (pixels[index] >> 8) & 0xFF;
                B = pixels[index] & 0xFF;

                //A+=intensity;
                B+=intensity;
                G+=intensity;
                R+=intensity;
                if(R > 255) { R = 255; }
                else if(R < 0) { R = 0; }

                if(G > 255) { G = 255; }
                else if(G < 0) { G = 0; }

                if(B > 255) { B = 255; }
                else if(B < 0) { B = 0; }
                
                pixels[index] = 0xff000000 | (R << 16) | (G << 8) | B;
                                ++index;
                 }
        }
        returnBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return returnBitmap;
	}

	@Override
	public Bitmap changeOpacity(Bitmap image, int intensity) //range from 0 to 255
	{
        //size of input image
        int width = image.getWidth();
        int height = image.getHeight();
 
        int[] pixels = new int[width*height];
        int index = 0;
        image.getPixels(pixels, 0, width, 0, 0, width, height);
        Bitmap returnBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        int A,R,G,B;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                A = (pixels[index] >> 24) & 0xFF;
                R = (pixels[index] >> 16) & 0xFF;
                G = (pixels[index] >> 8) & 0xFF;
                B = pixels[index] & 0xFF;
                A=255-intensity;
                
                pixels[index] = (A << 24) | (R << 16) | (G << 8) | B;
                ++index;
            }
        }
        returnBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return returnBitmap;
	}
}
