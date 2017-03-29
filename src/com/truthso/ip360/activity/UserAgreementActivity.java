package com.truthso.ip360.activity;

import android.webkit.WebView;
import android.widget.TextView;

/**
 * 用户协议
 * Created by summer on 2017/3/25.
 */

public class UserAgreementActivity extends BaseActivity {
    private TextView tv_text;
    private WebView webView;
    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        //加载页面
        webView = (WebView) findViewById(R.id.webview);
        //允许JavaScript执行
        webView.getSettings().setJavaScriptEnabled(true);
        //找到Html文件，也可以用网络上的文件
        webView.loadUrl(" file:///android_asset/useragreement.html ");
        // 添加一个对象, 让JS可以访问该对象的方法, 该对象中可以调用JS中的方法
//        webView.addJavascriptInterface(new Contact(), "contact");

    }

    @Override
    public int setLayout() {
        return R.layout.activity_useragreement;
    }

    @Override
    public String setTitle() {
        return "服务协议";
    }
}
