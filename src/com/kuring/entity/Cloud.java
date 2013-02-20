package com.kuring.entity;

import java.util.Random;
import static com.kuring.util.ConstantUtil.*;

public class Cloud {

	private int x;			//图片左上角的x坐标
	
	private int y;			//图片左上方的y坐标
	
	private int bitmap;		//所使用的图片
	
	private int direction;	//方向，1表示从左向右，0表示从右向左，默认为从左向右
	
	private int speed;		//移动的速度
	
	public Cloud() {
		Random random = new Random();
		speed = random.nextInt(2) + 1;
		direction = 1;
		bitmap = random.nextInt(3);
		x = -100;
		y = random.nextInt((int)(SCREEN_HEIGHT * 0.3));
	}
	
	/**
	 * 检测当前云是否还可见
	 * @return
	 */
	public boolean isVisible() {
		if (x < -100 || x >SCREEN_WIDTH) {
			return false;
		}
		return true;
	}
	
	/**
	 * 云进行移动
	 */
	public void move() {
		//向左移动
		if (direction == 1) {
			this.x += speed;
		} else {
			this.x -= speed;
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getBitmap() {
		return bitmap;
	}

	public void setBitmap(int bitmap) {
		this.bitmap = bitmap;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
}
