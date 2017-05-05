package com.truthso.ip360.activity;



import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.net.MyAsyncHttpClient;
import com.truthso.ip360.utils.CheckUtil;

public abstract class BaseActivity extends Activity{
	private ImageButton btn_title_left;
	private TextView tv_title;
	private Dialog pDialog;
	private static final int NET_FIAL = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(setLayout());
		btn_title_left = (ImageButton) findViewById(R.id.btn_title_left);
		if(btn_title_left!=null){
			btn_title_left.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
		tv_title = (TextView) findViewById(R.id.tv_title);
		if(tv_title!=null){
			tv_title.setText(setTitle());
		}
		initData();
		initView();
		compatStatusbar();
	}
	
	public abstract void initData();
	
	public abstract void initView();
	
	public abstract int setLayout();
	
	public abstract String setTitle();
	

	public void showProgress(String msg) {
		if ((this instanceof Activity) && !this.isFinishing()) {
			if (pDialog == null) {
				pDialog = createLoadingDialog(this,msg); // 创建ProgressDialog对象
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
				Toast.makeText(BaseActivity.this, "网络请求超时，请检查网络后重试", Toast.LENGTH_SHORT).show();
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


	private void compatStatusbar(){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = this.getWindow();
//取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

//需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//设置状态栏颜色
			window.setStatusBarColor(getResources().getColor(R.color.jiuhong));

			ViewGroup mContentView = (ViewGroup) this.findViewById(Window.ID_ANDROID_CONTENT);
			View mChildView = mContentView.getChildAt(0);
			if (mChildView != null) {
				//注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 预留出系统 View 的空间.
				ViewCompat.setFitsSystemWindows(mChildView, true);
			}

		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			Window window = this.getWindow();
			ViewGroup mContentView = (ViewGroup) this.findViewById(Window.ID_ANDROID_CONTENT);

//First translucent status bar.
			window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			int statusBarHeight = getStatusBarHeight(this);

			View mChildView = mContentView.getChildAt(0);
			if (mChildView != null) {
				FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mChildView.getLayoutParams();
				//如果已经为 ChildView 设置过了 marginTop, 再次调用时直接跳过
				if (lp != null && lp.topMargin < statusBarHeight && lp.height != statusBarHeight) {
					//不预留系统空间
					ViewCompat.setFitsSystemWindows(mChildView, false);
					lp.topMargin += statusBarHeight;
					mChildView.setLayoutParams(lp);
				}
			}
			View statusBarView = mContentView.getChildAt(0);
			if (statusBarView != null && statusBarView.getLayoutParams() != null && statusBarView.getLayoutParams().height == statusBarHeight) {
				//避免重复调用时多次添加 View
				statusBarView.setBackgroundColor(getResources().getColor(R.color.jiuhong));
				return;
			}
			statusBarView = new View(this);
			ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
			statusBarView.setBackgroundColor(getResources().getColor(R.color.jiuhong));
//向 ContentView 中添加假 View
			mContentView.addView(statusBarView, 0, lp);
		}
	}

	public  int getStatusBarHeight(Context context)
	{
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0)
		{
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
	
}
