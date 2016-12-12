package com.truthso.ip360.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.bean.AccountStatusBean;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.bean.ExpenseBean;
import com.truthso.ip360.bean.FilePositionBean;
import com.truthso.ip360.bean.FilePositionBean.FilePosition;
import com.truthso.ip360.bean.UpLoadBean;
import com.truthso.ip360.bean.UpLoadBean.Upload;
import com.truthso.ip360.ossupload.UpLoadManager;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.utils.BaiduLocationUtil;
import com.truthso.ip360.utils.BaiduLocationUtil.locationListener;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.FileUtil;
import com.truthso.ip360.utils.SecurityUtil;
import com.truthso.ip360.utils.SharePreferenceUtil;

import cz.msebera.android.httpclient.Header;

/**
 *录音保全界面
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
	private int mintime,pkValue;
	private double fileSize_B;
	private long ll;
	private String url;
	private Dialog alertDialog;
	private double lat,longti;
	private String latitudeLongitude;
	private boolean filePreIsok = false;
	private  int expStatus;
	private Handler handler = new Handler(){
		 public void handleMessage(Message msg) {
			 switch (msg.what) {
			case 1:
				if (!CheckUtil.isEmpty(loc)) {
				tv_loc.setText(loc);
			}else{
				tv_loc.setText("获取位置信息失败");
			}
				latitudeLongitude = longti+","+lat;
				break;

			default:
				break;
			}
		 };
	};
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
		mintime = getIntent().getIntExtra("mintime", 0);
		fileName = getIntent().getStringExtra("fileName");
		date = getIntent().getStringExtra("date");
		fileSize = getIntent().getStringExtra("fileSize");
		time = getIntent().getStringExtra("fileTime");
		filePath = getIntent().getStringExtra("filePath");
//		loc = getIntent().getStringExtra("loc");
		fileSize_B = getIntent().getDoubleExtra("fileSize_B",0);
		ll = Math.round(fileSize_B);
		
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_save = (Button) findViewById(R.id.btn_preserved);
		btn_cancel.setOnClickListener(this);
		btn_save.setOnClickListener(this);
		
		
		tv_filename.setText(fileName);
	/*	if (!CheckUtil.isEmpty(loc)) {
			tv_loc.setText(loc);
		}else{
			tv_loc.setText("获取位置信息失败");
		}*/
		
		tv_date.setText(date);
		tv_filesize.setText(fileSize);
		tv_time.setText(time);
		
		useType = (Integer) SharePreferenceUtil.getAttributeByKey(
				LiveRecordPreActivity.this, MyConstants.SP_USER_KEY, "userType",
				SharePreferenceUtil.VALUE_IS_INT);
		//定位
		getLocation();
		//上传文件的hashcode
		filePre();
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
	 * String date, String fileSize, String name,
			String path, String recordTime
	 */
	private void saveData() {
		SqlDao sqlDao = SqlDao.getSQLiteOpenHelper();
		DbBean dbBean = new DbBean();
		dbBean.setType(MyConstants.RECORD);// 文件类别
		dbBean.setCreateTime(date);// 生成时间
		dbBean.setFileSize(fileSize);// 文件大小
		dbBean.setResourceUrl(filePath);
		dbBean.setTitle(fileName);// 名称
		dbBean.setRecordTime(time);
		dbBean.setLocation(loc);
		dbBean.setLlsize(ll+"");
		dbBean.setPkValue(pkValue+"");
		dbBean.setFileFormat("mar");
		dbBean.setStatus("0");
		dbBean.setExpStatus(expStatus);
		dbBean.setUserId((Integer) SharePreferenceUtil.getAttributeByKey(MyApplication.getApplication(), MyConstants.SP_USER_KEY, "userId", SharePreferenceUtil.VALUE_IS_INT));
		sqlDao.save(dbBean, "IP360_media_detail");// 存入数据库
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_cancel://放弃
				//取消上传文件
				CancelUploadFile();
				finish();
				break;
			case R.id.btn_preserved://保全
				if (!filePreIsok) {//保全的接口调不成功，再掉一次
					filePre();
				}
				//调获取本次保全费用，及是否可用的接口
				getport();
				break;
			case R.id.acition_bar_left://返回键
				//取消上传文件
				CancelUploadFile();
				break;

			default:
				break;
		}
	}
	/**
	 * 调接口，看是否可用，获取当次的计费
	 */
	private void getport() {
		showProgress("正在加载...");
		ApiManager.getInstance().getAccountStatus(MyConstants.PHOTOTYPE, mintime,
				new ApiCallback() {

//					private String yue;

					@Override
					public void onApiResultFailure(int statusCode,
							Header[] headers, byte[] responseBody,
							Throwable error) {

					}

					@Override
					public void onApiResult(int errorCode, String message,
							BaseHttpResponse response) {
						hideProgress();
						AccountStatusBean bean = (AccountStatusBean) response;
						if (!CheckUtil.isEmpty(bean)) {
							if (bean.getCode() == 200) {
								if (bean.getDatas().getStatus()== 1) {//0-不能使用；1-可以使用。
								/*yue = "￥"+ bean.getDatas().getCount()/10 +"."+bean.getDatas().getCount()%10+"元";
									
									if (useType ==1 ) {//用户类型1-付费用户（C）；
//										 String str = "此文件保存价格为："+yue+"是否确认支付？";
										  showDialog(bean.getDatas().getShowText());
									}else if(useType ==2 ){//2-合同用户（B）
									//上传文件信息，及存到数据库	
										isPre=true;
									}*/
									showDialog(bean.getDatas().getShowText());
								}else if(bean.getDatas().getStatus()== 0){//不能用
									
							/*		if (useType ==1 ) {//用户类型1-付费用户（C）；2-合同用户（B）
//										 String str1 = "此文件保存价格为："+yue+"当前余额不足，是否仍要存证？";
//										  showDialog(str1);
										  showDialog(bean.getDatas().getShowText());
									}else if(useType ==2 ){
										Toaster.showToast(LiveRecordPreActivity.this, "您已不能使用该项业务");
										
									}*/
									Toaster.showToast(LiveRecordPreActivity.this, "您已不能使用该项业务");

								}
								  
								 
								
							} else {
								Toaster.showToast(LiveRecordPreActivity.this,
										bean.getMsg());
							}
						} else {
							Toaster.showToast(LiveRecordPreActivity.this, "请重试");
						}
					}

				});
	}

	/**
	 * 文件保全（这个接口只传文件hashcode等信息，不上传文件）
	 * 
	 */
	private void filePre() {

		showProgress("上传文件信息...");
		String hashCode = SecurityUtil.SHA512(filePath);
		String imei = MyApplication.getInstance().getDeviceImei();
		ApiManager.getInstance().uploadPreserveFile(fileName,MyConstants.RECORDTYPE,
				ll+"", hashCode, date, loc,time,imei,latitudeLongitude,
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
								filePreIsok = true;
								Upload datas = bean.getDatas();
								pkValue = datas.getPkValue();
								//getPosition(pkValue);
								//上传
//								startUpLoad(0, pkValue);
								 url=	datas.getFileUrl();

                               	Toaster.showToast(LiveRecordPreActivity.this, "文件正在上传请在传输列表查看");



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

	/*private void getPosition(int pkValue) {
		ApiManager.getInstance().getFilePosition(pkValue, new ApiCallback() {

			@Override
			public void onApiResultFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
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
	}*/
/*	*//**
	 * 开始上传文件
	 * 
	 * @param position
	 * @param resourceId
	 *//*
	private void startUpLoad(int position, int resourceId) {
		Toaster.showToast(LiveRecordPreActivity.this, "文件正在上传，请在传输列表查看");
		FileInfo info=new FileInfo();
		info.setFileName(fileName);
		info.setFilePath(filePath);
		info.setFileSize(ll+"");
		info.setPosition(position);
		info.setResourceId(resourceId);
		UpLoadManager.getInstance().startUpload(info);
	}*/
	/**
	 * 弹出框
	 */
	private void showDialog(String msg) {
		alertDialog = new AlertDialog.Builder(this).setTitle("温馨提示")
				.setMessage(msg).setIcon(R.drawable.ww)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//调扣费的接口
						GetPayment(pkValue, MyConstants.RECORDTYPE, mintime);

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
	/**
	 * 定位
	 */
	private void getLocation() {
		BaiduLocationUtil.getLocation(getApplicationContext(),
				new locationListener() {

					@Override
					public void location(String s, double latitude,
							double longitude) {
						loc = s;
						lat = latitude;
						longti =longitude;
						Message message = handler .obtainMessage();
						message.what = 1;
						handler.sendMessage(message);	
					}
				
				});
	}
	/**
	 * 上传文件
	 */
	private void UpLoadFile() {

		FileInfo info=new FileInfo();
		info.setFileName(fileName);
		info.setFilePath(filePath);
		info.setFileSize(ll+"");
		info.setResourceId(pkValue);
		info.setObjectKey(url);
		//上传文件
		UpLoadManager.getInstance().resuambleUpload(info);

	}
	/**
	 * 扣费
	 *
	 * @param type  （拍照（50001）、录像（50003）、录音（50002）
	 * @param count 当次消费业务量
	 */
	private void GetPayment(int pkValue, int type, int count) {
		showProgress("正在加载...");
		ApiManager.getInstance().Payment(pkValue, type, count, new ApiCallback() {
			@Override
			public void onApiResult(int errorCode, String message, BaseHttpResponse response) {
				ExpenseBean bean = (ExpenseBean) response;
				if (!CheckUtil.isEmpty(bean)){
					if(bean.getCode() == 200){
						hideProgress();
						//欠费状态
						expStatus = bean.getDatas().getStatus();
						//上传文件
						UpLoadFile();
						//保全文件到数据库
						saveData();
						finish();
						Toaster.showToast(LiveRecordPreActivity.this,"文件正在上传，请在传输列表查看");
					}else{
						Toaster.showToast(LiveRecordPreActivity.this,"保全失败，请点保全按钮重试！");
					}
				}else{
					Toaster.showToast(LiveRecordPreActivity.this,"保全失败，请点保全按钮重试！");
				}
			}

			@Override
			public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

			}
		});
	}
	/**
	 * 取消上传文件
	 */
	private void CancelUploadFile() {
		ApiManager.getInstance().DeleteFileInfo(pkValue, new ApiCallback() {
			@Override
			public void onApiResult(int errorCode, String message, BaseHttpResponse response) {

			}

			@Override
			public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

			}
		});



	}
}
