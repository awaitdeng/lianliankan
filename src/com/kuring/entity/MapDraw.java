package com.kuring.entity;

import static com.kuring.util.ConstantUtil.*;

import java.util.HashSet;
import java.util.Set;

import com.kuring.activity.R;
import com.kuring.view.GameView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

/**
 * ��������map����
 * @author Administrator
 *
 */
public class MapDraw {

	private int[][] map;
	
	//�������õĵ�ͼƬ��Դ
	private Bitmap[] blocks;

	private GameView gameView;
	
	/**
	 * ��ǰѡ�е�ͼƬ����ǰѡ�е�ͼƬ���Ƶ�ʱ��Ŵ�
	 */
	private Point curPoint;
	
	private Paint paint = new Paint();
	
	/**
	 * �Ƿ���ʾ��������ʱ��������Ķ���Ч��
	 */
	private boolean isAnimation = false;
	
	/**
	 * ��Ҫ���µĵ�ļ���
	 */
	private Set<Point> updatePoints = new HashSet<Point>();
	
	/**
	 * ����֮����ʧ��ͼƬ1
	 */
	private Point vanishPoint1;
	
	/**
	 * ����֮����ʧ��ͼƬ2
	 */
	private Point vanishPoint2;
	
	/**
	 * ���µ���������ƫ����
	 */
	private int updatePointLeftOffset = 0;
	
	/**
	 * ���µ�����ҷ����ƫ����
	 */
	private int updatePointUpOffset = 0;
	
	/**
	 * Ҫ���µ��ˮƽ�������ƫ����
	 */
	private int updatePointMaxLeftOffset = 0;
	
	/**
	 * Ҫ���µ�Ĵ�ֱ�������ƫ����
	 */
	private int updatePointMaxUpOffset = 0;
	
	/**
	 * ÿ�λ���ƫ�����ı�Ĵ�С
	 */
	private int offSet = 0;
	
	/**
	 * ͼƬ���˶�����
	 * direction=0����ʾ��������ʱ��������λ�ò���
	 * direction=1����ʾ��������ʱ�������������ƶ�
	 * direction=2����ʾ��������ʱ�������������ƶ�
	 * direction=3����ʾ��������ʱ�������������ƶ�
	 * direction=4����ʾ��������ʱ�������������ƶ�
	 */
	private int direction = 0;
	
	public void init(int[][] map, GameView gameView) {
		this.map = map;
		this.gameView = gameView;
		initImageResource();
		offSet = (int)(IMAGEDIMENSION * 0.5);
	}
	
	public boolean doDraw(Canvas canvas) {
		//�������е�СͼƬ
		Rect rect = new Rect();
		for (int i=1; i<=ROWSIZE; i++) {
			for (int j=1; j<=COLUMNSIZE; j++) {
				if (isAnimation && updatePoints.contains(new Point(j, i))) {
					if (map[i][j] > 0) {
						rect.set(IMAGETOLEFTDISTANCE + (j - 1) * IMAGEDIMENSION + updatePointLeftOffset - updatePointMaxLeftOffset,
							IMAGETOTOPDISTANCE + (i - 1) * IMAGEDIMENSION + updatePointUpOffset - updatePointMaxUpOffset, 
							IMAGETOLEFTDISTANCE + j * IMAGEDIMENSION + updatePointLeftOffset - updatePointMaxLeftOffset,
							IMAGETOTOPDISTANCE + i * IMAGEDIMENSION + updatePointUpOffset - updatePointMaxUpOffset);
					}
				} else {
					rect.set(IMAGETOLEFTDISTANCE + (j - 1) * IMAGEDIMENSION, 
						IMAGETOTOPDISTANCE + (i - 1) * IMAGEDIMENSION, 
						IMAGETOLEFTDISTANCE + j * IMAGEDIMENSION,
						IMAGETOTOPDISTANCE + i * IMAGEDIMENSION);
				}
				if (map[i][j] > 0 && map[i][j] <= CURRENTIMAGECOUNT) {
					canvas.drawBitmap(blocks[map[i][j] - 1], null, rect, paint);
				}
			}
		}
		//���Ʊ�ѡ�е�ͼƬ
		if (curPoint != null && map[curPoint.y][curPoint.x] != 0) {
			Point curPointCoordinate = getCoordinateByIndex(curPoint);
			if (curPointCoordinate != null) {
				rect.set((int)(curPointCoordinate.x - IMAGEDIMENSION * 0.1),
						(int)(curPointCoordinate.y - IMAGEDIMENSION * 0.1), 
						(int)(curPointCoordinate.x + IMAGEDIMENSION * 1.1), 
						(int)(curPointCoordinate.y + IMAGEDIMENSION * 1.1));
				canvas.drawBitmap(blocks[map[curPoint.y][curPoint.x] - 1], null, rect, paint);
				//System.out.printf("curPoint.y=%d,curPoint.x=%d\n", curPoint.y, curPoint.x);
				//System.out.println("map[curPoint.y][curPoint.x]=" + map[curPoint.y][curPoint.x]);
			}
		}
		if (isAnimation) {
			switch (direction) {
			case 1:
				updatePointUpOffset -= offSet;
				break;
			case 2:
				updatePointUpOffset += offSet; 
				break;
			case 3:
				updatePointLeftOffset -= offSet;
				break;
			case 4:
				updatePointLeftOffset += offSet;
				break;
			}
			if (Math.abs(updatePointLeftOffset) > Math.abs(updatePointMaxLeftOffset) 
					|| Math.abs(updatePointUpOffset) > Math.abs(updatePointMaxUpOffset)) {
				isAnimation = false;
				updatePoints.clear();
			}
		}
		return isAnimation;
	}
	
