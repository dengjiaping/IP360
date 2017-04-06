package com.truthso.ip360.activity;

import android.webkit.WebView;

/**
 * Created by summer on 2017/4/6.
 */

public class DocumentDetailActivity extends BaseActivity {
    private WebView webView;
    @Override
    public void initData() {

    }

    @Override
    public void initView() {
       String url= getIntent().getStringExtra("url");
        webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl(url);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_document_detail;
    }

    @Override
    public String setTitle() {
        return null;
    }
}
