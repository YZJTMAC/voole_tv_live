package com.gntv.tv.view;

import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gntv.tv.R;
import com.gntv.tv.common.ap.AuthManager;
import com.gntv.tv.common.ap.User;
import com.gntv.tv.common.utils.ClickUtils;
import com.gntv.tv.common.utils.LocalManager;
import com.gntv.tv.common.utils.LogUtil;
import com.gntv.tv.model.error.ErrorInfo;
import com.gntv.tv.model.error.ErrorManager;
import com.gntv.tv.model.test.TestManager;
import com.gntv.tv.model.test.TestManager.RepairInterface;
import com.gntv.tv.model.test.TestResult;
import com.gntv.tv.report.PageActionReport;
import com.gntv.tv.report.PageActionReport.Action;
import com.gntv.tv.report.PageActionReport.FocusType;
import com.gntv.tv.report.PageActionReport.ModuleType;
import com.gntv.tv.report.PageActionReport.PageType;
import com.gntv.tv.view.base.TVAlertDialog;
import android.util.Log;

public class DetectView extends RelativeLayout{
	public static final int LOGIN_MSG = 301;
	public static final int MOVIE_MSG = 302;
	public static final int CONN_MSG = 303;
	public static final int SPEED_MSG = 304;
	private static final String FIRST_CHANNEL_ID = "FIRST_CHANNEL_ID";
	private static final String FIRST_RESOURCE_ID = "FIRST_RESOURCE_ID";
	
//	private TextView tvLoginTest;
//	private TextView tvMovieTest;
//	private TextView tvSpeedTest;
//	private TextView tvConnectTest;
//	
//	private TextView tvMiddleLogin;
//	private TextView tvMiddleMovie;
//	private TextView tvMiddleSpeed;
//	private TextView tvMiddleConnect;
	private DetectItemView loginDetectItemView,movieDetectItemView,connectDetectItemView,speedDetectItemView;
	private DetectItemView[] detectItemViews= {loginDetectItemView,movieDetectItemView,connectDetectItemView,speedDetectItemView};
	private String[] detectNames = {"用户登录测试","获取影片内容测试","服务器连接测试","网络测试"};
	private String[] detectState = {"正常","检测中","异常"};
	
	
	
	private TextView tvVersion;
	private TextView tvOemid;
	private TextView tvUid;
	
//	private ImageView ivLoginTest;
//	private ImageView ivMovieTest;
//	private ImageView ivConnectTest;
//	private ImageView ivSpeedTest;
	
	private LinearLayout ll_buffer;
	
	private Button btnDetection;
	private AtomicBoolean isStop = new AtomicBoolean(false);
	private Animation operaAnimation = null;
	private Context ctx;
	private Activity activity = null;
	private String versionCode = null;
	private int currentTimes = 0;
	private String errorMsg = null;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(isStop.get()){
				clearText();
				return;
			}
			TestResult testResult = (TestResult) msg.obj;
			
			if(!"0".equals(testResult.getStatus())){
				errorMsg = ErrorManager.GetInstance().getErrorMsg(testResult.getStatus(),
						testResult.getOtherStatus(), testResult.getOtherDesc()).getErroeMessageAndCode();
			}
			
