package com.truthso.ip360.activity;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;

import cz.msebera.android.httpclient.Header;

/**
 * 关于我们->用户反馈
 * Created by summer on 2017/3/27.
 */

public class UserAdviceActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_useradvice,et_account,et_name;
    private boolean isAdviceEmp,isAccountEmp,isNameEmp;
    private Button btn_commit;
    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        btn_commit = (Button) findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(this);
        et_useradvice = (EditText) findViewById(R.id.et_useradvice);
        et_account = (EditText) findViewById(R.id.et_account);
        et_name = (EditText) findViewById(R.id.et_name);

        et_useradvice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            if (!CheckUtil.isEmpty(s.toString().trim())){
                isAdviceEmp = true;
            }else{
                isAdviceEmp = false;
            }
                checkButtonStatus();
            }
        });
        et_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!CheckUtil.isEmpty(s.toString().trim())){
                    isAccountEmp = true;
                }else{
                    isAccountEmp = false;
                }
                checkButtonStatus();
            }


        });
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!CheckUtil.isEmpty(s.toString().trim())){
                    isNameEmp = true;
                }else{
                    isNameEmp = false;
                }
                checkButtonStatus();
            }


        });
    }
    /**
     * 按钮是否是彩色可点击
     */
    private void checkButtonStatus() {
        if (isAdviceEmp && isAccountEmp && isNameEmp) {
            btn_commit.setEnabled(true);
            btn_commit.setTextColor(Color.WHITE);
            btn_commit.setBackgroundResource(R.drawable.round_corner_bg);
        } else {
            btn_commit.setEnabled(false);
            btn_commit.setBackgroundResource(R.drawable.round_corner_white);
            btn_commit.setTextColor(getResources().getColor(R.color.huise));
        }
    }
    @Override
    public int setLayout() {
        return R.layout.activity_useradvice;
    }

    @Override
    public String setTitle() {
        return "用户反馈";
    }

    @Override
    public void onClick(View v) {
        //提交
//        调接口
        String str = et_useradvice.getText().toString().trim();
        String str1 = et_account.getText().toString().trim();
        String str2 = et_name.getText().toString().trim();
            commit(str,str1,str2);


    }
    //调接口提交反馈内容
    public void commit(String cotent,String contact,String name){
        showProgress("正在提交...");
        ApiManager.getInstance().UserAdvice(cotent, contact, name,new ApiCallback() {
            @Override
            public void onApiResult(int errorCode, String message, BaseHttpResponse response) {
                hideProgress();
                if (!CheckUtil.isEmpty(response)){
                        if (response.getCode() == 200){
                            Toaster.showToast(UserAdviceActivity.this, "反馈提交成功！");
                        }else{
                            Toaster.showToast(UserAdviceActivity.this, response.getMsg());
                        }
                }else{
                    Toaster.showToast(UserAdviceActivity.this, "请重试");
                }
            }

            @Override
            public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
