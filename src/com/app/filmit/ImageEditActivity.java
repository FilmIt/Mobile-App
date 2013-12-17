package com.app.filmit;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.app.filmit.utils.Constants;
import com.example.image_effects.ImageEffects;

public class ImageEditActivity extends Activity {

	SeekBar seekBar;
	private ImageEffects imageEffects;
	private ImageView imageView;
	private ImageButton okButton;
	private ImageButton cancelButton;
	private Bitmap bitmap;
	private TextView titleTextView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_edit);
		
		int effect = 0;
		Bundle extras = getIntent().getExtras();
		  if (extras != null)
		   effect = extras.getInt("effect");
		   
		
		imageEffects = new ImageEffects();
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		imageView = (ImageView) findViewById(R.id.imageView);
		okButton = (ImageButton) findViewById(R.id.button_ok);
		cancelButton = (ImageButton) findViewById(R.id.button_cancel);
		titleTextView = (TextView) findViewById(R.id.titleText);
		
		bitmap = VideoPlayerActivity.bitmap;
		imageView.setImageBitmap(bitmap); 
		switch(effect) {
		
		case Constants.FLEA :
			titleTextView.setText("Noise Flea");
			imageView.setImageBitmap(imageEffects.Flea(bitmap)); 
			seekBar.setVisibility(View.GONE);
			break;
		
		case Constants.INVERT :
			titleTextView.setText("Invert");
			imageView.setImageBitmap(imageEffects.Invert((bitmap))); 
			seekBar.setVisibility(View.GONE);
			break;
			
		case Constants.SLIDER_EFFECT:
			titleTextView.setText("Pixellate");
			handleSeekBar();
			break;
			
		case Constants.GREY_SCALE:
			titleTextView.setText("Grey Scale");
			imageView.setImageBitmap(imageEffects.GrayScale(bitmap)); 
			seekBar.setVisibility(View.GONE);
			break;
		default :
				break;
		}
		

		
		addButtonListeners();
	}

	private void handleSeekBar() {
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int progress = 0;

			@Override
			public void onProgressChanged(SeekBar seekBar, int progresValue,
					boolean fromUser) {
				progress = progresValue;

				imageView.setImageBitmap(imageEffects.Pixelate(bitmap, progress));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// Do something here,
				// if you want to do anything at the start of
				// touching the seekbar
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
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
			}
			
		});
		
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				imageView.setImageBitmap(bitmap);
				seekBar.setVisibility(View.GONE);
				cancelButton.setVisibility(View.GONE);
				okButton.setVisibility(View.GONE);
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.slider_bar_edit, menu);
		return true;
	}

}
