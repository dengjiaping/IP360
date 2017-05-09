package com.truthso.ip360.activity;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lidroid.xutils.util.LogUtils;
import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.bean.PersonalMsgBean;
import com.truthso.ip360.bean.VerUpDateBean;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.dao.UpDownLoadDao;
import com.truthso.ip360.fragment.BaseFragment;
import com.truthso.ip360.fragment.CloudEvidence;
import com.truthso.ip360.fragment.HomeFragment;
import com.truthso.ip360.fragment.NativeEvidence;
import com.truthso.ip360.fragment.PersonalCenter;
import com.truthso.ip360.fragment.TransList;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.ossupload.DownLoadHelper;
import com.truthso.ip360.ossupload.UpLoadManager;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.utils.BaiduLocationUtil;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.DownLoadApkUtli;
import com.truthso.ip360.utils.FragmentTabUtils;
import com.truthso.ip360.utils.NetStatusUtil;
import com.truthso.ip360.utils.FragmentTabUtils.OnRgsExtraCheckedChangedListener;
import com.truthso.ip360.utils.SharePreferenceUtil;
import com.truthso.ip360.view.xrefreshview.MyProgressdialog;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.NetUtils;

public class MainActivity extends FragmentActivity implements
		OnRgsExtraCheckedChangedListener {
	private String	accountBalance;
	private RadioGroup radioGroup;
	private SharedPreferences sp;
	private PersonalCenter personalCenter;
	private RadioButton rb_pc;
	private FragmentTabUtils fragmentTabUtils;
	private String downloadUrl, iVersion;
    private MyWifiReceiver myWifiReceiver;
	private Dialog alertDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();
		checkUpdate();
		checkIsUpDownload();
		/*registWifiStatusListener();*/
	}

	// 初始化控件
	private void initView() {
		List<Fragment> fragmentList = new ArrayList<Fragment>();
		fragmentList.add(new HomeFragment());// 首页（现在取证）
		fragmentList.add(new CloudEvidence());// 云端证据
		//fragmentList.add(new NativeEvidence());// 本地证据
		fragmentList.add(new TransList());// 传输列表
		fragmentList.add(new PersonalCenter());// 个人中心
		radioGroup = (RadioGroup) findViewById(R.id.main_RadioGroup);
		((RadioButton) radioGroup.getChildAt(0)).setChecked(true);
		((RadioButton) radioGroup.getChildAt(0)).setTextColor(getResources().getColor(R.color.jiuhong));
		fragmentTabUtils = new FragmentTabUtils(getSupportFragmentManager(),
				fragmentList, R.id.main_fragment, radioGroup);
		isNeedPay();

		makDirs();//創建所需文件夾
	}

	private void makDirs() {
		File downloadFile = new File(MyConstants.DOWNLOAD_PATH);
		if (!downloadFile.exists()) {
			downloadFile.mkdirs();
		}
		File folder=new File(MyConstants.PHOTO_PATH);
		if(!folder.exists()){
			folder.mkdirs();
		}
	}

	@Override
	public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId,
			int index) {

	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// BaseFragment baseFragment = (BaseFragment)
		// getSupportFragmentManager().findFragmentByTag("fragment");
		BaseFragment currentFragment = (BaseFragment) fragmentTabUtils
				.getCurrentFragment();
		if (!CheckUtil.isEmpty(currentFragment) && currentFragment.onKeyDown(keyCode, event)) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

	}

	@Override
	protected void onDestroy() {
		// BaiduLocationUtil.cancelLocation();
		super.onDestroy();
     //  unregisterReceiver(myWifiReceiver );
//       unbindService()
		DownLoadHelper.getInstance().cancleDownload();
		UpLoadManager.getInstance().cancelAll();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 0:
				// 对话框通知用户升级程序
				showUpdataDialog();
				break;

			case 1:
				// 下载apk失败
//				Toaster.showToast(MainActivity.this, "获取版本号失败");
				break;
			case 2:
				// 下载apk失败
				Toaster.showToast(MainActivity.this, "下载新版本失败");
				break;

			}
		};

	};
	private List<FileInfo> queryUpLoadList;
	private List<FileInfo> queryDownLoadList;

	private int getVersion() {
		try {
			// 通过PackageManager获取安装包信息
			PackageInfo packageInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);

			// 返回版本信息
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			return 0;
		}

	}
	/**
	 * 调接口联网检查更新
	 */
	private void checkUpdate() {
		final String version = getVersion()+"";
		long lastCancleTime= (long) SharePreferenceUtil.getAttributeByKey(MainActivity.this,MyConstants.SP_USER_KEY,"cancleTime",SharePreferenceUtil.VALUE_IS_LONG);
		long currentTimeMillis = System.currentTimeMillis();
		if(currentTimeMillis-lastCancleTime<8*60*60*1000){
            return;
		}
		ApiManager.getInstance().getVerUpDate(version, new ApiCallback() {
			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
				VerUpDateBean bean = (VerUpDateBean) response;
				if (!CheckUtil.isEmpty(bean)) {
					if (bean.getCode() == 200) {
						downloadUrl = bean.getDatas().getApkURl();
						iVersion = bean.getDatas().getiVersionCode();
						if (version.equals(iVersion)) {// 不需要更新
						// Message msg = new Message();
						// msg.what = UPDATA_NONEED;
						// handler.sendMessage(msg);

						} else {// 需要更新//
							Message msg = new Message();
							msg.what = 0;
							handler.sendMessage(msg);
						}
					} else {
//						Toaster.showToast(MainActivity.this, bean.getMsg());
					}
				} else {
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				}

			}

			@Override
			public void onApiResultFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

			}
		});
	}

	/**
	 * * 弹出对话框通知用户更新程序
	 * 
	 * 弹出对话框的步骤： 1.创建alertDialog的builder. 2.要给builder设置属性, 对话框的内容,样式,按钮
	 * 3.通过builder 创建一个对话框 4.对话框show()出来
	 */
	protected void showUpdataDialog() {
		Builder builer = new Builder(this);
		builer.setTitle("版本升级");
		builer.setMessage("检测到最新版本，是否立即更新？");
		// 当点确定按钮时从服务器上下载 新的apk 然后
		builer.setPositiveButton("确定", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				downLoadApk();
			}
		});
		builer.setNegativeButton("取消", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			long cancleTime=System.currentTimeMillis();
				SharePreferenceUtil.saveOrUpdateAttribute(MainActivity.this, MyConstants.SP_USER_KEY,"cancleTime",cancleTime);
			}

		});
		AlertDialog dialog = builer.create();
		dialog.show();
	}

	/**
	 * 从服务器中下载APK
	 */
	protected void downLoadApk() {
		/*final ProgressDialog mDialog; // 进度条对话框
		mDialog = new ProgressDialog(this);
		mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mDialog.setMessage("正在下载更新");
		mDialog.show();*/
		final MyProgressdialog mDialog = new MyProgressdialog(this);

		mDialog.setMessage("正在下载更新");
		mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mDialog.show();

		new Thread() {
			@Override
			public void run() {
				try {
					File file = DownLoadApkUtli.getFileFromServer(downloadUrl,
							mDialog);
					sleep(2000);
					installApk(file);
					mDialog.dismiss(); // 结束掉进度条对话框
				} catch (Exception e) {
					Message msg = new Message();
					msg.what = 2;
					handler.sendMessage(msg);
					e.printStackTrace();
				}
			}
		}.start();
	}

	// 安装apk
	protected void installApk(File file) {
		Intent intent = new Intent();
		// 执行动作
		intent.setAction(Intent.ACTION_VIEW);
		intent.addCategory("android.intent.category.DEFAULT");
		// 执行的数据类型
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	private void checkIsUpDownload() {
		queryUpLoadList = UpDownLoadDao.getDao()
				.queryUpLoadListUnComplete();
		queryDownLoadList = UpDownLoadDao.getDao()
				.queryDownLoadListUnComplete();
		
		if(queryUpLoadList.size()>0||queryDownLoadList.size()>0){
			boolean wifiValid = NetStatusUtil.isWifiValid(this);
			if (wifiValid) {
				upOrDownLoad();
			} else {
				showUploadDialog();
			}
		}		
	}

	private void upOrDownLoad() {

		if (queryUpLoadList.size() > 0) {
			for (int i = 0; i < queryUpLoadList.size(); i++) {
				UpLoadManager.getInstance().resuambleUploadUnCaseNet(
						queryUpLoadList.get(i));
			}
		}
		if (  queryDownLoadList.size() > 0) {
			for (int i = 0; i < queryDownLoadList.size(); i++) {
				DownLoadHelper.getInstance().downloadFileUnCaseNet(
						queryDownLoadList.get(i));
			}
		}

	}

	private void showUploadDialog() {
		AlertDialog ad = new AlertDialog.Builder(this).setTitle("提示")
				.setMessage("网络不是WiFi状态，是否继续上传/下载?")
				.setNegativeButton("否", null)
				.setPositiveButton("是", new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						upOrDownLoad();
					}
				}).show();
		ad.setCancelable(false);
	}

	private void registWifiStatusListener(){
		IntentFilter filter = new IntentFilter();
		//filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		//filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

		myWifiReceiver = new MyWifiReceiver();
		registerReceiver(myWifiReceiver, filter);

	}


	public class MyWifiReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

				ConnectivityManager cm=	(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo info = cm.getActiveNetworkInfo();
				if(info != null && info.isAvailable()) {

					//网络连接
					String name = info.getTypeName();

					if(info.getType()==ConnectivityManager.TYPE_WIFI){
						//WiFi网络
                     if(info.isConnected()){
						 Log.i("djj","wifi");
					 }

					}else if(info.getType()==ConnectivityManager.TYPE_ETHERNET){
						//有线网络

					}else if(info.getType()==ConnectivityManager.TYPE_MOBILE){
						//3g网络

					}
				} else {
					//网络断开

				}
			}
		}
	}

	/**
	 * 是否需要充值
	 */
	public void isNeedPay() {
		ApiManager.getInstance().getPersonalMsg(new ApiCallback() {
			@Override
			public void onApiResult(int errorCode, String message, BaseHttpResponse response) {
				PersonalMsgBean	bean = (PersonalMsgBean) response;

					if(!CheckUtil.isEmpty(bean)){
						if(bean.getCode()==200){
							int balance = bean.getDatas().getAccountBalance();
							double account = balance*0.01;
							DecimalFormat dec = new DecimalFormat("0.00");
							accountBalance = "￥"+ dec.format(account);
								if (balance<0){//欠费
								showDialog();
							}
						}
					}
			}

			@Override
			public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

			}
		});
	}
	private void showDialog() {
		alertDialog = new AlertDialog.Builder(this).setTitle("温馨提示")
				.setMessage("您的账户已欠费，是否现在充值？").setIcon(R.drawable.ww)
				.setPositiveButton("去充值", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//跳转充值页面
						Intent intent = new Intent(MainActivity.this,AccountPayActivity.class);
						intent.putExtra("accountBalance",accountBalance);
						startActivity(intent);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						alertDialog.dismiss();
					}
				}).create();
		alertDialog.show();
	}

}