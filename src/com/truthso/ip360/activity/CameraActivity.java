package com.truthso.ip360.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.truthso.ip360.adapter.CommonAdapter;
import com.truthso.ip360.bean.CommonBean;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.GroupDao;
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.utils.FileSizeUtil;
import com.truthso.ip360.viewholder.ViewHolder;

/**
 * 拍照页面的activity
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-6-6下午8:36:40
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class CameraActivity extends CommonMediaActivity implements OnClickListener {
	private static final int CAMERA = 0;
	private List<DbBean> mDatas;
	// private CommonBean commonBean;
	private Button takephoto;
	private String photoPath;
	private ListView listView;
	private CommonAdapter<DbBean> adapter;

	@Override
	public void initData() {
		photoPath=Environment.getExternalStorageDirectory().getPath()+"/IP360/MyPhoto";
		mDatas=GroupDao.getInstance(this).queryByFileType(0);
		adapter=new CommonAdapter<DbBean>(this,mDatas,R.layout.item_photos_video_list) {
			
			@Override
			public void convert(ViewHolder helper, DbBean item, int position) {
				item = mDatas.get(position);
				helper.setText(R.id.tv_filename, item.getTitle());
				helper.setText(R.id.tv_time, item.getCreateTime());
//				String fileType = item.getFileType();
				helper.setImageBitmap(R.id.iv_image, BitmapFactory.decodeFile(item.getResourceUrl()));
			}
		};
	}

	@Override
	public void initView() {
		takephoto=(Button) findViewById(R.id.btn_takephoto);
		takephoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, CAMERA);
			}
		});
	}

	@Override
	public View instantiateView(int position) {
        if (position == 0) {
        	listView = new ListView(CameraActivity.this);
    		listView.setAdapter(adapter);
		}
		
        return listView;
	}



	/**
	 * 调用系统的拍照跟摄像模块用到的回调方法
	 * 
	 * @author wsx_summer
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == CAMERA && resultCode == Activity.RESULT_OK
				&& null != data) {
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
				return;
			}
			String name = new DateFormat().format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA))
					+ ".jpg";

			Bundle bundle = data.getExtras();
			// 获取相机返回的数据，并转换为Bitmap图片格式
			Bitmap bitmap = (Bitmap) bundle.get("data");

			File file = new File(photoPath);
			if (!file.exists()) {
				file.mkdirs();// 创建文件夹
			}
			String fileName = photoPath + File.separator + name;

			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy年MM月dd日    HH:mm:ss     ");
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			String date = formatter.format(curDate);

			try {
				BufferedOutputStream bos = new BufferedOutputStream(
						new FileOutputStream(fileName));
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
				bos.flush();
				bos.close();

				String fileSize = FileSizeUtil.getAutoFileOrFilesSize(fileName);
				saveData(date, fileSize, name, fileName);
			} catch (IOException e) {
				e.printStackTrace();

			}

			mDatas.clear();
			
			mDatas.addAll(GroupDao.getInstance(this).queryByFileType(0));
			adapter.notifyDataSetChanged();
		}

	}

	/**
	 * 将数据保存到数据库
	 */
	private void saveData(String date, String fileSize, String name, String path) {
		SqlDao sqlDao = new SqlDao(this);
		DbBean dbBean = new DbBean();
		dbBean.setType(MyConstants.PHOTO);// 文件类别
		dbBean.setCreateTime(date);// 生成时间
		dbBean.setFileSize(fileSize);// 文件大小
		// dbBean.setJsonObject(jsonObject);
		dbBean.setResourceUrl(path);
		dbBean.setTitle(name);// 照片名称
		sqlDao.save(dbBean, "IP360_media_detail");// 存入数据库
	}

}