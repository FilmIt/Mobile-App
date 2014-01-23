package com.app.filmkit;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.app.filmit.model.Effect;
import com.app.filmkit.utils.Constants;
import com.app.filmkit.video.Video;

public class EditScreenActivity extends Activity {
	VideoView videoView;
	MediaController mediaController;
	PopupMenu popup;
	Bitmap originalImage = null;
	boolean themeMenuOpen  = false, adjustmentsMenuOpen = false;
	public static Bitmap frameBitmap = null;
	ImageView frameImageView = null;
	ProgressDialog progDialog;
	String path = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_edit_screen);
		ActionBar actionBar = getActionBar();
		actionBar.hide();

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			path = extras.getString("path");
			Log.d("VIDEO PATH: ", path);
		}

		Uri video = Uri.parse(path);
		MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
		mediaMetadataRetriever.setDataSource(this, video);
		frameImageView = (ImageView) findViewById(R.id.imageFrame);
		frameBitmap = originalImage = mediaMetadataRetriever.getFrameAtTime();
		frameImageView.setImageBitmap(frameBitmap);
		addButtonListeners();

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (frameBitmap != null) {
			frameImageView.setImageBitmap(frameBitmap);
		}

	}

	private void addButtonListeners() {
		
		findViewById(R.id.save_button).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						if(ImageEditActivity.effects == null || ImageEditActivity.effects.size() <=0)
							Toast.makeText(getApplicationContext(), "No effect applied!", Toast.LENGTH_SHORT).show();
						else
							showSaveDialg();
					}

				});
		
		findViewById(R.id.undo_button).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						if(ImageEditActivity.effects == null || ImageEditActivity.effects.size() <=0)
							return;
						
						ImageEditActivity.effects.remove(ImageEditActivity.effects.size()-1);
						if(ImageEditActivity.effects.size() <=0) {
							frameImageView.setImageBitmap(originalImage);
							return;
						}
						Effect currEff = ImageEditActivity.effects.get(ImageEditActivity.effects.size()-1);
						frameImageView.setImageBitmap(currEff.getBitmap());
					}

				});

		findViewById(R.id.noise_button).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						goToImageEditScreen(Constants.EffectType.FLEA, false);

					}

				});

		findViewById(R.id.invert_button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						goToImageEditScreen(Constants.EffectType.INVERT, false);
					}

				});

		findViewById(R.id.greyscale_button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						goToImageEditScreen(Constants.EffectType.GREY_SCALE,
								false);
					}

				});

		findViewById(R.id.pixelate_button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						goToImageEditScreen(Constants.EffectType.PIXELLATE,
								true);

					}

				});

		findViewById(R.id.tint_button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						goToImageEditScreen(Constants.EffectType.TINT, true);

					}

				});
		
		findViewById(R.id.contrast_button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						goToImageEditScreen(Constants.EffectType.CONTRAST, true);

					}

				});
		
		findViewById(R.id.birghtness_button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						goToImageEditScreen(Constants.EffectType.BRIGHTNESS, true);

					}

				});
		
		findViewById(R.id.opacity_button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						goToImageEditScreen(Constants.EffectType.OPACITY, true);

					}
				});

	}

	protected void goToImageEditScreen(Constants.EffectType effectType,
			boolean seekbar) {

		Intent intent = new Intent(this.getApplicationContext(),
				ImageEditActivity.class);
		intent.putExtra("path", path);
		intent.putExtra("effect", effectType);
		intent.putExtra("seekbar", seekbar);

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
						Dialog f = (Dialog) dialog;
						EditText nameEditText = (EditText) f.findViewById(R.id.name);
						String name = nameEditText.getText().toString();
						renderVideo(name);
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// this.getDialog().cancel();

							}
						});
		builder.create();
		builder.show();

	}

	protected void renderVideo(String name) {
		String outPath = Constants.getMyFolderDir().getAbsolutePath() + "/"+name+".mp4";
		Log.d(Constants.TAG, "In Path: " + path);
		Log.d(Constants.TAG, "Out Path: " + outPath);
		
		
		this.progDialog = ProgressDialog.show(this, "Loading", "Rendering Video...");
		
		new MyLoaderAsyncTask(outPath).execute();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_screen, menu);
		return true;

	}
	
	class MyLoaderAsyncTask extends AsyncTask<Void, Void, Boolean>{

		String outPath = "";
		public MyLoaderAsyncTask(String outPat) {
			outPath = outPat;
		}


		@Override
		protected Boolean doInBackground(Void... arg0) {
			Effect effect = ImageEditActivity.effects.get(ImageEditActivity.effects.size()-1);
				switch(effect.getEffectType()) {
				case FLEA :
					Video.flea(path, outPath);
					break;
				
				case INVERT :
					Video.invert(path, outPath);
					break;
					
				case GREY_SCALE:
					Video.invert(path, outPath);
					break;
					
				case PIXELLATE:
					Video.pixelate(path, outPath, effect.getIntensity());
					break;
					
				case TINT:
					Video.tint(path, outPath, effect.getIntensity());
					break;
					
				case BRIGHTNESS:
					Video.changeBrightness(path, outPath, effect.getIntensity());
					break;
					
				case CONTRAST:
					Video.changeContrast(path, outPath, effect.getIntensity());
					break;
					
				case OPACITY:
					Video.changeOpacity(path, outPath, effect.getIntensity());
					break;
					
				default :
						break;
				}
			
			return true;
		}
	

	     protected void onPostExecute(Boolean result) {
	         if(progDialog !=null && progDialog.isShowing()) {
	        	 progDialog.dismiss();
	         }
	     }

		
	}

	@SuppressLint("SdCardPath")
	void playVideo() {

		// videoView = (VideoView) findViewById(R.id.videoView1);
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
		// videoView.start();
	}

	public void showPopup(View v) {
		popup = new PopupMenu(this, v);
		MenuInflater inflater = popup.getMenuInflater();
		inflater.inflate(R.menu.actions, popup.getMenu());
		popup.show();
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
	
	public void showAdjustmentsMenu(View v) {
		if (!adjustmentsMenuOpen) {
			RelativeLayout tm = (RelativeLayout) findViewById(R.id.adjustments_layout);
			tm.setVisibility(View.VISIBLE);
			adjustmentsMenuOpen = true;
		} else {
			RelativeLayout tm = (RelativeLayout) findViewById(R.id.adjustments_layout);
			tm.setVisibility(View.INVISIBLE);
			adjustmentsMenuOpen = false;
		}
	}

}