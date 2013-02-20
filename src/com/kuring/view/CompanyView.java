package com.kuring.view;

import com.kuring.activity.MainActivity;
import com.kuring.view.datathread.CompanyViewDataThread;
import com.kuring.view.drawthread.CompanyViewDrawThread;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;
import static com.kuring.util.ConstantUtil.*;

//公司信息的宣传界面
public class CompanyView extends SurfaceView implements Callback {

	private MainActivity mainActivity;
	
	private CompanyViewDrawThread companyViewDrawThread;
	
	private CompanyViewDataThread dataThread = null;
	
	private Paint paint = null;		//负责绘制静态的文字的画笔
	
	private Paint paint2 = null;	//负责绘制动态的文字的画笔
	
	private Paint paint3 = null;	//负责绘制页面最底下的文字内容
	
	private String companyInfo = "Kuring SoftWare";
	
	private String copyright = "@Copyright kuring soft 2011";
	
	public CompanyView(MainActivity mainActivity) {
		super(mainActivity);
		this.mainActivity = mainActivity;
		getHolder().addCallback(this);
		//启动线程
		companyViewDrawThread = new CompanyViewDrawThread(this, getHolder());
		dataThread = new CompanyViewDataThread(this);
		//初始化静态画笔
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextSize(COMPANY_FONTSIZE);
		paint.setAntiAlias(true);			//消除锯齿
		paint.setTextAlign(Align.CENTER);	//设置文本相对坐标的对齐方式为居中对齐
		
		//初始化动态画笔
		paint2 = new Paint();
		paint2.setColor(Color.WHITE);
		paint2.setAntiAlias(true);			//消除锯齿
		paint2.setTextAlign(Align.CENTER);	//设置文本相对坐标的对齐方式为居中对齐
		
		//初始化动态画笔
		paint3 = new Paint();
		paint3.setColor(Color.WHITE);
		paint3.setAntiAlias(true);			//消除锯齿
		paint3.setTextAlign(Align.CENTER);	//设置文本相对坐标的对齐方式为居中对齐
		paint3.setTextSize(12);
	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		//绘制静态文字
		canvas.drawText(companyInfo, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, paint);
		//绘制动态文字
		canvas.drawText(companyInfo, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, paint2);
		
		canvas.drawText(copyright, SCREEN_WIDTH / 2, SCREEN_HEIGHT - 20, paint3);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		companyViewDrawThread.setFlag(true);
		companyViewDrawThread.start();
		//启动更改数据的线程
		dataThread.setFlag(true);
		dataThread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceDestroyed (SurfaceHolder holder) {
		boolean retry = true;
		companyViewDrawThread.setFlag(false);
		while (retry) {
            try {
            	companyViewDrawThread.join();//等待刷帧线程结束
                retry = false;
            } 
            catch (InterruptedException e) {//不断地循环，直到等待的线程结束
            }
        }
		
		retry = true;
		dataThread.setFlag(false);
		while (retry) {
            try {
            	dataThread.join();//等待刷帧线程结束
                retry = false;
            } 
            catch (InterruptedException e) {//不断地循环，直到等待的线程结束
            }
        }
	}

	public MainActivity getMainActivity() {
		return mainActivity;
	}

	public void setMainActivity(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}

	public Paint getPaint2() {
		return paint2;
	}

	public void setPaint2(Paint paint2) {
		this.paint2 = paint2;
	}

}
