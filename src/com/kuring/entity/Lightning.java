package com.kuring.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Point;

/**
 * 闪电类
 * @author Administrator
 *
 */
public class Lightning {

	public int alpha;
	
	public List<Point> points = new ArrayList<Point>();
	
	public int width;
	
	public int speed;
	
	public Lightning() {
		Random random = new Random();
		alpha = random.nextInt(255);
		width = random.nextInt(1) + 1;
		speed = 5;
	}
	
	/**
	 * 返回点的数组
	 * @return
	 */
	public float[] getPointArray() {
		float[] pointArray = new float[4 * points.size() - 4];
		pointArray[0] = points.get(0).x;
		pointArray[1] = points.get(0).y;
		for (int i=1; i<(points.size()-1); i++) {
			pointArray[i * 4 - 2] = points.get(i).x;
			pointArray[i * 4] = points.get(i).x;
			pointArray[i * 4 - 1] = points.get(i).y;
			pointArray[i * 4 + 1] = points.get(i).y;
		}
		pointArray[4 * points.size() - 6] = points.get(points.size() - 1).x;
		pointArray[4 * points.size() - 5] = points.get(points.size() - 1).y;
		/*Debug用
		String string = "";
		for (int i=0; i<pointArray.length; i++) {
			String temp = (int)pointArray[i] + ",";
			string += temp;
		}
		System.out.println(string);*/
		return pointArray;
	}
}
