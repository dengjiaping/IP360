package com.truthso.ip360.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.truthso.ip360.utils.AdvancedWebView;

/**
 * Created by summer on 2017/4/6.
 */

public class DocumentDetailActivity extends BaseActivity{
    private WebView webView;
    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        String url = getIntent().getStringExtra("url");
        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });

// webview必须设置支持Javascript才可打开
        webView.getSettings().setJavaScriptEnabled(true);

// 设置此属性,可任意比例缩放
        webView.getSettings().setUseWideViewPort(true);

//通过在线预览office文档的地址加载

        
        webView.loadUrl(url);

    }

    @Override
    public int setLayout() {
        return R.layout.activity_document_detail;
    }

    @Override
    public String setTitle() {
        return "文档预览";
    }



}
