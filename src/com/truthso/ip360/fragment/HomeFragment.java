package com.truthso.ip360.fragment;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
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
import android.database.Cursor;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.truthso.ip360.activity.AboutUsAcctivity;
import com.truthso.ip360.activity.AccountPayActivity;
import com.truthso.ip360.activity.ChargeRulerActivity;
import com.truthso.ip360.activity.LiveRecordImplementationActivity;
import com.truthso.ip360.activity.MainActivity;
import com.truthso.ip360.activity.PhotoPreserved;


import com.truthso.ip360.activity.R;
import com.truthso.ip360.activity.VideoPreserved;
import com.truthso.ip360.adapter.ImagePagerAdapter;
import com.truthso.ip360.bean.AccountStatusBean;
import com.truthso.ip360.bean.PictureList;
import com.truthso.ip360.bean.ShowPictureBean;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.BaiduLocationUtil;
import com.truthso.ip360.utils.BaiduLocationUtil.locationListener;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.FileSizeUtil;
import com.truthso.ip360.view.CircleFlowIndicator;
import com.truthso.ip360.view.ViewFlow;
import com.truthso.ip360.view.xrefreshview.LogUtils;

import cz.msebera.android.httpclient.Header;



/**
 * @despriction :首页
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-7-21下午12:54:01
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class HomeFragment extends BaseFragment implements OnClickListener {
	private static final int CAMERA = 0;
	private static final int CASE_VIDEO = 1;
	private String timeUsed;
	private Dialog alertDialog;
	private int timeUsedInsec;
	private MainActivity mActivity;
	private RelativeLayout mTakePhoto,mTakeVideo, mRecord;
	private TextView tv_ruler;
	private File photo;
	private double lat,longti;
	private File photoDir;
	private String date1;
	private String loc;
	private boolean isUseable = false;
	private int sec;
	private int hor;
	private int min;
	private int minTime;
	private String time;
	private String title;
	private String size;
	private double video_fileSize_B;
	private long duration;
	private CircleFlowIndicator mFlowIndicator;
	private ViewFlow mViewFlow;
	private ArrayList<String> imageUrlList = new ArrayList<String>();
	private String accountBalance;
	@Override
	protected void initView(View view, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mTakePhoto = (RelativeLayout) view.findViewById(R.id.ll_take_photo);
		mTakePhoto.setOnClickListener(this);
		mTakeVideo = (RelativeLayout) view.findViewById(R.id.ll_take_video);
		mTakeVideo.setOnClickListener(this);
		mRecord = (RelativeLayout) view.findViewById(R.id.ll_record);
		mRecord.setOnClickListener(this);

		mViewFlow = (ViewFlow)view. findViewById(R.id.viewflow);
		mFlowIndicator = (CircleFlowIndicator) view.findViewById(R.id.viewflowindic);

		tv_ruler = (TextView) view.findViewById(R.id.tv_ruler);
		tv_ruler.setOnClickListener(this);
		//获取轮播图
		getFlowViewData();
		//进来就定位
		getLocation();
	}

	private void getFlowViewData() {
		ApiManager.getInstance().getShowPicture(new ApiCallback() {
			@Override
			public void onApiResult(int errorCode, String message, BaseHttpResponse response) {
				ShowPictureBean bean= (ShowPictureBean) response;
				if(bean.getCode()==200&&bean.getDatas()!=null){
					List<PictureList> pictureList = bean.getDatas().getPictureList();
					for (int j=0;j<pictureList.size();j++){
						imageUrlList.add(pictureList.get(j).getPictureUrl());
					}
						initBanner(imageUrlList);
					}else{
					Toast.makeText(getActivity(),"数据获取失败",Toast.LENGTH_SHORT).show();
				}

			}

			@Override
			public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

			}
		});
	}

	@Override
	public int setViewId() {
		return R.layout.fragment_home;
	}

	@Override
	protected void initData() {

	}
	private void initBanner(ArrayList<String> imageUrlList) {
//		mFlowIndicator.setFillColor(0xFFFFFFFF);
//		mFlowIndicator.setStrokeColor(0xFFE4848F);
		mViewFlow.setAdapter(new ImagePagerAdapter(getActivity(), imageUrlList).setInfiniteLoop(true));
		mViewFlow.setmSideBuffer(imageUrlList.size()); // 实际图片张数，
		if (imageUrlList.size()>1){//说明不止一张，开始播放，一张就不播
			mFlowIndicator.setFillColor(0xFFFFFFFF);
			mFlowIndicator.setStrokeColor(0xFFE4848F);
			mViewFlow.setFlowIndicator(mFlowIndicator);
			mViewFlow.setTimeSpan(4500);
			mViewFlow.setSelection(imageUrlList.size() * 1000); // 设置初始位置
			mViewFlow.startAutoFlowTimer(); // 启动自动播放
			mFlowIndicator.setFillColor(0xFFFFFFFF);
		   mFlowIndicator.setStrokeColor(0xFFE4848F);
			mViewFlow.setScanScroll(true);
		}else{
			mViewFlow.setScanScroll(false);
		}



	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ll_take_photo:// 拍照取证
			getLocation();
			//调接口,看是否可以拍照
			getPort(MyConstants.PHOTOTYPE,1);
			break;
		case R.id.ll_take_video:// 录像取证
			getLocation();
			//调接口,看是否可以录像
			getPort(MyConstants.VIDEOTYPE,1);
			break;
		case R.id.ll_record:// 录音取证
			getLocation();
			//调接口,看是否可以录音
			getPort(MyConstants.RECORDTYPE,1);
			break;
			case R.id.tv_ruler://计价规则
				Intent intent = new Intent(getActivity(), ChargeRulerActivity.class);
				startActivity(intent);
				break;
		default:
			break;
		}
	}
	/**
	 * 调是否可以拍照的接口
	 */
	private void getPort(final int type,int count) {
		showProgress("加载中...");
		 requestHandle = ApiManager.getInstance().getAccountStatus(type, count, new ApiCallback() {
			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
				hideProgress();
				AccountStatusBean bean = (AccountStatusBean)response;
				if (!CheckUtil.isEmpty(bean)) {
					if (bean.getCode()== 200) {
						if (bean.getDatas().getStatus()== 1) {//0-不能使用；1-可以使用。
							switch (type) {
							case MyConstants.PHOTOTYPE:
//								boolean isHasPremiss = checkWriteExternalPermission("android.permission.CAMERA");
								boolean isHasPremiss =cameraIsCanUse();
								if (isHasPremiss){//有权限
									photoDir = new File(MyConstants.PHOTO_PATH);
									if (!photoDir.exists()) {
										photoDir.mkdirs();
									}
									String name = "temp.jpg";
									photo = new File(photoDir, name);
									Uri photoUri = Uri.fromFile(photo);
									Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
									intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									startActivityForResult(intent, CAMERA);
								}else{//没权限
									PreshowDialog("您没有开启拍照权限！");
								}

								break;

							case MyConstants.VIDEOTYPE:
//								boolean isHasPremiss_video = checkWriteExternalPermission("android.permission.CAMERA");
								boolean isHasPremiss_video =cameraIsCanUse();
								if (isHasPremiss_video){//用户给了有权限
									Intent intent1 = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
									intent1.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//								intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									startActivityForResult(intent1, CASE_VIDEO);
									SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
									date1 = formatter.format(curDate);
								}else{//用户没给权限
									PreshowDialog("您没有开启录像权限！");
								}
								break;
							case MyConstants.RECORDTYPE:
								Intent intent2 = new Intent(getActivity(), LiveRecordImplementationActivity.class);
								intent2.putExtra("loc", loc);
//								intent2.putExtra("longlat",longti+","+lat);
								intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 //								addTimeUsed();
								startActivity(intent2);
								break;
							}
						}else if(bean.getDatas().getStatus()== 0){//不可用
//							Toaster.showToast(getActivity(), "余额不足，请充值！");
						int account = bean.getDatas().getAccountBalance();//余额


							if (account == 0){
								accountBalance = "￥0.00";
							}else{
								accountBalance = "￥"+account*0.01;
							}
						showDialog("余额不足，请充值！");
						}
					}else{
						Toaster.showToast(getActivity(), bean.getMsg());
					}
				}else{
					Toaster.showToast(getActivity(), "加载失败，请重试！");
				}
			}
			@Override
			public void onApiResultFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				
			}
		});
	}

	/**
	 * 调用系统的拍照跟摄像模块用到的回调方法
	 * 
	 * @author wsx_summer
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
				
		if (requestCode == CAMERA && resultCode == Activity.RESULT_OK) {

				 if (!CheckUtil.isEmpty(photo)&&photo.exists()){

				String name = new DateFormat().format("yyyyMMdd_HHmmss",
						Calendar.getInstance(Locale.CHINA)) + ".jpg";
				File newFile = new File(photoDir, name);
				photo.renameTo(newFile);
				String fileSize = FileSizeUtil.getAutoFileOrFilesSize(newFile
						.getAbsolutePath());
				

				long length=newFile.length();
				double fileSize_B = FileSizeUtil.FormetFileSize(length, FileSizeUtil.SIZETYPE_B);
				String date = new DateFormat().format("yyyy-MM-dd HH:mm:ss",Calendar.getInstance(Locale.CHINA)).toString();
				
				Intent intent = new Intent(getActivity(), PhotoPreserved.class);
				intent.putExtra("path", newFile.getAbsolutePath());
				intent.putExtra("title", name);
				intent.putExtra("size", fileSize);
				intent.putExtra("date", date);
//				intent.putExtra("loc", loc);
				intent.putExtra("fileSize_B", fileSize_B);
				intent.putExtra("loc",loc);
				intent.putExtra("longlat",longti+","+lat);
				startActivity(intent);
			}
		}
		if (requestCode == CASE_VIDEO && resultCode == Activity.RESULT_OK
				&& null != data) {
			Uri uri = data.getData();
			String filePath = "";
			
			if (uri == null) {
				return;
			} else {
				Cursor c = getActivity().getContentResolver().query(uri,new String[] { MediaStore.MediaColumns.DATA,MediaStore.MediaColumns.SIZE,MediaStore.MediaColumns.TITLE ,MediaStore.Video.Media.DURATION}, null,null,null);
//				Cursor c = getActivity().getContentResolver().query(uri,new String[] { MediaStore.MediaColumns.DATA}, null,null,null);
					if (c != null && c.moveToFirst()) {
					filePath = c.getString(0);
					size = c.getString(1);
					title =c.getString(2);
					String dur =c.getString(3);
					duration =Long.parseLong(dur);
//					filePath = c.getString(c.getColumnIndex(MediaStore.Video.Media.DATA));
//				    size=c.getString(c.getColumnIndex(MediaStore.Video.Media.SIZE));
//					size=c.getString(c.getColumnIndex(MediaStore.MediaColumns.SIZE));
//				    title = c.getString(c.getColumnIndex(MediaStore.MediaColumns.TITLE));
				    //总时长 ms
//				    duration = c.getLong(c.getColumnIndex(MediaStore.Video.Media.DURATION));

				    timeUsedInsec = (int) (duration/1000);//秒
					time = getHor() + ":" + getMin() + ":" + getSec();
					if (sec> 0) {
						minTime = hor*60 +min+1;
					}else{
						 minTime= hor*60 +min;
					}
					c.close();
				File file = new File(filePath);
				long length = file.length();
				video_fileSize_B = FileSizeUtil.FormetFileSize(length, FileSizeUtil.SIZETYPE_B);
				}
			}

			Intent intent = new Intent(getActivity(), VideoPreserved.class);
			intent.putExtra("filePath", filePath);
			intent.putExtra("date", date1);
			intent.putExtra("loc", loc);
			intent.putExtra("time", time);
			intent.putExtra("minTime", minTime);
			intent.putExtra("size",size);
			intent.putExtra("video_fileSize_B", video_fileSize_B);
			intent.putExtra("title", title);
			intent.putExtra("loc",loc);
			intent.putExtra("longlat",longti+","+lat);
			startActivity(intent);
		}
	}
	private void getLocation(){
		  BaiduLocationUtil.getLocation(getActivity(), new locationListener() {

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


	/**
	 * 弹出框
	 * @param msg
	 */
	private void showDialog(String msg) {
		alertDialog = new AlertDialog.Builder(getActivity()).setTitle("温馨提示")
				.setMessage(msg).setIcon(R.drawable.ww)
				.setPositiveButton("去充值", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//跳转到充值
				Intent intent = new Intent(getActivity(), AccountPayActivity.class);
		               intent.putExtra("accountBalance",accountBalance);
				startActivity(intent);
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).create();
		alertDialog.show();
	}


	/**
	 *  用户是否开了权限
	 *  返回true 表示可以使用  返回false表示不可以使用
	 */
	public boolean cameraIsCanUse() {
		boolean isCanUse = true;
		Camera mCamera = null;
		try {
			mCamera = Camera.open();
			Camera.Parameters mParameters = mCamera.getParameters(); //针对魅族手机
			mCamera.setParameters(mParameters);
		} catch (Exception e) {
			isCanUse = false;
		}

		if (mCamera != null) {
			try {
				mCamera.release();
			} catch (Exception e) {
				e.printStackTrace();
				return isCanUse;
			}
		}
		return isCanUse;
	}

	/**
	 * 获取手机类型
	 */
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
			getAppDetailSettingIntent(getActivity());
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
			Toaster.showToast(getActivity(), "跳转失败");
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
			localIntent.putExtra("extra_pkgname",getActivity().getPackageName());
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
		Uri uri = Uri.fromParts("package",getActivity().getPackageName(), null);
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
			packageinfo = getActivity().getPackageManager().getPackageInfo(packagename, 0);
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
		List<ResolveInfo> resolveinfoList = getActivity().getPackageManager()
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
	/**
	 * 跳转到权限设置界面
	 */
	private void getAppDetailSettingIntent(Context context){
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if(Build.VERSION.SDK_INT >= 9){
			intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
			intent.setData(Uri.fromParts("package", getActivity().getPackageName(), null));
		} else if(Build.VERSION.SDK_INT <= 8){
			intent.setAction(Intent.ACTION_VIEW);
			intent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
			intent.putExtra("com.android.settings.ApplicationPkgName", getActivity().getPackageName());
		}
		startActivity(intent);
	}

	/**
	 * 弹框提醒没权限
	 */
	private void PreshowDialog(String msg) {
		alertDialog = new AlertDialog.Builder(getActivity()).setTitle("温馨提示").setCancelable(false)
				.setMessage(msg).setIcon(R.drawable.ww)
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
					}
				}).create();
		alertDialog.show();
	}
}
