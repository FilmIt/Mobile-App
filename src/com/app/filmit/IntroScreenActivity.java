package com.app.filmit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class IntroScreenActivity extends Activity {

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

		String targetPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath();

		Toast.makeText(getApplicationContext(), targetPath, Toast.LENGTH_LONG)
				.show();
		loaddata();
		/*
		 * File targetDirector = new File(targetPath);
		 * 
		 * File[] files = targetDirector.listFiles(); for (File file : files) {
		 * myGallery.addView(insertPhoto(file.getAbsolutePath())); }
		 */
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				 Intent intent = new Intent(getApplicationContext(), EditScreenActivity.class);
	                intent.putExtra("path", videos.get(position).path);
	                startActivity(intent);
				
			}
		});
	}
	

	private void loaddata() {
		ContentResolver cr = getContentResolver();
		String[] proj = { BaseColumns._ID,
				MediaStore.Video.Media.DISPLAY_NAME , 
				MediaStore.Video.Media.DATA};

		Cursor c = cr.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, proj,
				null, null, null);
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
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				View gridView;
				if(convertView == null) {
				// get layout from mobile.xml
				gridView = inflater.inflate(R.layout.gridview_element, null);

				// set value into textview
				TextView textView = (TextView) gridView
						.findViewById(R.id.vide_name);
				textView.setText(videos.get(position).name);

				// set image based on selected text
				ImageView imageView = (ImageView) gridView
						.findViewById(R.id.thumnail);
				imageView.setImageBitmap(videos.get(position).bitmap);

				}
				
				else 
					gridView = (View)convertView;
			return gridView;

		}

	}
	}	
