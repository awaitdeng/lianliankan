package com.kuring.view.drawthread;

import com.kuring.view.GameView;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameViewDrawThread extends Thread {

	private GameView gameView;
	
	private SurfaceHolder surfaceHolder;
	
	private boolean flag;

	private long sleepSpan = 100;
	
	public GameViewDrawThread(GameView gameView, SurfaceHolder surfaceHolder) {
		this.gameView = gameView;
		this.surfaceHolder = surfaceHolder;
	}

	@Override
	public void run() {
		Canvas c;//»­²¼
		while (flag) {
			c = null;
			c = surfaceHolder.lockCanvas(null);
			synchronized (this.surfaceHolder) {
				try{
					gameView.onDraw(c);
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
