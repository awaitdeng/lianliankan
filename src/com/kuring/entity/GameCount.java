package com.kuring.entity;

import static com.kuring.util.ConstantUtil.*;
import android.util.Log;

/**
 * 游戏统计类
 * @author Administrator
 *
 */
public class GameCount {

	/**
	 * 本关游戏开始时间
	 */
	private long startTime;
	
	/**
	 * 本关游戏结束时间
	 */
	private long endTime;
	
	/**
	 * 错误次数
	 */
	private int wrongCount;
	
	/**
	 * 最长连接长度
	 */
	private int maxLinkLength;
	
	/**
	 * 本次分数
	 */
	private int score;
	
	/**
	 * 最高连击次数
	 */
	private int maxHitCount;
	
	/**
	 * 当前连接的连击次数
	 */
	private int curHitCount;
	
	/**
	 * 记录将两个连接在一块后的时间
	 */
	private long linkTime;
	
	/**
	 * 初始化游戏统计信息
	 */
	public void init() {
		startTime = 0;
		endTime = 0;
		wrongCount = 0;
		maxLinkLength = 0;
		score = 0;
		maxHitCount = 0;
		linkTime = 0;
	}

	public int getWrongCount() {
		return wrongCount;
	}

	public void setWrongCount(int wrongCount) {
		this.wrongCount = wrongCount;
	}

	public int getScore() {
		return score - wrongCount;
	}

	public void addScore(int score) {
		this.score += score;
	}

	public int getMaxHitCount() {
		return maxHitCount;
	}

	public long getLinkTime() {
		return linkTime;
	}

	public void setLinkTime(long linkTime) {
		Log.i("本次连接时间", String.valueOf(linkTime));
		Log.i("上次连接时间", String.valueOf(this.linkTime));
		/**
		 * 如果linkTime==0或者两次连击之间的时间间隔小于2s
		 */
		if ((linkTime - this.linkTime) < MAXSPACETIME) {
			curHitCount++;
			if (curHitCount > maxHitCount) {
				maxHitCount = curHitCount;
			}
		} else {
			curHitCount = 0;
		}
		this.linkTime = linkTime;
	}

	public int getMaxLinkLength() {
		return maxLinkLength;
	}

	/**
	 * 设置连接的连个方块之间的最大长度
	 * @param maxLinkLength
	 */
	public void setMaxLinkLength(int maxLinkLength) {
		if (maxLinkLength > this.maxLinkLength) {
			this.maxLinkLength = maxLinkLength;
		}
	}
	
	/**
	 * 获取本关游戏所用时间
	 * @return
	 */
	public String getGameTime() {
		int time = (int)(endTime - startTime) / 1000;
		int minute = (int)(time / 60);
		int second = time % 60;
		return String.format("%02d:%02d", minute, second);
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
}
