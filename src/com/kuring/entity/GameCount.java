package com.kuring.entity;

import static com.kuring.util.ConstantUtil.*;
import android.util.Log;

/**
 * ��Ϸͳ����
 * @author Administrator
 *
 */
public class GameCount {

	/**
	 * ������Ϸ��ʼʱ��
	 */
	private long startTime;
	
	/**
	 * ������Ϸ����ʱ��
	 */
	private long endTime;
	
	/**
	 * �������
	 */
	private int wrongCount;
	
	/**
	 * ����ӳ���
	 */
	private int maxLinkLength;
	
	/**
	 * ���η���
	 */
	private int score;
	
	/**
	 * �����������
	 */
	private int maxHitCount;
	
	/**
	 * ��ǰ���ӵ���������
	 */
	private int curHitCount;
	
	/**
	 * ��¼������������һ����ʱ��
	 */
	private long linkTime;
	
	/**
	 * ��ʼ����Ϸͳ����Ϣ
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
		Log.i("��������ʱ��", String.valueOf(linkTime));
		Log.i("�ϴ�����ʱ��", String.valueOf(this.linkTime));
		/**
		 * ���linkTime==0������������֮���ʱ����С��2s
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
	 * �������ӵ���������֮�����󳤶�
	 * @param maxLinkLength
	 */
	public void setMaxLinkLength(int maxLinkLength) {
		if (maxLinkLength > this.maxLinkLength) {
			this.maxLinkLength = maxLinkLength;
		}
	}
	
	/**
	 * ��ȡ������Ϸ����ʱ��
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
