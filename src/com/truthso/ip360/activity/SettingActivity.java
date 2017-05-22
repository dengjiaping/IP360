package com.truthso.ip360.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.UpDownLoadDao;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.ossupload.DownLoadHelper;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.FileSizeUtil;
import com.truthso.ip360.utils.FileUtil;
import com.truthso.ip360.utils.SharePreferenceUtil;

import java.io.File;

import cz.msebera.android.httpclient.Header;

/**
 * @despriction :设置页面
 *
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2017/5/22 15:13
 * @version 1.3
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private RelativeLayout rl_amend_psd,rl_about_us,rl_clearCache;
    private CheckBox cb_iswifi;
    private String str;
    private String tag;
    private TextView tv_cache_size;
    private Dialog alertDialog;
    private Button btn_logout;
    @Override
    public void initData() {

    }

    @Override
    public void initView() {

        rl_amend_psd = (RelativeLayout)findViewById(R.id.rl_amend_psd);
        rl_amend_psd.setOnClickListener(this);
        rl_about_us = (RelativeLayout) findViewById(R.id.rl_about_us);
        rl_about_us.setOnClickListener(this);
        cb_iswifi= (CheckBox)findViewById(R.id.cb_iswifi);
        cb_iswifi.setChecked(true);
        cb_iswifi.setOnCheckedChangeListener(this);
        boolean isWifi= (Boolean) SharePreferenceUtil.getAttributeByKey(this, MyConstants.SP_USER_KEY,MyConstants.ISWIFI,SharePreferenceUtil.VALUE_IS_BOOLEAN);
        cb_iswifi.setChecked(isWifi);
        rl_clearCache = (RelativeLayout) findViewById(R.id.rl_clearCache);
        rl_clearCache.setOnClickListener(this);
        tv_cache_size= (TextView) findViewById(R.id.tv_cache_size);
        String dirSize= FileSizeUtil.getAutoFileOrFilesSize(MyConstants.CACHE_PATH);
        tv_cache_size.setText(dirSize);
        btn_logout = (Button)findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(this);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public String setTitle() {
        return "设置";
    }

    @Override
    public void onClick(View v) {
            switch(v.getId()){
                case R.id.rl_amend_psd://修改密码
                    Intent intent5 = new Intent(this, AemndPsdActivity.class);
                    startActivity(intent5);
                    break;
                case R.id.rl_about_us:// 关于我们
                    Intent intent6 = new Intent(this, AboutUsAcctivity.class);
                    startActivity(intent6);
                    break;
                case R.id.rl_clearCache://清除缓存
                    str ="是否确定清除缓存？";
                    tag = "clearCache";
                    showDialog(str);
                    break;
                case R.id.btn_logout:// 退出登录
                    str = "是否确定退出登录";
                    tag = "logout";
                    showDialog(str);
                default:
                    break;
            }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharePreferenceUtil.saveOrUpdateAttribute(this,MyConstants.SP_USER_KEY,MyConstants.ISWIFI,isChecked);
    }
    private void showDialog(String str) {
        alertDialog = new AlertDialog.Builder(SettingActivity.this).setTitle("温馨提示")
                .setMessage(str).setIcon(R.drawable.ww)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(tag.equals("logout")){
                            logOut();
                        }else if (tag.equals("clearCache")){
                            clearCache();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                }).create();
        alertDialog.show();
    }/**
     * 清除缓存
     */
    private void clearCache(){
        DownLoadHelper.getInstance().cancleDownload();
        try {
            FileUtil.deleteAllFiles(new File(MyConstants.DOWNLOAD_PATH));
        }catch (Exception e){
            Toaster.showToast(SettingActivity.this,"删除文件失败");
        }
        UpDownLoadDao.getDao().deleteAll();
        tv_cache_size.setText("0KB");
        Toast.makeText(SettingActivity.this,"清除缓存成功",Toast.LENGTH_SHORT).show();
    }
    // 退出登录
    private void logOut() {
        showProgress("正在退出...");
        ApiManager.getInstance().LogOut(new ApiCallback() {
            @Override
            public void onApiResult(int errorCode, String message,
                                    BaseHttpResponse response) {
                hideProgress();
                if (!CheckUtil.isEmpty(response)) {
                    if (response.getCode() == 200) {
                        Toaster.showToast(SettingActivity.this, "已退出登录");
                        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        // 清空登录的token
                        SharePreferenceUtil.saveOrUpdateAttribute(
                                SettingActivity.this, MyConstants.SP_USER_KEY,
                                "token", null);
                    } else {
                        Toaster.showToast(SettingActivity.this, response.getMsg());
                    }
                } else {
                    Toaster.showToast(SettingActivity.this, "退出登录失败");
                }
            }

            @Override
            public void onApiResultFailure(int statusCode, Header[] headers,
                                           byte[] responseBody, Throwable error) {

            }
        });
    }


}
