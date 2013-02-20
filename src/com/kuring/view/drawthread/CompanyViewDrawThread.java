package com.kuring.view.drawthread;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.kuring.view.CompanyView;

//��CompanyView���ж����ػ�
public class CompanyViewDrawThread extends Thread {

	private int sleepSpan = 4;//˯�ߵĺ�����
	
	private boolean flag = true;//ѭ�����λ
	
	CompanyView companyView;
	
	SurfaceHolder surfaceHolder = null;
	
	public CompanyViewDrawThread (CompanyView companyView,SurfaceHolder surfaceHolder) {
		this.companyView = companyView;
		this.surfaceHolder = surfaceHolder;
	}
	
	@Override
	public void run() {
		Canvas c;//����
		while (flag) {
			c = null;
			c = surfaceHolder.lockCanvas(null);
			synchronized (this.surfaceHolder) {
				try{
					companyView.onDraw(c);
		    	} catch (Exception e) {
		    		e.printStackTrace();
		    	} finally {
		    		//�����������̨��ʱ��canvas�ǻ�ȡ������
		    		if (c != null) {
		    			surfaceHolder.unlockCanvasAndPost(c);
		    		}
		    	}
				try {
					Thread.sleep(sleepSpan);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		    }
		}
	}
	
	public void setFlag(boolean flag) {//����ѭ�����
    	this.flag = flag;
    }

}
