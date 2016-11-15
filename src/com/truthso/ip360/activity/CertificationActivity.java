package com.truthso.ip360.activity;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.truthso.ip360.activity.R.string;
import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.bean.CertificateInfoBean;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.NetStatusUtil;
import com.truthso.ip360.utils.SharePreferenceUtil;

import cz.msebera.android.httpclient.Header;

/**
 * @despriction :查看证书的页面
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-8-10下午6:41:08
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class CertificationActivity extends BaseActivity {
	private int pkValue,type;
	private WebView webview;
	@Override
	public void initData() {
		pkValue = getIntent().getIntExtra("pkValue", 0);
		type = getIntent().getIntExtra("type", 0);
		Log.i("djj",pkValue+"_"+type);
	}

	@Override
	public void initView() {

		webview = (WebView) findViewById(R.id.webview);
		WebSettings webSettings = webview.getSettings();
		  
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);  
		webSettings.setUseWideViewPort(true);//关键点  
		  
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);  
		      
		webSettings.setDisplayZoomControls(false);  
		webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本  
		webSettings.setAllowFileAccess(true); // 允许访问文件  
		webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮  
		webSettings.setSupportZoom(true); // 支持缩放  
		webSettings.setLoadWithOverviewMode(true);
		
		DisplayMetrics metrics = new DisplayMetrics();  
		  getWindowManager().getDefaultDisplay().getMetrics(metrics);  
		  int mDensity = metrics.densityDpi;  
		  if (mDensity == 240) {   
		   webSettings.setDefaultZoom(ZoomDensity.FAR);  
		  } else if (mDensity == 160) {  
		     webSettings.setDefaultZoom(ZoomDensity.MEDIUM);  
		  } else if(mDensity == 120) {  
		   webSettings.setDefaultZoom(ZoomDensity.CLOSE);  
		  }else if(mDensity == DisplayMetrics.DENSITY_XHIGH){  
		   webSettings.setDefaultZoom(ZoomDensity.FAR);   
		  }else if (mDensity == DisplayMetrics.DENSITY_TV){  
		   webSettings.setDefaultZoom(ZoomDensity.FAR);   
		  }else{  
		      webSettings.setDefaultZoom(ZoomDensity.MEDIUM);  
		  } 
		/**  
		 * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：  
		 * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放  
		 */  
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);


		getPort();

	}

	private void getPort() {
		showProgress("正在加载...");
		Log.i("djj",pkValue+":"+type);
		ApiManager.getInstance().getCertificateInfo(pkValue, type, new ApiCallback() {
			
			@Override
			public void onApiResultFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				
			}
			
			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
				hideProgress();
				CertificateInfoBean bean = (CertificateInfoBean) response;
				if (!CheckUtil.isEmpty(bean)) {
					if (bean.getCode() == 200) {
						webview.loadUrl(bean.getDatas().getCertificateUrl());
					}else{
						Toaster.showToast(CertificationActivity.this,bean.getMsg());
					}
				}else{
					Toaster.showToast(CertificationActivity.this, "加载证书失败");
				}
			}
		});
		
	}

	@Override
	public int setLayout() {
		return R.layout.activity_look_certification;
	}

	@Override
	public String setTitle() {
		return "查看证书";
	}
	/**
     * 按键响应，在WebView中查看网页时，按返回键的时候按浏览历史退回,如果不做此项处理则整个WebView返回退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack())
        {
            // 返回键退回
        	webview.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up
        // to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }
}