			switch (msg.what) {
			case LOGIN_MSG:
				
				if("0".equals(testResult.getStatus())){

					
					loginDetectItemView.setStateTipText(detectState[0]);
					movieDetectItemView.setStateTipText(detectState[1]);
					setDevInfo(testResult.getUser());
					setSuccessStyle(loginDetectItemView,movieDetectItemView);
					detectMovie();
					
				}else{
					loginDetectItemView.setStateTipText(detectState[2]);
					loginDetectItemView.setMarkImageResource(R.drawable.detect_error);
					loginDetectItemView.clearMarkImageAnimation();
					if(!TVAlertDialog.isShowing){
						alertDialog = new TVAlertDialog.Builder(ctx)
						.setTitle(errorMsg)
						.setCancelable(false)
						.setNegativeButton("尝试修复",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();
										alertDialog = null;
										ll_buffer.setVisibility(View.VISIBLE);
										setFocus();
										TestManager.GetInstance().repairUser(new RepairInterface() {
											@Override
											public void doRepair(final boolean result) {
												activity.runOnUiThread(new Runnable() {
													@Override
													public void run() {
														ll_buffer.setVisibility(View.GONE);
														btnDetection.setText("重新检测");
														if(result){
															loginDetectItemView.setStateTipText(detectState[0]);
															movieDetectItemView.setStateTipText(detectState[1]);
															User user = AuthManager.GetInstance().getUser();
															LogUtil.i("DetectionActivity---->Repair--->user-->"+user);
															if(user!=null){
																setDevInfo(user);
															}
															setSuccessStyle(loginDetectItemView,movieDetectItemView);
															detectMovie();
														}
													}
												});
											}
										});
									}
								})
						.setPositiveButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();
										alertDialog = null;
										btnDetection.setText("重新检测");
										setFocus();
						
									}
								}).create();
						alertDialog.show();
						clearButtonFocus();
					}else{
						btnDetection.setText("重新检测");
					}
					
						
				}
				
				
				break;
			case MOVIE_MSG:
				if(!"0".equals(testResult.getStatus()))
				{
					movieDetectItemView.setStateTipText(detectState[2]);
					movieDetectItemView.setMarkImageResource(R.drawable.detect_error);
					movieDetectItemView.clearMarkImageAnimation();
					if(!TVAlertDialog.isShowing){
						alertDialog = new TVAlertDialog.Builder(ctx)
						.setTitle(errorMsg)
						.setCancelable(false)
						.setNegativeButton("尝试修复",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();
										alertDialog = null;
										setFocus();
										ll_buffer.setVisibility(View.VISIBLE);
										TestManager.GetInstance().repairMovie(new RepairInterface() {
											@Override
											public void doRepair(final boolean result) {
												activity.runOnUiThread(new Runnable() {
													@Override
													public void run() {
														ll_buffer.setVisibility(View.GONE);
														btnDetection.setText("重新检测");
														if(result){
															
															movieDetectItemView.setStateTipText(detectState[0]);
															connectDetectItemView.setStateTipText(detectState[1]);
															setSuccessStyle(movieDetectItemView,connectDetectItemView);
															detectConnect();
														}
													}
												});
											}
										});
									}
								})
						.setPositiveButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();
										alertDialog = null;
										btnDetection.setText("重新检测");
										setFocus();
						
									}
								}).create();
						alertDialog.show();
						clearButtonFocus();
					}else{
						btnDetection.setText("重新检测");
					}
					
				}else{

					movieDetectItemView.setStateTipText(detectState[0]);
					connectDetectItemView.setStateTipText(detectState[1]);
					setSuccessStyle(movieDetectItemView,connectDetectItemView);
					detectConnect();
					//detectSpeed();
				}
			
				break;
			case CONN_MSG:
				/*Boolean result = (Boolean) msg.obj;*/
				if(!"0".equals(testResult.getStatus())){
					
					
					connectDetectItemView.setStateTipText(detectState[2]);
					connectDetectItemView.setMarkImageResource(R.drawable.detect_error);

					connectDetectItemView.clearMarkImageAnimation();
					if(!TVAlertDialog.isShowing){
						alertDialog = new TVAlertDialog.Builder(ctx)
						.setTitle(errorMsg)
						.setCancelable(false)
						.setNegativeButton("尝试修复",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										alertDialog = null;
										dialog.cancel();
										ll_buffer.setVisibility(View.VISIBLE);
										setFocus();
										TestManager.GetInstance().repairConnect(new RepairInterface() {
											@Override
											public void doRepair(final boolean result) {
												activity.runOnUiThread(new Runnable() {
													@Override
													public void run() {
														new Handler().postDelayed(new Runnable() {
															@Override
															public void run() {
																ll_buffer.setVisibility(View.GONE);
																btnDetection.setText("重新检测");
															}
														}, 1000);
														
													}
												});
											}
										});
									}
								})
						.setPositiveButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();
										alertDialog = null;
										btnDetection.setText("重新检测");
										setFocus();
						
									}
								}).create();
						alertDialog.show();
						clearButtonFocus();
					}else{
						btnDetection.setText("重新检测");
					}
					
				}else{
					
					connectDetectItemView.setStateTipText(detectState[0]);
					speedDetectItemView.setStateTipText(detectState[1]);
					
					setSuccessStyle(connectDetectItemView,speedDetectItemView);
					detectSpeed();
				}
				break;
			case SPEED_MSG:
				//Double speed = (Double) msg.obj;
				Log.d("ly","detectspeed--->######");
				Log.d("ly","currentTimes::"+currentTimes+",Times::"+testResult.getTimes());
				if(currentTimes!=testResult.getTimes()){
					break;
				}
				if(!"0".equals(testResult.getStatus())){
					speedDetectItemView.setStateTipText(detectState[2]);
					speedDetectItemView.setMarkImageResource(R.drawable.detect_error);
					speedDetectItemView.clearMarkImageAnimation();
					
					btnDetection.setText("重新检测");
					if(!TVAlertDialog.isShowing){
						alertDialog = new TVAlertDialog.Builder(ctx)
						.setTitle(errorMsg)
						.setCancelable(false)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();
										alertDialog = null;
										setFocus();
										alertDialog = null;
									}
								})
						.create();
						alertDialog.show();
						clearButtonFocus();
					}
					
				}else{
					int speedReal = (int) Math.round(testResult.getSpeed());
					String downspeed = null;
					if(speedReal<512){
						downspeed = speedReal + " KB/S";
					}else{
						double mSpeed = speedReal/1024.0;
						downspeed = (Math.round(mSpeed*100)/100.0) + " MB/S";
					}
					speedDetectItemView.setStateTipText(downspeed);
					btnDetection.setText("重新检测");
					speedDetectItemView.setMarkImageResource(R.drawable.detect_success);
					speedDetectItemView.clearMarkImageAnimation();
					speedDetectItemView.setTextColor(getResources().getColor(R.color.light_white));
				}
				break;
			default:
				break;
			}
		};
	};

	public DetectView(Context context,Activity activity,String versionCode,User user) {
		super(context);
		init(context,activity,versionCode,user);
	}
	
	private void init(Context context,Activity activity,String versionCode,User user){
		setFocusable(true);
		setFocusableInTouchMode(true);
		setClickable(true);
		ctx = context;
		this.activity = activity;
		this.versionCode = versionCode;
		View view = LayoutInflater.from(context).inflate(R.layout.detect_main, null);
		this.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT));
		addView(view);
		
		//detectDialog = new DetectDialog(activity,R.style.ProgramDetailDialog);
		
