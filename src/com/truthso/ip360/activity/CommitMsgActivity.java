package com.truthso.ip360.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.truthso.ip360.event.CityEvent;
import com.truthso.ip360.event.NotaryOfficEvent;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.view.xrefreshview.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cz.msebera.android.httpclient.Header;

/**
 * @despriction :我的公正—>信息提交页面
 *
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2017/5/23 19:34
 * @version 1.3
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class CommitMsgActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_name,et_fenshu,et_huji,et_currloc,et_name_lingqu,et_carsid_lingqu,et_phonenum,et_email;
    private RelativeLayout rl_gongzhengchu ,rl_gzc_loc,rl_lingquren;
    private Button btn_commit;
    private Dialog alertDialog;
    private String name,fenshu,huji,currloc,name_lingqu,cardid_lingqu,phonenum,email;
    private int notaryId;//公证处id
    private String pkValue; //"2-111,3-223"
    private int type,linkcount;
    private String requestName,requestCardId,requestPhoneNum,requestEmail;
    private TextView tv_zhengju,tv_name_shenqing,tv_cardid_shenqing,tv_city_name,tv_gongzhengchu,tv_receiver;
    private int fenshu_int;
    private String cityName,gongzhengchu,tvreceiver;
    private CityEvent cityEvent;
    private String receiver;
    @Override
    public void initData() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void initView() {
//        type =getIntent().getIntExtra("type",0);
        pkValue = getIntent().getStringExtra("pkValue");
        linkcount = getIntent().getIntExtra("linkcount",0);
        requestName = getIntent().getStringExtra("requestName");
        requestCardId = getIntent().getStringExtra("requestCardId");
        requestPhoneNum = getIntent().getStringExtra("requestPhoneNum");
        requestEmail = getIntent().getStringExtra("requestEmail");

        tv_zhengju = (TextView) findViewById(R.id.tv_zhengju);
        tv_zhengju.setText("您正在将"+linkcount+"个证据统一申请公证");
        et_name = (EditText) findViewById(R.id.et_name);
        et_fenshu = (EditText) findViewById(R.id.et_fenshu);
        et_huji = (EditText) findViewById(R.id.et_huji);
        et_currloc = (EditText) findViewById(R.id.et_currloc);
        et_name_lingqu = (EditText) findViewById(R.id.et_name_lingqu);
        et_carsid_lingqu = (EditText) findViewById(R.id.et_carsid_lingqu);
        et_phonenum = (EditText) findViewById(R.id.et_phonenum);
        et_email = (EditText) findViewById(R.id.et_email);
        rl_gongzhengchu = (RelativeLayout) findViewById(R.id.rl_gongzhengchu);
        rl_gongzhengchu.setOnClickListener(this);
        rl_gzc_loc = (RelativeLayout) findViewById(R.id.rl_gzc_loc);
        tv_name_shenqing = (TextView) findViewById(R.id.tv_name_shenqing);
        tv_name_shenqing.setText(requestName);
        tv_cardid_shenqing = (TextView) findViewById(R.id.tv_cardid_shenqing);
        tv_cardid_shenqing.setText(requestCardId);
        rl_lingquren = (RelativeLayout) findViewById(R.id.rl_lingquren);
        rl_lingquren.setOnClickListener(this);
        btn_commit = (Button) findViewById(R.id.btn_commit);
        tv_city_name= (TextView) findViewById(R.id.tv_city_name);
        tv_receiver = (TextView)findViewById(R.id.tv_receiver);
        tv_gongzhengchu = (TextView) findViewById(R.id.tv_gongzhengchu);
        rl_gzc_loc.setOnClickListener(this);
        btn_commit.setOnClickListener(this);
    }

    @Subscribe
    public void getCity(CityEvent event){
        this.cityEvent=event;
        tv_city_name.setText(event.getProvinceName()+"\t"+event.getCityName());
    }

    private int notarOfficId;
    @Subscribe
    public void getNotaryOffic(NotaryOfficEvent event){
        notarOfficId=event.getNotarOfficId();
        tv_gongzhengchu.setText(event.getNotarOfficName());
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
        switch(v.getId()){
            case R.id.rl_gzc_loc://选择公证处所在地
                Intent intent = new Intent(CommitMsgActivity.this,NotarLocActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_gongzhengchu://选择公证处
                if(cityEvent==null){
                    return;
                }
                Intent intent1 = new Intent(CommitMsgActivity.this,NotarLocActivity.class);
                intent1.putExtra("page_type","notaryoffic");
                intent1.putExtra("citycode",cityEvent.getCityCode());
                startActivity(intent1);
                break;
            case R.id.rl_lingquren://选择领取人 1-本人领取；2-其他自然人领取
                Intent intent2 = new Intent(CommitMsgActivity.this,NotarLocActivity.class);
                intent2.putExtra("page_type","receiver");
                startActivityForResult(intent2,101);
                break;
            case R.id.btn_commit://提交
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


         if(CheckUtil.isEmpty(name)){
                Toaster.showToast(this,"请填写申请公证的名称");
                return;
            }else if (gongzhengchu.equals("请选择")){
                Toaster.showToast(this,"请选择公证处");
                return;
            }else if(tvreceiver.equals("请选择")){
                Toaster.showToast(this,"请选择领取人");
                return;
            }else if (cityName.equals("请选择")){
                Toaster.showToast(this,"请选择公证处所在地");
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

                if(!CheckUtil.isEmpty(fenshu_int)){
                    fenshu_int =  Integer.parseInt(fenshu);
                }

                showDialog("是否确认提交?");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==101){
            receiver= data.getStringExtra("receiver");
            if(receiver!=null&&receiver.equals("1")){
                tv_receiver.setText("本人领取");
                et_name_lingqu.setText(requestName);
                et_carsid_lingqu.setText(requestCardId);
                et_phonenum.setText(requestPhoneNum);
                et_email.setText(requestEmail);
            }else{
                tv_receiver.setText("其他自然人领取");
                et_name_lingqu.setText("");
                et_carsid_lingqu.setText("");
                et_phonenum.setText("");
                et_email.setText("");
            }
        }
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

//     * @param notarName 公证名称 notaryId 公证处id notarCopies 公证书所需份数 receiver 领取类型 1-本人领取；2-其他人自然领取
//     * @param domicileLoc 申请人户籍所在地currentAddress 申请人现居地址pkValue  receiverName 领取者姓名receiverCardId 领取者身份证号
//     * @param receiverPhoneNum 领取者手机号receiverEmail 领取者邮箱
//     * @return  code 503 公证名称已存在
    /**
     * 提交信息
     */
    private void commitMsg() {

        showProgress("正在提交...");
//       String notarName,int notaryId,int notarCopies,String receiver,String domicileLoc,String currentAddress,String pkValue,String receiverName,String receiverCardId,String receiverPhoneNum,String receiverEmail
        ApiManager.getInstance().commitNotarMsg(name, notarOfficId,fenshu_int,receiver, huji, currloc, "2-2856", name_lingqu, cardid_lingqu, phonenum,email, new ApiCallback() {
        @Override
            public void onApiResult(int errorCode, String message, BaseHttpResponse response) {
                hideProgress();
                    if (!CheckUtil.isEmpty(response)){
                        if (response.getCode() == 200){
                            //跳到信息审核页面
                            Intent intent = new Intent(CommitMsgActivity.this,MsgCheckActivity.class);
                            startActivity(intent);
                            finish();
                        }else if(response.getCode() == 503){//公证名称已存在
                            Toaster.showToast(CommitMsgActivity.this,response.getMsg());
                        }
                    }else{
                        Toaster.showToast(CommitMsgActivity.this,"信息提交失败");
                    }
            }

            @Override
            public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
