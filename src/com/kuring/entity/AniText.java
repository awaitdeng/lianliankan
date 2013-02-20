package com.kuring.entity;

import android.graphics.Canvas;
import android.graphics.Paint;
import static com.kuring.util.ConstantUtil.*;
/**
 * 显示界面上增加分数的文本和连击的文本
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
	 * 增加文本
	 * @param text 文本内容
	 * @param x	文本的x坐标
	 * @param y	文本的y坐标
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
	 * 增加文本
	 * @param text
	 * @param fontSize 文本字体大小
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
