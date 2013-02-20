package com.kuring.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import static com.kuring.util.ConstantUtil.*;

public class MagicBitmap {

	private Bitmap leftTopBitmap;			//���Ͻ�ͼƬ
	private Bitmap rightTopBitmap;			//���Ͻ�ͼƬ
	private Bitmap leftBottomBitmap;		//���½�ͼƬ
	private Bitmap rightBottomBitmap;		//���½�ͼƬ
	
	private int alpha;						//ͼƬ��͸����
	private int distance;					//ͼƬ�ľ���ԭ��ͼƬ�ľ���
	
	private int x;							//���ͼƬ�����ĵ��x����
	private int y;							//���ͼƬ�����ĵ��y����
	private float bitmapRatio;				//ͼƬ���ű���
	
	/**
	 * ���ݸ����Ĵ�ͼƬ�����ĸ�СͼƬ
	 * @param bitmap
	 * @param x	ͼƬ��x����
	 * @param y	ͼƬ��y����
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
	 * ����ͼƬ
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
