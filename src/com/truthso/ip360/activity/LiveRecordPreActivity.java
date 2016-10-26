package com.truthso.ip360.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.bean.AccountStatusBean;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.bean.FilePositionBean;
import com.truthso.ip360.bean.UpLoadBean;
import com.truthso.ip360.bean.FilePositionBean.FilePosition;
import com.truthso.ip360.bean.UpLoadBean.Upload;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.constants.URLConstant;
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.updownload.UpLoadManager;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.FileUtil;
import com.truthso.ip360.utils.SecurityUtil;

import cz.msebera.android.httpclient.Header;

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
	private boolean isPre=false;
	private int useType;
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
			filePre();
			finish();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 文件保全（这个接口只传文件hashcode等信息，不上传文件）
	 * 
	 * @param fileType
	 *            文件类型 文件类型 （拍照（50001）、录像（50003）、录音（50002） 非空
	 * @param fileSize
	 *            文件大小，单位为B
	 * @param hashCode
	 *            哈希值 非空
	 * @param fileDate
	 *            取证时间
	 * @param fileUrl
	 *            上传oss的文件路径
	 * @param fileLocation
	 *            取证地点 可空
	 * @param fileTime
	 *            取证时长 录像 录音不为空
	 * @param imei手机的IMEI码
	 * @param callback
	 * @return
	 */
	private void filePre() {

		showProgress("上传文件信息...");
		String hashCode = SecurityUtil.SHA512(FileUtil.File2byte(filePath));
		String imei = MyApplication.getInstance().getDeviceImei();
	//	String fileTitle,int fileType,String fileSize,String hashCode,
		//String fileDate,String fileLocation,String fileTime,String imei,ApiCallback callback
		ApiManager.getInstance().uploadPreserveFile(fileName,MyConstants.VIDEOTYPE,
				fileSize, hashCode, date, loc, time,imei,
				new ApiCallback() {

					@Override
					public void onApiResultFailure(int statusCode,
							Header[] headers, byte[] responseBody,
							Throwable error) {
					}

					@Override
					public void onApiResult(int errorCode, String message,
							BaseHttpResponse response) {
						hideProgress();
						UpLoadBean bean = (UpLoadBean) response;
						if (!CheckUtil.isEmpty(bean)) {
							if (bean.getCode() == 200) {
								Upload datas = bean.getDatas();
								int pkValue = datas.getPkValue();
								getPosition(pkValue);
						
							} else {
								Toaster.showToast(LiveRecordPreActivity.this,
										bean.getMsg());
							}
						} else {
							Toaster.showToast(LiveRecordPreActivity.this, "请求失败");
						}
					}

				});
	}

	private void getPosition(int pkValue) {
		ApiManager.getInstance().getFilePosition(pkValue, new ApiCallback() {

			@Override
			public void onApiResultFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
				FilePositionBean bean = (FilePositionBean) response;
				if (!CheckUtil.isEmpty(bean)) {
					if (bean.getCode() == 200) {
						FilePosition datas = bean.getDatas();
						startUpLoad(datas.getPosition(), datas.getResourceId());
						finish();
					} else {
						Toaster.showToast(LiveRecordPreActivity.this, bean.getMsg());
					}
				} else {
					Toaster.showToast(LiveRecordPreActivity.this, "请求失败");
				}
			}

		});
	}

	private void startUpLoad(int position, int resourceId) {
//		showProgress("开始上传文件...");
		FileInfo info=new FileInfo();
		info.setFileName(fileName);
		info.setFilePath(filePath);
		info.setFileSize(fileSize);
		info.setPosition(position);
		info.setResourceId(resourceId);
		UpLoadManager.getInstance().startUpload(info);
	}

}
