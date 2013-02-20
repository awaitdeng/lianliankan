package com.kuring.entity;

import java.util.Random;

import android.graphics.Color;

/**
 * 当图片消失时产生的小矩形块
 * @author Administrator
 *
 */
public class Star {

	static final float PI = 3.1415926f;
	int color;
	int speed;
	int x;
	int y;
	double direction;
	int width;
	int time;		//表示矩形已经运行的时间
	int alpha;
	
	public Star(int x, int y) {
		Random random = new Random();
		switch (random.nextInt(12) + 1) {
		case 1:
			color = Color.BLACK;
			break;
		case 2:
			color = Color.BLUE;
			break;
		case 3:
			color = Color.CYAN;
			break;
		case 4:
			color = Color.DKGRAY;
			break;
		case 5:
			color = Color.GRAY;
			break;
		case 6:
			color = Color.GREEN;
			break;
		case 7:
			color = Color.LTGRAY;
			break;
		case 8:
			color = Color.MAGENTA;
			break;
		case 9:
			color = Color.RED;
			break;
		case 10:
			color = Color.TRANSPARENT;
			break;
		case 11:
			color = Color.WHITE;
			break;
		case 12:
			color = Color.YELLOW;
			break;
		}
		direction = Math.random() * PI * 2;
		speed = random.nextInt(5) + 1;
		this.x = x;
		this.y = y;
		width = random.nextInt(5) + 1;
		time = 0;
		alpha = 255;
	}
	
	public void move() {
		time++;
		x += Math.cos(direction) * speed * time;
		y += Math.sin(direction) * speed * time;
		if (alpha > 0) {
			alpha -= 30;
		}
	}
}
