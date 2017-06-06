package com.truthso.ip360.activity;

import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
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
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void initData() {
        getData();
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
                            } else {
                                if (list.size() == 0) {
                                } else {
                                    listView.setLoadComplete("没有更多数据了");
                                }
                            }

                            adapter.notifyDataChange(list);
                        }else{
                            Toaster.showToast(MyNotarFile.this,bean.getMsg());
                        }
                }else{
                    Toaster.showToast(MyNotarFile.this,"加载失败");
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

    }

    @Override
    public void toOnLoad() {
//        lastPosition = 0;
        pagerNumber++;
        getData();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("MyNotarFile Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
