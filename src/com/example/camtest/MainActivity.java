package com.example.camtest;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.FrameLayout;

public class MainActivity extends Activity {

	private static String TAG = "MainActivity";
	private Camera camera;
	private CameraPreview cameraPreview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		camera = getCameraInstance();
		if(camera != null) {
			cameraPreview = new CameraPreview(this, camera);
			FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
			preview.addView(cameraPreview);
		}
		else
			Log.d(TAG, "Camera Returned Null");
	}

	private Camera getCameraInstance() {
		Camera cam = null;
		try {
			cam = Camera.open(); 
		}
		catch (Exception e) {
			Log.d(TAG, "Can't access Camera.." + e.getMessage());
		}
		return cam;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
