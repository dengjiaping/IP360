package com.truthso.ip360.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.SqlDao;

/**
 * 首页面跳转到的现场录音列表的activity
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-6-13上午10:48:56
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class LiveRecordPreActivity extends BaseActivity implements
		OnClickListener {

	private TextView tv_filename, tv_loc, tv_date, tv_filesize, tv_time;
	private String date, fileName, loc, fileSize, time, filePath;
	private Button btn_cancel, btn_save;

	@Override
	public void initData() {

	}

	@Override
	public void initView() {
		tv_filename = (TextView) findViewById(R.id.tv_filename);
		tv_loc = (TextView) findViewById(R.id.tv_loc);
		tv_date = (TextView) findViewById(R.id.tv_date);
		tv_filesize = (TextView) findViewById(R.id.tv_filesize);
		tv_time = (TextView) findViewById(R.id.tv_time);

		fileName = getIntent().getStringExtra("fileName");
		date = getIntent().getStringExtra("date");
		fileSize = getIntent().getStringExtra("fileSize");
		time = getIntent().getStringExtra("fileTime");
		filePath = getIntent().getStringExtra("filePath");
		loc = getIntent().getStringExtra("loc");
		
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_save = (Button) findViewById(R.id.btn_preserved);
		btn_cancel.setOnClickListener(this);
		btn_save.setOnClickListener(this);
		
		
		tv_filename.setText(fileName);
		tv_loc.setText(loc);
		tv_date.setText(date);
		tv_filesize.setText(fileSize);
		tv_time.setText(time);

	}

	@Override
	public int setLayout() {
		return R.layout.activity_prerecord;
	}

	@Override
	public String setTitle() {
		return "录音完成";
	}

	/**
	 * 将数据保存到数据库
	 */
	private void saveData(String date, String fileSize, String name,
			String path, String recordTime) {
		SqlDao sqlDao = new SqlDao(this);
		DbBean dbBean = new DbBean();
		dbBean.setType(MyConstants.RECORD);// 文件类别
		dbBean.setCreateTime(date);// 生成时间
		dbBean.setFileSize(fileSize);// 文件大小
		dbBean.setResourceUrl(path);
		dbBean.setTitle(name);// 名称
		dbBean.setRecordTime(recordTime);
		sqlDao.save(dbBean, "IP360_media_detail");// 存入数据库
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel:// 放弃
			Intent intent = new Intent(this,MainActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.btn_preserved:// 保全
			saveData(date, fileSize, fileName, filePath, time);
			Intent intent1 = new Intent(this,MainActivity.class);
			startActivity(intent1);
			finish();
			break;
		default:
			break;
		}
	}
}
