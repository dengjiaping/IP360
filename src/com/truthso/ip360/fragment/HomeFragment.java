package com.truthso.ip360.fragment;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
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
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.utils.BaiduLocationUtil;
import com.truthso.ip360.utils.FileSizeUtil;
import com.truthso.ip360.utils.BaiduLocationUtil.locationListener;
import com.truthso.ip360.view.xrefreshview.LogUtils;

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
			getPortPhoto();
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
			getLocation();
			Intent intent1 = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			intent1.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			startActivityForResult(intent1, CASE_VIDEO);
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy年MM月dd日    HH:mm:ss     ");
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			date1 = formatter.format(curDate);
			break;
		case R.id.ll_record:// 录音取证
			getLocation();
			Intent intent2 = new Intent(getActivity(),
					LiveRecordImplementationActivity.class);
			intent2.putExtra("loc", loc);
			addTimeUsed();
			startActivity(intent2);
			break;
		default:
			break;
		}
	}
	/**
	 * 调是否可以拍照的接口
	 */
	private void getPortPhoto() {
//		
//	
//		ApiManager.getInstance().getAccountStatus(MyConstants.PHOTOTYPE ,null, new ApiCallback() {
//			
//			@Override
//			public void onApiResult(int errorCode, String message,
//					BaseHttpResponse response) {
//				
//			}
//		});
//		
		
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
			if (photo.exists()) {
				
				String name = new DateFormat().format("yyyyMMdd_HHmmss",
						Calendar.getInstance(Locale.CHINA)) + ".jpg";
				File newFile = new File(photoDir, name);
				photo.renameTo(newFile);
				String fileSize = FileSizeUtil.getAutoFileOrFilesSize(newFile
						.getAbsolutePath());
				String date = new DateFormat().format("yyyy年MM月dd日 HH:mm:ss",
						Calendar.getInstance(Locale.CHINA)).toString();
				
				Intent intent = new Intent(getActivity(), PhotoPreserved.class);
				intent.putExtra("path", newFile.getAbsolutePath());
				intent.putExtra("title", name);
				intent.putExtra("size", fileSize);
				intent.putExtra("date", date);
				intent.putExtra("loc", loc);
				startActivity(intent);
			}
		}
		if (requestCode == CASE_VIDEO && resultCode == Activity.RESULT_OK
				&& null != data) {
			String time = getHor() + ":" + getMin() + ":" + getSec();
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
			startActivity(intent);
		}
	}
	private void getLocation(){
		  BaiduLocationUtil.getLocation(getActivity(), new locationListener() {
				
				@Override
				public void location(String s) {
					loc = s;
					LogUtils.e("位置位置位置位置位置位置切换"+loc);
				}
			});
	}
	public void addTimeUsed() {
		timeUsedInsec = timeUsedInsec + 1;
		timeUsed = this.getMin() + ":" + this.getSec();
	}

	public CharSequence getHor() {
		int hor = timeUsedInsec / 3600;
		return hor < 10 ? "0" + hor : String.valueOf(hor);

	}

	public CharSequence getMin() {
		int min = timeUsedInsec / 60;
		return min < 10 ? "0" + min : String.valueOf(min);

	}

	public CharSequence getSec() {
		int sec = timeUsedInsec % 60;
		return sec < 10 ? "0" + sec : String.valueOf(sec);
	}

}
