package com.app.filmit;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Face;
import android.hardware.Camera.FaceDetectionListener;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class CaptureScreenActivity extends Activity {

	public static final int VIDEO_PICK = 1;
	private static String TAG = "MainActivity";
	private Camera camera;
	private CameraPreview cameraPreview;
	boolean isRecording = false;
	boolean isCameraBack= true;
	boolean isFaceDetection = false;
	MediaRecorder mediaRecorder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture_screen);
		//Intent myIntent = new Intent(MainActivity.this, AudioRecordTest.class);
		//this.startActivity(myIntent);
		camera = getCameraInstance();
		if(camera != null) {
			cameraPreview = new CameraPreview(this, camera);
			FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
			preview.addView(cameraPreview);
		}
		else
			Log.d(TAG, "Camera Returned Null");
		
		setButtonListeners();
		
		
	}
	
	void setButtonListeners() {
		final Button recordButton = (Button) findViewById(R.id.button_capture);
		recordButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 if (isRecording) {
		                // stop recording and release camera
		                mediaRecorder.stop();  // stop the recording
		                releaseMediaRecorder(); // release the MediaRecorder object
		              
		                // inform the user that recording has stopped
		                recordButton.setText("Record!");
		                isRecording = false;
		            } 
				 else {
		                // initialize video camera
		                if (setMediaRecorder()) {
		                    // Camera is available and unlocked, MediaRecorder is prepared,
		                    // now you can start recording
		                    mediaRecorder.start();

		                    // inform the user that recording has started
		                    Toast.makeText(getBaseContext(), "Recording started!", Toast.LENGTH_SHORT).show();
		                    recordButton.setText("Stop!");
		                    isRecording = true;
		                } else {
		                    // prepare didn't work, release the camera
		                    releaseMediaRecorder();
		                    // inform user
		                }
		            }
		        }
				
			
		});
	
		final Button cameraTypeButton = (Button) findViewById(R.id.camera_front_back);
		if(camera.getNumberOfCameras()<=1) {
			cameraTypeButton.setEnabled(false);
		}
		else {
			cameraTypeButton.setOnClickListener(new OnClickListener() {
				int camId = 0;
				public void onClick(View v) {
					if(isCameraBack) {
						camId = Camera.CameraInfo.CAMERA_FACING_FRONT;
						cameraTypeButton.setText("Back Camera");
						isCameraBack = false;
						
					}
					else {
						cameraTypeButton.setText("Front Camera");
						camId = Camera.CameraInfo.CAMERA_FACING_BACK;
						isCameraBack = true;
						
					}
					
					camera.stopPreview();
					camera.release();
					try {
						camera = Camera.open(camId);
						camera.setPreviewDisplay(cameraPreview.getHolder());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					camera.startPreview();
				}
				
			});
		}
		
		final Button faceDetectButton = (Button) findViewById(R.id.face_detection);
		faceDetectButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!isFaceDetection) {
					faceDetectButton.setText("Face Detect (On)");
					camera.setFaceDetectionListener(null);
					isFaceDetection = true;
					camera.stopFaceDetection();
				}
				else {
					faceDetectButton.setText("Face Detect (Off)");
					isFaceDetection = false;
					camera.setFaceDetectionListener(new FaceDetectionListener() {

						@Override
						public void onFaceDetection(Face[] faces, Camera camera) {
							 if (faces.length == 0){
						           Log.i(TAG, " No Face Detected! ");
						        }else{
						            Log.i(TAG,String.valueOf(faces.length) + " Face Detected :) [ "
						                    + faces[0].rect.flattenToString()
						                    + "Coordinates : Left Eye - " + faces[0].leftEye + "]"
						            ) ;
						            Log.i("TEST", "face coordinates = Rect :" + faces[0].rect.flattenToString());
						            Log.i("TEST", "face coordinates = Left eye : " + String.valueOf(faces[0].leftEye));
						            Log.i("TEST", "face coordinates = Right eye - " + String.valueOf(faces[0].rightEye));
						            Log.i("TEST", "face coordinates = Mouth - " + String.valueOf(faces[0].mouth));
						        }
							
						}
						
					});
					camera.startFaceDetection();
				}
				
			}
			
		});
		
//		final Button galleryButton = (Button) findViewById(R.id.button_gallery);
//		galleryButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				camera.stopPreview();
//				Intent intent = new Intent();
//			    intent.setType("video/*");
//			    intent.setAction(Intent.ACTION_PICK);
//			    
//			    startActivityForResult(Intent.createChooser(intent,"Select Any"), VIDEO_PICK);
//			}
//			
//		});
	}

	 @Override
	    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	        if (requestCode == VIDEO_PICK) {
	            if (resultCode == Activity.RESULT_OK) {
	                Uri path = data.getData();
	                Intent intent = new Intent(this, EditScreenActivity.class);
	                intent.putExtra("path", path.toString());
	                startActivity(intent);
	            } 

	        }
	    }
	private boolean setMediaRecorder() {

		mediaRecorder = new MediaRecorder();
		camera.stopPreview();
		camera.unlock();
		mediaRecorder.setCamera(camera);
		
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
		mediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
		
		
		mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
		
		mediaRecorder.setOutputFile(this.getOutputMediaFile().toString());
		
		mediaRecorder.setPreviewDisplay(cameraPreview.getHolder().getSurface());
		
		
		 // Step 6: Prepare configured MediaRecorder
	    try {
	        mediaRecorder.prepare();
	    } catch (IllegalStateException e) {
	        Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
	        releaseMediaRecorder();
	        return false;
	    } catch (IOException e) {
	        Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
	        releaseMediaRecorder();
	        return false;
	    }
	    return true;
	}
	
	  private void releaseMediaRecorder(){
	        if (mediaRecorder != null) {
	            mediaRecorder.reset();   // clear recorder configuration
	            mediaRecorder.release(); // release the recorder object
	            mediaRecorder = null;
	            camera.lock();           // lock camera for later use
	        }
	    }
	  private void releaseCamera(){
	        if (camera != null){
	            camera.release();        // release the camera for other applications
	            camera = null;
	        }
	  }
	private static File getOutputMediaFile(){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_DCIM), "MyCameraApp");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "VID_"+ timeStamp + ".mp4");
	 

	    return mediaFile;
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