//		tvLoginTest = (TextView) findViewById(R.id.tv_logintest);
//		tvMovieTest = (TextView) findViewById(R.id.tv_movietest);
//		tvSpeedTest = (TextView) findViewById(R.id.tv_speedtest);
//		tvConnectTest = (TextView) findViewById(R.id.tv_connecttest);
//		
//		tvMiddleLogin = (TextView) findViewById(R.id.tv_middle_login);
//		tvMiddleMovie = (TextView) findViewById(R.id.tv_middle_movie);
//		tvMiddleSpeed = (TextView) findViewById(R.id.tv_middle_speed);
//		tvMiddleConnect = (TextView) findViewById(R.id.tv_middle_connect);
		
		loginDetectItemView = (DetectItemView) view
				.findViewById(R.id.ll_logintest);
		movieDetectItemView = (DetectItemView) view
				.findViewById(R.id.ll_movietest);
		connectDetectItemView = (DetectItemView) view
				.findViewById(R.id.ll_connecttest);
		speedDetectItemView = (DetectItemView) view
				.findViewById(R.id.ll_speedtest);
		
		tvVersion = (TextView) findViewById(R.id.tv_version);
		tvOemid = (TextView) findViewById(R.id.tv_oemid);
		tvUid = (TextView) findViewById(R.id.tv_uid);
		
