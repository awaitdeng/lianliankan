package com.kuring.view.drawthread;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.kuring.view.MenuView;

public class MenuViewDrawThread extends Thread {
	
	private MenuView menuView;
	
	private SurfaceHolder surfaceHolder = null;
	
	private boolean flag;

	private long sleepSpan = 10;

	public MenuViewDrawThread(MenuView menuView, SurfaceHolder surfaceHolder) {
		this.menuView = menuView;
		this.surfaceHolder = surfaceHolder;
	}
	
	public void run() {
		Canvas c;//»­²¼		
		while (flag) {
			c = null;
			c = surfaceHolder.lockCanvas(null);
			synchronized (this.surfaceHolder) {
				try{
					menuView.onDraw(c);
		    	} catch (Exception e) {
		    		e.printStackTrace();
		    	} finally {
		    		if (c != null) {
		    			surfaceHolder.unlockCanvasAndPost(c);
		    		}
		    	}
				try {
					Thread.sleep(sleepSpan );
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
