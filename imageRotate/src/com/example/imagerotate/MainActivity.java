package com.example.imagerotate;

import java.io.File;


import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class MainActivity extends Activity {
	ImageView bitmapview;
	Bitmap ref11;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		File myFile = new File("/sdcard/photo.jpg");
		bitmapview = (ImageView)findViewById(R.id.imageView1);
		 Uri mImageCaptureUri;
		 
		 BitmapDrawable d = new BitmapDrawable(getResources(), myFile.getAbsolutePath());
		 final Bitmap d1=d.getBitmap();
		 bitmapview.setScaleType(ScaleType.CENTER);
         bitmapview.setImageBitmap(d1);
         ref11=d1;
         Button rbutton=(Button) findViewById(R.id.rotate_button);
         rbutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
		         
				rotate(90,ref11);
			}
		});
		//
	//	jpgView.setImageDrawable(d);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void rotate(int x,Bitmap photo)
	{
		int width = photo.getWidth();

        int height = photo.getHeight();


        int newWidth = 200;

        int newHeight  = 200;

        // calculate the scale - in this case = 0.4f

         float scaleWidth = ((float) newWidth) / width;

         float scaleHeight = ((float) newHeight) / height;

         Matrix matrix = new Matrix();

         matrix.postScale(scaleWidth, scaleHeight);
         matrix.postRotate(x);

         photo = Bitmap.createBitmap(photo, 0, 0,width, height, matrix, true);

        ref11=photo;
        bitmapview.setScaleType(ScaleType.CENTER);
         bitmapview.setImageBitmap(photo);
	}
	}


