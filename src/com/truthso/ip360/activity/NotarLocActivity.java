package com.truthso.ip360.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.truthso.ip360.adapter.CityAdapter;
import com.truthso.ip360.adapter.NotarLocAdapter;
import com.truthso.ip360.adapter.NotarOfficAdapter;
import com.truthso.ip360.bean.City;
import com.truthso.ip360.bean.NotarCityBean;
import com.truthso.ip360.bean.Notary;
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
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @version 1.3
 * @despriction :公证处所在地址
 * @date 创建时间：2017/6/5 15:01
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class NotarLocActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ListView listview;
    private NotarCityBean bean;
    private String page_type, citycode;
    private LinearLayout ll_reciver;
    private TextView tv_self,tv_other;

    @Override
    public void initData() {
        page_type = getIntent().getStringExtra("page_type");
        citycode = getIntent().getStringExtra("citycode");
    }

    @Override
    public void initView() {
        ll_reciver = (LinearLayout) findViewById(R.id.ll_reciver);
        listview = (ListView) findViewById(R.id.listview);
        if (page_type != null && page_type.equals("receiver")) {
            listview.setVisibility(View.GONE);
            ll_reciver.setVisibility(View.VISIBLE);
            tv_self= (TextView) findViewById(R.id.tv_self);
            tv_other= (TextView) findViewById(R.id.tv_other);
            tv_self.setOnClickListener(this);
            tv_other.setOnClickListener(this);
        } else {
            listview.setVisibility(View.VISIBLE);
            ll_reciver.setVisibility(View.GONE);
            listview.setOnItemClickListener(this);
            getCityDatas();
        }

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
    public void getCityDatas() {
        ApiManager.getInstance().getNotaryCity(new ApiCallback() {
            @Override
            public void onApiResult(int errorCode, String message, BaseHttpResponse response) {
                if (response != null) {
                    bean = (NotarCityBean) response;
                    if (bean.getCode() == 200) {
                        Log.i("djj", page_type + ":" + citycode);
                        if (page_type != null && page_type.equals("notaryoffic")) {
                            List<Notary> datas = bean.getDatas().getNotary();
                            List<Notary> notarys = bean.getDatas().getNotary();
                            for (int i = 0; i < notarys.size(); i++) {
                                if (notarys.get(i).getCityCode() == citycode) {
                                    datas.add(notarys.get(i));
                                }
                            }
                            listview.setAdapter(new NotarOfficAdapter(datas, NotarLocActivity.this));
                        } else {
                            listview.setAdapter(new NotarLocAdapter(bean.getDatas().getProvince(), NotarLocActivity.this));
                        }
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
        List<City> cities = new ArrayList<>();
        for (int i = 0; i < bean.getDatas().getCity().size(); i++) {
            if (bean.getDatas().getCity().get(i).getProvinceCode().equals(provinceCode)) {
                cities.add(bean.getDatas().getCity().get(i));
            }
        }
        listview.setAdapter(new CityAdapter(cities, NotarLocActivity.this, province.getProvinceName()));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_other:
                Intent intent=new Intent();
                intent.putExtra("receiver","1");
                setResult(101,intent);
                finish();
                break;
            case R.id.tv_self:
                Intent intent1=new Intent();
                intent1.putExtra("receiver","2");
                setResult(101,intent1);
                finish();
                break;
        }
    }
}
