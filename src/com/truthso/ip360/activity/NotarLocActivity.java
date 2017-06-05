package com.truthso.ip360.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;

import cz.msebera.android.httpclient.Header;

/**
 * @despriction :公证处所在地址
 *
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2017/6/5 15:01
 * @version 1.3
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class NotarLocActivity extends BaseActivity{
    private ListView listview;
//    private static final String[] strs = new String[] {"北京"};
    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        getCityDatas();
        listview = (ListView) findViewById(R.id.listview);
//        listview.setAdapter(new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, strs));

    }

    @Override
    public int setLayout() {
        return R.layout.activity_notarloc;
    }

    @Override
    public String setTitle() {
        return "选择公证处";
    }

    /**
     * 获取城市信息
     */
    public void  getCityDatas(){
        ApiManager.getInstance().getNotaryCity(new ApiCallback() {
            @Override
            public void onApiResult(int errorCode, String message, BaseHttpResponse response) {

            }

            @Override
            public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

}
