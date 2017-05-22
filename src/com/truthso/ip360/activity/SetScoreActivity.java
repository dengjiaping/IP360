package com.truthso.ip360.activity;

import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.truthso.ip360.constants.URLConstant;
import com.truthso.ip360.utils.NetStatusUtil;

/**
 * @despriction :关于我们->去评分
 *
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2017/5/22 17:02
 * @version  1.3
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class SetScoreActivity extends BaseActivity{
    private WebView webview;
    private LinearLayout ll_background;
    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        ll_background = (LinearLayout) findViewById(R.id.ll_background);
        webview = (WebView) findViewById(R.id.webview);
        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
         * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        //无网络
        if(!NetStatusUtil.isNetValid(SetScoreActivity.this)){
            ll_background.setBackgroundResource((R.drawable.nonet));
            webview.setVisibility(View.GONE);
        }else{
            //允许JavaScript执行
            webview.getSettings().setJavaScriptEnabled(true);
            //找到Html文件，也可以用网络上的文件
            webview.loadUrl("http://www.pc6.com/az/416354.html");
        }
    }

    @Override
    public int setLayout() {
        return R.layout.activity_setsore;
    }

    @Override
    public String setTitle() {
        return "评分";
    }
}
