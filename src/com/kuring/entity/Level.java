package com.kuring.entity;

import static com.kuring.util.ConstantUtil.*;

/**
 * ��Ϸ�ȼ���
 * ������Ϸ�����ȼ�Ϊ50��
 * @author Administrator
 *
 */
public class Level {
	
	/**
	 * ��Ϸ�����ȼ�Ϊ50��
	 */
	private int level;
	
	/**
	 * ��Ϸ�ĺ��뼶ʱ��
	 */
	private long time;
	
	/**
	 * ��Ϸ��ģʽ
	 * mode=0����ʾ��������ʱ��������λ�ò���
	 * mode=1����ʾ��������ʱ�������������ƶ�
	 * mode=2����ʾ��������ʱ�������������ƶ�
	 * mode=3����ʾ��������ʱ�������������ƶ�
	 * mode=4����ʾ��������ʱ�������������ƶ�
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
		//ǰ10��ģʽ��Ϊ0
		if (level < 8) {
			mode = 0;
		}
		mode = (level - 1) % 5;
	}

	private void setTime() {
		//ǰ10�ֵ�ʱ��Ϊ������
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
