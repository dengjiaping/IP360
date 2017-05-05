package com.truthso.ip360.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.truthso.ip360.activity.BaseActivity;
import com.truthso.ip360.activity.R;
import com.truthso.ip360.net.MyAsyncHttpClient;

/**
 * Created by Administrator on 2017/5/5.
 */

public class ProgressUtil {
    private Dialog pDialog;
    private Activity activity;
    private static final int NET_FIAL = 0;

    public ProgressUtil(Activity activity) {
        this.activity = activity;
    }

    public void showProgress(String msg) {
        if (!activity.isFinishing()) {
            if (pDialog == null) {
                pDialog = createLoadingDialog(activity,msg); // 创建ProgressDialog对象
            }
            pDialog.setCancelable(false); // 设置ProgressDialog 是否可以按退回按键取消
            if (!pDialog.isShowing()) {
                pDialog.show();// 让ProgressDialog显示
            }
            pDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if(pDialog.isShowing()){
                            hideProgress();
                        }
                    }
                    return false;
                }
            });
        }
        new Thread(){
            public void run() {
                try {
                    sleep(61000);
                    if(pDialog!=null){
                        if(pDialog.isShowing()){
//							hideProgress();
//							handler.sendEmptyMessage(NET_FIAL);//因为保全页面的接口要弹框提示，不土司，每个接口单独提示网络链接超时
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NET_FIAL:
                    MyAsyncHttpClient.cancel();
                    Toast.makeText(activity, "网络请求超时，请检查网络后重试", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    };

    public void hideProgress() {
        if (null != pDialog && pDialog.isShowing())
            pDialog.dismiss();

    }

    public Dialog createLoadingDialog(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字

        tipTextView.setText(msg);// 设置加载信息
        final Dialog loadingDialog = new Dialog(context,R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(true);// 不可以用“返回键”取消
        loadingDialog.setContentView(v);

        return loadingDialog;
    }
}
