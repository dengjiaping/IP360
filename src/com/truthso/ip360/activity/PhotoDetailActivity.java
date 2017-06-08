package com.truthso.ip360.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.truthso.ip360.activity.BaseActivity;
import com.truthso.ip360.activity.R;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.ImageLoaderUtil;
import com.truthso.ip360.utils.StreamTool;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.truthso.ip360.utils.DisplayUtil.getScreenWidth;


/**
 * @despriction :照片详情(原图)
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-8-8下午3:00:50
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class PhotoDetailActivity extends BaseActivity {
	private ImageView iv_photo,iv_chuo;
	private String url;
	private Bitmap mBitmap;

	@Override
	public void initData() {
		url=getIntent().getStringExtra("url");
	}

	private Handler mHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			byte[] bytes= (byte[]) msg.obj;
			mBitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
			if(mBitmap!=null){
				iv_photo.setImageBitmap(mBitmap);
			}else{
				Toast.makeText(PhotoDetailActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
			}
			hideProgress();
		}
	};

	@Override
	public void initView() {
		iv_photo = (ImageView) findViewById(R.id.iv_photo);


		if (url.contains("http")) {
				showProgress("加载中...");
				ImageLoaderUtil.dispalyImage(url, iv_photo, new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						showProgress("正在加载...");
					}

					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						Toaster.showToast(PhotoDetailActivity.this, "加载失败");
						finish();
					}

					@Override
					public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
						hideProgress();
//						iv_chuo.setVisibility(View.VISIBLE);

					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {

					}
				});
			} else {
				ImageLoaderUtil.displayFromSDCardopt(url, iv_photo, null);
			}
	}
		@Override
		public int setLayout () {
			return R.layout.activity_photo_datail;
		}

		@Override
		public String setTitle () {
			return "证据查看";
		}


}
