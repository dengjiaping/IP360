package com.truthso.ip360.activity;



import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.net.MyAsyncHttpClient;

public abstract class BaseActivity extends Activity{
	private Button btn_title_left;
	private TextView tv_title;
	private Dialog pDialog;
	private static final int NET_FIAL = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(setLayout());
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_left.setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(setTitle());
		initData();
		initView();
	}
	
	public abstract void initData();
	
	public abstract void initView();
	
	public abstract int setLayout();
	
	public abstract String setTitle();
	

	public void showProgress() {
		if ((this instanceof Activity) && !this.isFinishing()) {
			if (pDialog == null) {
				pDialog = createLoadingDialog(this,"努力加载中..."); // 创建ProgressDialog对象
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
							hideProgress();
							handler.sendEmptyMessage(NET_FIAL);
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
				Toast.makeText(BaseActivity.this, "网络请求超时，请检查网络～", Toast.LENGTH_SHORT).show();
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


	public void onProcess() {
		if (null != pDialog && pDialog.isShowing())
			pDialog.dismiss();
		MyAsyncHttpClient.cancelRequest(MyApplication.getInstance());
		Log.i("cancelHttp", "cancelHttp");
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
