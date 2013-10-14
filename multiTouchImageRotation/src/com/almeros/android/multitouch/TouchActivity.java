package com.almeros.android.multitouch;

import java.io.File;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import com.almeros.android.multitouch.gesturedetectors.RotateGestureDetector;

public class TouchActivity extends Activity implements OnTouchListener {
	private static String LOG_TAG = "Touch";
	
	private Matrix mMatrix = new Matrix();
    private float mScaleFactor = .4f;
    private float mRotationDegrees = 0.f;
    private float mFocusX = 0.f;
    private float mFocusY = 0.f;  
    private int mAlpha = 255;
    private int mImageHeight, mImageWidth;

    private RotateGestureDetector mRotateDetector;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ImageView view = (ImageView) findViewById(R.id.imageView);
		view.setOnTouchListener(this);
		
		// View is scaled by matrix, so scale initially
		mMatrix.postScale(mScaleFactor, mScaleFactor);
		view.setImageMatrix(mMatrix);
		
		File myFile = new File("/sdcard/photo.jpg");
		
		//final Bitmap d1=d.getBitmap();
		
		// Dimensions of image
		BitmapDrawable d = new BitmapDrawable(getResources(), myFile.getAbsolutePath());
		//Drawable d 		= this.getResources().getDrawable(R.drawable.earth);
		mImageHeight 	= d.getIntrinsicHeight();
		mImageWidth 	= d.getIntrinsicWidth();
		Log.d(LOG_TAG, "Image dimensions -> height: " + mImageHeight + "px, width: " + mImageWidth + "px");

		// Setup Gesture Detectors
		mRotateDetector = new RotateGestureDetector(getApplicationContext(), new RotateListener());
	}
	
	public boolean onTouch(View v, MotionEvent event) {
        //mScaleDetector.onTouchEvent(event);
        mRotateDetector.onTouchEvent(event);
        //mMoveDetector.onTouchEvent(event);
        //mShoveDetector.onTouchEvent(event);

        float scaledImageCenterX = (mImageWidth*mScaleFactor)/2;
        float scaledImageCenterY = (mImageHeight*mScaleFactor)/2;
        
        mMatrix.reset();
        mMatrix.postScale(mScaleFactor, mScaleFactor);
        mMatrix.postRotate(mRotationDegrees,  scaledImageCenterX, scaledImageCenterY);
        //mMatrix.postTranslate(mFocusX - scaledImageCenterX, mFocusY - scaledImageCenterY);
        mMatrix.postTranslate(mFocusX, mFocusY);
        
		ImageView view = (ImageView) v;
		view.setImageMatrix(mMatrix);
		view.setAlpha(mAlpha);

		return true; // indicate event was handled
	}
	
	private class RotateListener extends RotateGestureDetector.SimpleOnRotateGestureListener {
		@Override
		public boolean onRotate(RotateGestureDetector detector) {
			mRotationDegrees -= detector.getRotationDegreesDelta();
			return true;
		}
	}	
	
}