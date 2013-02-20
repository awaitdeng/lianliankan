package com.kuring.entity;

import android.graphics.Canvas;
import static com.kuring.util.ConstantUtil.*;

/**
 * 游戏显示的进度条
 * @author Administrator
 *
 */
public class MagicProcessBar {

	private int process;
	
	/**
	 * 进度条的宽度
	 */
	private int width;
	
	/**
	 * 进度条初始化
	 */
	public void init() {
		width = (int)(SCREEN_WIDTH * 0.6);
	}
	
	public void doDraw(Canvas canvas) {
		
	}
	
	
}
