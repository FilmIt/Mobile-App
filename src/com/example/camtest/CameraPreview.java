package com.example.camtest;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback  {

	private SurfaceHolder surfaceHolder;
	private Camera camera;
	private static String TAG = "CamerPreview";
	
	public CameraPreview(Context context, Camera cam) {
		super(context);
		this.camera = cam;
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
		
		//we don't need the depricated method if running on above 3.0 version
		//surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); 
	}

	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
		if(surfaceHolder.getSurface() == null) {// camera not in used 
			return;
		}
		
		//preview needs to be stopped from camera before making surface changes
		try {
			camera.stopPreview();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		//if custom size is to be set. Set it here! use getSupportedPreviewSize()
		try {
			camera.setPreviewDisplay(surfaceHolder);
			camera.startPreview();

		} catch (Exception e){
			Log.d(TAG, "Error starting camera preview: " + e.getMessage());
		}
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
		try {
			camera.setPreviewDisplay(surfaceHolder);
			camera.startPreview();
		}
		catch(IOException e) {
			Log.d(TAG, "Error setting camera preview: " + e.getMessage());
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.release();
		
	}

}
