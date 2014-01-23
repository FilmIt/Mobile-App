package com.app.filmkit.utils;

import java.io.File;

import android.os.Environment;
import android.util.Log;

public class Constants {
	public static final String DURATION_LIMIT = "30000";
	public static final String TAG = "Film KIT";
	
	public static final int SLIDER_EFFECT = 1;
	public static final int EMPTY_EFFECT = 2;
	public static final int FLEA = 11;
	public static final int INVERT = 12;
	public static final int GREY_SCALE = 13;
	public static final String LOG = "Film Kit";
	public static enum EffectType{FLEA, TINT, GREY_SCALE, INVERT, CONTRAST, BRIGHTNESS, PIXELLATE, OPACITY }

	public static File getMyFolderDir() {
	    // Get the directory for the user's public pictures directory.
	    File file = new File(Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_MOVIES), "Film Kit");
	    if (!file.mkdirs()) {
	        Log.e(Constants.LOG, "Directory not created");
	    }
	    return file;
	}
}
