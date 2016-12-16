package com.truthso.ip360.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.truthso.ip360.view.xrefreshview.LogUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @version 1.0
 * @despriction :个人中心模块，合同用户余额那栏点击进去的账号信息
 * @date 创建时间：2016-10-12下午2:43:57
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
/*intent.putExtra("accountBalance", accountBalance);

        if (isHaveCombo){//有套餐了传参

        }*/
public class AccountMagActivity extends BaseActivity {
    //账户余额
    private TextView tv_account_balance;
    private TextView tv_status_taocan;//套餐状态
    private TextView tv_date_photo;//拍照的套餐时间
    private TextView tv_account_photo;
    private TextView tv_date_video;//录像的套餐时间
    private TextView tv_account_video;
    private TextView tv_date_record;//录音的套餐时间
    private TextView tv_account_record;
    private String usedCount_photo, usedCount_video, usedCount_record;// 累积使用量
    private String buyCount_photo,buyCount_video,buyCount_record,accountBalance;//买的量
    private String contractStart_photo,contractStart_video,contractStart_record,contractEnd_video,contractEnd_photo,contractEnd_record;
    //    private RelativeLayout rl_taocan_detail;
    private LinearLayout ll_taocan;
    private  boolean isHaveCombo;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private String unit_photo,unit_video,unit_record;
    @Override
    public void initData() {
        accountBalance = getIntent().getStringExtra("accountBalance");
        isHaveCombo =    getIntent().getBooleanExtra("isHaveCombo",false);
        if (isHaveCombo){
            //业务的开始时间与结束时间
            contractStart_photo =  getIntent().getStringExtra("contractStart_photo");
            contractEnd_photo =  getIntent().getStringExtra("contractEnd_photo");
            contractStart_video =  getIntent().getStringExtra("contractStart_video");
            contractEnd_video =  getIntent().getStringExtra("contractEnd_video");
            contractStart_record =  getIntent().getStringExtra("contractStart_record");
            contractEnd_record =  getIntent().getStringExtra("contractEnd_record");
            //购买的量
            buyCount_photo =  getIntent().getStringExtra("buyCount_photo");
            buyCount_video =  getIntent().getStringExtra("buyCount_video");
            buyCount_record =  getIntent().getStringExtra("buyCount_record");
            //已经使用的量
            usedCount_photo =  getIntent().getStringExtra("usedCount_photo");
            usedCount_video =  getIntent().getStringExtra("usedCount_video");
            usedCount_record =  getIntent().getStringExtra("usedCount_record");
            //业务量单位
            unit_photo = getIntent().getStringExtra("unit_photo");
            unit_video = getIntent().getStringExtra("unit_video");
            unit_record = getIntent().getStringExtra("unit_record");
        }

//        rl_taocan_detail = (RelativeLayout) findViewById(R.id.rl_taocan_detail);
        ll_taocan = (LinearLayout) findViewById(R.id.ll_taocan);
        tv_account_balance = (TextView) findViewById(R.id.tv_account_balance);
        tv_status_taocan = (TextView) findViewById(R.id.tv_status_taocan);
        tv_date_photo = (TextView) findViewById(R.id.tv_date_photo);
        tv_account_photo = (TextView) findViewById(R.id.tv_account_photo);
        tv_date_video = (TextView) findViewById(R.id.tv_date_video);
        tv_account_video = (TextView) findViewById(R.id.tv_account_video);
        tv_date_record = (TextView) findViewById(R.id.tv_date_record);
        tv_account_record = (TextView) findViewById(R.id.tv_account_record);

        tv_account_balance.setText(accountBalance);
        //获取当前时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String currDate = dateFormat.format(Calendar.getInstance().getTime());
        int icurrDate = Integer.parseInt(currDate);
//        LogUtils.e(icurrDate+"当前日期");

        if (isHaveCombo){
            //拍照
            String  str_contractEnd_photo = contractEnd_photo.replace("-","");
            if (buyCount_photo.equals("0")){//合同不约定预购用量的情况，此时只显示已经使用的
                tv_account_photo.setText(usedCount_photo+unit_photo);
                tv_date_photo.setText(contractStart_photo.replace("-",".")+"-"+contractEnd_photo.replace("-","."));
            }else if (Integer.parseInt(usedCount_photo)>Integer.parseInt(buyCount_photo)){//已经使用的超过预购的,字体设置成红色
                tv_account_photo.setTextColor(getResources().getColor(R.color.red));
                tv_account_photo.setText(usedCount_photo+unit_photo+"/"+buyCount_photo+unit_photo);
                tv_date_photo.setText(contractStart_photo.replace("-",".")+"-"+contractEnd_photo.replace("-","."));
            }else if (icurrDate>Integer.parseInt(str_contractEnd_photo)){//已过期 日期设置成红色
                tv_date_photo.setTextColor(getResources().getColor(R.color.red));
                tv_date_photo.setText(contractStart_photo.replace("-",".")+"-"+contractEnd_photo.replace("-","."));
                tv_account_photo.setText(usedCount_photo+unit_photo+"/"+buyCount_photo+unit_photo);
            }else{
                tv_date_photo.setText(contractStart_photo.replace("-",".")+"-"+contractEnd_photo.replace("-","."));
                tv_account_photo.setText(usedCount_photo+unit_photo+"/"+buyCount_photo+unit_photo);

            }
            //录像
          String  str_contractEnd_video = contractEnd_video.replace("-","");
            if (buyCount_video.equals("0")){//合同不约定预购用量的情况，此时只显示已经使用的
                tv_date_video.setText(contractStart_video.replace("-",".")+"-"+contractEnd_video.replace("-","."));
                tv_account_video.setText(usedCount_video+unit_video);
            }else if (Integer.parseInt(usedCount_video)>Integer.parseInt(buyCount_video)){//已经使用的超过预购的,字体设置成红色
                tv_account_video.setTextColor(getResources().getColor(R.color.red));
                tv_date_video.setText(contractStart_video.replace("-",".")+"-"+contractEnd_video.replace("-","."));
                tv_account_video.setText(usedCount_video+unit_video+"/"+buyCount_video+unit_video);
            }else if (icurrDate>Integer.parseInt(str_contractEnd_video)){//已过期 日期设置成红色
                tv_date_video.setTextColor(getResources().getColor(R.color.red));
                tv_date_video.setText(contractStart_video.replace("-",".")+"-"+contractEnd_video.replace("-","."));
                tv_account_video.setText(usedCount_video+unit_video+"/"+buyCount_video+unit_video);
            }else{
                tv_date_video.setText(contractStart_video.replace("-",".")+"-"+contractEnd_video.replace("-","."));
                tv_account_video.setText(usedCount_video+unit_video+"/"+buyCount_video+unit_video);
            }
            //录音
           String  strcontractEnd_record = contractEnd_record.replace("-","");
            if (buyCount_record.equals("0")){//合同不约定预购用量的情况，此时只显示已经使用的
                tv_date_record.setText(contractStart_record.replace("-",".")+"-"+contractEnd_record.replace("-","."));
                tv_account_record.setText(usedCount_record+unit_record);
            }else if (Integer.parseInt(usedCount_record)>Integer.parseInt(buyCount_record)){//已经使用的超过预购的,字体设置成红色
                tv_account_record.setTextColor(getResources().getColor(R.color.red));
                tv_date_record.setText(contractStart_record.replace("-",".")+"-"+contractEnd_record.replace("-","."));
                tv_account_record.setText(usedCount_record+unit_record+"/"+buyCount_record+unit_record);
            }else if (icurrDate>Integer.parseInt(strcontractEnd_record)){//已过期 日期设置成红色
                tv_date_record.setTextColor(getResources().getColor(R.color.red));
                tv_date_record.setText(contractStart_record.replace("-",".")+"-"+contractEnd_record.replace("-","."));
                tv_account_record.setText(usedCount_record+unit_record+"/"+buyCount_record+unit_record);
            }else{
                tv_date_record.setText(contractStart_record.replace("-",".")+"-"+contractEnd_record.replace("-","."));
                tv_account_record.setText(usedCount_record+unit_record+"/"+buyCount_record+unit_record);
            }

        }else{//无套餐
            tv_status_taocan.setText("当前无套餐");
            ll_taocan.setVisibility(View.GONE);
        }
    }

    @Override
   public void initView() {


    }

    @Override
    public int setLayout() {
        return R.layout.activity_accountmsg;
    }

    @Override
    public String setTitle() {
        return "账号信息";
    }

}
