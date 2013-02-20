package com.kuring.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.kuring.view.GameView;

import android.R.integer;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import static com.kuring.util.ConstantUtil.*; 

/**
 * 闪电效果
 * @author Administrator
 *
 */
public class MagicLightning {

	private List<Lightning> lightnings = new ArrayList<Lightning>();
	
	private Paint paint = new Paint();
	
	private Random random = new Random();
	
	private int lightningCount = 6;			//闪电的条数
	
	public MagicLightning() {
		paint.setAntiAlias(true);
	}
	
	void clear() {
		lightnings.clear();
	}
	
	/**
	 * 根据点增加几条闪电
	 * @param points
	 */
	public void addLightings(List<Point> points) {
		clear();
		Point curPoint = null;
		Point nextPoint = null;
		//增加三条闪电
		for (int i=0; i<lightningCount; i++) {
			lightnings.add(new Lightning());
		}
		//往闪电集合中加入点
		for (Iterator<Point> iterator = points.iterator(); iterator.hasNext();) {
			nextPoint = (Point) iterator.next();
			if (nextPoint == null) {
				return;
			} else if (curPoint == null) {
				curPoint = nextPoint;
			} else {
				int curPointRealX = GameView.getCenterCoordinateByIndex(curPoint).x;
				int curPointRealY = GameView.getCenterCoordinateByIndex(curPoint).y;
				//将开始点加入集合中
				for (int i=0; i<lightningCount; i++) {
					lightnings.get(i).points.add(GameView.getCenterCoordinateByIndex(curPoint));
					/*System.out.printf("将点(%d,%d)加入集合中\n", GameView.getCenterCoordinateByIndex(curPoint).x, 
							GameView.getCenterCoordinateByIndex(curPoint).y);*/
				}
				if (curPoint.x == nextPoint.x) {	//这条线为垂直方向
					System.out.println("这条线为垂直方向");
					int count = (nextPoint.y - curPoint.y) * 3;
					int maxDistance = IMAGEDIMENSION / 3;
					int num;
					if (count > 0) {
						num = 1;
					} else {
						num = -1;
					}
					for (int i=0; i<Math.abs(count); i++) {
						for (int j=0; j<lightningCount; j++) {
							Point point = new Point();
							int y = curPointRealY + i * maxDistance * num;
							y += maxDistance * Math.random() * num;
							point.y = y;
							point.x = curPointRealX + random.nextInt(15) - 6;
							lightnings.get(j).points.add(point);
						}
					}
				} else {		//这条线为水平方向
					System.out.println("这条线为水平方向");
					int count = (nextPoint.x - curPoint.x) * 3;
					int maxDistance = IMAGEDIMENSION / 3;
					int num;
					if (count > 0) {
						num = 1;
					} else {
						num = -1;
					}
					for (int i=0; i<Math.abs(count); i++) {
						for (int j=0; j<lightningCount; j++) {
							Point point = new Point();
							int x = curPointRealX + i * maxDistance * num;
							x += maxDistance * Math.random() * num;
							point.x = x;
							point.y = (int)(curPointRealY + random.nextInt(15)) - 6;
							lightnings.get(j).points.add(point);
						}
					}
				}
				//将结束点加入到集合中
				for (int i=0; i<lightningCount; i++) {
					lightnings.get(i).points.add(GameView.getCenterCoordinateByIndex(nextPoint));
					/*System.out.printf("将点(%d,%d)加入集合中\n", GameView.getCenterCoordinateByIndex(nextPoint).x, 
							GameView.getCenterCoordinateByIndex(nextPoint).y);*/
				}
				curPoint = nextPoint;
			}
		}
	}
	
	/**
	 * 绘制闪电效果
	 * @param canvas
	 * @return 当闪电已经绘制完成返回false，当闪电未绘制完成返回true
	 */
	public boolean doDraw(Canvas canvas) {
		for (int i=0; i<lightnings.size(); i++) {
			Paint paint = new Paint();
			paint.setColor(Color.WHITE);
			paint.setStrokeWidth(lightnings.get(i).width);
			paint.setAlpha(lightnings.get(i).alpha);
			paint.setAntiAlias(true);
			/*Point curPoint = null, nextPoint = null;
			for (int j=0; j<lightnings.get(i).points.size(); j++) {
				nextPoint = lightnings.get(i).points.get(j);
				if (nextPoint == null) {
					System.out.println("null");
					break;
				} else if (curPoint == null) {
					curPoint = nextPoint;
				} else {
					canvas.drawLine(curPoint.x, curPoint.y, nextPoint.x, nextPoint.y, paint);
					curPoint = nextPoint;
				}				
			}*/
			canvas.drawLines(lightnings.get(i).getPointArray(), paint);
			lightnings.get(i).alpha--;
			
		}
		for (int i=0; i<lightnings.size(); i++) {
			if (lightnings.get(i).alpha > 0) {
				return false;
			}
		}
		return true;
	}
}
