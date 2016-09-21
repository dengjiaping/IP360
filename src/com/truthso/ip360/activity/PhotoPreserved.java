package com.truthso.ip360.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.utils.BaiduLocationUtil;
import com.truthso.ip360.utils.BaiduLocationUtil.locationListener;
import com.truthso.ip360.utils.ImageLoaderUtil;
import com.truthso.ip360.view.xrefreshview.LogUtils;
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
	private String path,title,size,date,loc;
	private TextView tv_filename,tv_loc,tv_date,tv_filesize;
	@Override
	public void initData() {
		path =getIntent().getStringExtra("path");
		title =getIntent().getStringExtra("title");
		size =getIntent().getStringExtra("size");
		date =getIntent().getStringExtra("date");
		loc =getIntent().getStringExtra("loc");
		getLocation();
	}
	
	@Override
	public void initView() {
		btn_cancel=(Button) findViewById(R.id.btn_cancel);
		btn_preserved=(Button) findViewById(R.id.btn_preserved);
		tv_filename = (TextView) findViewById(R.id.tv_filename);
		tv_filename.setText(title);
		tv_loc = (TextView) findViewById(R.id.tv_loc);
		tv_loc.setText(loc);
		tv_date = (TextView) findViewById(R.id.tv_date);
		tv_date.setText(date);
		tv_filesize = (TextView) findViewById(R.id.tv_filesize);
		tv_filesize.setText(size);
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
private void getLocation(){
	  BaiduLocationUtil.getLocation(getApplicationContext(), new locationListener() {
			
			@Override
			public void location(String s) {
				loc = s;
				LogUtils.e("ssssssssssssssssssssssssssssss"+loc);
			}
		});
}

  
	//保存照片信息到数据库
	private void saveToDb(){
		final DbBean dbBean =new DbBean();
		dbBean.setTitle(title);
		dbBean.setCreateTime(date);
		dbBean.setResourceUrl(path);
		dbBean.setType(MyConstants.PHOTO);
		dbBean.setFileSize(size);
		dbBean.setLocation(loc);
		SqlDao.getSQLiteOpenHelper(this).save(dbBean, MyConstants.TABLE_MEDIA_DETAIL);
		finish();
	}
	
	//保存到云端
	private void saveToNet(){
		//
	}

	@Override
	public String setTitle() {
		return "拍照完成";
	}

}
