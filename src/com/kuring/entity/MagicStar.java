package com.kuring.entity;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 当图片消失时，在图片周围产生的多个小矩形框
 * @author Administrator
 *
 */
public class MagicStar {
	
	private int starCount = 20;
	
	Star[] stars;
	
	int x,y;
	
	Paint paint = new Paint();
	
	public void clearStars() {
		stars = null;
	}
	
	public void addStars(int x, int y) {
		this.x = x;
		this.y = y;
		stars = new Star[starCount];
		for (int i=0; i<starCount; i++) {
			stars[i] = new Star(x, y);
		}
	}
	
	/**
	 * 绘制多个多边形
	 * 当多边形绘制完成返回true，未绘制完成返回false
	 * @param canvas
	 * @return
	 */
	public boolean doDraw(Canvas canvas) {
		int count = 0;
		for (int i=0; i<starCount; i++) {
			paint.setColor(stars[i].color);
			paint.setAlpha(stars[i].alpha);
			canvas.drawRect(stars[i].x, stars[i].y, stars[i].x + stars[i].width, 
					stars[i].y + stars[i].width, paint);
			stars[i].move();
			if (stars[i].alpha <= 0) {
				count++;
			}
		}
		return count == starCount ? true : false;
	}
}
