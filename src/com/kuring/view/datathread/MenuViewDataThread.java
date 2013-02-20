package com.kuring.view.datathread;

import com.kuring.view.MenuView;

public class MenuViewDataThread extends Thread {

	private MenuView menuView;
	
	private boolean flag = true;
	
	private int direction = 1;		//表示图片旋转的方向，1表示正向，2表示反向
	
	private int sleepSpan = 20;//睡眠的毫秒数
	
	public MenuViewDataThread(MenuView menuView) {
		this.menuView = menuView;
	}

	@Override
	public void run() {
		while (flag ) {
			if (direction == 1) {
				if (menuView.getLogoAngle() < 2) {
					menuView.setLogoAngle(menuView.getLogoAngle() + 0.1f);
				} else {
					direction = 2;
				}
			}
			if (direction == 2) {
				if (menuView.getLogoAngle() > -2) {
					menuView.setLogoAngle(menuView.getLogoAngle() - 0.1f);
				} else {
					direction = 1;
				}
			}
			try {
				Thread.sleep(sleepSpan);
			} catch (InterruptedException e) {
				e.printStackTrace();
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
