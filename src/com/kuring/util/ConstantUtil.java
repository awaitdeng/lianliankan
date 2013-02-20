package com.kuring.util;

import static com.kuring.util.ConstantUtil.COLUMNSIZE;
import static com.kuring.util.ConstantUtil.IMAGEDIMENSION;
import static com.kuring.util.ConstantUtil.IMAGETOLEFTDISTANCE;
import static com.kuring.util.ConstantUtil.IMAGETOTOPDISTANCE;
import static com.kuring.util.ConstantUtil.ROWSIZE;

import java.util.Random;

import android.R.integer;
import android.graphics.Color;
import android.graphics.Point;

public class ConstantUtil {
	
	public static int SCREEN_WIDTH = 0;			//屏幕宽度 
	public static int SCREEN_HEIGHT = 0;		//屏幕高度
	
	public static int COMPANY_FONTSIZE = 28;	//公司的字体大小
	
	//地图的行数和列数，其中成绩必须保证是偶数
	public static int ROWSIZE = 10;				//行数为10行
	public static int COLUMNSIZE = 7;			//列数为7列
	public static int IMAGEDIMENSION = 0;		//实际显示的图片宽度和高度，需要根据屏幕参数计算
	public static int IMAGETOLEFTDISTANCE = 0;	//左边第一列图片到屏幕左边的距离
	public static int IMAGETOTOPDISTANCE = 0;	//上面第一行图片到屏幕上边的距离
	
	public static final int CURRENTIMAGECOUNT = 10;	//当前关卡图片的种类个数
	
	public static final int IMAGEMAXCOUNT = 12;		//游戏中的最大的图片个数
	
	public static int MAXSPACETIME = 2000;			//两次连击之间的最大时间间隔
	
	/**
	 * 获取随机的颜色
	 * @return
	 */
	public static int getRandomColor() {
		Random random = new Random();
		int color = 0;
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
		default:
			color = Color.BLACK;
			break;
		}
		return color;
	}
	
	/**
	 * 根据map数组索引得到该图片在屏幕的左上方的像素值
	 * @param indexPoint
	 * @return
	 */
	public static Point getCoordinateByIndex(Point indexPoint) {
		if (indexPoint == null) {
			return null;
		}
		if (indexPoint.x >= 0 && indexPoint.y >= 0 && indexPoint.x <= COLUMNSIZE + 1 && indexPoint.y <= ROWSIZE + 1) {
			Point point = new Point();
			point.x = IMAGETOLEFTDISTANCE + (indexPoint.x - 1) *  IMAGEDIMENSION;
			point.y = IMAGETOTOPDISTANCE + (indexPoint.y - 1) *  IMAGEDIMENSION;
			//System.out.printf("将索引转换成坐标的x=%d，y=%d\n", point.x, point.y);
			return point;
		}
		return null;
	}

}