	/**
	 * ��ʼ��Ҫ��ʾ�ڽ����ϵ�ͼƬ��Դ
	 */
	private void initImageResource() {
		blocks = new Bitmap[IMAGEMAXCOUNT];
		blocks[0] = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.block1);
		blocks[1] = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.block2);
		blocks[2] = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.block3);
		blocks[3] = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.block4);
		blocks[4] = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.block5);
		blocks[5] = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.block6);
		blocks[6] = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.block7);
		blocks[7] = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.block8);
		blocks[8] = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.block9);
		blocks[9] = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.block10);
		blocks[10] = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.block11);
		blocks[11] = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.block12);
	}

	public void setMap(int[][] map) {
		this.map = map;
	}

	public void setCurPoint(Point curPoint) {
		this.curPoint = curPoint;
	}

	public void setUpdatePoints(Set<Point> updatePoints) {
		this.updatePoints = updatePoints;
		//���ö���Ч����־Ϊtrue
		if (updatePoints != null && updatePoints.isEmpty() == false) {
			isAnimation = true;
			updatePointLeftOffset = 0;
			updatePointUpOffset = 0;
			updatePointMaxUpOffset = 0;
			updatePointMaxLeftOffset = 0;
		}
		Log.i("direction=", "" + direction);
		//�ж���Ҫ����������ƫ������һ��ͼƬ�ߴ绹������ͼƬ�ߴ�
		//����������ӵ��x�������
		if (vanishPoint1.x == vanishPoint2.x && Math.abs(vanishPoint1.y - vanishPoint2.y) == 1 
				&& (direction == 1 || direction == 2)) {
			//��ֱ��������������һ��
			if (direction == 1) {			//�����ƶ�
				updatePointMaxUpOffset = -IMAGEDIMENSION * 2;
			} else if (direction == 2) {	//�����ƶ�
				updatePointMaxUpOffset = IMAGEDIMENSION * 2;
			}
		} else if (vanishPoint1.y == vanishPoint2.y && Math.abs(vanishPoint1.x - vanishPoint2.x) == 1 
				&& (direction == 3 || direction == 4)) {		//����������y�᷽�����˵����ͬһ��ˮƽ����
			if (direction == 3) {			//�����ƶ�
				updatePointMaxLeftOffset = -IMAGEDIMENSION * 2;
			} else if (direction == 4) {	//�����ƶ�
				updatePointMaxLeftOffset = IMAGEDIMENSION * 2;
			}
		} else {
			switch (direction) {
			case 1:
				updatePointMaxUpOffset = -IMAGEDIMENSION;
				break;
			case 2:
				updatePointMaxUpOffset = IMAGEDIMENSION;
				break;
			case 3:
				updatePointMaxLeftOffset = -IMAGEDIMENSION;
				break;
			case 4:
				updatePointMaxLeftOffset = IMAGEDIMENSION;
			default:
				break;
			}
		}
		Log.i("����ͼƬ������ƶ�����", "ͼƬ�Ŀ��Ϊ" + IMAGEDIMENSION + "��ͼƬ��ֱ����ƶ�����Ϊ" + updatePointMaxUpOffset + ",ͼƬˮƽ����ƶ�����Ϊ" + updatePointLeftOffset);
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public void setVanishPoint1(Point vanishPoint1) {
		this.vanishPoint1 = vanishPoint1;
	}

	public void setVanishPoint2(Point vanishPoint2) {
		this.vanishPoint2 = vanishPoint2;
	}
}
