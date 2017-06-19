package com.truthso.ip360.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.utils.CheckUtil;

/**
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @version 1.3
 * @despriction :公正信息审核
 * @date 创建时间：2017/5/23 19:35
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class MsgCheckActivity extends BaseActivity implements View.OnClickListener {
    private String reason, notarName, notarOfficeName, notarOfferAddress, requestName, receiver, applicationCard, receiverName, receiverCard, fileMount, pkValue;
    private TextView tv_reason;
    private Button btn_commitagin;
    private ImageView iv_icon;
    @Override
    public void initData() {

    }

    @Override
    public void initView() {
//        intent_0.putExtra("reason",reason);//审核拒绝的原因
//        intent_0.putExtra("notarName",notarName);//公证名称
//        intent_0.putExtra("notarOfficeName",notarOfficeName);//公证处名称
//        intent_0.putExtra("notarOfferAddress",notarOfferAddress);//公证处地址
//        intent_0.putExtra("requestName",requestName);//申请人姓名
//        intent_0.putExtra("receiver",receiver);//领取人是本人还是其他自然人
//        intent_0.putExtra("applicationCard",applicationCard);//申请人证件号
//        intent_0.putExtra("receiverName",receiverName);//领取人姓名
//        intent_0.putExtra("receiverCard",receiverCard);//领取人身份证号
//        intent_0.putExtra("fileMount",fileMount);//文件个数
//        intent_0.putExtra("pkValue",pkValue);//此条公证服务的id
        reason = getIntent().getStringExtra("reason");
        notarName = getIntent().getStringExtra("notarName");
        notarOfficeName = getIntent().getStringExtra("notarOfficeName");
        notarOfferAddress = getIntent().getStringExtra("notarOfferAddress");
        requestName = getIntent().getStringExtra("requestName");
        receiver = getIntent().getStringExtra("receiver");
        applicationCard = getIntent().getStringExtra("applicationCard");
        receiverName = getIntent().getStringExtra("receiverName");
        receiverCard = getIntent().getStringExtra("receiverCard");
        fileMount = getIntent().getStringExtra("fileMount");
        pkValue = getIntent().getStringExtra("pkValue");
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        btn_commitagin = (Button) findViewById(R.id.btn_commitagin);
        btn_commitagin.setOnClickListener(this);
        tv_reason = (TextView) findViewById(R.id.tv_reason);
        if(!CheckUtil.isEmpty(reason)){//审核没通过的原因
            tv_reason.setText(reason);
            btn_commitagin.setVisibility(View.VISIBLE);
            iv_icon.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int setLayout() {
        return R.layout.activity_msgcheck;
    }

    @Override
    public String setTitle() {
        return "信息审核";
    }


    @Override
    public void onClick(View v) {
       Intent intent = new Intent(this,CommitAgainNotarMsg.class);
//        intent.putExtra("reason",reason);//审核拒绝的原因
        intent.putExtra("notarName",notarName);//公证名称
        intent.putExtra("notarOfficeName",notarOfficeName);//公证处名称
        intent.putExtra("notarOfferAddress",notarOfferAddress);//公证处地址
        intent.putExtra("requestName",requestName);//申请人姓名
        intent.putExtra("receiver",receiver);//领取人是本人还是其他自然人
        intent.putExtra("applicationCard",applicationCard);//申请人证件号
        intent.putExtra("receiverName",receiverName);//领取人姓名
        intent.putExtra("receiverCard",receiverCard);//领取人身份证号
        intent.putExtra("fileMount",fileMount);//文件个数
        intent.putExtra("pkValue",pkValue);//此条公证服务的id
        startActivity(intent);
    }
}
