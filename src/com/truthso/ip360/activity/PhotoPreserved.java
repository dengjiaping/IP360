package com.truthso.ip360.activity;

import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.service.GpsService;
import com.truthso.ip360.utils.BaiduLocationUtil;
import com.truthso.ip360.utils.BaiduLocationUtil.locationListener;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.ImageLoader;
import com.truthso.ip360.utils.ImageLoaderUtil;
import com.truthso.ip360.view.xrefreshview.LogUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
/**
 * @despriction :照片保全的界面
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-7-21下午5:18:47
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class PhotoPreserved extends BaseActivity implements OnClickListener {
	private Button btn_cancel,btn_preserved;
	private ImageView iv_photo;
	private String path,title,size,date;
	
	@Override
	public void initData() {
		path =getIntent().getStringExtra("path");
		title =getIntent().getStringExtra("title");
		size =getIntent().getStringExtra("size");
		date =getIntent().getStringExtra("date");
	}
	
	@Override
	public void initView() {
		btn_cancel=(Button) findViewById(R.id.btn_cancel);
		btn_preserved=(Button) findViewById(R.id.btn_preserved);
		btn_cancel.setOnClickListener(this);
		btn_preserved.setOnClickListener(this);
		iv_photo=(ImageView) findViewById(R.id.iv_photo);
		Bitmap decodeFile = BitmapFactory.decodeFile(path);
	    ImageLoaderUtil.displayFromSDCardopt(path, iv_photo, null);
	}
	
	@Override
	public int setLayout() {
		return R.layout.activity_photopreserved;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel:
			finish();
			break;
		case R.id.btn_preserved:
			saveToDb();
			break;
		default:
			break;
		}
		
	}

	//保存照片信息到数据库
	private void saveToDb() {
		final DbBean dbBean =new DbBean();
    BaiduLocationUtil.getLocation(getApplicationContext(), new locationListener() {
			
			@Override
			public void location(String s) {
				dbBean.setLocation(s);
			}
		});
		
		dbBean.setTitle(title);
		dbBean.setCreateTime(date);
		dbBean.setResourceUrl(path);
		dbBean.setType(MyConstants.PHOTO);
		dbBean.setFileSize(size);
		SqlDao.getSQLiteOpenHelper(this).save(dbBean, MyConstants.TABLE_MEDIA_DETAIL);
		finish();
	}
	
	//保存到云端
	private void saveToNet(){
		//
	}

	@Override
	public String setTitle() {
		// TODO Auto-generated method stub
		return "拍照完成";
	}

}
