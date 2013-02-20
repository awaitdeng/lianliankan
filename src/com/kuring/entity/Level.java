package com.kuring.entity;

import static com.kuring.util.ConstantUtil.*;

/**
 * 游戏等级类
 * 设置游戏的最大等级为50级
 * @author Administrator
 *
 */
public class Level {
	
	/**
	 * 游戏的最大等级为50级
	 */
	private int level;
	
	/**
	 * 游戏的毫秒级时间
	 */
	private long time;
	
	/**
	 * 游戏的模式
	 * mode=0，表示方块消除时其他方块位置不懂
	 * mode=1，表示方块消除时其他方块向上移动
	 * mode=2，表示方块消除时其他方块向下移动
	 * mode=3，表示方块消除时其他方块向左移动
	 * mode=4，表示方块消除时其他方块向右移动
	 */
	private int mode;
	
	public void setLevel(int level) {
		this.level = level;
		setTime();
		setMode();
		setMap();
	}

	private void setMap() {
		switch (level) {
		case 1:
			COLUMNSIZE = 2;
			ROWSIZE = 2;
			break;
		case 2:
			COLUMNSIZE = 5;
			ROWSIZE = 6;
			break;
		case 3:
			COLUMNSIZE = 6;
			ROWSIZE = 6;
			break;
		case 4:
			COLUMNSIZE = 6;
			ROWSIZE = 7;
			break;
		case 5:
			COLUMNSIZE = 6;
			ROWSIZE = 8;
			break;
		case 6:
			COLUMNSIZE = 7;
			ROWSIZE = 8;
			break;
		case 7:
			COLUMNSIZE = 7;
			ROWSIZE = 9;
			break;
		case 8:
			COLUMNSIZE = 7;
			ROWSIZE = 10;
			break;
		default:
			COLUMNSIZE = 7;
			ROWSIZE = 10;
			break;
		}
	}

	private void setMode() {
		if (level > 50) {
			return;
		}
		//前10级模式均为0
		if (level < 8) {
			mode = 0;
		}
		mode = (level - 1) % 5;
	}

	private void setTime() {
		//前10局的时间为两分钟
		if (level < 8) {
			time = 2 * 1000 * 60;
		} else {
			time = 2 * 1000 * 60 - 1000 * level;
		}
	}

	public long getTime() {
		return time;
	}

	public int getMode() {
		return mode;
	}

	public int getLevel() {
		return level;
	}
}
