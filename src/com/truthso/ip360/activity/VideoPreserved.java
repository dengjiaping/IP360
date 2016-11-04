package com.truthso.ip360.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
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
import com.truthso.ip360.ossupload.UpLoadManager;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.utils.BaiduLocationUtil;
import com.truthso.ip360.utils.BaiduLocationUtil.locationListener;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.FileUtil;
import com.truthso.ip360.utils.GetFileSizeUtil;
import com.truthso.ip360.utils.SecurityUtil;
import com.truthso.ip360.utils.SharePreferenceUtil;

import cz.msebera.android.httpclient.Header;

/**
 * @despriction :录像保全的界面
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-7-21下午5:21:25
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class VideoPreserved extends BaseActivity implements OnClickListener {
	private Dialog alertDialog;
	private String mVideoPath;
	private String mVideoName;
	private ImageView iv_video;
	private String mVideoSize,size,title;
	private String mDate,loc,time;
	private Button btn_preserved,btn_cancel;
	private int minTime;
	private TextView tv_filename,tv_loc,tv_date,tv_filesize,tv_time,tv_account;

	private boolean isPre=false;

	private int useType;
	private double video_fileSize_B;
	private long ll;


	@Override
	public void initData() {
		getLocation();
		mVideoPath = getIntent().getStringExtra("filePath");
		mDate = getIntent().getStringExtra("date");
		loc =getIntent().getStringExtra("loc");
		time = getIntent().getStringExtra("time");
		minTime = getIntent().getIntExtra("minTime", 0);
		size=getIntent().getStringExtra("size");
		title=getIntent().getStringExtra("title");
		video_fileSize_B = getIntent().getDoubleExtra("video_fileSize_B",0);
    	ll = Math.round(video_fileSize_B);
	}

	@Override
	public void initView() {
			
//		iv_video = (ImageView) findViewById(R.id.iv_video);
		tv_filename = (TextView) findViewById(R.id.tv_filename);
		
		tv_loc = (TextView) findViewById(R.id.tv_loc);
		
		tv_date = (TextView) findViewById(R.id.tv_date);
		
		tv_filesize = (TextView) findViewById(R.id.tv_filesize);
		
		tv_time = (TextView) findViewById(R.id.tv_time);
		
//		tv_account = (TextView) findViewById(R.id.tv_account);
	
		mVideoName = mVideoPath.substring(mVideoPath.lastIndexOf("/") + 1);
//		iv_video.setImageBitmap(getVideoThumbnail(mVideoPath, 80, 80,
//				MediaStore.Images.Thumbnails.MICRO_KIND));
		mVideoSize = GetFileSizeUtil.FormatFileSize(mVideoPath);
		
		tv_filename.setText(mVideoName);
		if (!CheckUtil.isEmpty(loc)) {
			tv_loc.setText(loc);
		}else{
			tv_loc.setText("获取位置信息失败");
		}
		
		tv_date.setText(mDate);
		tv_filesize.setText(mVideoSize);
		tv_time.setText(time.toString().trim());

		
		btn_preserved = (Button) findViewById(R.id.btn_preserved);
		btn_preserved.setOnClickListener(this);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(this);
		
		useType = (Integer) SharePreferenceUtil.getAttributeByKey(VideoPreserved.this, MyConstants.SP_USER_KEY, "userType",SharePreferenceUtil.VALUE_IS_INT);
		
	
	}
	/**
	 * 调接口，看是否可用，和当次消费
	 */
	private void getport() {

		showProgress("正在加载...");
		ApiManager.getInstance().getAccountStatus(MyConstants.VIDEOTYPE, minTime, new ApiCallback() {
			
			

			private String yue;

			@Override
			public void onApiResultFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				
			}
			
			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
				hideProgress();		
				AccountStatusBean bean = (AccountStatusBean)response;
				if (!CheckUtil.isEmpty(bean)) {
					if (bean.getCode()== 200) {
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
								Toaster.showToast(VideoPreserved.this, "您已不能使用该项业务");
								
							}
						}
						  
						 
						
					}else{
						Toaster.showToast(VideoPreserved.this, bean.getMsg());
					}
				}else{
					Toaster.showToast(VideoPreserved.this, "请重试");
				}
			}
		});
	
		
	}

	@Override
	public int setLayout() {
		return R.layout.activity_videopreserved;
	}

	@Override
	public String setTitle() {
		return "录像完成";
	}

	/**
	 * 获取视频的缩略图 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
	 * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
	 * 
	 * @author wsx_summer
	 * 
	 * @param videoPath
	 *            视频的路径
	 * @param width
	 *            指定输出视频缩略图的宽度
	 * @param height
	 *            指定输出视频缩略图的高度度
	 * @param kind
	 *            参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
	 *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
	 * @return 指定大小的视频缩略图
	 */
	@SuppressWarnings("unused")
	private Bitmap getVideoThumbnail(String videoPath, int width, int height,
			int kind) {
		Bitmap bitmap = null;
		// 获取视频的缩略图
		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel://放弃
			finish();
			break;
		case R.id.btn_preserved://保全
			getport();
			if(isPre){
				filePre();
				saveToDB();
			}
			break;

		default:
			break;
		}
	}
	/**
	 * 文件保全（这个接口只传文件hashcode等信息，不上传文件）
	 * @param fileType 文件类型 文件类型 （拍照（50001）、录像（50003）、录音（50002） 非空
	 * @param fileSize 文件大小，单位为B
	 * @param hashCode  哈希值 非空
	 * @param fileDate取证时间
	 * @param fileLocation  取证地点 可空
	 * @param fileTime  取证时长 录像 录音不为空
	 * @param imei手机的IMEI码
	 * @param callback
	 * @return
	 */
	private void filePre() {

		showProgress("正在上传文件信息...");
		String hashCode = SecurityUtil.SHA512(FileUtil.File2byte(mVideoPath));
		String imei = MyApplication.getInstance().getDeviceImei();
		ApiManager.getInstance().uploadPreserveFile(mVideoName,MyConstants.VIDEOTYPE,
				ll+"", hashCode, mDate, loc,time ,imei,
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
								//上传
//								startUpLoad(0, pkValue);
							String url=	datas.getFileUrl();
								FileInfo info=new FileInfo();
                               	info.setFileName(mVideoName);
                               	info.setFilePath(mVideoPath);
                               	info.setFileSize(ll+"");
                               	info.setResourceId(pkValue);
                               	info.setObjectKey(url);
                              //上传文件
 							   UpLoadManager.getInstance().resuambleUpload(info);
								finish();
						
							} else {
								Toaster.showToast(VideoPreserved.this,
										bean.getMsg());
							}
						} else {
							Toaster.showToast(VideoPreserved.this, "请求失败");
						}
					}

				});
	}
	/**
	 * 获取文件上传到的位置
	 * @param pkValue
	 */
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
						Toaster.showToast(VideoPreserved.this, bean.getMsg());
					}
				} else {
					Toaster.showToast(VideoPreserved.this, "请求失败");
				}
			}

		});
	}
	/**
	 * 上传文件的接口
	 * @param position
	 * @param resourceId
	 */
	private void startUpLoad(int position, int resourceId) {
		Toaster.showToast(VideoPreserved.this, "文件正在上传，请在传输列表查看");
		FileInfo info=new FileInfo();
		info.setFileName(mVideoName);
		info.setFilePath(mVideoPath);
		info.setFileSize(ll+"");
		info.setPosition(position);
		info.setResourceId(resourceId);
	//	UpLoadManager.getInstance().startUpload(info);
		
		
	}
	/**
	 * 定位
	 */
	private void getLocation(){
		  BaiduLocationUtil.getLocation(getApplicationContext(), new locationListener() {
				@Override
				public void location(String s) {
					loc = s;
					
				}
			});
	}
	//保存录像的数据到数据库
	private void saveToDB() {
			DbBean dbBean =new DbBean();
			dbBean.setTitle(mVideoName);
			dbBean.setCreateTime(mDate);
			dbBean.setResourceUrl(mVideoPath);
			dbBean.setType(MyConstants.VIDEO);
			dbBean.setFileSize(mVideoSize);
			dbBean.setLocation(loc);
			dbBean.setRecordTime(time);
			SqlDao.getSQLiteOpenHelper(this).save(dbBean, MyConstants.TABLE_MEDIA_DETAIL);
		
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
						saveToDB();//保存到数据库
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
