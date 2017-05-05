package com.truthso.ip360.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.BaiduLocationUtil;
import com.truthso.ip360.utils.BaiduLocationUtil.locationListener;
import com.truthso.ip360.utils.CheckAudioPermission;
import com.truthso.ip360.utils.DateUtil;
import com.truthso.ip360.utils.FileSizeUtil;
import com.truthso.ip360.utils.TimeUtile;
import com.truthso.ip360.view.VoiceLineView;
import com.truthso.ip360.view.xrefreshview.LogUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 
 * 执行现场录音的界面
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-6-13下午4:39:51
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class LiveRecordImplementationActivity extends BaseActivity implements
		OnClickListener {
	private Dialog alertDialog;
	private ImageButton btn_title_left;
	private String loc;
	private TextView mRecordTime;
	private String timeUsed;
	private double lat,longti;
	private int timeUsedInsec;
	private boolean isPause;
	private Handler uiHandle = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (!isPause) {
					addTimeUsed();
					updateClockUI();
				}
				uiHandle.sendEmptyMessageDelayed(1, 1000);
				break;
			default:
			case 2:
				if (mediaRecorder == null)
					return;
				double ratio = (double) mediaRecorder.getMaxAmplitude() / 100;
				double db = 0;// 分贝
				// 默认的最大音量是100,可以修改，但其实默认的，在测试过程中就有不错的表现
				// 你可以传自定义的数字进去，但需要在一定的范围内，比如0-200，就需要在xml文件中配置maxVolume
				// 同时，也可以配置灵敏度sensibility
				if (ratio > 1)
					db = 40 * Math.log10(ratio);
				// 只要有一个线程，不断调用这个方法，就可以使波形变化
				// 主要，这个方法必须在ui线程中调用
				voiceLineView.setVolume((int) (db));
				break;
			}
		}
	};

	private Button mButton, btn_cancle, btn_save;
	/** 录音控件 */
	private MediaRecorder mediaRecorder = null;
	/** 路径名 */
	private String fileDir;
	// 录音文件名
	private String filePath;

	private boolean isRecording = false;
	private String fileSize;
	private String fileName;
	private String recTotalTime;
	private VoiceLineView voiceLineView;
	private boolean isAlive = true;
	private int hor;
	private int min;
	private int sec;
	private int mintime;
	private int i;
	private Long serviceTime;


	private void startTime() {
		uiHandle.sendEmptyMessageDelayed(1, 1000);
	}

	/**
	 * 更新时间的显示
	 */
	private void updateClockUI() {
		mRecordTime.setText(getHor() + ":" + getMin() + ":" + getSec());
	
	}

	public void addTimeUsed() {
		timeUsedInsec = timeUsedInsec + 1;
		timeUsed = this.getMin() + ":" + this.getSec();
	}

	public CharSequence getHor() {
		hor = timeUsedInsec / 3600;
		return hor < 10 ? "0" + hor : String.valueOf(hor);

	}

	public CharSequence getMin() {
		min = timeUsedInsec / 60;
		return min < 10 ? "0" + min : String.valueOf(min);

	}

	public CharSequence getSec() {
		sec = timeUsedInsec % 60;
		return sec < 10 ? "0" + sec : String.valueOf(sec);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_title_left:
				showDialogIsCancel("是否确定放弃录音？");

				break;


		case R.id.btn_record:
			if (!isRecording) {
				isAlive = true;
				isRecording = true;
				mButton.setSelected(true);
				recordVoice();
				uiHandle.removeMessages(1);
				startTime();
				isPause = false;
				new Thread(new Runnable() {

					@Override
					public void run() {
						while (isAlive) {
							uiHandle.sendEmptyMessage(2);

							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}).start();

			} else {
				isAlive = false;
				isRecording = false;
				mButton.setSelected(false);
				mButton.setVisibility(View.GONE);
				stoprecordVoice();
				int currentTime = TimeUtile.getCurrentTime();
				long date= serviceTime+currentTime*1000;//服务器返回的时间加上本地计时器时间
				String dateStr= DateUtil.formatDate(new Date(date),"yyyy-MM-dd HH:mm:ss");

				fileSize = FileSizeUtil.getAutoFileOrFilesSize(new File(filePath).getAbsolutePath());
				File file = new File(filePath);
				   long length = file.length();
				   double fileSize_B = FileSizeUtil.FormetFileSize(length, FileSizeUtil.SIZETYPE_B);
				   fileName = filePath.substring(filePath.lastIndexOf("/")+1);
//				   LogUtils.e(fileName+"录音的文件名");
				recTotalTime = mRecordTime.getText().toString().trim();
				i = timeUsedInsec%60;
				if (i > 0) {
					mintime = timeUsedInsec/60+1;
				}else{
					mintime = timeUsedInsec/60;
				}
				LogUtils.e(fileSize_B+"录音文件的大小"+fileSize);
				isPause = true;
				timeUsedInsec = 0;
				Intent intent = new Intent(this,LiveRecordPreActivity.class);
				intent.putExtra("fileTime", recTotalTime);
				intent.putExtra("date", dateStr);
				intent.putExtra("fileSize", fileSize);
				intent.putExtra("fileSize_B", fileSize_B);
				intent.putExtra("fileName", fileName);
				intent.putExtra("filePath", filePath);
				intent.putExtra("mintime", mintime);
				intent.putExtra("loc", loc);
				intent.putExtra("longlat",longti+","+lat);
				startActivity(intent);
				finish();		
			}
			break;

		default:
			break;
		}
	}

	/** 开始录音 */
	private void recordVoice() {
		mediaRecorder = null;
		mediaRecorder = new MediaRecorder();
		filePath = fileDir +"/"+ new DateFormat().format("yyyyMMdd_HHmmss", Calendar.getInstance(Locale.CHINA)) + ".amr";
		LogUtils.e(filePath+"录音的路径");
		// 设置录音的编码格式,即数据源的格式,这里设置什么格式主要根据录音的用途来判断
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		// 设置录音文件的格式,这里是指文件的后缀名格式,这个设置的3GP
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		// 设置录音的解码格式,这个必须在setOutputFile方法前设置,否则无效
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mediaRecorder.setOutputFile(filePath);// 设置录音文件的输出路径
		try {
			mediaRecorder.prepare();
			mediaRecorder.start(); // 开始录音
		} catch (IllegalStateException e) {
			// stoprecordVoice();
			e.printStackTrace();
		} catch (IOException e) {
			// stoprecordVoice();
			e.printStackTrace();
		}

	}

	/** 停止录音 */
/*	private void stoprecordVoice() {
		if (mediaRecorder != null) {
			mediaRecorder.stop(); // 停止录音
		//	mediaRecorder.reset(); // 在释放资源时,必须要重置一下,不然下一步释放时可能会出错
			mediaRecorder.release(); // 这个是否录音控件的,不然会一直占据资源
			mediaRecorder=null;
		}
	}*/
	/** 停止录音 */
	private void stoprecordVoice() {
		if (mediaRecorder != null) {
			try {
				mediaRecorder.stop(); // 停止录音
			}catch (IllegalStateException e) {
				// stoprecordVoice();
				e.printStackTrace();
			} catch (Exception e) {
				// stoprecordVoice();
				e.printStackTrace();
			}

		//	mediaRecorder.reset(); // 在释放资源时,必须要重置一下,不然下一步释放时可能会出错
			mediaRecorder.release(); // 这个是否录音控件的,不然会一直占据资源
			mediaRecorder=null;
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
	}

	

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		fileDir =MyConstants.RECORD_PATH;
		File f=new File(fileDir);
		if(!f.exists()){
			f.mkdirs();
		}
	}

	@Override
	public void initView() {
		serviceTime = getIntent().getLongExtra("serviceTime",0);//服務器初始時間
	boolean  isHasPermission = CheckAudioPermission.isHasPermission(this);
		if(!isHasPermission){
			showDialog();
		}
		getLocation();
		btn_title_left = (ImageButton) findViewById(R.id.btn_title_left);
		btn_title_left.setOnClickListener(this);

		mButton = (Button) findViewById(R.id.btn_record);
		voiceLineView = (VoiceLineView) findViewById(R.id.voicLine);
		mButton.setOnClickListener(this);

		mRecordTime = (TextView) findViewById(R.id.tv_record_time);

	}

	@Override
	public int setLayout() {
		return R.layout.activity_liverecord_implement;
	}

	@Override
	public String setTitle() {
		return "录音取证";
	}
	private void getLocation(){
		BaiduLocationUtil.getLocation(this, new locationListener() {

			@Override
			public void location(String s, double latitude, double longitude) {
				loc = s;
				lat = latitude;
				longti =longitude;
//				Message message = handler .obtainMessage();
//				message.what = 1;
//				handler.sendMessage(message);
			}


		});
	}
	/**
	 * 监听系统的返回键
	 */
	@Override
	public void onBackPressed() {
		showDialogIsCancel("是否确定放弃录音？");
	}
	private void showDialog() {
		alertDialog = new AlertDialog.Builder(this).setTitle("温馨提示").setCancelable(false)
				.setMessage("您没有开启录音权限！").setIcon(R.drawable.ww)
				.setPositiveButton("去设置", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//去设置
//						getAppDetailSettingIntent(LiveRecordImplementationActivity.this);
						getPhoneleixing();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						alertDialog.dismiss();
						finish();
					}
				}).create();
		alertDialog.show();
	}
	/**
	 * 跳转到权限设置界面
	 */
	private void getAppDetailSettingIntent(Context context){
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if(Build.VERSION.SDK_INT >= 9){
			intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
			intent.setData(Uri.fromParts("package", getPackageName(), null));
		} else if(Build.VERSION.SDK_INT <= 8){
			intent.setAction(Intent.ACTION_VIEW);
			intent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
			intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
		}
		startActivity(intent);
	}
	public  void getPhoneleixing(){
		String name= Build.MANUFACTURER;
		/**
		 * HUAWEI，vivo，OPPO......手机机型标注不可以改变
		 */
		if("HUAWEI".equals(name)){
			goHuaWeiMainager();
		}else if ("vivo".equals(name)){
			goVivoMainager();
		}else if ("OPPO".equals(name)){
			goOppoMainager();
		}else if ("Coolpad".equals(name)){
			goCoolpadMainager();
		}else if ("Meizu".equals(name)){
//			goMeizuMainager();
			getAppDetailSettingIntent(LiveRecordImplementationActivity.this);
		}else if ("Xiaomi".equals(name)){
			goXiaoMiMainager();
		}else if ("samsung".equals(name)){
			goSangXinMainager();
		}else{
			goIntentSetting();
		}
	}
	private void goHuaWeiMainager() {
		try {
			Intent intent = new Intent("demo.vincent.com.tiaozhuan");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
			intent.setComponent(comp);
			startActivity(intent);
		} catch (Exception e) {
			Toaster.showToast(LiveRecordImplementationActivity.this, "跳转失败");
			e.printStackTrace();
			goIntentSetting();
		}
	}
	private void goXiaoMiMainager(){
		try {
			Intent localIntent = new Intent(
					"miui.intent.action.APP_PERM_EDITOR");
			localIntent
					.setClassName("com.miui.securitycenter",
							"com.miui.permcenter.permissions.AppPermissionsEditorActivity");
			localIntent.putExtra("extra_pkgname",getPackageName());
			startActivity(localIntent);
		} catch (ActivityNotFoundException localActivityNotFoundException) {
			goIntentSetting();
		}
	}
	private void goMeizuMainager(){
		try {
			Intent intent=new Intent("com.meizu.safe.security.SHOW_APPSEC");
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			intent.putExtra("packageName", "xiang.settingpression");
			startActivity(intent);
		} catch (ActivityNotFoundException localActivityNotFoundException) {
			localActivityNotFoundException.printStackTrace();
			goIntentSetting();
		}
	}
	private void goSangXinMainager(){
		//三星4.3可以直接跳转
		goIntentSetting();
	}
	private void goIntentSetting(){
		Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		Uri uri = Uri.fromParts("package",getPackageName(), null);
		intent.setData(uri);
		try {
			startActivity(intent);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	private void goOppoMainager(){
		doStartApplicationWithPackageName("com.coloros.safecenter");
	}

	/**
	 * doStartApplicationWithPackageName("com.yulong.android.security:remote")
	 * 和Intent open = getPackageManager().getLaunchIntentForPackage("com.yulong.android.security:remote");
	 startActivity(open);
	 本质上没有什么区别，通过Intent open...打开比调用doStartApplicationWithPackageName方法更快，也是android本身提供的方法
	 */
	private void goCoolpadMainager(){
		doStartApplicationWithPackageName("com.yulong.android.security:remote");
      /*  Intent openQQ = getPackageManager().getLaunchIntentForPackage("com.yulong.android.security:remote");
        startActivity(openQQ);*/
	}
	//vivo
	private void goVivoMainager(){
		doStartApplicationWithPackageName("com.bairenkeji.icaller");
     /*   Intent openQQ = getPackageManager().getLaunchIntentForPackage("com.vivo.securedaemonservice");
        startActivity(openQQ);*/
	}
	private void doStartApplicationWithPackageName(String packagename) {

		// 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
		PackageInfo packageinfo = null;
		try {
			packageinfo = getPackageManager().getPackageInfo(packagename, 0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		if (packageinfo == null) {
			return;
		}
		// 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(packageinfo.packageName);
		// 通过getPackageManager()的queryIntentActivities方法遍历
		List<ResolveInfo> resolveinfoList = getPackageManager()
				.queryIntentActivities(resolveIntent, 0);
		Log.i("MainActivity","resolveinfoList"+resolveinfoList.size());
		for (int i = 0; i < resolveinfoList.size(); i++) {
			Log.i("MainActivity",resolveinfoList.get(i).activityInfo.packageName+resolveinfoList.get(i).activityInfo.name);
		}
		ResolveInfo resolveinfo = resolveinfoList.iterator().next();
		if (resolveinfo != null) {
			// packagename = 参数packname
			String packageName = resolveinfo.activityInfo.packageName;
			// 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
			String className = resolveinfo.activityInfo.name;
			// LAUNCHER Intent
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			// 设置ComponentName参数1:packagename参数2:MainActivity路径
			ComponentName cn = new ComponentName(packageName, className);
			intent.setComponent(cn);
			try {
				startActivity(intent);
			}catch (Exception e){
				goIntentSetting();
				e.printStackTrace();
			}
		}
	}

	private void showDialogIsCancel(String msg) {
		alertDialog = new AlertDialog.Builder(this).setTitle("温馨提示")
				.setMessage(msg).setIcon(R.drawable.ww)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(isRecording){
							stoprecordVoice();
						}
						finish();


					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).create();
		alertDialog.show();
	}

	@Override
	protected void onStop() {
		super.onStop();
		TimeUtile.cancelTime();
	}
}
