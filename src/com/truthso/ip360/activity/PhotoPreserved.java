package com.truthso.ip360.activity;

import java.io.File;
import java.io.FileOutputStream;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestHandle;
import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.bean.AccountStatusBean;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.bean.FilePositionBean;
import com.truthso.ip360.bean.FilePositionBean.FilePosition;
import com.truthso.ip360.bean.UpLoadBean;
import com.truthso.ip360.bean.UpLoadBean.Upload;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.constants.URLConstant;
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.ossupload.UpDownLoadManager;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.updownload.UpLoadManager;
import com.truthso.ip360.utils.BaiduLocationUtil;
import com.truthso.ip360.utils.BaiduLocationUtil.locationListener;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.FileUtil;
import com.truthso.ip360.utils.ImageLoaderUtil;
import com.truthso.ip360.utils.SecurityUtil;
import com.truthso.ip360.utils.SharePreferenceUtil;
import com.truthso.ip360.view.xrefreshview.LogUtils;

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
	private Button btn_cancel, btn_preserved;
	private ImageView iv_photo;
	private String path, title, size, date, loc;
	private long length;
	private TextView tv_filename, tv_loc, tv_date, tv_filesize, tv_account;
	private int useType;
	private boolean isPre;
	private double fileSize_B;
	private long ll;
	private Dialog alertDialog;
	@Override
	public void initData() {
		path = getIntent().getStringExtra("path");
		title = getIntent().getStringExtra("title");
		size = getIntent().getStringExtra("size");
		date = getIntent().getStringExtra("date");
		loc = getIntent().getStringExtra("loc");
//		length = getIntent().getLongExtra("length", 0);
		fileSize_B = getIntent().getDoubleExtra("fileSize_B",0);
		ll = Math.round(fileSize_B);
//		LogUtils.e(ll+"wsx");
		getLocation();
	}

	@Override
	public void initView() {
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_preserved = (Button) findViewById(R.id.btn_preserved);
		tv_filename = (TextView) findViewById(R.id.tv_filename);
		tv_filename.setText(title);
		tv_loc = (TextView) findViewById(R.id.tv_loc);
		if (!CheckUtil.isEmpty(loc)) {
			tv_loc.setText(loc);
		}else{
			tv_loc.setText("获取位置信息失败");
		}
		tv_date = (TextView) findViewById(R.id.tv_date);
		tv_date.setText(date);
		tv_filesize = (TextView) findViewById(R.id.tv_filesize);
		tv_filesize.setText(size);
		btn_cancel.setOnClickListener(this);
		btn_preserved.setOnClickListener(this);
		iv_photo = (ImageView) findViewById(R.id.iv_photo);
		tv_account = (TextView) findViewById(R.id.tv_account);
		Bitmap decodeFile = BitmapFactory.decodeFile(path);
		ImageLoaderUtil.displayFromSDCardopt(path, iv_photo, null);

		useType = (Integer) SharePreferenceUtil.getAttributeByKey(
				PhotoPreserved.this, MyConstants.SP_USER_KEY, "userType",
				SharePreferenceUtil.VALUE_IS_INT);
		
		
	}

	// 调接口获取当次交易的金额

	private void getport() {
		showProgress("正在加载...");
		ApiManager.getInstance().getAccountStatus(MyConstants.PHOTOTYPE, 1,
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
										Toaster.showToast(PhotoPreserved.this, "您已不能使用该项业务");
										
									}
								}
								  
								 
								
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
		String hashCode = SecurityUtil.SHA512(FileUtil.File2byte(path));
		String imei = MyApplication.getInstance().getDeviceImei();
		
		ApiManager.getInstance().uploadPreserveFile(title,MyConstants.PHOTOTYPE,
				ll + "", hashCode, date, loc, null, imei,
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
                               								
						    	//getPosition(pkValue);
							   //UpDownLoadManager.getInstance().resuambleUpload(uploadFilePath,String token);
							  //获取token
						           
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
/**
 * 获取文件断点的位置
 * @param pkValue
 */
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
						Toaster.showToast(PhotoPreserved.this, bean.getMsg());
					}
				} else {
					Toaster.showToast(PhotoPreserved.this, "请求失败");
				}
			}

		});
	}

	private void startUpLoad(int position, int resourceId) {
		Toaster.showToast(PhotoPreserved.this, "文件正在上传，请在传输列表查看");
		FileInfo info=new FileInfo();
		info.setFileName(title);
		info.setFilePath(path);
		info.setFileSize(ll+"");
		info.setPosition(position);
		info.setResourceId(resourceId);
		Log.i("djj", info.toString());
		//上传的路径，文件的路径，上传的位置，id
		UpLoadManager.getInstance().startUpload(info);
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
			getport();
			if(isPre){
				filePre();
				saveToDb();
			}
			break;
		default:
			break;
		}

	}

	private void getLocation() {
		BaiduLocationUtil.getLocation(getApplicationContext(),
				new locationListener() {

					@Override
					public void location(String s) {
						loc = s;
						
					}
				});
	}

	// 保存照片信息到数据库
	private void saveToDb() {
		final DbBean dbBean = new DbBean();
		dbBean.setTitle(title);
		dbBean.setCreateTime(date);
		dbBean.setResourceUrl(path);
		dbBean.setType(MyConstants.PHOTO);
		dbBean.setFileSize(size);
		dbBean.setLocation(loc);
		SqlDao.getSQLiteOpenHelper(this).save(dbBean,
				MyConstants.TABLE_MEDIA_DETAIL);
		
	}

	// 保存到云端
	private void saveToNet(){
		
	}

	@Override
	public String setTitle() {
		return "拍照完成";
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
						saveToDb();//保存到数据库
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
}
