package com.kuring.view.datathread;

import com.kuring.view.CompanyView;

import static com.kuring.util.ConstantUtil.*;
import com.kuring.util.MessageEnum;

//��CompanyView��ĳ�Ա���������޸�
public class CompanyViewDataThread extends Thread {

	CompanyView companyView = null;
	
	private Boolean flag = true;
	
	private int sleepSpan = 4;//˯�ߵĺ�����
	
	private int alpha = 250;		//���û��ʵ�͸���ȵĳ�ʼֵ��ֵԽ��Խ��͸��
	
	private int textSize = COMPANY_FONTSIZE;
	
	private Boolean isSecond = false;
	
	public CompanyViewDataThread (CompanyView companyView) {
		this.companyView = companyView;
	}
	
	@Override
	public void run() {
		while (flag) {
			companyView.getPaint2().setAlpha(alpha);			
			companyView.getPaint2().setTextSize(textSize);
			if (isSecond == false) {
				if (textSize < 100) {
					alpha -= 5;
					textSize += 1;
				} else {
					isSecond = true;
					textSize -= 1;
				}
			} else {
				if (textSize > COMPANY_FONTSIZE) {
					textSize -= 1;
				} else {
					flag = false;
					companyView.getMainActivity().myHandler.sendEmptyMessage(MessageEnum.COMPANY_FINISH.ordinal());
				}
			}			
			
			try {
				Thread.sleep(sleepSpan);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

}
