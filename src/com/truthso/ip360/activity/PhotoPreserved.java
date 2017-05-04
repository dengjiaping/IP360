package com.truthso.ip360.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.util.LogUtils;
import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.bean.AccountStatusBean;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.bean.ExpenseBean;
import com.truthso.ip360.bean.UpLoadBean;
import com.truthso.ip360.bean.UpLoadBean.Upload;
import com.truthso.ip360.bean.WaituploadBean;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.dao.WaituploadDao;
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
	private Button btn_title_right, btn_preserved;
	private ImageButton btn_title_left;
	private ImageView iv_photo;
	private String path, title, size, date, loc,currLoc,currLonglat,longlat,fileDate;
	private long length;
	private TextView tv_filename, tv_loc, tv_date, tv_filesize, tv_account;
	private int useType,pkValue;
	private boolean isPre;
	private double fileSize_B;
	private long ll;
	private Dialog alertDialog;
	private int resourceId;
	private String objectkey;

	private int  expStatus;//扣费状态
	private boolean filePreIsok = false;
	private String hashCode;

	@Override
	public void initData() {
		path = getIntent().getStringExtra("path");
		title = getIntent().getStringExtra("title");
		size = getIntent().getStringExtra("size");
		date = getIntent().getStringExtra("date");
		loc = getIntent().getStringExtra("loc");
		longlat= getIntent().getStringExtra("longlat");
		fileSize_B = getIntent().getDoubleExtra("fileSize_B",0);
		ll = Math.round(fileSize_B);
		getLocation();
	}

	@Override
	public void initView() {
		btn_title_left = (ImageButton) findViewById(R.id.btn_title_left);
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

		tv_filesize = (TextView) findViewById(R.id.tv_filesize);
		tv_filesize.setText(size);
		btn_title_right.setOnClickListener(this);
		btn_preserved.setOnClickListener(this);
		iv_photo = (ImageView) findViewById(R.id.iv_photo);
		iv_photo.setOnClickListener(this);

		ImageLoaderUtil.displayFromSDCardopt(path, iv_photo, null);
		if(!CheckUtil.isEmpty(currLoc)&&!currLoc.equals("nullnull")){//当前能获取位置用当前的位置，
			loc = currLoc;
			longlat = currLonglat;
			tv_loc.setText(loc);
		}else{//当前没有位置,用取证前时候的位置

			if (!CheckUtil.isEmpty(loc)&&!loc.equals("nullnull")) {
				tv_loc.setText(loc);
			} else {
				tv_loc.setText("获取位置信息失败");
				loc ="获取位置信息失败";
			}

		}

		useType = (Integer) SharePreferenceUtil.getAttributeByKey(
				PhotoPreserved.this, MyConstants.SP_USER_KEY, "userType",
				SharePreferenceUtil.VALUE_IS_INT);

		//上传文件信息
		filePre();
		
	}

	// 调接口获取当次交易的金额

	private void getport() {
		showProgress("正在加载...");
		ApiManager.getInstance().getAccountStatus(MyConstants.PHOTOTYPE, 1,
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
						AccountStatusBean bean = (AccountStatusBean) response;
						if (!CheckUtil.isEmpty(bean)) {
							if (bean.getCode() == 200) {
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
		showProgress("正在加载...");
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
		//	 * @param fileType文件类型 文件类型 （拍照（50001）、录像（50003）、录音（50002） 非空 fileSize 文件大小，单位为BhashCode哈希值 非空//fileDate 取证时间 fileUrl 上传oss的文件路径 fileLocation 取证地点 可空 fileTime 取证时长 录像 录音不为空 imei手机的IMEI码

				ApiManager.getInstance().uploadPreserveFile(title,MyConstants.PHOTOTYPE,
				ll + "", hashCode, date, loc, null, imei,longlat,null,0,
				new ApiCallback() {

					@Override
					public void onApiResultFailure(int statusCode,
							Header[] headers, byte[] responseBody,
							Throwable error) {
						hideProgress();
						//网络超时请重试
						showDialogNoNet("网络链接超时，是否重试？");

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
								fileDate = datas.getFileDate();//从服务器获取的保全时间
								tv_date.setText(fileDate);

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
		case R.id.iv_photo://点击查看详情
			Intent intent = new Intent(PhotoPreserved.this,PhotoDetailActivity.class);
			intent.putExtra("url",path);
			startActivity(intent);
				break;
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
	 */
	@Override
	public void onBackPressed() {

		showDialogIsCancel("是否确认放弃保全？");
		//取消上传文件
		CancelUploadFile();
//		super.onBackPressed();//该句返回true不注释掉会立马返回
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

	/**
	 * 网络加载失败，稍后重试
	 * @param msg
     */
	private void showDialogNoNet(String msg) {
		alertDialog = new AlertDialog.Builder(this).setTitle("温馨提示")
				.setMessage(msg).setIcon(R.drawable.ww)
				.setCancelable(false)
				.setPositiveButton("重试", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						filePre();//上传文件信息

					}
				}).setNegativeButton("稍后保全", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//上传列表等待上传
						saveToDb();
						Toaster.showToast(PhotoPreserved.this,"已进入上传列表等待上传");
						Intent intent = new Intent(PhotoPreserved.this,MainActivity.class);
						startActivity(intent);
						finish();
					}
				}).create();
		alertDialog.show();
	}

	// 保存照片信息到数据库
	private void saveToDb() {
		FileInfo info = new FileInfo();
		info.setFilePath(path);
		info.setFileName(title);
		info.setType(MyConstants.PHOTOTYPE);
		info.setFileSize(size);
		info.setHashCode(hashCode);
		info.setFileCreatetime(date);
		info.setFileLoc(loc);
		info.setPriKey((String)SharePreferenceUtil.getAttributeByKey(this,MyConstants.RSAINFO,MyConstants.PRIKEY,SharePreferenceUtil.VALUE_IS_STRING));
		info.setRsaId((int)SharePreferenceUtil.getAttributeByKey(this,MyConstants.RSAINFO,MyConstants.RSAID,SharePreferenceUtil.VALUE_IS_INT));
		info.setLatitudeLongitude(longlat);
		WaituploadDao.getDao().save(info);
	}

	/**
	 * 定位
	 */
	private void getLocation(){
		BaiduLocationUtil.getLocation(PhotoPreserved.this, new locationListener() {

			@Override
			public void location(String s, double latitude, double longitude) {
			currLoc = s;
			currLonglat = longitude+","+latitude;
			}


		});
	}
}


