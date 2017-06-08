package com.truthso.ip360.activity;

import android.net.Uri;
import android.os.Bundle;

import com.truthso.ip360.adapter.MyNotarFileAdapter;
import com.truthso.ip360.bean.NotarMsg;
import com.truthso.ip360.bean.NotarMsgBean;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.view.RefreshListView;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @version 1.3
 * @despriction :个人中心，我的公证列表
 * @date 创建时间：2017/5/24 16:38
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class MyNotarFile extends BaseActivity implements RefreshListView.OnRefreshListener, RefreshListView.OnloadListener {
    private RefreshListView listView;
    private int pagerNumber = 1;
    private List<NotarMsg> list = new ArrayList<NotarMsg>();
    private MyNotarFileAdapter adapter;
    private List<NotarMsg> datas;
    private NotarMsgBean bean;

    @Override
    public void initData() {

    }

    /**
     * 获取数据
     */
    private void getData() {
        showProgress("正在加载...");
        ApiManager.getInstance().getNotarMsg(pagerNumber, 10, new ApiCallback() {
            @Override
            public void onApiResult(int errorCode, String message, BaseHttpResponse response) {
                hideProgress();
                //停止刷新
                listView.onRefreshFinished();
                listView.onLoadFinished();
                bean = (NotarMsgBean) response;
                if (!CheckUtil.isEmpty(bean)){
                        if (bean.getCode() == 200){
                            datas = bean.getDatas();
                            if (!CheckUtil.isEmpty(datas)) {
                                list.addAll(datas);
                            }
                            adapter.notifyDataChange(list);
                        }else{
                            Toaster.showToast(MyNotarFile.this,bean.getMsg());
                        }
                }else{
                    adapter.notifyDataChange(list);
                    Toaster.showToast(MyNotarFile.this,"未查询到数据");
                }
            }

            @Override
            public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    @Override
    public void initView() {
        listView = (RefreshListView) findViewById(R.id.lv_listview);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        listView.setOnLoad(true);
        listView.setOnRefresh(true);

        adapter = new MyNotarFileAdapter(this, list);
        listView.setAdapter(adapter);
        getData();
    }

    @Override
    public int setLayout() {
        return R.layout.activity_mynoar_file;
    }

    @Override
    public String setTitle() {
        return "我的公证";
    }

    @Override
    public void toRefresh() {
       refreshPage();
    }

    //刷新页面
    public void refreshPage() {
        list.clear();
        pagerNumber=1;
        getData();
    }

    @Override
    public void toOnLoad() {
//        lastPosition = 0;
        pagerNumber++;
        getData();
    }
}
