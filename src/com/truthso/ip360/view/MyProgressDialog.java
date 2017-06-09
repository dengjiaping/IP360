package com.truthso.ip360.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.truthso.ip360.activity.R;
import com.truthso.ip360.listener.ProgressDialogDismissListener;


/**
 * Created by Administrator on 2017/3/23.
 */

public class MyProgressDialog {

    private Activity activity;
    private Dialog pDialog;
    private ProgressDialogDismissListener listener;

    public MyProgressDialog(Activity activity, ProgressDialogDismissListener listener) {
        this.activity = activity;
        this.listener=listener;
    }

    public void showProgress(String msg) {
        if ((activity instanceof Activity) ) {
            if (pDialog == null) {
                pDialog = createLoadingDialog(activity, msg); // 创建ProgressDialog对象
            }
            if (!pDialog.isShowing()&&!activity.isFinishing()) {
                pDialog.show();// 让ProgressDialog显示
            }

            pDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_UP) {
                        if (pDialog.isShowing()) {
                            hideProgress();
                            if(listener!=null){
                                listener.onDismiss();
                            }
                        }
                    }
                    return false;
                }
            });
        }
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

    public void hideProgress() {
        if (null != pDialog && pDialog.isShowing()){
            pDialog.dismiss();
        }
    }
}
