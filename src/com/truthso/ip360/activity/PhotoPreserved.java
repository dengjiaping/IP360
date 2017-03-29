package com.truthso.ip360.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.util.LogUtils;
import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.bean.AccountStatusBean;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.bean.ExpenseBean;
import com.truthso.ip360.bean.UpLoadBean;
import com.truthso.ip360.bean.UpLoadBean.Upload;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.ossupload.UpLoadManager;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.utils.BaiduLocationUtil;
import com.truthso.ip360.utils.BaiduLocationUtil.locationListener;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.FileUtil;
import com.truthso.ip360.utils.ImageLoaderUtil;
import com.truthso.ip360.utils.SecurityUtil;
import com.truthso.ip360.utils.SharePreferenceUtil;

import cz.msebera.android.httpclient.Header;

/**
 * @despriction :照片保全的界面
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-7-21下午5:18:47
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class PhotoPreserved extends BaseActivity implements OnClickListener {
	private Button btn_title_right, btn_preserved,btn_title_left;
	private ImageView iv_photo;
	private String path, title, size, date, loc,longlat;
	private long length;
	private TextView tv_filename, tv_loc, tv_date, tv_filesize, tv_account;
	private int useType,pkValue;
	private boolean isPre;
	private double fileSize_B;
	private long ll;
	private Dialog alertDialog;
	private int resourceId;
	private String objectkey;
	private double lat,longti;
	private String latitudeLongitude;
	private int  expStatus;//扣费状态
	private boolean filePreIsok = false;
	private String hashCode;
	/*private Handler handler = new Handler(){
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
	};*/
	@Override
	public void initData() {
		path = getIntent().getStringExtra("path");
		title = getIntent().getStringExtra("title");
		size = getIntent().getStringExtra("size");
		date = getIntent().getStringExtra("date");
		loc = getIntent().getStringExtra("loc");
		longlat= getIntent().getStringExtra("longlat");

//		length = getIntent().getLongExtra("length", 0);
		fileSize_B = getIntent().getDoubleExtra("fileSize_B",0);
		ll = Math.round(fileSize_B);
//		getLocation();
//		LogUtils.e(ll+"wsx");
	/*	getLocation();
		//上传文件信息
		filePre();*/
	}

	@Override
	public void initView() {
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_left.setOnClickListener(this);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		btn_title_right.setVisibility(View.VISIBLE);
		btn_title_right.setText("放弃");
		btn_title_right.setTextColor(getResources().getColor(R.color.white));
		btn_preserved = (Button) findViewById(R.id.btn_preserved);
		tv_filename = (TextView) findViewById(R.id.tv_filename);
		tv_filename.setText(title);
		tv_loc = (TextView) findViewById(R.id.tv_loc);
		tv_date = (TextView) findViewById(R.id.tv_date);
		tv_date.setText(date);
		tv_filesize = (TextView) findViewById(R.id.tv_filesize);
		tv_filesize.setText(size);
		btn_title_right.setOnClickListener(this);
		btn_preserved.setOnClickListener(this);
		iv_photo = (ImageView) findViewById(R.id.iv_photo);
//		tv_account = (TextView) findViewById(R.id.tv_account);
		Bitmap decodeFile = BitmapFactory.decodeFile(path);
		ImageLoaderUtil.displayFromSDCardopt(path, iv_photo, null);
		if (!CheckUtil.isEmpty(loc)&&!loc.equals("null")) {
			tv_loc.setText(loc);
		} else {
			tv_loc.setText("获取位置信息失败");
		}
		useType = (Integer) SharePreferenceUtil.getAttributeByKey(
				PhotoPreserved.this, MyConstants.SP_USER_KEY, "userType",
				SharePreferenceUtil.VALUE_IS_INT);
//		getLocation();
		//上传文件信息
		filePre();
		
	}

	// 调接口获取当次交易的金额

	private void getport() {
		showProgress("正在加载...");
		ApiManager.getInstance().getAccountStatus(MyConstants.PHOTOTYPE, 1,
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
							/*	if (bean.getDatas().getStatus()== 1) {//0-不能使用；1-可以使用。

							*//*		if (useType ==1 ) {//用户类型1-付费用户（C）；
										//弹出提示框
										  showDialog(bean.getDatas().getShowText());
									}else if(useType ==2 ){//2-合同用户（B）
//										//合同用户可用时，上传文件，保存文件信息到数据库
											UpLoadFile();
											saveToDb();
									}*//*

								}else if(bean.getDatas().getStatus()== 0){//不能用

									Toaster.showToast(PhotoPreserved.this, "您已不能使用该项业务");

									*//*if (useType ==1 ) {//用户类型1-付费用户（C）；2-合同用户（B）
//										 String str1 = "此文件保存价格为："+yue+"当前余额不足，是否仍要存证？";
										  showDialog(bean.getDatas().getShowText());
									}else if(useType ==2 ){
										Toaster.showToast(PhotoPreserved.this, "您已不能使用该项业务");

									}*//*
								}*/

								showDialog(bean.getDatas().getShowText());

							} else {
								Toaster.showToast(PhotoPreserved.this,
										bean.getMsg());
							}
						} else {
							Toaster.showToast(PhotoPreserved.this, "请重试");
						}
					}

				});
	}

	/**
	 * 文件保全（这个接口只传文件hashcode等信息，不上传文件）
	 *
	 * @return
	 */
	private void filePre() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				hashCode = SecurityUtil.SHA512(path);
				if (hashCode != null) {
					handler.sendEmptyMessage(0);
				}

			}
		}.start();
	}
		private Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
		String imei = MyApplication.getInstance().getDeviceImei();
		//	 * @param fileType文件类型 文件类型 （拍照（50001）、录像（50003）、录音（50002） 非空 fileSize 文件大小，单位为BhashCode哈希值 非空
