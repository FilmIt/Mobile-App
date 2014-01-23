package com.app.filmkit;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.app.filmit.model.Effect;
import com.app.filmkit.image.ImageEffects;
import com.app.filmkit.utils.Constants;
import com.app.filmkit.video.Video;



public class ImageEditActivity extends Activity {

	SeekBar seekBar;
	private ImageEffects imageEffects;
	private ImageView imageView;
	private ImageButton okButton;
	private ImageButton cancelButton;
	private Bitmap bitmap;
	private TextView titleTextView;
	public Context context;
	ProgressDialog progDialog;
	String inPath =  "";
	int value = 0;
	boolean isSeekbar = false;
	static ArrayList<Effect> effects = new ArrayList<Effect>();
	static Constants.EffectType currEffect = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_edit);
		
		Log.d(Constants.LOG, "In OnCreate of ImageEditActivity");
		
		Bundle extras = getIntent().getExtras();
		  if (extras != null) {
		   currEffect = (Constants.EffectType)extras.getSerializable("effect");
		   isSeekbar = extras.getBoolean("seekbar");
		   inPath = extras.getString("path");
		  }
		   
		
		imageEffects = new ImageEffects();
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		imageView = (ImageView) findViewById(R.id.imageView);
		okButton = (ImageButton) findViewById(R.id.button_ok);
		cancelButton = (ImageButton) findViewById(R.id.button_cancel);
		titleTextView = (TextView) findViewById(R.id.titleText);
		
		bitmap = EditScreenActivity.frameBitmap;
		
		if(!isSeekbar)
			seekBar.setVisibility(View.GONE);
		else 
			handleSeekBar();
		
		imageView.setImageBitmap(bitmap); 
		context = this;
		
		this.progDialog = ProgressDialog.show(this, "Loading", "Applying effect...");
		
		addButtonListeners();
		
		new MyLoaderAsyncTask().execute();
		
	}
	
	private void handleSeekBar() {
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int progress = 0;
			Bitmap bmp = null;
			@Override
			public void onProgressChanged(SeekBar seekBar, int progresValue,
					boolean fromUser) {
				
				progress = progresValue;
				switch(currEffect) {
				case PIXELLATE:
					bmp = imageEffects.Pixelate(bitmap, progress);
					seekBar.setMax(100);
					break;
				case TINT:
					bmp = imageEffects.Tint(bitmap, progress);
					seekBar.setMax(100);
					break;
				case BRIGHTNESS:
					bmp = imageEffects.changeBrightness(bitmap, progress);
					break;
				case CONTRAST:
					bmp = imageEffects.changeContrast(bitmap, progress);
					break;
				case OPACITY:
					bmp = imageEffects.changeOpacity(bitmap, progress);
					break;
				}
				
				imageView.setImageBitmap(bmp);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				bitmap = bmp;
				value = progress;
			}
		});
	}
	private void addButtonListeners() {
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				seekBar.setVisibility(View.GONE);
				cancelButton.setVisibility(View.GONE);
				okButton.setVisibility(View.GONE);
				effects.add(new Effect(bitmap, currEffect, value));
				EditScreenActivity.frameBitmap = bitmap;
				onBackPressed();

			}
			
		});
		
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				imageView.setImageBitmap(bitmap);
				seekBar.setVisibility(View.GONE);
				cancelButton.setVisibility(View.GONE);
				okButton.setVisibility(View.GONE);
				onBackPressed();
			}
			
		});
	}

	
	class MyLoaderAsyncTask extends AsyncTask<Void, Void, Boolean>{

		Bitmap finalBmp = null;
		String effectName = "";
		@Override
		protected Boolean doInBackground(Void... arg0) {
			switch(currEffect) {
			case FLEA :
				finalBmp = imageEffects.Flea(bitmap); 
				effectName = "Flea";
				break;
			
			case INVERT :
				finalBmp = imageEffects.Invert(bitmap); 
				effectName = "Invert";
				break;
				
			case GREY_SCALE:
				finalBmp = imageEffects.GrayScale(bitmap);
				effectName = "Grey Scale";
				break;
				
			case PIXELLATE:
				effectName = "Pixellate";
				finalBmp = bitmap;
				break;
				
			case TINT:
				effectName = "Tint";
				finalBmp = bitmap;
				break;
				
			case BRIGHTNESS:
				effectName = "Adjust Brightness";
				finalBmp = bitmap;
				break;
				
			case CONTRAST:
				effectName = "Adjust Contrast";
				finalBmp = bitmap;
				break;
				
			case OPACITY:
				effectName = "Adjust Opacity";
				finalBmp = bitmap;
				break;
				
			default :
					break;
			}
			return true;
		}
	

	     protected void onPostExecute(Boolean result) {
	         if(progDialog.isShowing()) {
	        	 progDialog.dismiss();
	        	 imageView.setImageBitmap(finalBmp);
	        	 bitmap = finalBmp;
	        	 titleTextView.setText(effectName);
	         }
	     }

		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.slider_bar_edit, menu);
		return true;
	}
	
	

	
}
