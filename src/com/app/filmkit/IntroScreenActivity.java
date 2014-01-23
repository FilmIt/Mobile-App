package com.app.filmkit;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.filmkit.utils.Constants;

public class IntroScreenActivity extends Activity {

	Context context;
	static final int REQUEST_VIDEO_CAPTURE = 1;
	
	public class VideoInfo {
		public Bitmap bitmap;
		public String name;
		public String path;
		public VideoInfo(Bitmap b, String n, String pa) {
			bitmap = b;
			name = n;
			path = pa;
		}
	}
	ArrayList<VideoInfo> videos = new ArrayList<VideoInfo>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro_screen);

		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new ImageAdapter(this));
		context = this;

		String targetPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath();

		Toast.makeText(getApplicationContext(), targetPath, Toast.LENGTH_LONG)
				.show();
		loaddata();
		
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				 Intent intent = new Intent(getApplicationContext(), EditScreenActivity.class);
	                intent.putExtra("path", videos.get(position).path);
	                startActivity(intent);
				
			}
		});
		
		addButtonListeners();
	}
	

	private void addButtonListeners() {
		findViewById(R.id.video_capture_button).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				 dispatchTakeVideoIntent();
				//startActivity(new Intent(context, CaptureScreenActivity.class));
			}
			
		});
	}



	private void dispatchTakeVideoIntent() {
	    Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
	    if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
	        startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
	    }
	}
	
	private void loaddata() {
		ContentResolver cr = getContentResolver();
		String[] proj = { BaseColumns._ID,
				MediaStore.Video.Media.DISPLAY_NAME , 
				MediaStore.Video.Media.DATA};
		String selection = "" + MediaStore.Video.Media.DURATION + "<= " + Constants.DURATION_LIMIT;
		Cursor c = cr.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, proj,
				selection, null, null);
		
		if (c.moveToFirst()) {
			do {
				int id = c.getInt(0);
				
				String name = c.getString(1);
				String path = c.getString(2);
				Log.d("NAME OF VIDEO:", name);
				Log.d("PATH = ", path);
				
				BitmapFactory.Options options=new BitmapFactory.Options();
				options.inSampleSize = 1;
				Bitmap b = MediaStore.Video.Thumbnails.getThumbnail(cr, id, MediaStore.Video.Thumbnails.MICRO_KIND, options);
				
				//Bitmap b = ThumbnailUtils.createVideoThumbnail(path,MediaStore.Images.Thumbnails.MINI_KIND);
				//Bitmap b = MediaStore.Video.Thumbnails.getThumbnail(cr, id,	MediaStore.Video.Thumbnails.MINI_KIND, null);
				Log.d("*****My Thumbnail*****", "onCreate bitmap " + b);
				if(b != null )
					videos.add(new VideoInfo(b, name, path));

			} while (c.moveToNext());
		}
		c.close();
		
		for(int i=0; i< videos.size(); i++) {
			Log.d("VIDEO NAME FINAL:" , videos.get(i).name);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		ActionBar actionBar = getActionBar();
        actionBar.hide();
		getMenuInflater().inflate(R.menu.intro_screen, menu);
		

		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_camera:
	        	 Intent in = new Intent(this, CaptureScreenActivity.class);
	             startActivity(in);
	            return true;
	        case R.id.action_settings:
	            //openSettings();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	public class ImageAdapter extends BaseAdapter {
		private Context mContext;

		public ImageAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			return videos.size();
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {

			View v;
		    if (convertView == null) {  // if it's not recycled, initialize some attributes
		        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(     Context.LAYOUT_INFLATER_SERVICE );
		        v = inflater.inflate(R.layout.gridview_element, parent, false);
		    } else {
		        v = (View) convertView;
		    }
		   
		    ImageView image = (ImageView)v.findViewById(R.id.thumnail);
		    image.setImageBitmap(videos.get(position).bitmap);
		    
		    ImageView play = (ImageView)v.findViewById(R.id.play);
		    play.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
		    return v;
			
		}

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
	        Uri videoUri = data.getData();
	        Intent i = new Intent(getApplicationContext(), VideoPlayerActivity.class);
	        Log.d(Constants.TAG, "Path in IntroScreen = " + videoUri.getPath());
	        String filePath = getRealPathFromURI(this, videoUri);
	        Log.d(Constants.TAG, "File path in IntroScreen = " + filePath);
            i.putExtra("path", filePath);
            startActivity(i);
	    }
	}
	
	public String getRealPathFromURI(Context context, Uri contentUri) {
		  Cursor cursor = null;
		  try { 
		    String[] proj = { MediaStore.Images.Media.DATA };
		    cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
		    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		    cursor.moveToFirst();
		    return cursor.getString(column_index);
		  } finally {
		    if (cursor != null) {
		      cursor.close();
		    }
		  }
		}
}	
