package com.app.filmkit;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

import com.app.filmkit.utils.Constants;

public class VideoPlayerActivity extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl {

    SurfaceView videoSurface;
    MediaPlayer player;
    VideoControllerView controller;
    String path =  "" ;
    boolean themeMenuOpen = false;
	private MediaMetadataRetriever mediaMetadataRetriever;
	public static Bitmap bitmap;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_video);
        Log.d(Constants.TAG, "In OnCreate");
        Intent intent = getIntent();
        path = intent.getExtras().getString("path");
        Log.d(Constants.TAG, "Path in VideoPlayerActivity = " + path);
        videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
        mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(this, Uri.parse(path));
        SurfaceHolder videoHolder = videoSurface.getHolder();
        videoHolder.addCallback(this);      
        //addButtonListeners();
    }
    
    public void onResume() {
    	super.onResume();
    	 player = new MediaPlayer();
         controller = new VideoControllerView(this);
         controller.setPrevNextListeners(null, null); // just to enable buttons
         try {
             player.setAudioStreamType(AudioManager.STREAM_MUSIC);
             player.setDataSource(this, Uri.parse(path));
             player.setOnPreparedListener(this);
             player.setOnErrorListener(this);
         } catch (IllegalArgumentException e) {
             e.printStackTrace();
         } catch (SecurityException e) {
             e.printStackTrace();
         } catch (IllegalStateException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         
         
    	
    }

	
	
	protected void goToEditScreenActivity() {
		Intent intent = new Intent(this.getApplicationContext(),EditScreenActivity.class);
		intent.putExtra("path", path);
		player.stop();
		this.startActivity(intent);

	}
	
	
	
	
	
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        controller.show();
        return false;
    }

    // Implement SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    	Log.d(Constants.TAG, "In SurfaceChanged");
    	
        
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    	Log.d(Constants.TAG, "In SurfaceCreated");
    	player.setDisplay(holder);
    	player.prepareAsync();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    	Log.d(Constants.TAG, "In SurfaceDestroyed");
    	player.release();
  
    }
    // End SurfaceHolder.Callback

    // Implement MediaPlayer.OnPreparedListener
    @Override
    public void onPrepared(MediaPlayer mp) {
    	Log.d(Constants.TAG, "In OnPrepared");
        controller.setMediaPlayer(this);
        controller.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
        player.start();
    }
    // End MediaPlayer.OnPreparedListener

    // Implement VideoMediaController.MediaPlayerControl
    @Override
    public boolean canPause() {
        return true;
    }
    
    @Override
    public void onBackPressed() {
    	Log.d(Constants.TAG, "In OnBackpressed");
    	//player.stop();
    	super.onBackPressed();
    }
    
    @Override 
    public void onPause() {
    	Log.d(Constants.TAG, "In OnPause");
    	player.stop();
    	super.onPause();
    }
 

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return player.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public void pause() {
        player.pause();
    }

    @Override
    public void seekTo(int i) {
        player.seekTo(i);
    }

    @Override
    public void start() {
        player.start();
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public void toggleFullScreen() {
        
    }
    // End VideoMediaController.MediaPlayerControl

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		mp.reset();
		 player.setAudioStreamType(AudioManager.STREAM_MUSIC);
         try {
			player.setDataSource(this, Uri.parse(path));
			player.prepare();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.video_play, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_edit:
	        	goToEditScreenActivity();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	

}
