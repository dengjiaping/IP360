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
import com.truthso.ip360.utils.FileSizeUtil;

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

	private MainActivity mActivity;
	private LinearLayout mTakePhoto;
	private LinearLayout mTakeVideo;
	private LinearLayout mRecord;
	private File photo;

	private File photoDir;

	private String date1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View homeView = inflater.inflate(R.layout.fragment_home, container,
				false);
		// 初始化控件
		initView(homeView);

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
			Intent intent1 = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			intent1.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			startActivityForResult(intent1, CASE_VIDEO);
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy年MM月dd日    HH:mm:ss     ");
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			date1 = formatter.format(curDate);
			break;
		case R.id.ll_record:// 录音取证
			Intent intent2 = new Intent(getActivity(),
					LiveRecordImplementationActivity.class);
			startActivity(intent2);
			break;
		default:
			break;
		}
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
				String name = new DateFormat().format("yyyyMMdd_hhmmss",
						Calendar.getInstance(Locale.CHINA)) + ".jpg";
				File newFile = new File(photoDir, name);
				photo.renameTo(newFile);
				String fileSize = FileSizeUtil.getAutoFileOrFilesSize(newFile
						.getAbsolutePath());
				String date = new DateFormat().format("yyyy年MM月dd日 hh:mm:ss",
						Calendar.getInstance(Locale.CHINA)).toString();
				Intent intent = new Intent(getActivity(), PhotoPreserved.class);
				intent.putExtra("path", newFile.getAbsolutePath());
				intent.putExtra("title", name);
				intent.putExtra("size", fileSize);
				intent.putExtra("date", date);
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
			startActivity(intent);
		}
	}

}
