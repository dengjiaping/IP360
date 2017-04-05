package com.truthso.ip360.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
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

import static com.truthso.ip360.utils.UIUtils.getResources;

/**
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @version 1.0
 * @despriction :个人中心模块，合同用户余额那栏点击进去的账号信息
 * @date 创建时间：2016-10-12下午2:43:57
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class AccountMagActivity extends BaseActivity {
    //账户余额
    private TextView tv_account_balance, tv_account;
    private TextView tv_status_taocan;//套餐状态
    private TextView tv_date_photo;//拍照的套餐时间
    private TextView tv_account_photo;
    private TextView tv_date_video;//录像的套餐时间
    private TextView tv_account_video;
    private TextView tv_date_record;//录音的套餐时间
    private TextView tv_account_record;
    private String usedCount_photo, usedCount_video, usedCount_record;// 累积使用量
    private String buyCount_photo, buyCount_video, buyCount_record, accountBalance;//买的量
    private String contractStart_photo, contractStart_video, contractStart_record, contractEnd_video, contractEnd_photo, contractEnd_record;
    //    private RelativeLayout rl_taocan_detail;
    private LinearLayout ll_taocan;
    private boolean isHaveCombo;
    private MyListview listView;
    private List<GiftsProduct> giftsProduct = new ArrayList<>();
    private List<product> productBalance;
    private int icurrDate;
    private RelativeLayout rl_shipin, rl_luyin, rl_paizhao;
    private RelativeLayout rl_liang;
    private boolean layout_paizhao ,layout_shipin,layout_luyin;//看统计用量是不是三个条目都Gone掉了
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        //获取当前时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String currDate = dateFormat.format(Calendar.getInstance().getTime());
        tv_date_photo = (TextView) findViewById(R.id.tv_date_photo);
        tv_account_photo = (TextView) findViewById(R.id.tv_account_photo);
        tv_date_video = (TextView) findViewById(R.id.tv_date_video);
        tv_account_video = (TextView) findViewById(R.id.tv_account_video);
        tv_date_record = (TextView) findViewById(R.id.tv_date_record);
        tv_account_record = (TextView) findViewById(R.id.tv_account_record);
        icurrDate = Integer.parseInt(currDate);
        tv_account = (TextView) findViewById(R.id.tv_account);
        String userAccount = (String) SharePreferenceUtil.getAttributeByKey(AccountMagActivity.this, MyConstants.SP_USER_KEY, "userAccount", SharePreferenceUtil.VALUE_IS_STRING);
        tv_account.setText(userAccount);
        tv_account_balance = (TextView) findViewById(R.id.tv_account_balance);
        rl_shipin = (RelativeLayout) findViewById(R.id.rl_shipin);
        rl_luyin = (RelativeLayout) findViewById(R.id.rl_luyin);
        rl_paizhao = (RelativeLayout) findViewById(R.id.rl_paizhao);
        rl_liang = (RelativeLayout) findViewById(R.id.rl_liang);

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
                PersonalMsgBean bean = (PersonalMsgBean) response;
                if (!CheckUtil.isEmpty(bean)) {
                    if (bean.getCode() == 200) {
                        // 账户余额
                        int balance = bean.getDatas().getAccountBalance();
                        accountBalance = "余额￥" + balance / 100 + "." + balance
                                % 100 / 10 + balance % 100 % 10 + "元";
                        tv_account_balance.setText(accountBalance);
                        productBalance = bean.getDatas().getProductBalance();
                        for (int i = 0; i < productBalance.size(); i++) {
                            int type = productBalance.get(i).getType();
                            String bugCount = productBalance.get(i).getBuyCount();
                            String contractend = productBalance.get(i).getContractEnd().replace("-", "");
                            if (type == MyConstants.PHOTOTYPE) {// 拍照
                                if (bugCount.equals("0")) {//没有时不显示
                                    rl_paizhao.setVisibility(View.GONE);
                                    layout_paizhao = true;
                                     }else{
                                            if (icurrDate > Integer.parseInt(contractend)) {//过期
                                                tv_date_photo.setText("已过期");
                                                tv_date_photo.setTextColor(getResources().getColor(R.color.red));
                                                tv_account_photo.setText(productBalance.get(i).getUsedCount() + productBalance.get(i).getUnit() + "/" + productBalance.get(i).getBuyCount() + productBalance.get(i).getUnit());
                                            } else {
                                                tv_date_photo.setText(productBalance.get(i).getContractStart().replace("-", ".") + "-" + productBalance.get(i).getContractEnd().replace("-", "."));
                                                tv_account_photo.setText(productBalance.get(i).getUsedCount() + productBalance.get(i).getUnit() + "/" + productBalance.get(i).getBuyCount() + productBalance.get(i).getUnit());
                                            }
                                     }


                            } else if (type == MyConstants.VIDEOTYPE) {// 录像
                                if (bugCount.equals("0")) {//没有时不显示
                                        rl_shipin.setVisibility(View.GONE);
                                    layout_shipin = true;
                                }else{
                                    if (icurrDate > Integer.parseInt(contractend)) {//过期
                                        tv_date_video.setText("已过期");
                                        tv_date_video.setTextColor(getResources().getColor(R.color.red));
                                        tv_account_video.setText(productBalance.get(i).getUsedCount() + productBalance.get(i).getUnit() + "/" + productBalance.get(i).getBuyCount() + productBalance.get(i).getUnit());
                                    } else {
                                        tv_date_video.setText(productBalance.get(i).getContractStart().replace("-", ".") + "-" + productBalance.get(i).getContractEnd().replace("-", "."));
                                        tv_account_video.setText(productBalance.get(i).getUsedCount() + productBalance.get(i).getUnit() + "/" + productBalance.get(i).getBuyCount() + productBalance.get(i).getUnit());
                                    }
                                }
                            } else if (type == MyConstants.RECORDTYPE) {// 录音

                                if (bugCount.equals("0")) {//没有时不显示
                                    rl_luyin.setVisibility(View.GONE);
                                    layout_luyin = true;
                                }else{
                                        if (icurrDate > Integer.parseInt(contractend)) {//过期
                                            tv_date_record.setText("已过期");
                                            tv_date_record.setTextColor(getResources().getColor(R.color.red));
                                            tv_account_record.setText(productBalance.get(i).getUsedCount() + productBalance.get(i).getUnit() + "/" + productBalance.get(i).getBuyCount() + productBalance.get(i).getUnit());
                                        } else {
                                            tv_date_record.setText(productBalance.get(i).getContractStart().replace("-", ".") + "-" + productBalance.get(i).getContractEnd().replace("-", "."));
                                            tv_account_record.setText(productBalance.get(i).getUsedCount() + productBalance.get(i).getUnit() + "/" + productBalance.get(i).getBuyCount() + productBalance.get(i).getUnit());
                                        }
                                }

                            }
                             if (layout_luyin &&layout_shipin && layout_paizhao){//三个条目都没有，则统计用量也gone
                                 rl_liang.setVisibility(View.GONE);
                             }
                        }
                        for (int i = 0; i < productBalance.size(); i++) {
                            giftsProduct.addAll(productBalance.get(i).getGiftsProduct());

                            if (productBalance.get(i).getGiftsProduct().size() > 0) {
                                for (int j = 0; j < productBalance.get(i).getGiftsProduct().size(); j++) {
                                    if(productBalance.get(i).getGiftsProduct().get(j).getGiftsCount() == 0||(productBalance.get(i).getGiftsProduct().get(j).getType()!= MyConstants.PHOTOTYPE && productBalance.get(i).getGiftsProduct().get(j).getType()!= MyConstants.RECORDTYPE && productBalance.get(i).getGiftsProduct().get(j).getType()!= MyConstants.VIDEOTYPE)){
                                        giftsProduct.remove(productBalance.get(i).getGiftsProduct().get(j));
                                    }


                                }
                            }

                        }
                        listView.setAdapter(new AccountMagAdapter(AccountMagActivity.this, giftsProduct));
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("AccountMag Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}


