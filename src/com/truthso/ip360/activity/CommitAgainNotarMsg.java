package com.truthso.ip360.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;

import cz.msebera.android.httpclient.Header;

/**
 * @despriction :公证信息重新提交页面
 *
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2017/6/8 14:41
 * @version 1.3
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class CommitAgainNotarMsg extends BaseActivity implements View.OnClickListener {
    private String reason, notarName, notarOfficeName, notarOfferAddress, requestName, receiver, applicationCard, receiverName, receiverCard, fileMount, pkValue;
    private EditText et_name,et_fenshu,et_huji,et_currloc,et_name_lingqu,et_carsid_lingqu,et_phonenum,et_email;
    private TextView tv_zhengju,tv_name_shenqing,tv_cardid_shenqing,tv_city_name,tv_gongzhengchu,tv_receiver;
    private Button btn_commit;
    private int fenshu_int;
    private Dialog alertDialog;
    private String name,fenshu,huji,currloc,name_lingqu,cardid_lingqu,phonenum,email;
    private String cityName,gongzhengchu,tvreceiver;
    @Override
    public void initData() {

    }

    @Override
    public void initView() {
//        reason = getIntent().getStringExtra("reason");
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


        tv_zhengju = (TextView) findViewById(R.id.tv_zhengju);
        tv_zhengju.setText("您正在将"+fileMount+"个证据统一申请公证");
        et_name = (EditText) findViewById(R.id.et_name);
        et_name .setText(notarName);
        et_fenshu = (EditText) findViewById(R.id.et_fenshu);
        et_huji = (EditText) findViewById(R.id.et_huji);
        et_currloc = (EditText) findViewById(R.id.et_currloc);
        et_name_lingqu = (EditText) findViewById(R.id.et_name_lingqu);
        et_name_lingqu.setText(receiverName);
        et_carsid_lingqu = (EditText) findViewById(R.id.et_carsid_lingqu);
        et_carsid_lingqu.setText(receiverCard);
        et_phonenum = (EditText) findViewById(R.id.et_phonenum);
        et_email = (EditText) findViewById(R.id.et_email);
        tv_name_shenqing = (TextView) findViewById(R.id.tv_name_shenqing);
        tv_name_shenqing.setText(requestName);
        tv_cardid_shenqing = (TextView) findViewById(R.id.tv_cardid_shenqing);
        tv_cardid_shenqing.setText(applicationCard);
        btn_commit = (Button) findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(this);
        tv_city_name= (TextView) findViewById(R.id.tv_city_name);
        tv_city_name.setText(notarOfferAddress);
        tv_receiver = (TextView)findViewById(R.id.tv_receiver);
        tv_receiver.setText(receiver);
        tv_gongzhengchu = (TextView) findViewById(R.id.tv_gongzhengchu);
        tv_gongzhengchu.setText(notarOfficeName);

    }

    @Override
    public int setLayout() {
        return R.layout.activity_commitmsg;
    }

    @Override
    public String setTitle() {
        return "信息提交";
    }

    @Override
    public void onClick(View v) {

        name = et_name.getText().toString().trim();
        fenshu = et_fenshu.getText().toString().trim();

        huji = et_huji.getText().toString().trim();
        currloc = et_currloc.getText().toString().trim();
        name_lingqu = et_name_lingqu.getText().toString().trim();
        cardid_lingqu = et_carsid_lingqu.getText().toString().trim();
        phonenum = et_phonenum.getText().toString().trim();
        email = et_email.getText().toString().trim();
        cityName = tv_city_name.getText().toString().trim();
        gongzhengchu = tv_gongzhengchu.getText().toString().trim();
        tvreceiver = tv_receiver.getText().toString().trim();
        if (gongzhengchu.equals("请选择")){
            Toaster.showToast(this,"请选择公证处");
            return;
        }else if(tvreceiver.equals("请选择")){
            Toaster.showToast(this,"请选择领取人");
            return;
        }else if (cityName.equals("请选择")){
            Toaster.showToast(this,"请选择公证处所在地");
            return;
        }else if(CheckUtil.isEmpty(name)){
            Toaster.showToast(this,"请填写申请公证的名称");
            return;
        }else if(CheckUtil.isEmpty(name_lingqu)){
            Toaster.showToast(this,"请填写领取人的姓名");
            return;
        }
        if(CheckUtil.isEmpty(currloc)){
            Toaster.showToast(this,"请填写申请人的现居地址");
            return;
        }else if(currloc.length()<5||currloc.length()>50){
            Toaster.showToast(this,"地址长度在5到50个字之间");
            return;
        }
        if (CheckUtil.isEmpty(cardid_lingqu)){
            Toaster.showToast(this,"请填写领取人的身份证号");
            return;
        }else{
            if(!CheckUtil.isIDFormat(cardid_lingqu)){
                Toaster.showToast(this,"请填写正确格式的领取人的身份证号");
                return;
            }
        }
        if(CheckUtil.isEmpty(phonenum)){
            Toaster.showToast(this,"请填写领取人手机号");
            return;
        }else{
            if (!CheckUtil.isPhoneNum(phonenum)){
                Toaster.showToast(this,"请填写正确格式的领取人手机号");
                return;
            }
        }
        if (CheckUtil.isEmpty(email)){
            Toaster.showToast(this,"请填写领取人邮箱");
            return;
        }else if(!CheckUtil.isEmailFormat(email)){
            Toaster.showToast(this,"请填写领取人正确格式的邮箱");
            return;
        }
        if(CheckUtil.isEmpty(fenshu)){//请输入份数
            Toaster.showToast(this,"请输入份数");
            return;
        }else{
                fenshu_int =  Integer.parseInt(fenshu);
            if (fenshu_int<1||fenshu_int>100){
                Toaster.showToast(this,"份数应该是1-100之间的整数");
                return;
            }
        }
        if(CheckUtil.isEmpty(huji)){
            Toaster.showToast(this,"请填写户籍");
            return;
        }else if(huji.length()<5||huji.length()>50){
            Toaster.showToast(this,"地址长度在5到50个字之间");
            return;
        }

        showDialog("是否确认提交");
    }

    private void commitMsg() {
        showProgress("正在提交信息...");
        ApiManager.getInstance().commitAgainNotarMsg(fenshu_int, huji, currloc, phonenum, email, pkValue, new ApiCallback() {
            @Override
            public void onApiResult(int errorCode, String message, BaseHttpResponse response) {
                hideProgress();
                    if (!CheckUtil.isEmpty(response)){
                        if (response.getCode() == 200){
                            Toaster.showToast(CommitAgainNotarMsg.this,"提交成功");
                            finish();
                        }else{
                            Toaster.showToast(CommitAgainNotarMsg.this,response.getMsg());
                        }
                    }else{
//                        Toaster.showToast(CommitAgainNotarMsg.this,"信息提交失败，请稍后重试");
                    }

            }

            @Override
            public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void showDialog(String str) {
        alertDialog = new AlertDialog.Builder(this).setTitle("温馨提示")
                .setMessage(str).setIcon(R.drawable.ww)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //调提交接口
                        commitMsg();

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                }).create();
        alertDialog.show();
    }
}
