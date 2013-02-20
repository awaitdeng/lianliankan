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

//��˾��Ϣ����������
public class CompanyView extends SurfaceView implements Callback {

	private MainActivity mainActivity;
	
	private CompanyViewDrawThread companyViewDrawThread;
	
	private CompanyViewDataThread dataThread = null;
	
	private Paint paint = null;		//������ƾ�̬�����ֵĻ���
	
	private Paint paint2 = null;	//������ƶ�̬�����ֵĻ���
	
	private Paint paint3 = null;	//�������ҳ������µ���������
	
	private String companyInfo = "Kuring SoftWare";
	
	private String copyright = "@Copyright kuring soft 2011";
	
	public CompanyView(MainActivity mainActivity) {
		super(mainActivity);
		this.mainActivity = mainActivity;
		getHolder().addCallback(this);
		//�����߳�
		companyViewDrawThread = new CompanyViewDrawThread(this, getHolder());
		dataThread = new CompanyViewDataThread(this);
		//��ʼ����̬����
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextSize(COMPANY_FONTSIZE);
		paint.setAntiAlias(true);			//�������
		paint.setTextAlign(Align.CENTER);	//�����ı��������Ķ��뷽ʽΪ���ж���
		
		//��ʼ����̬����
		paint2 = new Paint();
		paint2.setColor(Color.WHITE);
		paint2.setAntiAlias(true);			//�������
		paint2.setTextAlign(Align.CENTER);	//�����ı��������Ķ��뷽ʽΪ���ж���
		
		//��ʼ����̬����
		paint3 = new Paint();
		paint3.setColor(Color.WHITE);
		paint3.setAntiAlias(true);			//�������
		paint3.setTextAlign(Align.CENTER);	//�����ı��������Ķ��뷽ʽΪ���ж���
		paint3.setTextSize(12);
	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		//���ƾ�̬����
		canvas.drawText(companyInfo, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, paint);
		//���ƶ�̬����
		canvas.drawText(companyInfo, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, paint2);
		
		canvas.drawText(copyright, SCREEN_WIDTH / 2, SCREEN_HEIGHT - 20, paint3);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		companyViewDrawThread.setFlag(true);
		companyViewDrawThread.start();
		//�����������ݵ��߳�
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
            	companyViewDrawThread.join();//�ȴ�ˢ֡�߳̽���
                retry = false;
            } 
            catch (InterruptedException e) {//���ϵ�ѭ����ֱ���ȴ����߳̽���
            }
        }
		
		retry = true;
		dataThread.setFlag(false);
		while (retry) {
            try {
            	dataThread.join();//�ȴ�ˢ֡�߳̽���
                retry = false;
            } 
            catch (InterruptedException e) {//���ϵ�ѭ����ֱ���ȴ����߳̽���
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
