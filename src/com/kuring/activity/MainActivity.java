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

//游戏的控制器，负责切换界面
public class MainActivity extends Activity {
	
	private DisplayMetrics displayMetrics;
	
	private CompanyView companyView;
	
	private MenuView menuView;
	
	public GameView gameView;
	
	public GameCount gameCount;
	
	private GameCountView gameCountView;

	//接收各个view类发送的消息，并对消息进行处理
	public Handler myHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if (msg.obj instanceof GameCount) {		//如果是游戏的统计界面
				gameCount = (GameCount)msg.obj;
				initGameCount();
				Log.i("切换视图", "切换到游戏统计视图成功");
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
		//全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
				              WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//强制设置屏幕
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//得到屏幕的分辨率
		displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		SCREEN_WIDTH = displayMetrics.widthPixels;
		SCREEN_HEIGHT = displayMetrics.heightPixels;
		//initCompanyView();
		//initMenuView();
		initGameView();
	}

	//初始化游戏界面
	protected void initGameView() {
		gameView = new GameView(this);
//		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		gameView.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
		this.setContentView(gameView);
	}

	//初始化公司界面
	private void initCompanyView() {
		companyView = new CompanyView(this);
		this.setContentView(companyView);
	}
	
	//初始化菜单界面
	private void initMenuView() {
		 menuView = new MenuView(this);
		 this.setContentView(menuView);
	}
	
	/**
	 * 初始化游戏的统计界面
	 */
	private void initGameCount() {
		gameCountView = new GameCountView(this);
		this.setContentView(gameCountView);
	}
	
	/**
	 * 开始下一关游戏
	 */
	private void nextLevel() {
		if (gameView == null) {
			gameView = new GameView(this);
		}
		this.setContentView(gameView);
		gameView.nextLevel();
	}
}
