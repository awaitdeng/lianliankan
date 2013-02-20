package com.kuring.view.drawthread;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.kuring.view.CompanyView;

//对CompanyView进行定期重绘
public class CompanyViewDrawThread extends Thread {

	private int sleepSpan = 4;//睡眠的毫秒数
	
	private boolean flag = true;//循环标记位
	
	CompanyView companyView;
	
	SurfaceHolder surfaceHolder = null;
	
	public CompanyViewDrawThread (CompanyView companyView,SurfaceHolder surfaceHolder) {
		this.companyView = companyView;
		this.surfaceHolder = surfaceHolder;
	}
	
	@Override
	public void run() {
		Canvas c;//画布
		while (flag) {
			c = null;
			c = surfaceHolder.lockCanvas(null);
			synchronized (this.surfaceHolder) {
				try{
					companyView.onDraw(c);
		    	} catch (Exception e) {
		    		e.printStackTrace();
		    	} finally {
		    		//当程序切入后台的时候canvas是获取不到的
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
	
	public void setFlag(boolean flag) {//设置循环标记
    	this.flag = flag;
    }

}
