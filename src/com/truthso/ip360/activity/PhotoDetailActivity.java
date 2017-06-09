package com.truthso.ip360.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.ImageLoaderUtil;
import com.truthso.ip360.view.TouchImageView;



/**
 * @despriction :照片详情(原图)
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-8-8下午3:00:50
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class PhotoDetailActivity extends BaseActivity {
	private TouchImageView touchImageView;
	private String url;

	@Override
	public void initData() {
		url=getIntent().getStringExtra("url");
	}

	@Override
	public void initView() {

		touchImageView = (TouchImageView) findViewById(R.id.iv_touch);
		if (url.contains("http")) {
			showProgress("加载中...");
			ImageLoaderUtil.dispalyImage(url, touchImageView, new ImageLoadingListener() {

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
					touchImageView.setImageBitmap(arg2);
				}

				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
				}
			});
		} else {
			touchImageView.setImageBitmap(BitmapFactory.decodeFile(url));
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
