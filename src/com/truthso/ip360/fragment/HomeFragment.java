package com.truthso.ip360.fragment;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.truthso.ip360.activity.LiveRecordImplementationActivity;
import com.truthso.ip360.activity.MainActivity;
import com.truthso.ip360.activity.PhotoPreserved;
import com.truthso.ip360.activity.R;
import com.truthso.ip360.activity.VideoPreserved;
import com.truthso.ip360.bean.AccountStatusBean;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.BaiduLocationUtil;
import com.truthso.ip360.utils.BaiduLocationUtil.locationListener;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.FileSizeUtil;
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

public class HomeFragment extends Fragment implements OnClickListener {
	private static final int CAMERA = 0;
	private static final int CASE_VIDEO = 1;
	private String timeUsed;
	private int timeUsedInsec;
	private MainActivity mActivity;
	private LinearLayout mTakePhoto;
	private LinearLayout mTakeVideo;
	private LinearLayout mRecord;
	private File photo;

	private File photoDir;

	private String date1;
	private String loc;
	private boolean isUseable = false;
	private int sec;
	private int hor;
	private int min;
	private int minTime;
	private String time;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View homeView = inflater.inflate(R.layout.fragment_home, container,
				false);
		// 初始化控件
		initView(homeView);
		getLocation();
		return homeView;
	}

	private void initView(View view) {
		mTakePhoto = (LinearLayout) view.findViewById(R.id.ll_take_photo);
		mTakePhoto.setOnClickListener(this);
		mTakeVideo = (LinearLayout) view.findViewById(R.id.ll_take_video);
		mTakeVideo.setOnClickListener(this);
		mRecord = (LinearLayout) view.findViewById(R.id.ll_record);
		mRecord.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ll_take_photo:// 拍照取证
			//调接口,看是否可以拍照
			//getPort(MyConstants.PHOTOTYPE,0);		
			getLocation();
			photoDir = new File(MyConstants.PHOTO_PATH);
			if (!photoDir.exists()) {
				photoDir.mkdirs();
			}
			String name = "temp.jpg";
			photo = new File(photoDir, name);
			Uri photoUri = Uri.fromFile(photo);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
			startActivityForResult(intent, CAMERA);
			break;
		case R.id.ll_take_video:// 录像取证
			//调接口,看是否可以录像
			getPort(MyConstants.VIDEOTYPE,0);		
			break;
		case R.id.ll_record:// 录音取证
			//调接口,看是否可以录音
			getPort(MyConstants.RECORDTYPE,0);
			break;
		default:
			break;
		}
	}
	/**
	 * 调是否可以拍照的接口
	 */
	private void getPort(final int type,int count) {
		ApiManager.getInstance().getAccountStatus(type, count, new ApiCallback() {
			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
				AccountStatusBean bean = (AccountStatusBean)response;
				if (!CheckUtil.isEmpty(bean)) {
					if (bean.getCode()== 200) {
						if (bean.getDatas().getStatus()== 1) {//0-不能使用；1-可以使用。
							
							switch (type) {
							case MyConstants.PHOTOTYPE:
								getLocation();
								photoDir = new File(MyConstants.PHOTO_PATH);
								if (!photoDir.exists()) {
									photoDir.mkdirs();
								}
								String name = "temp.jpg";
								photo = new File(photoDir, name);
								Uri photoUri = Uri.fromFile(photo);
								Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
								intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivityForResult(intent, CAMERA);
								break;

							case MyConstants.VIDEOTYPE:
								getLocation();
								Intent intent1 = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
								intent1.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
								intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivityForResult(intent1, CASE_VIDEO);
								SimpleDateFormat formatter = new SimpleDateFormat(
										"yyyy年MM月dd日    HH:mm:ss     ");
								Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
								date1 = formatter.format(curDate);
								break;

							case MyConstants.RECORDTYPE:
								getLocation();
								Intent intent2 = new Intent(getActivity(),
										LiveRecordImplementationActivity.class);
								intent2.putExtra("loc", loc);
								intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								addTimeUsed();
								startActivity(intent2);
								break;
							}
						}
					}else{
						Toaster.showToast(getActivity(), bean.getMsg());
					}
				}else{
					Toaster.showToast(getActivity(), "请重试");
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
		Log.i("djj", requestCode+":"+Activity.RESULT_OK+"");
		if (requestCode == CAMERA && resultCode == Activity.RESULT_OK) {
			if (photo.exists()) {
				
				String name = new DateFormat().format("yyyyMMdd_HHmmss",
						Calendar.getInstance(Locale.CHINA)) + ".jpg";
				File newFile = new File(photoDir, name);
				photo.renameTo(newFile);
				String fileSize = FileSizeUtil.getAutoFileOrFilesSize(newFile
						.getAbsolutePath());

				long length=newFile.length();
				
				String date = new DateFormat().format("yyyy年MM月dd日 HH:mm:ss",
						Calendar.getInstance(Locale.CHINA)).toString();
				
				Intent intent = new Intent(getActivity(), PhotoPreserved.class);
				intent.putExtra("path", newFile.getAbsolutePath());
				intent.putExtra("title", name);
				intent.putExtra("size", fileSize);
				intent.putExtra("date", date);
				intent.putExtra("loc", loc);
				intent.putExtra("length", length/8);
				startActivity(intent);
			}
		}
		if (requestCode == CASE_VIDEO && resultCode == Activity.RESULT_OK
				&& null != data) {
			time = getHor() + ":" + getMin() + ":" + getSec();
			if (sec> 0) {
				minTime = hor*60 +min+1;
			}else{
				 minTime= hor*60 +min;
			}
			
			Uri uri = data.getData();
			String filePath = "";
			if (uri == null) {
				return;
			} else {
				Cursor c = getActivity().getContentResolver().query(uri,
						new String[] { MediaStore.MediaColumns.DATA }, null,
						null, null);
				if (c != null && c.moveToFirst()) {
					filePath = c.getString(0);
				}
			}

			Intent intent = new Intent(getActivity(), VideoPreserved.class);
			intent.putExtra("filePath", filePath);
			intent.putExtra("date", date1);
			intent.putExtra("loc", loc);
			intent.putExtra("time", time);
			intent.putExtra("minTime", minTime);
			startActivity(intent);
		}
	}
	private void getLocation(){
		  BaiduLocationUtil.getLocation(getActivity(), new locationListener() {
				
				@Override
				public void location(String s) {
					loc = s;
//					LogUtils.e("位置位置位置位置位置位置切换"+loc);
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

}
