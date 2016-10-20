package com.truthso.ip360.activity;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.truthso.ip360.bean.AccountStatusBean;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.BaiduLocationUtil;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.GetFileSizeUtil;
import com.truthso.ip360.utils.SharePreferenceUtil;
import com.truthso.ip360.utils.BaiduLocationUtil.locationListener;
import com.truthso.ip360.view.xrefreshview.LogUtils;

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
	private String mVideoPath;
	private String mVideoName;
	private ImageView iv_video;
	private String mVideoSize;
	private String mDate,loc,time;
	private Button btn_preserved,btn_cancel;
	private int minTime;
	private TextView tv_filename,tv_loc,tv_date,tv_filesize,tv_time,tv_account;
	@Override
	public void initData() {
		getLocation();
		mVideoPath = getIntent().getStringExtra("filePath");
		mDate = getIntent().getStringExtra("date");
		loc =getIntent().getStringExtra("loc");
		time = getIntent().getStringExtra("time");
		minTime = getIntent().getIntExtra("minTime", 0);
	}

	@Override
	public void initView() {
	
		
		iv_video = (ImageView) findViewById(R.id.iv_video);
		tv_filename = (TextView) findViewById(R.id.tv_filename);
		
		tv_loc = (TextView) findViewById(R.id.tv_loc);
		
		tv_date = (TextView) findViewById(R.id.tv_date);
		
		tv_filesize = (TextView) findViewById(R.id.tv_filesize);
		
		tv_time = (TextView) findViewById(R.id.tv_time);
		
		tv_account = (TextView) findViewById(R.id.tv_account);
	
		mVideoName = mVideoPath.substring(mVideoPath.lastIndexOf("/") + 1);
		iv_video.setImageBitmap(getVideoThumbnail(mVideoPath, 80, 80,
				MediaStore.Images.Thumbnails.MICRO_KIND));
		mVideoSize = GetFileSizeUtil.FormatFileSize(mVideoPath);
		
		tv_filename.setText(mVideoName);
		tv_loc.setText(loc);
		tv_date.setText(mDate);
		tv_filesize.setText(mVideoSize);
		tv_time.setText(time);
		
		btn_preserved = (Button) findViewById(R.id.btn_preserved);
		btn_preserved.setOnClickListener(this);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(this);
		
	int	useType = (Integer) SharePreferenceUtil.getAttributeByKey(VideoPreserved.this, MyConstants.SP_USER_KEY, "userType",SharePreferenceUtil.VALUE_IS_STRING);
		  if (useType ==1 ) {//用户类型1-付费用户（C）；2-合同用户（B）
			  getport();
		}else if(useType ==2 ){
			String str = minTime+"分钟";
			tv_account.setText(str);
		}
	}

	private void getport() {

		showProgress("正在加载...");
		ApiManager.getInstance().getAccountStatus(MyConstants.PHOTOTYPE, 1, new ApiCallback() {
			
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
							//可以继续保全
						}
						
							String yue = bean.getDatas().getCount()/10 +"."+bean.getDatas().getCount()%10;
							tv_account.setText("￥"+yue);
						
						
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
			saveToDB();
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
			SqlDao.getSQLiteOpenHelper(this).save(dbBean, MyConstants.TABLE_MEDIA_DETAIL);
			finish();
	}
}
