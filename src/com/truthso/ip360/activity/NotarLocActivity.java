package com.truthso.ip360.activity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.truthso.ip360.adapter.CityAdapter;
import com.truthso.ip360.adapter.NotarLocAdapter;
import com.truthso.ip360.bean.City;
import com.truthso.ip360.bean.NotarCityBean;
import com.truthso.ip360.bean.Province;
import com.truthso.ip360.event.CityEvent;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * @despriction :公证处所在地址
 *
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2017/6/5 15:01
 * @version 1.3
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class NotarLocActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ListView listview;
    private NotarCityBean bean;
    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        getCityDatas();
        listview = (ListView) findViewById(R.id.listview);
        listview.setOnItemClickListener(this);
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
                if(response!=null){
                    bean= (NotarCityBean) response;
                    if(bean.getCode()==200){
                        listview.setAdapter(new NotarLocAdapter(bean.getDatas().getProvince(),NotarLocActivity.this));
                    }
                }
            }

            @Override
            public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Province province = bean.getDatas().getProvince().get(position);
        String provinceCode = province.getProvinceCode();
        List<City> cities=new ArrayList<>();
        for (int i=0;i<bean.getDatas().getCity().size();i++){
            if(bean.getDatas().getCity().get(i).getProvinceCode().equals(provinceCode)){
                cities.add(bean.getDatas().getCity().get(i));
            }
        }
        listview.setAdapter(new CityAdapter(cities,NotarLocActivity.this,province.getProvinceName()));
    }




}
