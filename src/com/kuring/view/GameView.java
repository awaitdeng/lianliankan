package com.kuring.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.kuring.activity.MainActivity;
import com.kuring.activity.R;
import com.kuring.engine.MapEngine;
import com.kuring.entity.AniText;
import com.kuring.entity.GameCount;
import com.kuring.entity.Level;
import com.kuring.entity.MagicCloud;
import com.kuring.entity.MagicLightning;
import com.kuring.entity.MagicStar;
import com.kuring.entity.MagicText;
import com.kuring.entity.MapDraw;
import com.kuring.util.MessageEnum;
import com.kuring.view.drawthread.GameViewDrawThread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import static com.kuring.util.ConstantUtil.*;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

	private MainActivity mainActivity;
	
	//游戏中的矩形框
	private Rect backgroundRect = new Rect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
	
	private Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
	
	//计算出每一个元素的平均个数
	int averageSize = (ROWSIZE * COLUMNSIZE) / CURRENTIMAGECOUNT;
	
	//地图的矩阵，行数和列数要比实际显示的多2
	private int[][] map;

	private Paint paint = new Paint();
	
	private Point curPoint;					//当用户选中一个图片时记录点的坐标
	private long curLinkTime;				//记录连接时当前的时间
	private int curContinueLinkCount;		//当前的连击次数
	
	List<Point> pointList = new ArrayList<Point>();
	
	//判断是否显示效果的变量
	private boolean isLink = false;				//判断是否已经连接
	private boolean isExplosed = false;			//图片爆炸效果是否已经完成
	private boolean isMagicText = false;		//检测是否显示提示信息
	private boolean isAddScoreText = false;		//显示增加分数的信息
	private boolean isContinueLinkText = false;	//显示连击的文本信息
	private boolean isMapDraw = true;			//是否需要对地图进行绘制
	private boolean isAnimation = false;		//是否图片是否正在显示动画效果
	//界面中用到的线程
	private GameViewDrawThread gameViewDrawThread;
	
	//游戏中用到的引擎
	MapEngine mapEngine = new MapEngine();
	MagicText magicText = new MagicText();
	
	//用到的实体类
	MagicCloud magicCloud = new MagicCloud(this);
	private MagicLightning magicLightning = new MagicLightning();
	private MagicStar magicStar1 = new MagicStar();
	private MagicStar magicStar2 = new MagicStar();
	private AniText addScoreText = new AniText();		//增加分数的文本显示
	private AniText continueLinkText = new AniText();	//连续击中的文本显示
	public Level level = new Level();
	private GameCount gameCount = new GameCount();
	private MapDraw mapDraw = new MapDraw();
	
	public GameView(MainActivity mainActivity) {
		super(mainActivity);
		this.mainActivity = mainActivity;
		getHolder().addCallback(this);
		level.setLevel(2);						//游戏开始时游戏等级
		initImageDimension();					//初始化图片的尺寸
		initImageToLeftDistance();				//初始化左边第一列图片到屏幕左边的距离
		initImageToTopDistance();				//初始化上面第一行图片到屏幕上边的距离
		map = new int[ROWSIZE + 2][COLUMNSIZE + 2];
		mapEngine.initMap(map);					//初始化地图
		mapDraw.init(map, this);				//初始化绘制地图的类
		isMagicText = true;
		magicText.init("开始游戏", Color.BLUE);
		gameCount.init();						//游戏统计类进行初始化
		magicCloud.initMagicCloud();			//初始化魔法云效果
		gameCount.setStartTime(System.currentTimeMillis());
	}
	
	/**
	 * 开始下一关游戏
	 */
	public void nextLevel() {
		level.setLevel(level.getLevel() + 1);
		initImageDimension();
		initImageToLeftDistance();				//初始化左边第一列图片到屏幕左边的距离
		initImageToTopDistance();				//初始化上面第一行图片到屏幕上边的距离
		map = new int[ROWSIZE + 2][COLUMNSIZE + 2];
		mapEngine.initMap(map);
		mapDraw.setMap(map);
		isMagicText = true;
		magicText.init("开始游戏", Color.BLUE);
		gameCount.init();
		gameCount.setStartTime(System.currentTimeMillis());
	}
	
	/**
	 * 初始化左边第一列图片到屏幕左边的距离
	 */
	private void initImageToTopDistance() {
		//首先要初始化图片的尺寸
		if (IMAGEDIMENSION == 0) {
			initImageDimension();
		}
		IMAGETOTOPDISTANCE = (SCREEN_HEIGHT - IMAGEDIMENSION * ROWSIZE) / 2;
		System.out.println("imageToTopDistance=" + IMAGETOTOPDISTANCE);
	}

	/**
	 * 初始化上面第一行图片到屏幕上边的距离
	 */
	private void initImageToLeftDistance() {
		//首先要初始化图片的尺寸
		if (IMAGEDIMENSION == 0) {
			initImageDimension();
		}
		IMAGETOLEFTDISTANCE = (SCREEN_WIDTH - IMAGEDIMENSION * COLUMNSIZE) / 2;
		System.out.println("imageToLeftDistance=" + IMAGETOLEFTDISTANCE);
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (isMapDraw) {
			canvas.drawBitmap(background, null, backgroundRect, paint);
			magicCloud.doDraw(canvas);
			//绘制图片
			isAnimation = mapDraw.doDraw(canvas);
			 
			//显示闪电效果
			if (isLink) {
				isLink = magicLightning.doDraw(canvas);
				magicLightning.doDraw(canvas);
			}
			if (isExplosed) {
				boolean result1,result2;
				result1 = magicStar1.doDraw(canvas);
				result2 = magicStar2.doDraw(canvas);
				isExplosed = !(result1 && result2);
			}
			//显示提示信息
			if (isMagicText) {
				isMagicText = magicText.doDraw(canvas);
			}
			//显示增加分数的信息
			if (isAddScoreText) {
				isAddScoreText = addScoreText.doDraw(canvas);
			}
			//显示连击的文本
			if (isContinueLinkText) {
				isContinueLinkText = continueLinkText.doDraw(canvas);
			}
		}else {
			Log.i("在绘制地图的时候丢了一桢", "在绘制地图的时候丢了一桢");
		}
	}
	
	/**
	 * 初始化实际显示的图片宽度和高度，需要根据屏幕参数计算，图片的宽度和高度是相等的
	 * 算法为：
	 * width = (int)(屏幕宽度/(columSize+1))
	 * height = (int)(屏幕高度/(rowSize+3))
	 * 返回width和height中的较小者
	 */
	private void initImageDimension() {
		int width = SCREEN_WIDTH / (COLUMNSIZE + 1);
		int height = SCREEN_HEIGHT / (ROWSIZE + 3);
		IMAGEDIMENSION = (width < height) ? width : height;
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		gameViewDrawThread = new GameViewDrawThread(this, getHolder());
		gameViewDrawThread.setFlag(true);
		gameViewDrawThread.start();
		Log.i("游戏界面绘制线程", "开启成功");
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height){
		Log.i("调用界面改变方法", "调用界面改变方法");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		gameViewDrawThread.setFlag(false);
		while (retry) {
            try {
            	gameViewDrawThread.join();//等待刷帧线程结束
                retry = false;
            } 
            catch (InterruptedException e) {//不断地循环，直到等待的线程结束
            }
        }
		gameViewDrawThread = null;
		Log.i("游戏界面绘制线程", "关闭成功");
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN && !isMagicText && !isAnimation){//屏幕被按下
			double x = event.getX();//得到X坐标
			double y = event.getY();//得到Y坐标
			System.out.printf("单击屏幕上x点的坐标为%f,y点的坐标为%f\n", x, y);
			int xindex = getArrayXIndex(x);
			int yindex = getArrayYIndex(y);
			System.out.printf("转换为索引之后的X坐标为%d，Y坐标为%d\n", xindex, yindex);
			if (isInMapArea(x, y) && map[yindex][xindex] != 0) {
				if (curPoint == null) {
					//首先判断当前的地图位置是否有图片
					if (map[getArrayYIndex(y)][getArrayXIndex(x)] != 0) {
						curPoint = new Point(getArrayXIndex(x), getArrayYIndex(y));
						mapDraw.setCurPoint(curPoint);
					}
				} else {
					Point endPoint = new Point(getArrayXIndex(x), getArrayYIndex(y));
					if (map[curPoint.y][curPoint.x] != map[endPoint.y][endPoint.x]) {//判断当前点击的图片和上一张图片是同一种类型的图片
						curPoint = endPoint;
						mapDraw.setCurPoint(curPoint);
						gameCount.setWrongCount(gameCount.getWrongCount() + 1);			//游戏统计的错误数加1
					} else {
						pointList.clear();
						pointList = mapEngine.searchRoute(map, curPoint, endPoint);
						if (pointList == null || pointList.size() == 0) {	//如果没有路径可通
							System.out.println("没有路径可通");
							curPoint = endPoint;
							mapDraw.setCurPoint(curPoint);
							gameCount.setWrongCount(gameCount.getWrongCount() + 1);			//游戏统计的错误数加1
							curContinueLinkCount = 0;	//连击次数清0
						} else {	//有路径可通
							//不相邻的两个点连接显示闪电效果
							if (!isNeighbor(new Point(curPoint.x, curPoint.y), new Point(endPoint.x, endPoint.y))) {
								isLink = true;
								magicLightning.addLightings(pointList);		//增加闪电效果
							}
							//对地图进行更新
							isMapDraw = false;
							Set<Point> points = mapEngine.update(map, level, curPoint, endPoint);
							//如果要更新位置的点集合不为空，说明有点需要更新位置，交给mapDraw对象更新显示动画效果
							if (points != null && points.isEmpty() == false) {
								/*String str = "";
								for (Iterator iterator = points.iterator(); iterator
										.hasNext();) {
									Point point = (Point) iterator.next();
									str += "(" + point.y + "," + point.x + "),";
								}
								System.out.println("得到需要更新的点的集合为：" + str);*/
								mapDraw.setCurPoint(null);
								mapDraw.setVanishPoint1(curPoint);
								mapDraw.setVanishPoint2(endPoint);
								mapDraw.setDirection(level.getMode());
								mapDraw.setUpdatePoints(points);
							}
							isMapDraw = true;
							//增加图片周围的粒子效果
							isExplosed = true;
							magicStar1.clearStars();
							magicStar2.clearStars();
							magicStar1.addStars(getCenterCoordinateByIndex(new Point(curPoint.x, curPoint.y)).x, 
									getCenterCoordinateByIndex(new Point(curPoint.x, curPoint.y)).y);
							magicStar2.addStars(getCenterCoordinateByIndex(new Point(endPoint.x, endPoint.y)).x, 
									getCenterCoordinateByIndex(new Point(endPoint.x, endPoint.y)).y);
							gameCount.setLinkTime(System.currentTimeMillis());		//设置当前击中的时间
							gameCount.setMaxLinkLength(pointList.size());			//设置当前连接的两个方块之间的长度
							gameCount.addScore(pointList.size());					//增加本关游戏的分数
							
							//显示+分数文本
							isAddScoreText = true;
							addScoreText.addText("+" + pointList.size(), getCenterCoordinateByIndex(new Point(endPoint.x, endPoint.y)).x, 
									getCenterCoordinateByIndex(new Point(endPoint.x, endPoint.y)).y, Paint.Align.CENTER);
							//显示连击文本
							if (displayHitText()) {
								Log.i("是否显示连击信息", "显示连击信息");
								isContinueLinkText = true;
								continueLinkText.addText(curContinueLinkCount + "连击", 32, 
										SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, Paint.Align.CENTER);
							}
							curPoint = null;
							
							//检测地图是否为空
							if (MapEngine.isMapEmpty(map, ROWSIZE, COLUMNSIZE)) {
								mapDraw.setCurPoint(null);
								Log.i("发送消息", "向主控制器发送更换到游戏统计视图页面消息");
								gameCount.setEndTime(System.currentTimeMillis());	//记录本关游戏结束时间
								//在切换界面之前先让界面停顿一秒的时间
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								mainActivity.myHandler.removeMessages(0);
								Message m = mainActivity.myHandler.obtainMessage(1, 1, 1, gameCount);
								mainActivity.myHandler.sendMessage(m);
							} else {
								Log.i("检测地图是否为空", "地图不为空");
								//检测是否还有方块可以消除，重新刷新地图
								if (mapEngine.isHaveMatchElement(map) == false) {
									Log.i("提示", "已经没有可以匹配的元素了");
									mapEngine.refreshMap(map);
								}
							}
						}
					}
				}
			}
		}
		return super.onTouchEvent(event);
	}
	
	/**
	 * 判断是否显示连击文本
	 */
	private boolean displayHitText() {
		long time = System.currentTimeMillis();
		if ((time - curLinkTime) < MAXSPACETIME) {
			curContinueLinkCount++;
			curLinkTime = time;
			return true;
		} else {
			curContinueLinkCount = 0;
		}
		curLinkTime = time;
		return false;
	}

	/**
	 * 判断给定的是否在屏幕的地图区域，在边界上算是在地图区域
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean isInMapArea(double x, double y) {
		if (x >= IMAGETOLEFTDISTANCE &&
			x <= IMAGETOLEFTDISTANCE + COLUMNSIZE * IMAGEDIMENSION &&
			y >= IMAGETOTOPDISTANCE &&
			y <= IMAGETOTOPDISTANCE + ROWSIZE * IMAGEDIMENSION) {
			return true;
		}
		return false;
	}
	
	/**
	 * 根据点的点在屏幕上的坐标获取在map数组中的x索引
	 * 如果不在目标区域内返回-1
	 * @param x
	 * @return
	 */
	private int getArrayXIndex(double x) {
		if (x < IMAGETOLEFTDISTANCE || x > (IMAGETOLEFTDISTANCE + COLUMNSIZE * IMAGEDIMENSION))
			return -1;
		return (int) ((x - IMAGETOLEFTDISTANCE) / IMAGEDIMENSION + 1);
	}
	
	/**
	 * 根据点的点在屏幕上的坐标获取在map数组中的Y索引
	 * 如果不在目标区域内返回-1
	 * @param y
	 * @return
	 */
	private int getArrayYIndex(double y) {
		if (y < IMAGETOTOPDISTANCE || y > (IMAGETOTOPDISTANCE + ROWSIZE * IMAGEDIMENSION))
			return -1;
		return (int) ((y - IMAGETOTOPDISTANCE) / IMAGEDIMENSION + 1);
	}
	
	/**
	 * 根据map数组索引得到该图片在屏幕的中心的像素值
	 * @param indexPoint
	 * @return
	 */
	public static Point getCenterCoordinateByIndex(Point indexPoint) {
		if (indexPoint == null) {
			return null;
		}
		if (indexPoint.x >= 0 && indexPoint.y >= 0 && indexPoint.x <= COLUMNSIZE + 1 && indexPoint.y <= ROWSIZE + 1) {
			Point point = new Point();
			point.x = (int)(IMAGETOLEFTDISTANCE + (indexPoint.x - 1 + 0.5) *  IMAGEDIMENSION);
			point.y = (int)(IMAGETOTOPDISTANCE + (indexPoint.y - 1 + 0.5) *  IMAGEDIMENSION);
			//System.out.printf("将索引转换成坐标的x=%d，y=%d\n", point.x, point.y);
			return point;
		}
		return null;
	}
	
	/**
	 * 判断两点是否相邻
	 * @param a
	 * @param b
	 * @return
	 */
	private boolean isNeighbor(Point a, Point b) {
		if ((a.x == b.x && Math.abs(a.y - b.y) == 1) || a.y == b.y && Math.abs(a.x - b.x) == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * 绘制连接线
	 * @param canvas
	 
	private void drawLine(Canvas canvas) {
		if (isLink) {
			if (pointList == null || pointList.isEmpty()) {
				return ;
			}
			Point curPoint = null;
			Point nextPoint = null;
			for (Iterator<Point> iterator = pointList.iterator(); iterator.hasNext();) {
				nextPoint = (Point) iterator.next();
				if (nextPoint == null) {
					return;
				} else if (curPoint == null) {
					curPoint = nextPoint;
				} else {
					Log.i("drawLine", "画线x（" + curPoint.x + "," + curPoint.y + "）-y（" + nextPoint.x + "," + nextPoint.y + ")");
					Paint paint = new Paint(Color.BLUE);
					paint.setStyle(Paint.Style.STROKE);
					paint.setStrokeWidth(6);
					canvas.drawLine(getCoordinateByIndex(curPoint).x + IMAGEDIMENSION / 2, 
							getCoordinateByIndex(curPoint).y + IMAGEDIMENSION / 2,
							getCoordinateByIndex(nextPoint).x + IMAGEDIMENSION / 2, 
							getCoordinateByIndex(nextPoint).y + IMAGEDIMENSION / 2, paint);
					curPoint = nextPoint;
				}
			}
		}
	}*/
}
