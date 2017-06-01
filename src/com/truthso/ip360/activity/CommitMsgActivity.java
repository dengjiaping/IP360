package com.truthso.ip360.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;

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
    private String notarDate;//申请日期
    @Override
    public void initData() {

    }

    @Override
    public void initView() {
//        pkValue= ?
//        notarDate =？
        et_name = (EditText) findViewById(R.id.et_name);
        et_fenshu = (EditText) findViewById(R.id.et_fenshu);
        et_huji = (EditText) findViewById(R.id.et_huji);
        et_currloc = (EditText) findViewById(R.id.et_currloc);
        et_name_lingqu = (EditText) findViewById(R.id.et_name_lingqu);
        et_carsid_lingqu = (EditText) findViewById(R.id.et_carsid_lingqu);
        et_phonenum = (EditText) findViewById(R.id.et_phonenum);
        et_email = (EditText) findViewById(R.id.et_email);

        rl_gongzhengchu = (RelativeLayout) findViewById(R.id.rl_gongzhengchu);
        rl_gzc_loc = (RelativeLayout) findViewById(R.id.rl_gzc_loc);
        rl_lingquren = (RelativeLayout) findViewById(R.id.rl_lingquren);
        btn_commit = (Button) findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(this);


        name = et_name.getText().toString().trim();
        fenshu = et_fenshu.getText().toString().trim();
        huji = et_huji.getText().toString().trim();
        currloc = et_currloc.getText().toString().trim();
        name_lingqu = et_name_lingqu.getText().toString().trim();
        cardid_lingqu = et_carsid_lingqu.getText().toString().trim();
        phonenum = et_phonenum.getText().toString().trim();
        email = et_email.getText().toString().trim();

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
            case R.id.rl_gzc_loc://公证处地址
                break;
            case R.id.rl_gongzhengchu://选择公证处
//                notaryId=？
                break;
            case R.id.rl_lingquren://选择领取人
                break;
            case R.id.btn_commit://提交
                showDialog("是否确认提交?");

                break;
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
//       String pkValue,String receiverName,String receiverCardId,String receiverPhoneNum,String notarDate,String receiverEmail,
        ApiManager.getInstance().commitNotarMsg(name, notaryId, Integer.parseInt(fenshu), name_lingqu, huji, currloc, pkValue, name_lingqu, cardid_lingqu, phonenum,email, new ApiCallback() {
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
}