//fileDate 取证时间 fileUrl 上传oss的文件路径 fileLocation 取证地点 可空 fileTime 取证时长 录像 录音不为空 imei手机的IMEI码
		ApiManager.getInstance().uploadPreserveFile(title,MyConstants.PHOTOTYPE,
				ll + "", hashCode, date, loc, null, imei,longlat,
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
                               objectkey = datas.getFileUrl();//文件上传的objectKey
									resourceId=datas.getPkValue();

							} else {
								Toaster.showToast(PhotoPreserved.this,
										bean.getMsg());
							}
						} else {
							Toaster.showToast(PhotoPreserved.this, "请求失败");
						}
					}

				});
			}
	};

	/**
	 * 上传文件
	 */
	private void UpLoadFile() {
		     	//上传文件
						FileInfo info=new FileInfo();
						info.setFileName(title);
						info.setFilePath(path);
						info.setFileSize(ll+"");
						info.setResourceId(resourceId);
						info.setObjectKey(objectkey);
						Toaster.showToast(PhotoPreserved.this, "文件正在上传请在传输列表查看");
						//上传文件
						UpLoadManager.getInstance().resuambleUpload(info);
	}

	@Override
	public int setLayout() {
		return R.layout.activity_photopreserved;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_title_right://标题右上角的放弃
			showDialogIsCancel("是否确认放弃保全？");
//			//取消上传文件
//			CancelUploadFile();
//			finish();
			break;
		case R.id.btn_preserved:
			if (!filePreIsok){//保全接口没调成功的话就再调一次
				filePre();
			}
			//调获取本次保全费用，及是否可用的接口
			getport();
			break;
		case R.id.btn_title_left://返回键
			showDialogIsCancel("是否确认放弃保全？");
//				//取消上传文件
//				CancelUploadFile();
				break;
		default:
			break;
		}

	}

/*	private void getLocation() {

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
	}*/

	// 保存照片信息到数据库
	private void saveToDb() {
		final DbBean dbBean = new DbBean();
		dbBean.setTitle(title);
		dbBean.setCreateTime(date);
		dbBean.setResourceUrl(path);
		dbBean.setType(MyConstants.PHOTO);
		dbBean.setFileSize(size);
		dbBean.setLlsize(ll+"");
		dbBean.setLocation(loc);
		dbBean.setPkValue(pkValue+"");
		dbBean.setFileFormat("jpg");
		dbBean.setStatus("0");
		dbBean.setDataType(3);//现场取证
		dbBean.setUserId((Integer) SharePreferenceUtil.getAttributeByKey(MyApplication.getApplication(), MyConstants.SP_USER_KEY, "userId", SharePreferenceUtil.VALUE_IS_INT));
		dbBean.setExpStatus(expStatus);
		SqlDao.getSQLiteOpenHelper().save(dbBean,
				MyConstants.TABLE_MEDIA_DETAIL);
		
	}



	@Override
	public String setTitle() {
		return "拍照完成";
	}

	/**
	 * 弹出框
	 * @param msg
     */
	private void showDialog(String msg) {
	alertDialog = new AlertDialog.Builder(this).setTitle("温馨提示")
				.setMessage(msg).setIcon(R.drawable.ww)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//调扣费的接口
						GetPayment(pkValue,MyConstants.PHOTOTYPE,1);


					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						CancelUploadFile();
					}
				}).create();
	alertDialog.show();
	}
	/**
	 * 取消上传文件
	 */
    private void CancelUploadFile() {
		ApiManager.getInstance().DeleteFileInfo(pkValue, new ApiCallback() {
			@Override
			public void onApiResult(int errorCode, String message, BaseHttpResponse response) {
							if (response.getCode()==200){
							hideProgress();
							}
			}

			@Override
			public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

			}
		});


	}

	/**
	 * 扣费
	 * @param type （拍照（50001）、录像（50003）、录音（50002）
	 * @param count 当次消费业务量
     */
	private void GetPayment(int pkValue,int type,int count){
			showProgress("正在加载...");
		ApiManager.getInstance().Payment(pkValue,type, count, new ApiCallback() {
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
						saveToDb();
						Toaster.showToast(PhotoPreserved.this,"文件正在上传，请在传输列表查看");
						finish();
					}else{
						Toaster.showToast(PhotoPreserved.this,bean.getMsg());
					}
				}else{
					Toaster.showToast(PhotoPreserved.this,"保全失败，请点保全按钮重试！");
				}

			}

			@Override
			public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

			}
		});

		}

	/**
	 * 监听系统的返回键
	 * @param keyCode
	 * @param event
     * @return
     *//*
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				//系统返回键调不上传的接口，通知后台清除数据
				CancelUploadFile();
//				return false;
			}
				return super.onKeyDown(keyCode, event);

		}*/

	/**
	 * 监听系统的返回键
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		LogUtils.e("返回键执行了");
		showDialogIsCancel("是否确认放弃保全？");
		//取消上传文件
		CancelUploadFile();
	}

	/**
	 * 是否确认放弃保全
	 * @param msg
     */
	private void showDialogIsCancel(String msg) {
		alertDialog = new AlertDialog.Builder(this).setTitle("温馨提示")
				.setMessage(msg).setIcon(R.drawable.ww)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//取消上传文件
						CancelUploadFile();
						finish();


					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						CancelUploadFile();
					}
				}).create();
		alertDialog.show();
	}
}


