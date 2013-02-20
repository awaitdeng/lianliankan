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
 * 用来绘制map的类
 * @author Administrator
 *
 */
public class MapDraw {

	private int[][] map;
	
	//界面上用的的图片资源
	private Bitmap[] blocks;

	private GameView gameView;
	
	/**
	 * 当前选中的图片，当前选中的图片绘制的时候放大
	 */
	private Point curPoint;
	
	private Paint paint = new Paint();
	
	/**
	 * 是否显示方块消除时其他方块的动画效果
	 */
	private boolean isAnimation = false;
	
	/**
	 * 需要更新的点的集合
	 */
	private Set<Point> updatePoints = new HashSet<Point>();
	
	/**
	 * 连接之后消失的图片1
	 */
	private Point vanishPoint1;
	
	/**
	 * 连接之后消失的图片2
	 */
	private Point vanishPoint2;
	
	/**
	 * 更新点的向左方向的偏移量
	 */
	private int updatePointLeftOffset = 0;
	
	/**
	 * 更新点的向右方向的偏移量
	 */
	private int updatePointUpOffset = 0;
	
	/**
	 * 要更新点的水平方向最大偏移量
	 */
	private int updatePointMaxLeftOffset = 0;
	
	/**
	 * 要更新点的垂直方向最大偏移量
	 */
	private int updatePointMaxUpOffset = 0;
	
	/**
	 * 每次绘制偏移量改变的大小
	 */
	private int offSet = 0;
	
	/**
	 * 图片的运动方向
	 * direction=0，表示方块消除时其他方块位置不懂
	 * direction=1，表示方块消除时其他方块向上移动
	 * direction=2，表示方块消除时其他方块向下移动
	 * direction=3，表示方块消除时其他方块向左移动
	 * direction=4，表示方块消除时其他方块向右移动
	 */
	private int direction = 0;
	
	public void init(int[][] map, GameView gameView) {
		this.map = map;
		this.gameView = gameView;
		initImageResource();
		offSet = (int)(IMAGEDIMENSION * 0.5);
	}
	
	public boolean doDraw(Canvas canvas) {
		//绘制所有的小图片
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
		//绘制被选中的图片
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
	 * 初始化要显示在界面上的图片资源
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
		//设置动画效果标志为true
		if (updatePoints != null && updatePoints.isEmpty() == false) {
			isAnimation = true;
			updatePointLeftOffset = 0;
			updatePointUpOffset = 0;
			updatePointMaxUpOffset = 0;
			updatePointMaxLeftOffset = 0;
		}
		Log.i("direction=", "" + direction);
		//判断需要动画点的最大偏移量是一个图片尺寸还是两个图片尺寸
		//如果两个连接点的x坐标相等
		if (vanishPoint1.x == vanishPoint2.x && Math.abs(vanishPoint1.y - vanishPoint2.y) == 1 
				&& (direction == 1 || direction == 2)) {
			//垂直方向两个点连在一块
			if (direction == 1) {			//向上移动
				updatePointMaxUpOffset = -IMAGEDIMENSION * 2;
			} else if (direction == 2) {	//向下移动
				updatePointMaxUpOffset = IMAGEDIMENSION * 2;
			}
		} else if (vanishPoint1.y == vanishPoint2.y && Math.abs(vanishPoint1.x - vanishPoint2.x) == 1 
				&& (direction == 3 || direction == 4)) {		//如果两个点的y轴方向相等说明在同一条水平线上
			if (direction == 3) {			//向左移动
				updatePointMaxLeftOffset = -IMAGEDIMENSION * 2;
			} else if (direction == 4) {	//向右移动
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
		Log.i("设置图片的最大移动距离", "图片的宽度为" + IMAGEDIMENSION + "，图片垂直最大移动距离为" + updatePointMaxUpOffset + ",图片水平最大移动距离为" + updatePointLeftOffset);
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
