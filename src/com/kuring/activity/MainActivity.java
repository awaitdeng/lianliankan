package com.kuring.activity;

import com.kuring.entity.GameCount;
import com.kuring.util.MessageEnum;
import com.kuring.view.CompanyView;
import com.kuring.view.GameCountView;
import com.kuring.view.GameView;
import com.kuring.view.MenuView;

import android.R.anim;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.AndroidCharacter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AnalogClock;
import static com.kuring.util.ConstantUtil.*;

//��Ϸ�Ŀ������������л�����
public class MainActivity extends Activity {
	
	private DisplayMetrics displayMetrics;
	
	private CompanyView companyView;
	
	private MenuView menuView;
	
	public GameView gameView;
	
	public GameCount gameCount;
	
	private GameCountView gameCountView;

	//���ո���view�෢�͵���Ϣ��������Ϣ���д���
	public Handler myHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if (msg.obj instanceof GameCount) {		//�������Ϸ��ͳ�ƽ���
				gameCount = (GameCount)msg.obj;
				initGameCount();
				Log.i("�л���ͼ", "�л�����Ϸͳ����ͼ�ɹ�");
			} else if (msg.what == MessageEnum.COMPANY_FINISH.ordinal()) {
				initMenuView();
			} else if (msg.what == MessageEnum.BEGIN_GAME.ordinal()) {
				initGameView();
			} else if (msg.what == MessageEnum.NEXTLEVEL.ordinal()) {
				nextLevel();
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȫ��
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
				              WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//ǿ��������Ļ
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//�õ���Ļ�ķֱ���
		displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		SCREEN_WIDTH = displayMetrics.widthPixels;
		SCREEN_HEIGHT = displayMetrics.heightPixels;
		//initCompanyView();
		//initMenuView();
		initGameView();
	}

	//��ʼ����Ϸ����
	protected void initGameView() {
		gameView = new GameView(this);
//		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		gameView.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
		this.setContentView(gameView);
	}

	//��ʼ����˾����
	private void initCompanyView() {
		companyView = new CompanyView(this);
		this.setContentView(companyView);
	}
	
	//��ʼ���˵�����
	private void initMenuView() {
		 menuView = new MenuView(this);
		 this.setContentView(menuView);
	}
	
	/**
	 * ��ʼ����Ϸ��ͳ�ƽ���
	 */
	private void initGameCount() {
		gameCountView = new GameCountView(this);
		this.setContentView(gameCountView);
	}
	
	/**
	 * ��ʼ��һ����Ϸ
	 */
	private void nextLevel() {
		if (gameView == null) {
			gameView = new GameView(this);
		}
		this.setContentView(gameView);
		gameView.nextLevel();
	}
}
