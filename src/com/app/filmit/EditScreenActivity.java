package com.app.filmit;

import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.VideoView;

public class EditScreenActivity extends Activity {
	VideoView videoView;
	MediaController mediaController;
	PopupMenu popup;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_edit_screen);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		
		playVideo();
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_screen, menu);
		return true;
		
	}
	
	@SuppressLint("SdCardPath")
	void playVideo(){
		
		videoView = (VideoView) findViewById(R.id.videoView1);
		mediaController = new MediaController(this);
		mediaController.setAnchorView(videoView);
		
		Bundle extras = getIntent().getExtras(); 
		String uri = null;

		if (extras != null) {
		    uri = extras.getString("path");
		    Log.d("VIDEO PATH: ", uri);
		    
		}
	
		Uri video = Uri.parse(uri);
		
		videoView.setVideoURI(video);
		videoView.setMediaController(mediaController);
		videoView.start();
	}
	public void showPopup(View v) {
		
		popup = new PopupMenu(this, v);
	    MenuInflater inflater = popup.getMenuInflater();
	    inflater.inflate(R.menu.actions, popup.getMenu());
	    popup.show();
	}

}