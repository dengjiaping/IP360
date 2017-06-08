package com.truthso.ip360.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.truthso.ip360.utils.CheckUtil;

/**
 * @despriction :申请公正 预约制证
 *
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2017/5/23 19:40
 * @version 1.3
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class MackCardActivity extends BaseActivity{
    private String status,receiverDate,notarOfficeName,notaryOfficeAddress;
    private LinearLayout ll_lingqu,ll_zhizheng;
    private TextView tv_lingqu_date,tv_gongzhengchu_name,tv_gonzhengchu_loc;
    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        ll_lingqu = (LinearLayout) findViewById(R.id.ll_lingqu);
        ll_zhizheng = (LinearLayout) findViewById(R.id.ll_zhizheng);
        tv_lingqu_date = (TextView) findViewById(R.id.tv_lingqu_date);
        tv_gongzhengchu_name = (TextView) findViewById(R.id.tv_gongzhengchu_name);
        tv_gonzhengchu_loc = (TextView) findViewById(R.id.tv_gonzhengchu_loc);
//        intent_5.putExtra("status","已公证");
//        intent_5.putExtra("receiverDate",notarnum2.getReceiverDate());//公证书领取时间
//        intent_5.putExtra("notarOfficeName",notarOfficeName);//公证处名称
//        intent_5.putExtra("notaryOfficeAddress",notarnum2.getNotaryOfficeAddress());
        status = getIntent().getStringExtra("status");

        if (!CheckUtil.isEmpty(status)){
            if (status.equals("等待制证")){

            }else if(status.equals("已公证")){
                receiverDate  =  getIntent().getStringExtra("receiverDate");
                notarOfficeName = getIntent().getStringExtra("notarOfficeName");
                notaryOfficeAddress = getIntent().getStringExtra("notaryOfficeAddress");
                ll_lingqu.setVisibility(View.VISIBLE);
                ll_zhizheng.setVisibility(View.GONE);
                tv_lingqu_date.setText(receiverDate);
                tv_gongzhengchu_name.setText(notarOfficeName);
                tv_gonzhengchu_loc.setText(notaryOfficeAddress);

            }
        }





    }

    @Override
    public int setLayout() {
        return R.layout.activity_makecard;
    }

    @Override
    public String setTitle() {
        return "预约取证";
    }
}
