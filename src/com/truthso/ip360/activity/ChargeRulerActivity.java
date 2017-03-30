package com.truthso.ip360.activity;

import android.webkit.WebView;

import com.truthso.ip360.constants.URLConstant;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;

import cz.msebera.android.httpclient.Header;

/**
 * @despriction :首页计价规则
 *
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2017/3/30 14:46
 * @version 1.1
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class ChargeRulerActivity extends BaseActivity {
    private WebView webview;
    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        webview = (WebView) findViewById(R.id.webview);
        //允许JavaScript执行
        webview.getSettings().setJavaScriptEnabled(true);
        //找到Html文件，也可以用网络上的文件
//        webview.loadUrl(URLConstant.getBillingRules);
        webview.loadUrl("www.baidu.com");
        //找到Html文件，也可以用网络上的文件
    }
    @Override
    public int setLayout() {
        return R.layout.activity_charge_rules;
    }

    @Override
    public String setTitle() {
        return "计价规则";
    }
}
