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
	
	//��Ϸ�еľ��ο�
	private Rect backgroundRect = new Rect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
	
	private Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
	
	//�����ÿһ��Ԫ�ص�ƽ������
	int averageSize = (ROWSIZE * COLUMNSIZE) / CURRENTIMAGECOUNT;
	
	//��ͼ�ľ�������������Ҫ��ʵ����ʾ�Ķ�2
	private int[][] map;

	private Paint paint = new Paint();
	
	private Point curPoint;					//���û�ѡ��һ��ͼƬʱ��¼�������
	private long curLinkTime;				//��¼����ʱ��ǰ��ʱ��
	private int curContinueLinkCount;		//��ǰ����������
	
	List<Point> pointList = new ArrayList<Point>();
	
	//�ж��Ƿ���ʾЧ���ı���
	private boolean isLink = false;				//�ж��Ƿ��Ѿ�����
	private boolean isExplosed = false;			//ͼƬ��ըЧ���Ƿ��Ѿ����
	private boolean isMagicText = false;		//����Ƿ���ʾ��ʾ��Ϣ
	private boolean isAddScoreText = false;		//��ʾ���ӷ�������Ϣ
	private boolean isContinueLinkText = false;	//��ʾ�������ı���Ϣ
	private boolean isMapDraw = true;			//�Ƿ���Ҫ�Ե�ͼ���л���
	private boolean isAnimation = false;		//�Ƿ�ͼƬ�Ƿ�������ʾ����Ч��
	//�������õ����߳�
	private GameViewDrawThread gameViewDrawThread;
	
	//��Ϸ���õ�������
	MapEngine mapEngine = new MapEngine();
	MagicText magicText = new MagicText();
	
	//�õ���ʵ����
	MagicCloud magicCloud = new MagicCloud(this);
	private MagicLightning magicLightning = new MagicLightning();
	private MagicStar magicStar1 = new MagicStar();
	private MagicStar magicStar2 = new MagicStar();
	private AniText addScoreText = new AniText();		//���ӷ������ı���ʾ
	private AniText continueLinkText = new AniText();	//�������е��ı���ʾ
	public Level level = new Level();
	private GameCount gameCount = new GameCount();
	private MapDraw mapDraw = new MapDraw();
	
	public GameView(MainActivity mainActivity) {
		super(mainActivity);
		this.mainActivity = mainActivity;
		getHolder().addCallback(this);
		level.setLevel(2);						//��Ϸ��ʼʱ��Ϸ�ȼ�
		initImageDimension();					//��ʼ��ͼƬ�ĳߴ�
		initImageToLeftDistance();				//��ʼ����ߵ�һ��ͼƬ����Ļ��ߵľ���
		initImageToTopDistance();				//��ʼ�������һ��ͼƬ����Ļ�ϱߵľ���
		map = new int[ROWSIZE + 2][COLUMNSIZE + 2];
		mapEngine.initMap(map);					//��ʼ����ͼ
		mapDraw.init(map, this);				//��ʼ�����Ƶ�ͼ����
		isMagicText = true;
		magicText.init("��ʼ��Ϸ", Color.BLUE);
		gameCount.init();						//��Ϸͳ������г�ʼ��
		magicCloud.initMagicCloud();			//��ʼ��ħ����Ч��
		gameCount.setStartTime(System.currentTimeMillis());
	}
	
	/**
	 * ��ʼ��һ����Ϸ
	 */
	public void nextLevel() {
		level.setLevel(level.getLevel() + 1);
		initImageDimension();
		initImageToLeftDistance();				//��ʼ����ߵ�һ��ͼƬ����Ļ��ߵľ���
		initImageToTopDistance();				//��ʼ�������һ��ͼƬ����Ļ�ϱߵľ���
		map = new int[ROWSIZE + 2][COLUMNSIZE + 2];
		mapEngine.initMap(map);
		mapDraw.setMap(map);
		isMagicText = true;
		magicText.init("��ʼ��Ϸ", Color.BLUE);
		gameCount.init();
		gameCount.setStartTime(System.currentTimeMillis());
	}
	
	/**
	 * ��ʼ����ߵ�һ��ͼƬ����Ļ��ߵľ���
	 */
	private void initImageToTopDistance() {
		//����Ҫ��ʼ��ͼƬ�ĳߴ�
		if (IMAGEDIMENSION == 0) {
			initImageDimension();
		}
		IMAGETOTOPDISTANCE = (SCREEN_HEIGHT - IMAGEDIMENSION * ROWSIZE) / 2;
		System.out.println("imageToTopDistance=" + IMAGETOTOPDISTANCE);
	}

	/**
	 * ��ʼ�������һ��ͼƬ����Ļ�ϱߵľ���
	 */
	private void initImageToLeftDistance() {
		//����Ҫ��ʼ��ͼƬ�ĳߴ�
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
			//����ͼƬ
			isAnimation = mapDraw.doDraw(canvas);
			 
			//��ʾ����Ч��
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
			//��ʾ��ʾ��Ϣ
			if (isMagicText) {
				isMagicText = magicText.doDraw(canvas);
			}
			//��ʾ���ӷ�������Ϣ
			if (isAddScoreText) {
				isAddScoreText = addScoreText.doDraw(canvas);
			}
			//��ʾ�������ı�
			if (isContinueLinkText) {
				isContinueLinkText = continueLinkText.doDraw(canvas);
			}
		}else {
			Log.i("�ڻ��Ƶ�ͼ��ʱ����һ��", "�ڻ��Ƶ�ͼ��ʱ����һ��");
		}
	}
	
	/**
	 * ��ʼ��ʵ����ʾ��ͼƬ��Ⱥ͸߶ȣ���Ҫ������Ļ�������㣬ͼƬ�Ŀ�Ⱥ͸߶�����ȵ�
	 * �㷨Ϊ��
	 * width = (int)(��Ļ���/(columSize+1))
	 * height = (int)(��Ļ�߶�/(rowSize+3))
	 * ����width��height�еĽ�С��
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
		Log.i("��Ϸ��������߳�", "�����ɹ�");
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height){
		Log.i("���ý���ı䷽��", "���ý���ı䷽��");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		gameViewDrawThread.setFlag(false);
		while (retry) {
            try {
            	gameViewDrawThread.join();//�ȴ�ˢ֡�߳̽���
                retry = false;
            } 
            catch (InterruptedException e) {//���ϵ�ѭ����ֱ���ȴ����߳̽���
            }
        }
		gameViewDrawThread = null;
		Log.i("��Ϸ��������߳�", "�رճɹ�");
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN && !isMagicText && !isAnimation){//��Ļ������
			double x = event.getX();//�õ�X����
			double y = event.getY();//�õ�Y����
			System.out.printf("������Ļ��x�������Ϊ%f,y�������Ϊ%f\n", x, y);
			int xindex = getArrayXIndex(x);
			int yindex = getArrayYIndex(y);
			System.out.printf("ת��Ϊ����֮���X����Ϊ%d��Y����Ϊ%d\n", xindex, yindex);
			if (isInMapArea(x, y) && map[yindex][xindex] != 0) {
				if (curPoint == null) {
					//�����жϵ�ǰ�ĵ�ͼλ���Ƿ���ͼƬ
					if (map[getArrayYIndex(y)][getArrayXIndex(x)] != 0) {
						curPoint = new Point(getArrayXIndex(x), getArrayYIndex(y));
						mapDraw.setCurPoint(curPoint);
					}
				} else {
					Point endPoint = new Point(getArrayXIndex(x), getArrayYIndex(y));
					if (map[curPoint.y][curPoint.x] != map[endPoint.y][endPoint.x]) {//�жϵ�ǰ�����ͼƬ����һ��ͼƬ��ͬһ�����͵�ͼƬ
						curPoint = endPoint;
						mapDraw.setCurPoint(curPoint);
						gameCount.setWrongCount(gameCount.getWrongCount() + 1);			//��Ϸͳ�ƵĴ�������1
					} else {
						pointList.clear();
						pointList = mapEngine.searchRoute(map, curPoint, endPoint);
						if (pointList == null || pointList.size() == 0) {	//���û��·����ͨ
							System.out.println("û��·����ͨ");
							curPoint = endPoint;
							mapDraw.setCurPoint(curPoint);
							gameCount.setWrongCount(gameCount.getWrongCount() + 1);			//��Ϸͳ�ƵĴ�������1
							curContinueLinkCount = 0;	//����������0
						} else {	//��·����ͨ
							//�����ڵ�������������ʾ����Ч��
							if (!isNeighbor(new Point(curPoint.x, curPoint.y), new Point(endPoint.x, endPoint.y))) {
								isLink = true;
								magicLightning.addLightings(pointList);		//��������Ч��
							}
							//�Ե�ͼ���и���
							isMapDraw = false;
							Set<Point> points = mapEngine.update(map, level, curPoint, endPoint);
							//���Ҫ����λ�õĵ㼯�ϲ�Ϊ�գ�˵���е���Ҫ����λ�ã�����mapDraw���������ʾ����Ч��
							if (points != null && points.isEmpty() == false) {
								/*String str = "";
								for (Iterator iterator = points.iterator(); iterator
										.hasNext();) {
									Point point = (Point) iterator.next();
									str += "(" + point.y + "," + point.x + "),";
								}
								System.out.println("�õ���Ҫ���µĵ�ļ���Ϊ��" + str);*/
								mapDraw.setCurPoint(null);
								mapDraw.setVanishPoint1(curPoint);
								mapDraw.setVanishPoint2(endPoint);
								mapDraw.setDirection(level.getMode());
								mapDraw.setUpdatePoints(points);
							}
							isMapDraw = true;
							//����ͼƬ��Χ������Ч��
							isExplosed = true;
							magicStar1.clearStars();
							magicStar2.clearStars();
							magicStar1.addStars(getCenterCoordinateByIndex(new Point(curPoint.x, curPoint.y)).x, 
									getCenterCoordinateByIndex(new Point(curPoint.x, curPoint.y)).y);
							magicStar2.addStars(getCenterCoordinateByIndex(new Point(endPoint.x, endPoint.y)).x, 
									getCenterCoordinateByIndex(new Point(endPoint.x, endPoint.y)).y);
							gameCount.setLinkTime(System.currentTimeMillis());		//���õ�ǰ���е�ʱ��
							gameCount.setMaxLinkLength(pointList.size());			//���õ�ǰ���ӵ���������֮��ĳ���
							gameCount.addScore(pointList.size());					//���ӱ�����Ϸ�ķ���
							
							//��ʾ+�����ı�
							isAddScoreText = true;
							addScoreText.addText("+" + pointList.size(), getCenterCoordinateByIndex(new Point(endPoint.x, endPoint.y)).x, 
									getCenterCoordinateByIndex(new Point(endPoint.x, endPoint.y)).y, Paint.Align.CENTER);
							//��ʾ�����ı�
							if (displayHitText()) {
								Log.i("�Ƿ���ʾ������Ϣ", "��ʾ������Ϣ");
								isContinueLinkText = true;
								continueLinkText.addText(curContinueLinkCount + "����", 32, 
										SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, Paint.Align.CENTER);
							}
							curPoint = null;
							
							//����ͼ�Ƿ�Ϊ��
							if (MapEngine.isMapEmpty(map, ROWSIZE, COLUMNSIZE)) {
								mapDraw.setCurPoint(null);
								Log.i("������Ϣ", "�������������͸�������Ϸͳ����ͼҳ����Ϣ");
								gameCount.setEndTime(System.currentTimeMillis());	//��¼������Ϸ����ʱ��
								//���л�����֮ǰ���ý���ͣ��һ���ʱ��
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								mainActivity.myHandler.removeMessages(0);
								Message m = mainActivity.myHandler.obtainMessage(1, 1, 1, gameCount);
								mainActivity.myHandler.sendMessage(m);
							} else {
								Log.i("����ͼ�Ƿ�Ϊ��", "��ͼ��Ϊ��");
								//����Ƿ��з����������������ˢ�µ�ͼ
								if (mapEngine.isHaveMatchElement(map) == false) {
									Log.i("��ʾ", "�Ѿ�û�п���ƥ���Ԫ����");
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
	 * �ж��Ƿ���ʾ�����ı�
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
	 * �жϸ������Ƿ�����Ļ�ĵ�ͼ�����ڱ߽��������ڵ�ͼ����
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
	 * ���ݵ�ĵ�����Ļ�ϵ������ȡ��map�����е�x����
	 * �������Ŀ�������ڷ���-1
	 * @param x
	 * @return
	 */
	private int getArrayXIndex(double x) {
		if (x < IMAGETOLEFTDISTANCE || x > (IMAGETOLEFTDISTANCE + COLUMNSIZE * IMAGEDIMENSION))
			return -1;
		return (int) ((x - IMAGETOLEFTDISTANCE) / IMAGEDIMENSION + 1);
	}
	
	/**
	 * ���ݵ�ĵ�����Ļ�ϵ������ȡ��map�����е�Y����
	 * �������Ŀ�������ڷ���-1
	 * @param y
	 * @return
	 */
	private int getArrayYIndex(double y) {
		if (y < IMAGETOTOPDISTANCE || y > (IMAGETOTOPDISTANCE + ROWSIZE * IMAGEDIMENSION))
			return -1;
		return (int) ((y - IMAGETOTOPDISTANCE) / IMAGEDIMENSION + 1);
	}
	
	/**
	 * ����map���������õ���ͼƬ����Ļ�����ĵ�����ֵ
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
			//System.out.printf("������ת���������x=%d��y=%d\n", point.x, point.y);
			return point;
		}
		return null;
	}
	
	/**
	 * �ж������Ƿ�����
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
	 * ����������
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
					Log.i("drawLine", "����x��" + curPoint.x + "," + curPoint.y + "��-y��" + nextPoint.x + "," + nextPoint.y + ")");
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
