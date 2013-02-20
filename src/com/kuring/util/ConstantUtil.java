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
	
	public static int SCREEN_WIDTH = 0;			//��Ļ��� 
	public static int SCREEN_HEIGHT = 0;		//��Ļ�߶�
	
	public static int COMPANY_FONTSIZE = 28;	//��˾�������С
	
	//��ͼ�����������������гɼ����뱣֤��ż��
	public static int ROWSIZE = 10;				//����Ϊ10��
	public static int COLUMNSIZE = 7;			//����Ϊ7��
	public static int IMAGEDIMENSION = 0;		//ʵ����ʾ��ͼƬ��Ⱥ͸߶ȣ���Ҫ������Ļ��������
	public static int IMAGETOLEFTDISTANCE = 0;	//��ߵ�һ��ͼƬ����Ļ��ߵľ���
	public static int IMAGETOTOPDISTANCE = 0;	//�����һ��ͼƬ����Ļ�ϱߵľ���
	
	public static final int CURRENTIMAGECOUNT = 10;	//��ǰ�ؿ�ͼƬ���������
	
	public static final int IMAGEMAXCOUNT = 12;		//��Ϸ�е�����ͼƬ����
	
	public static int MAXSPACETIME = 2000;			//��������֮������ʱ����
	
	/**
	 * ��ȡ�������ɫ
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
	 * ����map���������õ���ͼƬ����Ļ�����Ϸ�������ֵ
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
			//System.out.printf("������ת���������x=%d��y=%d\n", point.x, point.y);
			return point;
		}
		return null;
	}

}
