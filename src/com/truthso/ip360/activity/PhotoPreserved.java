package com.truthso.ip360.activity;

import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.truthso.ip360.system.Toaster;
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

	@Override
	public void initData() {
		path = getIntent().getStringExtra("path");
		title = getIntent().getStringExtra("title");
		size = getIntent().getStringExtra("size");
		date = getIntent().getStringExtra("date");
		loc = getIntent().getStringExtra("loc");
		length = getIntent().getLongExtra("length", 0);
		getLocation();
	}

	@Override
	public void initView() {
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_preserved = (Button) findViewById(R.id.btn_preserved);
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
		iv_photo = (ImageView) findViewById(R.id.iv_photo);
		tv_account = (TextView) findViewById(R.id.tv_account);
		Bitmap decodeFile = BitmapFactory.decodeFile(path);
		ImageLoaderUtil.displayFromSDCardopt(path, iv_photo, null);

		useType = (Integer) SharePreferenceUtil.getAttributeByKey(
				PhotoPreserved.this, MyConstants.SP_USER_KEY, "userType",
				SharePreferenceUtil.VALUE_IS_INT);
		if (useType == 1) {// 用户类型1-付费用户（C）；2-合同用户（B）
			getport();
		} else if (useType == 2) {
			tv_account.setText(1 + "次");
		}
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
//								LogUtils.e("ssssssssssssssss");
								String yue = bean.getDatas().getCount() / 10
										+ "." + bean.getDatas().getCount() % 10;
								tv_account.setText("￥" + yue);
                                if(bean.getDatas().getStatus()==1){
                                	isPre = true;
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
				length + "", hashCode, date, path, loc, null, imei,
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
								startUpLoad(0, pkValue);
//								finish();
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

/*	private void getPosition(int pkValue) {
		ApiManager.getInstance().getFilePosition(pkValue, new ApiCallback() {

			@Override
			public void onApiResultFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				Log.i("djj", "statusCode1"+statusCode);
			}

			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
				FilePositionBean bean = (FilePositionBean) response;
				if (!CheckUtil.isEmpty(bean)) {
					if (bean.getCode() == 200) {
//						Log.i("djj", "statusCode1"+201);
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
*/
	private void startUpLoad(int position, int resourceId) {
//		showProgress("开始上传文件...");
		 LogUtils.e("ssssssssssssssssssssssssssssss"+"执行了吗");
		UpLoadManager.getInstance().startUpload(URLConstant.UploadFile, path,
				position, resourceId);
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
			if(isPre){
				filePre();
			}
//			filePre();
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
	private void saveToNet() {
		
	}

	@Override
	public String setTitle() {
		return "拍照完成";
	}

}
