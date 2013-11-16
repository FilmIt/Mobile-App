package com.app.filmit;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenuActivity extends Activity {

	public static int PICK_VIDEO = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		
		Button galleryButton = (Button) findViewById(R.id.gallery_select_button);
		galleryButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				startActivity(new Intent(MainMenuActivity.this, IntroScreenActivity.class));
				
//				Intent intent = new Intent();
//			    intent.setType("video/*");
//			    intent.setAction(Intent.ACTION_PICK);
//			    startActivityForResult(Intent.createChooser(intent,"Select Any"), PICK_VIDEO);
				
			}
		});
		
		Button captureButton = (Button) findViewById(R.id.capture_select_button);
		captureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				 Intent in = new Intent(getApplicationContext(), CaptureScreenActivity.class);
	             startActivity(in);
				
			}
		
		});
	}

	 @Override
	    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	        if (requestCode == PICK_VIDEO) {
	            if (resultCode == Activity.RESULT_OK) {
	                Uri path = data.getData();
	                Intent intent = new Intent(this, EditScreenActivity.class);
	                intent.putExtra("path", path.toString());
	                startActivity(intent);
	            } 

	        }
	    }
	 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}
	

}
