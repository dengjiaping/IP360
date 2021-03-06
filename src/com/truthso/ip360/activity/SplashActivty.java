package com.truthso.ip360.activity;

import java.io.File;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.bean.VerUpDateBean;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.updownload.DownLoadManager;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.DownLoadApkUtli;
import com.truthso.ip360.utils.SharePreferenceUtil;

import cz.msebera.android.httpclient.Header;

/**
 * 
 * 闪屏页面
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-6-17下午3:06:14
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class SplashActivty extends Activity {
	private boolean isFirstOpen = false;
	private String token;
	private Context ctx;
	private String downloadUrl;
	private String iVersion;
	private final int UPDATA_NONEED = 0;
	private final int UPDATA_CLIENT = 1;
	private final int GET_UNDATAINFO_ERROR = 2;
	private final int SDCARD_NOMOUNTED = 3;
	private final int DOWN_ERROR = 4;
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		ctx = this;

		isFirstOpen = (Boolean) SharePreferenceUtil.getAttributeByKey(
				SplashActivty.this, MyConstants.APP_KEY,
				MyConstants.APP_ISFIRST_IN,
				SharePreferenceUtil.VALUE_IS_BOOLEAN);

		getVersion();
		token = MyApplication.getInstance().getTokenId();
//		// 渐变动画 从0到1
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.f, 1.f);
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rl_root);
		alphaAnimation.setDuration(500);
		relativeLayout.startAnimation(alphaAnimation);
		//是否跳转到首页
//		enterHome();
		handler.sendEmptyMessageDelayed(99,2000);
//		// 检查联网更新
	//	checkUpdate();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 99:
				enterHome();
				break;

			default:
				break;
			}
			};

	};

	private String getVersion() {
		try {
			// 通过PackageManager获取安装包信息
			PackageInfo packageInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			// 返回版本信息
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			return "";
		}
	}

	/**
	 * 调接口联网检查更新
	 */
	private void checkUpdate() {
		String version = getVersion();
		ApiManager.getInstance().getVerUpDate(version, new ApiCallback() {
			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
				VerUpDateBean bean = (VerUpDateBean) response;
				if (!CheckUtil.isEmpty(bean)) {

					if (bean.getCode() == 200) {
						downloadUrl = bean.getDatas().getApkURl();
						iVersion = bean.getDatas().getiVersionCode();
						if ("version".equals(iVersion)) {// 不需要更新
//							Message msg = new Message();
//							msg.what = UPDATA_NONEED;
//							handler.sendMessage(msg);

							enterHome();
						} else {// 需要更新
//							Message msg = new Message();
//							msg.what = UPDATA_CLIENT;
//							handler.sendMessage(msg);
						}
					} else {
						Toaster.showToast(ctx, bean.getMsg());
					}
				} else {
//					Message msg = new Message();
//					msg.what = UPDATA_NONEED;
//					handler.sendMessage(msg);
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
				//downLoadApk();
			}
		});
		builer.setNegativeButton("取消", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				enterHome();
				
			}

		
		});
		AlertDialog dialog = builer.create();
		dialog.show();
	}



	// 安装apk
	protected void installApk(File file) {
		Intent intent = new Intent();
		// 执行动作
		intent.setAction(Intent.ACTION_VIEW);
		// 执行的数据类型
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}
	/**
	 * 是否跳回主页
	 */
	private void enterHome() {
		if (isFirstOpen) {
			if (CheckUtil.isEmpty(token)) {
				// 进登录界面
				intent = new Intent(ctx, LoginActivity.class);
			} else {
				intent = new Intent(ctx, MainActivity.class);
			}
		} else {
			// 第一次启动先进入引导页
			intent = new Intent(ctx, GuideActivity.class);
			SharePreferenceUtil.saveOrUpdateAttribute(this, MyConstants.SP_USER_KEY,MyConstants.ISWIFI,true);
		}
		startActivity(intent);
		finish();
	}
}
