package com.kuring.entity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import static com.kuring.util.ConstantUtil.*;

/**
 * ħ�����֣���ʾ����Ļ���룬������ʾһЩ��Ϣ����ڼ��أ���һ��
 * @author Administrator
 *
 */
public class MagicText {

	private String text;
	
	private Paint textPaint = new Paint();//�ı���Ļ���
	
	private int fontSize;	//�ı��������С
	
	private int height;		//���ο�ĸ߶�
	
	private int alpha;		//���ο��͸����
	
	private Paint rectPaint = new Paint();//���ο�Ļ���
	
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
