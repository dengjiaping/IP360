package com.truthso.ip360.activity;

import android.webkit.WebView;

import com.truthso.ip360.constants.URLConstant;

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
     /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
         * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        //允许JavaScript执行
        webview.getSettings().setJavaScriptEnabled(true);
        //找到Html文件，也可以用网络上的文件
        webview.loadUrl(URLConstant.getBillingRules);
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
