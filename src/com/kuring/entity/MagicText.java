package com.kuring.entity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import static com.kuring.util.ConstantUtil.*;

/**
 * 魔法文字，显示在屏幕中央，用于提示一些信息，如第几关，下一关
 * @author Administrator
 *
 */
public class MagicText {

	private String text;
	
	private Paint textPaint = new Paint();//文本框的画笔
	
	private int fontSize;	//文本的字体大小
	
	private int height;		//矩形框的高度
	
	private int alpha;		//矩形框的透明度
	
	private Paint rectPaint = new Paint();//矩形框的画笔
	
	public void init(String text, int color) {
		this.text = text;
		this.height = 0;
		alpha = 180;
		rectPaint.setColor(Color.WHITE);
		rectPaint.setAlpha(alpha);
		fontSize = 40;
		textPaint.setColor(color);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setTextSize(fontSize);
		textPaint.setAntiAlias(true);
	}
	
	public boolean doDraw(Canvas canvas) {
		canvas.drawRect(0, SCREEN_HEIGHT / 2 - height, SCREEN_WIDTH, SCREEN_HEIGHT / 2 + height, rectPaint);
		if (height < 40) {
			height += 10;
		} else {
			return false;
		}
		if (height > 20) {
			canvas.drawText(text, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, textPaint);
		}
		return true;
	}
}
