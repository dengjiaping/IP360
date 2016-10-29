package com.truthso.ip360.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.bean.AccountStatusBean;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.bean.FilePositionBean;
import com.truthso.ip360.bean.FilePositionBean.FilePosition;
import com.truthso.ip360.bean.UpLoadBean;
import com.truthso.ip360.bean.UpLoadBean.Upload;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.updownload.UpLoadManager;
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
	private int mintime;
	private double fileSize_B;
	private long ll;
	private Dialog alertDialog;
	
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
		loc = getIntent().getStringExtra("loc");
		fileSize_B = getIntent().getDoubleExtra("fileSize_B",0);
		ll = Math.round(fileSize_B);
		
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_save = (Button) findViewById(R.id.btn_preserved);
		btn_cancel.setOnClickListener(this);
		btn_save.setOnClickListener(this);
		
		
		tv_filename.setText(fileName);
		if (!CheckUtil.isEmpty(loc)) {
			tv_loc.setText(loc);
		}else{
			tv_loc.setText("获取位置信息失败");
		}
		
		tv_date.setText(date);
		tv_filesize.setText(fileSize);
		tv_time.setText(time);
		getLocation();
		useType = (Integer) SharePreferenceUtil.getAttributeByKey(
				LiveRecordPreActivity.this, MyConstants.SP_USER_KEY, "userType",
				SharePreferenceUtil.VALUE_IS_INT);
		
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
		SqlDao sqlDao = new SqlDao(this);
		DbBean dbBean = new DbBean();
		dbBean.setType(MyConstants.RECORD);// 文件类别
		dbBean.setCreateTime(date);// 生成时间
		dbBean.setFileSize(fileSize);// 文件大小
		dbBean.setResourceUrl(filePath);
		dbBean.setTitle(fileName);// 名称
		dbBean.setRecordTime(time);
		dbBean.setLocation(loc);
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
			getport();
			if(isPre){
				filePre();
				saveData();
			}
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

					private String yue;

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
								yue = "￥"+ bean.getDatas().getCount()/10 +"."+bean.getDatas().getCount()%10+"元";
									
									if (useType ==1 ) {//用户类型1-付费用户（C）；
										 String str = "此文件保存价格为："+yue+"是否确认支付？";
										  showDialog(str);
									}else if(useType ==2 ){//2-合同用户（B）
									//上传文件信息，及存到数据库	
										isPre=true;
									}
									
								}else if(bean.getDatas().getStatus()== 0){//不能用
									
									if (useType ==1 ) {//用户类型1-付费用户（C）；2-合同用户（B）
										 String str1 = "此文件保存价格为："+yue+"当前余额不足，是否仍要存证？";
										  showDialog(str1);
									}else if(useType ==2 ){
										Toaster.showToast(LiveRecordPreActivity.this, "您已不能使用该项业务");
										
									}
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
	 * @param fileType
	 *            文件类型 文件类型 （拍照（50001）、录像（50003）、录音（50002） 非空
	 * @param fileSize
	 *            文件大小，单位为B
	 * @param hashCode
	 *            哈希值 非空
	 * @param fileDate
	 *            取证时间
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
		ApiManager.getInstance().uploadPreserveFile(fileName,MyConstants.RECORDTYPE,
				ll+"", hashCode, date, loc,time,imei,
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
//								startUpLoad(0,pkValue);
								finish();
						
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
	/**
	 * 开始上传文件
	 * 
	 * @param position
	 * @param resourceId
	 */
	private void startUpLoad(int position, int resourceId) {
		Toaster.showToast(LiveRecordPreActivity.this, "文件正在上传，请在传输列表查看");
		FileInfo info=new FileInfo();
		info.setFileName(fileName);
		info.setFilePath(filePath);
		info.setFileSize(ll+"");
		info.setPosition(position);
		info.setResourceId(resourceId);
		UpLoadManager.getInstance().startUpload(info);
	}
	/**
	 * 弹出框
	 */
	private void showDialog(String msg) {
		alertDialog = new AlertDialog.Builder(this).setTitle("温馨提示")
				.setMessage(msg).setIcon(R.drawable.ww)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//上传文件信息
						filePre();
						saveData();//保存到数据库
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
					public void location(String s) {
						loc = s;
						
					}
				});
	}
}
