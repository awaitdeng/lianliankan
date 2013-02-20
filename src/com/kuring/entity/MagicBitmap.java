package com.kuring.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import static com.kuring.util.ConstantUtil.*;

public class MagicBitmap {

	private Bitmap leftTopBitmap;			//左上角图片
	private Bitmap rightTopBitmap;			//右上角图片
	private Bitmap leftBottomBitmap;		//左下角图片
	private Bitmap rightBottomBitmap;		//右下角图片
	
	private int alpha;						//图片的透明度
	private int distance;					//图片的距离原来图片的距离
	
	private int x;							//最初图片的中心点的x坐标
	private int y;							//最初图片的中心点的y坐标
	private float bitmapRatio;				//图片缩放比例
	
	/**
	 * 根据给定的大图片创建四个小图片
	 * @param bitmap
	 * @param x	图片的x坐标
	 * @param y	图片的y坐标
	 */
	public void addBitmap(Bitmap bitmap, int x, int y) {
		int width = bitmap.getWidth() / 2;
		int height = bitmap.getHeight() / 2;
		leftTopBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
		rightTopBitmap = Bitmap.createBitmap(bitmap, width, 0, width, height);
		leftBottomBitmap = Bitmap.createBitmap(bitmap, 0, height, width, height);
		rightBottomBitmap = Bitmap.createBitmap(bitmap, width, height, width, height);
		alpha = 200;
		distance = 0;
		this.x = x;
		this.y = y;
		bitmapRatio = 0.5f;
	}
	
	/**
	 * 绘制图片
	 * @param canvas
	 */
	public boolean doDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAlpha(alpha);
		Rect rect = new Rect();
		rect.set(x - distance - IMAGEDIMENSION / 2, 
				y - distance - IMAGEDIMENSION / 2, 
				(int)(x - distance - IMAGEDIMENSION / 2 + IMAGEDIMENSION * bitmapRatio), 
				(int)(y - distance - IMAGEDIMENSION / 2 + IMAGEDIMENSION * bitmapRatio));
		canvas.drawBitmap(leftTopBitmap, null, rect, paint);
		rect.set(x + distance, 
				y - distance - IMAGEDIMENSION / 2, 
				(int)(x + distance + IMAGEDIMENSION * bitmapRatio), 
				(int)(y - distance - IMAGEDIMENSION / 2 + IMAGEDIMENSION * bitmapRatio));
		canvas.drawBitmap(rightTopBitmap, null, rect, paint);
		rect.set(x - distance - IMAGEDIMENSION / 2, 
				y + distance, 
				(int)(x - distance - IMAGEDIMENSION / 2 + IMAGEDIMENSION * bitmapRatio), 
				(int)(y + distance + IMAGEDIMENSION * bitmapRatio));
		canvas.drawBitmap(leftBottomBitmap, null, rect, paint);
		rect.set(x + distance, 
				y + distance, 
				(int)(x + distance + IMAGEDIMENSION * bitmapRatio), 
				(int)(y + distance + IMAGEDIMENSION * bitmapRatio));
		canvas.drawBitmap(rightBottomBitmap, null, rect, paint);
		alpha -= 50;
		distance += 3;
		if (bitmapRatio > 0.3) {
			bitmapRatio *= 0.8;
		}
		if (alpha <= 0) {
			return false;
		}
		return true;
	}
}
