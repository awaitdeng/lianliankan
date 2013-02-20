package com.kuring.entity;

import android.graphics.Canvas;
import android.graphics.Paint;
import static com.kuring.util.ConstantUtil.*;
/**
 * ��ʾ���������ӷ������ı����������ı�
 * @author Administrator
 *
 */
public class AniText {

	private int alpha;
	
	private String text;
	
	private Paint paint = new Paint();
	
	private float x;

	private float y;
	
	private int color;
	
	/**
	 * �����ı�
	 * @param text �ı�����
	 * @param x	�ı���x����
	 * @param y	�ı���y����
	 */
	public void addText(String text, float x, float y, Paint.Align align) {
		this.x = x;
		this.y = y;
		this.text = text;
		alpha = 250;
		color = getRandomColor();
		paint.setAntiAlias(true);
		paint.setColor(color);
		paint.setTextSize(30);
		paint.setTextAlign(align);
	}
	
	/**
	 * �����ı�
	 * @param text
	 * @param fontSize �ı������С
	 * @param x
	 * @param y
	 */
	public void addText(String text, int fontSize, float x, float y, Paint.Align align) {
		this.x = x;
		this.y = y;
		this.text = text;
		alpha = 250;
		color = getRandomColor();
		paint.setAntiAlias(true);
		paint.setColor(color);
		paint.setTextSize(fontSize);
		paint.setTextAlign(align);
	}
	
	public boolean doDraw(Canvas canvas) {
		paint.setAlpha(alpha);
		alpha -= 50;
		if (alpha < 0) {
			return false;
		}
		canvas.drawText(text, x, y, paint);
		y -= 10;
		return true;
	}
	
}
