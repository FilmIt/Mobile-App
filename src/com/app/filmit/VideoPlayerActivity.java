package com.app.filmit;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.app.filmit.utils.Constants;

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
        videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
        mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(path);
        SurfaceHolder videoHolder = videoSurface.getHolder();
        videoHolder.addCallback(this);      
        addButtonListeners();
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

	private void addButtonListeners() {
		findViewById(R.id.save_button).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showSaveDialg();
			}
		
		});
		
		findViewById(R.id.noise_button).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				player.pause();
				goToImageEditScreen(Constants.FLEA);
			}
		
		});
		
		findViewById(R.id.pixelate_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				player.pause();
				goToImageEditScreen(Constants.SLIDER_EFFECT);
			}
			
		});
		
		findViewById(R.id.invert_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				player.pause();
				goToImageEditScreen(Constants.INVERT);
			}
			
		});
		
		findViewById(R.id.greyscale_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				player.pause();
				goToImageEditScreen(Constants.GREY_SCALE);
			}
			
		});
		
		
	}
	
	protected void goToImageEditScreen(int effectType) {
		
		int pos = player.getCurrentPosition();
		bitmap = mediaMetadataRetriever.getFrameAtTime( pos * 1000); //unit in microsecond
		Intent intent = new Intent(this.getApplicationContext(),ImageEditActivity.class);
		intent.putExtra("effect", effectType);
		player.stop();
		
		this.startActivity(intent);

	}
	
	
	protected void showSaveDialg() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    // Get the layout inflater
	    LayoutInflater inflater = getLayoutInflater();

	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(inflater.inflate(R.layout.dialog_save_effect, null))
	    // Add action buttons
	           .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	                   // Save Effect...
	               }
	           })
	           .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   //this.getDialog().cancel();
	                   
	               }
	           });      
	    builder.create();
	    builder.show();
		
	}

	public void showThemesMenu(View v) {
		if (!themeMenuOpen) {
			RelativeLayout tm = (RelativeLayout) findViewById(R.id.effects_layout);
			tm.setVisibility(View.VISIBLE);
			themeMenuOpen = true;
		} else {
			RelativeLayout tm = (RelativeLayout) findViewById(R.id.effects_layout);
			tm.setVisibility(View.INVISIBLE);
			themeMenuOpen = false;
		}
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
    	player.setDisplay(holder);
        player.prepareAsync();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    	Log.d(Constants.TAG, "In SurfaceCreated");
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

}
