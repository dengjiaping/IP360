package com.truthso.ip360.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;

import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.SharePreferenceUtil;

/**
 * 
 * 闪屏页面
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-6-17下午3:06:14
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class SplashActivty extends Activity {

	private Context ctx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		ctx = this;
		handler.sendEmptyMessageDelayed(99, 2000);
	
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			boolean isFirstOpen = (Boolean) SharePreferenceUtil.getAttributeByKey(SplashActivty.this,
					MyConstants.APP_KEY, MyConstants.APP_ISFIRST_IN,
					SharePreferenceUtil.VALUE_IS_BOOLEAN);
			String token=MyApplication.getInstance().getTokenId();
			Intent intent;
			if (isFirstOpen) {	
				if(CheckUtil.isEmpty(token)){
					// 进登录界面
					 intent = new Intent(ctx, LoginActivity.class);
				}else{
					intent = new Intent(ctx, MainActivity.class);
				}
			} else {
				// 第一次启动先进入引导页
				 intent = new Intent(ctx, GuideActivity.class);
			}
	
			startActivity(intent);
			finish();
		};
		
	};

}
