package com.truthso.ip360.activity;


import android.view.View;
import android.widget.LinearLayout;

import com.truthso.ip360.adapter.CloudEvidenceAdapter;
import com.truthso.ip360.bean.CloudEviItemBean;
import com.truthso.ip360.bean.CloudEvidenceBean;
import com.truthso.ip360.fragment.UpdateItem;
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
 * @despriction :证据列表 二级页面下拉条目的公证详情
 *
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2017/6/2 11:49
 * @version 1.3
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class SecordLevelActivity extends BaseActivity implements RefreshListView.OnRefreshListener,UpdateItem, RefreshListView.OnloadListener {
    private CloudEvidenceAdapter adapter;
    private int lastPosition;
    private List<CloudEviItemBean> list=new ArrayList<CloudEviItemBean>();
    private List<CloudEviItemBean> datas;
    private RefreshListView listView;
    private UpdateItem updateItem;
    private int pagerNumber = 1;
    private int type,pkValue;
    @Override
    public void initData() {

    }

    @Override
    public void initView() {

        listView =  (RefreshListView)findViewById(R.id.lv_cloudevidence);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        listView.setOnLoad(true);
        listView.setOnRefresh(true);
        type  =getIntent().getIntExtra("type",0);
         pkValue =getIntent().getIntExtra("pkValue",0);
//        listView = (ListView) findViewById(R.id.listview);
        //获取二级页面的数据
        getSubEvidence(1);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_level_filedatail;
    }

    @Override
    public String setTitle() {
        return "证据详情";
    }

    /**
     * 获取二级链接的数据
     */
    public void getSubEvidence(int pagerNumber){
        showProgress("正在加载...");
        ApiManager.getInstance().getSubEvidence(type, pkValue, pagerNumber, 10, new ApiCallback() {
            @Override
            public void onApiResult(int errorCode, String message, BaseHttpResponse response) {
                hideProgress();
                CloudEvidenceBean bean= (CloudEvidenceBean) response;
                if (!CheckUtil.isEmpty(bean)) {
                    if (bean.getCode() == 200) {
                        datas = bean.getDatas();
                        if (!CheckUtil.isEmpty(datas)) {
                            list.addAll(datas);
                            adapter=new CloudEvidenceAdapter(SecordLevelActivity.this,list,type,0);
                            adapter.setUpdateItem(SecordLevelActivity.this);
                            listView.setAdapter(adapter);
                        } else {
                            if(list.size()==0){
//                                actionBar.setRightDisEnable();
//                                actionBar.setRightText("");
                            }else {
                                listView.setLoadComplete("没有更多数据了");
                            }
                        }
                        adapter.notifyDataChange(list,type, 0);
                    } else {
                        Toaster.showToast(SecordLevelActivity.this, bean.getMsg());
                    }
                } else {
                    Toaster.showToast(SecordLevelActivity.this, "数据加载失败请刷新重试");
                }
            }

            @Override
            public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    @Override
    public void toRefresh() {
        lastPosition=0;
        pagerNumber=1;
        list.clear();
        listView.setLoadStart("查看更多");
        getSubEvidence(pagerNumber);
    }

    @Override
    public void toOnLoad() {
        lastPosition=0;
        pagerNumber++;
        getSubEvidence(pagerNumber);
    }

    @Override
    public void update(int position) {
        if(position==adapter.getCount()-1){
            listView.setSelection(position);
        }

        if(position!=lastPosition){
            int fir=listView.getFirstVisiblePosition()-1;
            int las=listView.getLastVisiblePosition()-1;

            if(lastPosition>=fir&&lastPosition<=las){
                View view = listView.getChildAt(lastPosition - fir);
                if(view!=null){
                    LinearLayout ll_option=	(LinearLayout) view.findViewById(R.id.ll_option);
                    if(ll_option.getVisibility()==View.VISIBLE){
                        ll_option.setVisibility(View.GONE);
                        CloudEvidenceAdapter.ViewHolder vh=(CloudEvidenceAdapter.ViewHolder) view.getTag();
                        vh.cb_option.setChecked(false);
                    }
                }
            }
        }
        lastPosition=position;
    }
}