//		ivLoginTest = (ImageView) findViewById(R.id.iv_logintest);
//		ivMovieTest = (ImageView) findViewById(R.id.iv_movietest);
//		ivSpeedTest = (ImageView) findViewById(R.id.iv_speedtest);
//		ivConnectTest = (ImageView) findViewById(R.id.iv_connecttest);
		
		ll_buffer = (LinearLayout) findViewById(R.id.ll_buffer);
		
		operaAnimation = AnimationUtils.loadAnimation(activity, R.anim.tip);
		LinearInterpolator lin = new LinearInterpolator();
		operaAnimation.setInterpolator(lin);
		
		btnDetection = (Button) findViewById(R.id.btn_detection);
		
		//User user = AuthManager.GetInstance().getUser();
		if(user!=null){
			setDevInfo(user);
		}
		
		
		btnDetection.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!ClickUtils.isDoubleClick()){
					if(!"取消检测".equals(btnDetection.getText().toString())){
						//isStop = false;
						isStop.set(false);
						currentTimes++;
						btnDetection.setText("取消检测");
						clearText();
						loginDetectItemView.setMarkImageResource(R.drawable.detecting);
						if(operaAnimation!=null){
							loginDetectItemView.setMarkImageAnimation(operaAnimation);
						}
						loginDetectItemView.setStateTipText(detectState[1]);
						loginDetectItemView.setTextColor(getResources().getColor(R.color.blue_text));
						detectLogin();
						PageActionReport.GetInstance().reportPageAction(PageType.PlayPage, ModuleType.SetFunc, 
								"-1", FocusType.FaultDetection, Action.OKKey);
					}else{
						btnDetection.setText("开始检测");
						clearText();
						//isStop = true;
						isStop.set(true);
					}
				}
			}
		});
		
	}
	
	
	
	public void stopDetect() {
		LogUtil.i("DetectView-->stopDetect");
		isStop.set(true);
	}

	private void clearText(){

		
		ll_buffer.setVisibility(View.GONE);
		
		loginDetectItemView.reset();
		movieDetectItemView.reset();
		connectDetectItemView.reset();
		speedDetectItemView.reset();
	}
	
	private void detectLogin(){
		new Thread(){
			@Override
			public void run() {
				TestResult result = TestManager.GetInstance().testUser();
				Message msg = Message.obtain();
				msg.what = LOGIN_MSG;
				msg.obj = result;
				handler.sendMessage(msg);
			}
		}.start();
		
	}
	
	private void detectMovie(){
		new Thread(){
			public void run() {
				TestResult result = TestManager.GetInstance().testPorgram();
				Message msg = Message.obtain();
				msg.what = MOVIE_MSG;
				msg.obj = result;
				handler.sendMessage(msg);
			};
		}.start();
	}
	
	private void detectConnect()
	{
		new Thread(){
			public void run() {
				String channelCode = LocalManager.GetInstance().getLocal(FIRST_CHANNEL_ID, null);
				String channelName = LocalManager.GetInstance().getLocal(FIRST_RESOURCE_ID, null);
				
				TestResult result = TestManager.GetInstance().testServer(channelCode, channelName);
				Message msg = Message.obtain();
				msg.what = CONN_MSG;
				msg.obj = result;
				handler.sendMessage(msg);
			};
		}.start();
		
	}
	
	private void detectSpeed(){
		new Thread(){
			public void run() {
				int times = currentTimes;
				TestResult result = TestManager.GetInstance().testSpeed();
				result.setTimes(times);
				Message msg = Message.obtain();
				msg.what = SPEED_MSG;
				msg.obj = result;
				handler.sendMessage(msg);
			};
		}.start();
	}
	
	public void setSuccessStyle(DetectItemView successView,DetectItemView nextView)
	{
		successView.setMarkImageResource(R.drawable.detect_success);
		successView.clearMarkImageAnimation();
		nextView.setMarkImageResource(R.drawable.detecting);
		if(operaAnimation!=null){
			nextView.setMarkImageAnimation(operaAnimation);
		}
		successView.setTextColor(getResources().getColor(R.color.light_white));
		nextView.setTextColor(getResources().getColor(R.color.blue_text));
	}
	
	private void clearButtonFocus(){
		btnDetection.setBackgroundResource(R.drawable.dialog_button_background);
	}
	
	private void setFocus(){
		btnDetection.requestFocus();
		btnDetection.setBackgroundResource(R.drawable.border_shape);
	}
	
	public void requestButtonFocus(){
		requestFocus();
		setFocus();
		clearText();
		btnDetection.setText("开始检测");
	}
	
	private void setDevInfo(User user){
	
		tvVersion.setText("版本号:"+versionCode);
		tvOemid.setText("OEMID:"+user.getOemid());
		tvUid.setText("UID:"+user.getUid());
	}
	
	public void setCancelCheck(){
		stopDetect();
		clearText();
		btnDetection.setText("开始检测");
	}
	private TVAlertDialog alertDialog;
	public void cleanDialog(){
		if(alertDialog != null){
			if(alertDialog.isShowing()){
				alertDialog.dismiss();
				alertDialog = null;
			}
		}
	}
}
