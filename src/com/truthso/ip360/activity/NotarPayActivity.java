package com.truthso.ip360.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.truthso.ip360.bean.DefraymentBean;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;

import cz.msebera.android.httpclient.Header;

/**
 * @despriction :我的公正 支付
 *
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2017/5/23 19:38
 * @version 1.3
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class NotarPayActivity extends BaseActivity implements View.OnClickListener {
private String notarName,notarNum,notarOfficeName,requestName,receiverName,fenshu,fileMount,monery;
    private TextView tv_name,tv_numm,tv_gongzhengchu_name,tv_shenqing,tv_lingqu,tv_fenshu,tv_filemount,tv_money;
    private Button btn_pay;
    private Dialog alertDialog;
    @Override
    public void initData() {

    }

    @Override
    public void initView() {
//        intent_3.putExtra("notarName",notarName);//公证名称
//        intent_3.putExtra("notarNum",notarnum2.getNotaryNum());//公正编号
//        intent_3.putExtra("notarOfficeName",notarOfficeName);//公证处名称
//        intent_3.putExtra("requestName",requestName);//申请人姓名
//        intent_3.putExtra("receiverName",receiverName);//领取人姓名
//        intent_3.putExtra("fenshu", notarnum2.getNotaryPageNum());//公证书份数
//        intent_3.putExtra("fileMount",fileMount);//文件个数
//        intent_3.putExtra("monery",notarnum2.getMonery());//待支付费用
        notarName = getIntent().getStringExtra("notarName");
        notarNum = getIntent().getStringExtra("notarNum");
        notarOfficeName = getIntent().getStringExtra("notarOfficeName");
        requestName = getIntent().getStringExtra("requestName");
        receiverName = getIntent().getStringExtra("receiverName");
        fenshu = getIntent().getStringExtra("fenshu");
        fileMount = getIntent().getStringExtra("fileMount");
        monery = getIntent().getStringExtra("monery");

        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_name.setText(notarName);
        tv_numm = (TextView) findViewById(R.id.tv_numm);
        tv_numm.setText(notarNum);
        tv_gongzhengchu_name = (TextView) findViewById(R.id.tv_gongzhengchu_name);
        tv_gongzhengchu_name.setText(notarOfficeName);
        tv_shenqing = (TextView) findViewById(R.id.tv_shenqing);
        tv_shenqing.setText(requestName);
        tv_lingqu = (TextView) findViewById(R.id.tv_lingqu);
        tv_lingqu.setText(receiverName);
        tv_fenshu = (TextView) findViewById(R.id. tv_fenshu);
        tv_fenshu.setText(fenshu+"份");
        tv_filemount = (TextView) findViewById(R.id.tv_filemount);
        tv_filemount.setText(fileMount);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_money.setText(monery+"元");
        btn_pay = (Button) findViewById(R.id.btn_pay);
        btn_pay.setOnClickListener(this);

    }

    @Override
    public int setLayout() {
        return R.layout.activity_notarpay;
    }

    @Override
    public String setTitle() {
        return "支付费用";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_pay:
                showDialog();
                break;
        }
    }

    private void showDialog() {
        alertDialog = new AlertDialog.Builder(NotarPayActivity.this).
                setTitle("温馨提示").
                setMessage("是否确认支付？").
                setIcon(R.drawable.ww).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Pay();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        }).create();
        alertDialog.show();
    }

    private void Pay() {
        showProgress("正在支付...");
        ApiManager.getInstance().defrayment(notarNum, new ApiCallback() {
            @Override
            public void onApiResult(int errorCode, String message, BaseHttpResponse response) {
                hideProgress();
                DefraymentBean bean = (DefraymentBean) response;
                if (!CheckUtil.isEmpty(bean)){
                    if(bean.getCode() == 200){
//                        status:0-正常扣费 1-欠费 2-余额不足
                        String  status = bean.getDatas().getStatus();
                        if (status.equals("0")||status.equals("1")){
                            Intent intent_4 = new Intent(NotarPayActivity.this, MackCardActivity.class);
                            startActivity(intent_4);
                            finish();
                        }else{
                            showDialog1(bean.getDatas().getShowText());
                        }


                    }else{
                        Toaster.showToast(NotarPayActivity.this,bean.getMsg());
                    }
                }else{
                    Toaster.showToast(NotarPayActivity.this,"支付失败，请稍后重试！");
                }
            }

            @Override
            public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    /**
     * 余额不足
     * @param str
     */
    private void showDialog1(String str) {
        alertDialog = new AlertDialog.Builder(NotarPayActivity.this).
                setTitle("温馨提示").
                setMessage(str).
                setIcon(R.drawable.ww).
                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        }).create();
        alertDialog.show();
    }
}
