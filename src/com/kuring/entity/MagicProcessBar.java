package com.kuring.entity;

import android.graphics.Canvas;
import static com.kuring.util.ConstantUtil.*;

/**
 * ��Ϸ��ʾ�Ľ�����
 * @author Administrator
 *
 */
public class MagicProcessBar {

	private int process;
	
	/**
	 * �������Ŀ��
	 */
	private int width;
	
	/**
	 * ��������ʼ��
	 */
	public void init() {
		width = (int)(SCREEN_WIDTH * 0.6);
	}
	
	public void doDraw(Canvas canvas) {
		
	}
	
	
}
