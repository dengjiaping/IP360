package com.truthso.ip360.activity;

import android.util.Log;

import com.truthso.ip360.adapter.MyNotarFileAdapter;
import com.truthso.ip360.adapter.NotarFileDetailAdapter;
import com.truthso.ip360.bean.CloudEviItemBean;
import com.truthso.ip360.bean.File;
import com.truthso.ip360.bean.FileBean;
import com.truthso.ip360.bean.NotarMsg;
import com.truthso.ip360.bean.NotarMsgBean;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.view.RefreshListView;
import com.truthso.ip360.view.xrefreshview.LogUtils;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.truthso.ip360.constants.URLConstant.getNotarInfo;

/**
 * @despriction :公证列表中点击条目中的查看详情
 *
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2017/6/6 21:57
 * @version 1.3
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class NotarFileDetail extends BaseActivity implements RefreshListView.OnRefreshListener, RefreshListView.OnloadListener {
    private RefreshListView listView;
    private int pagerNumber = 1;
    private List<File> list = new ArrayList<File>();
    private List<File> datas;
    private NotarFileDetailAdapter adapter;
    private FileBean bean;
    private String pkValue;
    private int pkValue_int;
    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        pkValue = getIntent().getStringExtra("pkValue");
        if (!CheckUtil.isEmpty(pkValue)){
            pkValue_int = Integer.parseInt(pkValue);
        }
        listView = (RefreshListView) findViewById(R.id.lv_listview);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        listView.setOnLoad(true);
        listView.setOnRefresh(true);
        adapter = new NotarFileDetailAdapter(this,list);
        listView.setAdapter(adapter);
        getData();
    }

    @Override
    public int setLayout() {
        return R.layout.activity_notar_filedetail;
    }

    @Override
    public String setTitle() {
        return "公证详情";
    }

    @Override
    public void toRefresh() {
        list.clear();
        getData();
    }

    @Override
    public void toOnLoad() {
        pagerNumber++;
        pagerNumber=1;
        getData();
    }
    /**
     * 获取数据
     */
    private void getData() {
        showProgress("正在加载...");
        ApiManager.getInstance().getNotarInfo(pagerNumber, 10,pkValue_int, new ApiCallback() {
            @Override
            public void onApiResult(int errorCode, String message, BaseHttpResponse response) {
                hideProgress();
                //停止刷新
                listView.onRefreshFinished();
                listView.onLoadFinished();
                bean = (FileBean) response;
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
                        Toaster.showToast(NotarFileDetail.this,bean.getMsg());
                    }
                }else{
                    adapter.notifyDataChange(list);
                    if (list.size() == 0) {
                    } else {
                        listView.setLoadComplete("没有更多数据了");
                    }
                }

            }

            @Override
            public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
