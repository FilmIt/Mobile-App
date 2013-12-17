package com.app.filmit;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.VideoView;

public class EditScreenActivity extends Activity {
	VideoView videoView;
	MediaController mediaController;
	PopupMenu popup;
	boolean themeMenuOpen = false;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_edit_screen);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		addButtonListeners();
		playVideo();
		
	}
	

	private void addButtonListeners() {
		ImageButton saveButton = (ImageButton) findViewById(R.id.save_button);
		saveButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showSaveDialg();
			}
		
		});
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
		//videoView.start();
	}
	public void showPopup(View v) {
		
		popup = new PopupMenu(this, v);
	    MenuInflater inflater = popup.getMenuInflater();
	    inflater.inflate(R.menu.actions, popup.getMenu());
	    popup.show();
	}
	
	public void showThemesMenu(View v) {   
		 if(!themeMenuOpen){
		  RelativeLayout tm = (RelativeLayout) findViewById(R.id.effects_layout);
		  tm.setVisibility(View.VISIBLE);
		  themeMenuOpen = true;
		 }
		 else{
			 RelativeLayout tm = (RelativeLayout) findViewById(R.id.effects_layout);
			 tm.setVisibility(View.INVISIBLE);
			 themeMenuOpen = false;
		 }
}
	 
}