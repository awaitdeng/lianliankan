package com.kuring.view;

import com.kuring.activity.MainActivity;
import com.kuring.activity.R;
import com.kuring.entity.MagicBitmap;
import com.kuring.entity.MagicCloud;
import com.kuring.entity.MagicText;
import com.kuring.util.MessageEnum;
import com.kuring.view.datathread.MenuViewDataThread;
import com.kuring.view.drawthread.MenuViewDrawThread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;
import static com.kuring.util.ConstantUtil.*;

/**
 * 选择菜单界面
 * @author Administrator
 *
 */
public class MenuView extends SurfaceView implements Callback {
	
	private Bitmap logo;
	
	private Bitmap background;
	
	private Bitmap begin;
	
	private Bitmap help;
	
	private Bitmap option;
	
	private Bitmap exit;
	
	private int logoWidth;
	
	private int logoHeight;

	private MainActivity mainActivity;
	
	private Paint paint;
	
	private MenuViewDrawThread menuViewDrawThread;
	
	private MenuViewDataThread menuViewDataThread;
	
	private Rect backgroundRect;
	
	private float logoAngle = 0.0f;
	
	private Matrix matrix = new Matrix();
	
	MagicCloud magicCloud = new MagicCloud(this);
	
	
	public MenuView(MainActivity mainActivity) {
		super(mainActivity);
		this.mainActivity = mainActivity;
		getHolder().addCallback(this);
		
		//初始化图片资源
		logo = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
		begin = BitmapFactory.decodeResource(getResources(), R.drawable.begin);
		option = BitmapFactory.decodeResource(getResources(), R.drawable.option);
		help = BitmapFactory.decodeResource(getResources(), R.drawable.help);
		exit = BitmapFactory.decodeResource(getResources(), R.drawable.exit);
		background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
		
		//得到logo的宽度和高度
		logoWidth = logo.getWidth();
		logoHeight = logo.getHeight();
		
		backgroundRect = new Rect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		paint = new Paint();
		menuViewDrawThread = new MenuViewDrawThread(this, getHolder());
		menuViewDataThread = new MenuViewDataThread(this);
		magicCloud.initMagicCloud();
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(background, null, backgroundRect, paint);
		magicCloud.doDraw(canvas);
		matrix.reset();
		matrix.setRotate(logoAngle, logoWidth / 2, logoHeight);
		Bitmap newLogo = Bitmap.createBitmap(logo, 0, 0, logoWidth, logoHeight, matrix, true);
		canvas.drawBitmap(newLogo, (SCREEN_WIDTH - logoWidth) / 2, (int)(SCREEN_HEIGHT * 0.2), paint);
		canvas.drawBitmap(begin, (SCREEN_WIDTH - 120) / 2, (int)(SCREEN_HEIGHT * 0.4), paint);
		canvas.drawBitmap(option, (SCREEN_WIDTH - 120) / 2, (int)(SCREEN_HEIGHT * 0.5), paint);
		canvas.drawBitmap(help, (SCREEN_WIDTH - 120) / 2, (int)(SCREEN_HEIGHT * 0.6), paint);
		canvas.drawBitmap(exit, (SCREEN_WIDTH - 120) / 2, (int)(SCREEN_HEIGHT * 0.7), paint);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		menuViewDrawThread.setFlag(true);
		menuViewDrawThread.start();
		menuViewDataThread.setFlag(true);
		menuViewDataThread.start();
	}

	/**
	 * 监听屏幕
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){//屏幕被按下
			double x = event.getX();//得到X坐标
			double y = event.getY();//得到Y坐标
			if (x > (SCREEN_WIDTH - 120) / 2 && x < (SCREEN_WIDTH / 2 + 60) 
					&& y > SCREEN_HEIGHT * 0.4 && y < SCREEN_HEIGHT * 0.4 + 18) {
				mainActivity.myHandler.sendEmptyMessage(MessageEnum.BEGIN_GAME.ordinal());
			}
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		menuViewDrawThread.setFlag(false);
		while (retry) {
            try {
            	menuViewDrawThread.join();//等待刷帧线程结束
                retry = false;
            } 
            catch (InterruptedException e) {//不断地循环，直到等待的线程结束
            }
        }
		
		retry = true;
		menuViewDataThread.setFlag(false);
		while (retry) {
            try {
            	menuViewDataThread.join();//等待刷帧线程结束
                retry = false;
            } 
            catch (InterruptedException e) {//不断地循环，直到等待的线程结束
            }
        }
	}

	public float getLogoAngle() {
		return logoAngle;
	}

	public void setLogoAngle(float logoAngle) {
		this.logoAngle = logoAngle;
	}
}
