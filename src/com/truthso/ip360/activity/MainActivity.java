package com.truthso.ip360.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lidroid.xutils.util.LogUtils;
import com.truthso.ip360.bean.VerUpDateBean;
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

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.NetUtils;

public class MainActivity extends FragmentActivity implements
		OnRgsExtraCheckedChangedListener {
	private RadioGroup radioGroup;
	private SharedPreferences sp;
	private PersonalCenter personalCenter;
	private RadioButton rb_pc;
	private FragmentTabUtils fragmentTabUtils;
	private String downloadUrl, iVersion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_main);
		initView();
		checkUpdate();
		checkIsUpDownload();
	}

	// 初始化控件
	private void initView() {
		List<Fragment> fragmentList = new ArrayList<Fragment>();
		fragmentList.add(new HomeFragment());// 首页（现在取证）
		fragmentList.add(new CloudEvidence());// 云端证据
		fragmentList.add(new NativeEvidence());// 本地证据
		fragmentList.add(new TransList());// 传输列表
		fragmentList.add(new PersonalCenter());// 个人中心
		radioGroup = (RadioGroup) findViewById(R.id.main_RadioGroup);
		((RadioButton) radioGroup.getChildAt(0)).setChecked(true);
		fragmentTabUtils = new FragmentTabUtils(getSupportFragmentManager(),
				fragmentList, R.id.main_fragment, radioGroup);

	}

	@Override
	public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId,
			int index) {

	}

	public void replaceFragment(Fragment argFragment, String argName) {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_fragment, argFragment, argName).commit();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// BaseFragment baseFragment = (BaseFragment)
		// getSupportFragmentManager().findFragmentByTag("fragment");
		BaseFragment currentFragment = (BaseFragment) fragmentTabUtils
				.getCurrentFragment();
		Log.i("djj", CheckUtil.isEmpty(currentFragment) + "2");
		if (!CheckUtil.isEmpty(currentFragment)
				&& currentFragment.onKeyDown(keyCode, event)) {
			Log.i("djj", "back");
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
				Toaster.showToast(MainActivity.this, "获取版本号失败");
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
		final String version = getVersion();
		// LogUtils.e(version+"本地的版本号");
		ApiManager.getInstance().getVerUpDate(version, new ApiCallback() {
			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
				VerUpDateBean bean = (VerUpDateBean) response;
				if (!CheckUtil.isEmpty(bean)) {

					if (bean.getCode() == 200) {
						downloadUrl = bean.getDatas().getApkURl();
						iVersion = bean.getDatas().getiVersionCode();
						// LogUtils.e(iVersion+"服务器返回的版本号");
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
						Toaster.showToast(MainActivity.this, bean.getMsg());
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

			}

		});
		AlertDialog dialog = builer.create();
		dialog.show();
	}

	/**
	 * 从服务器中下载APK
	 */
	protected void downLoadApk() {
		final ProgressDialog pd; // 进度条对话框
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在下载更新");
		pd.show();
		new Thread() {
			@Override
			public void run() {
				try {
					File file = DownLoadApkUtli.getFileFromServer(downloadUrl,
							pd);
					sleep(3000);
					installApk(file);
					pd.dismiss(); // 结束掉进度条对话框
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
		// 执行的数据类型
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	private void checkIsUpDownload() {
		queryUpLoadList = UpDownLoadDao.getDao()
				.queryUpLoadList();
		queryDownLoadList = UpDownLoadDao.getDao()
				.queryDownLoadList();
		
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
		 Log.i("djj", "size"+queryUpLoadList.size());
		if (queryUpLoadList.size() > 0) {
			for (int i = 0; i < queryUpLoadList.size(); i++) {
				UpLoadManager.getInstance().resuambleUploadUnCaseNet(
						queryUpLoadList.get(i));
			       Log.i("djj", "objeckey"+queryUpLoadList.get(i).getObjectKey());
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

}