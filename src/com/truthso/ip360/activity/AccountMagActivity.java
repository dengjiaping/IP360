package com.truthso.ip360.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.truthso.ip360.adapter.AccountMagAdapter;
import com.truthso.ip360.adapter.CloudEvidenceAdapter;
import com.truthso.ip360.bean.GiftsProduct;
import com.truthso.ip360.bean.PersonalMsgBean;
import com.truthso.ip360.bean.product;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.SharePreferenceUtil;
import com.truthso.ip360.view.MyListview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.zip.Inflater;

import cz.msebera.android.httpclient.Header;

/**
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @version 1.0
 * @despriction :个人中心模块，合同用户余额那栏点击进去的账号信息
 * @date 创建时间：2016-10-12下午2:43:57
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class AccountMagActivity extends BaseActivity {
    //账户余额
    private TextView tv_account_balance,tv_account;
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
    private MyListview listView;
    private List<GiftsProduct> giftsProduct=new ArrayList<>();
    private List<product> productBalance;
    @Override
    public void initData() {

    }

    @Override
   public void initView() {
        tv_account = (TextView) findViewById(R.id.tv_account);
        String userAccount = (String) SharePreferenceUtil.getAttributeByKey(AccountMagActivity.this, MyConstants.SP_USER_KEY, "userAccount", SharePreferenceUtil.VALUE_IS_STRING);
        tv_account.setText(userAccount);
        tv_account_balance= (TextView) findViewById(R.id.tv_account_balance);

        listView = (MyListview) findViewById(R.id.listview);
        getPersonalMsg();
    }

    // 获取个人信息概要
    public void getPersonalMsg() {
        showProgress("正在获取信息，请稍后...");
        ApiManager.getInstance().getPersonalMsg(new ApiCallback() {
            @Override
            public void onApiResult(int errorCode, String message, BaseHttpResponse response) {
                hideProgress();
                PersonalMsgBean    bean = (PersonalMsgBean) response;
                if (!CheckUtil.isEmpty(bean)) {
                    if (bean.getCode() == 200) {
                        // 账户余额
                        int balance = bean.getDatas().getAccountBalance();
                        accountBalance = "余额￥" + balance / 100 + "." + balance
                                % 100 / 10 + balance % 100 % 10 + "元";
                        tv_account_balance.setText(accountBalance);
                        productBalance = bean.getDatas().getProductBalance();
                        for (int i=0;i<productBalance.size();i++){
                            giftsProduct.addAll(productBalance.get(i).getGiftsProduct());
                        }
                        listView.setAdapter(new AccountMagAdapter(AccountMagActivity.this,giftsProduct));
                    }
                }
            }
            @Override
            public void onApiResultFailure(int statusCode, Header[] headers,
                                           byte[] responseBody, Throwable error) {

            }
        });
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


