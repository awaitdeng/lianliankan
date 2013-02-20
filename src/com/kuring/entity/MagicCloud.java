package com.kuring.entity;

import java.util.ArrayList;
import java.util.List;

import com.kuring.activity.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class MagicCloud {

	Bitmap[] bitmaps;
	
	private View view;
	
	private List<Cloud> clouds;
	
	private int cloudCount = 4;
	
	Paint paint = new Paint();
	
	public MagicCloud(View view) {
		this.view = view;
	}
	
	public void initMagicCloud() {
		bitmaps = new Bitmap[3];
		bitmaps[0] = BitmapFactory.decodeResource(this.view.getResources(), R.drawable.cloud1);
		bitmaps[1] = BitmapFactory.decodeResource(this.view.getResources(), R.drawable.cloud2);
		bitmaps[2] = BitmapFactory.decodeResource(this.view.getResources(), R.drawable.cloud3);
		clouds = new ArrayList<Cloud>();
		paint.setColor(Color.RED);
	}
	
	public void doDraw(Canvas canvas) {
		for (int i=0; i<clouds.size(); i++) {
			Cloud cloud = clouds.get(i);
			canvas.drawBitmap(bitmaps[cloud.getBitmap()], cloud.getX(), cloud.getY(), paint);
			cloud.move();
			if (cloud.isVisible() == false) {
				clouds.remove(i);
			}
		}
		if (clouds.size() < 2) {
			createCloud();
		}
	}

	private void createCloud() {
		Cloud cloud = new Cloud();
		clouds.add(cloud);
	}
}
