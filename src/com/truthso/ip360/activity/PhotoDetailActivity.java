package com.truthso.ip360.activity;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.truthso.ip360.activity.BaseActivity;
import com.truthso.ip360.activity.R;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.ImageLoaderUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @despriction :照片详情(原图)
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-8-8下午3:00:50
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class PhotoDetailActivity extends BaseActivity {
	private ImageView iv_photo;
	private String url,from;
	@Override
	public void initData() {

		url=getIntent().getStringExtra("url");
		from=getIntent().getStringExtra("from");


		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					URL u=new URL(url);
					HttpURLConnection conn= (HttpURLConnection) u.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(6000);
					conn.addRequestProperty("Refere","http://appapi.truthso.com");
					conn.connect();
					Log.i("djj","code:"+conn.getResponseCode());
					InputStream inputStream = conn.getInputStream();

					byte[] b=new byte[1024];
					int length=0;
					Log.i("djj","haha");
					while ((length=(inputStream.read(b)))!=-1) {
						Log.i("djj","hehe");
					}
				} catch (MalformedURLException e) {
					Log.i("djj","MalformedURLException");
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}


			}
		}).start();
	}

	@Override
	public void initView(){
		 iv_photo = (ImageView) findViewById(R.id.iv_photo);
		/* BitmapUtils bitmap = new BitmapUtils(this);
         bitmap.display(iv_photo, url);*/
      /*  if(from.equals("cloud")){
			ImageLoaderUtil.dispalyImage(url,iv_photo,new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					showProgress("正在加载...");
				}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					Toaster.showToast(PhotoDetailActivity.this, "加载失败");
				}
				
				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
					hideProgress();
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					// TODO Auto-generated method stub
					
				}
			});
		}else {
			ImageLoaderUtil.displayFromSDCardopt(url,iv_photo,null);
		}*/

	}

	@Override
	public int setLayout() {
		return R.layout.activity_photo_datail;
	}

	@Override
	public String setTitle() {
		return "证据查看";
	}

}
