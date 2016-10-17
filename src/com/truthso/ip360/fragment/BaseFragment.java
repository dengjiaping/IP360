package com.truthso.ip360.fragment;

import com.truthso.ip360.activity.R;
import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.net.MyAsyncHttpClient;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @despriction :基类fragment
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-7-22下午3:04:30
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public abstract class BaseFragment extends Fragment {
	protected View mLayout;
	protected FragmentManager fragmentManager;
	private Dialog pDialog;
	private static final int NET_FIAL = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayout = inflater.inflate(setViewId(), container, false);
		fragmentManager = getFragmentManager();
		if (mLayout != null) {
			mLayout.setOnClickListener(null);
		}
		View view = mLayout;
		initView(view, inflater, container, savedInstanceState);
		return mLayout;
	}

	protected abstract void initView(View view, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState);

	public abstract int setViewId();

	protected abstract void initData();
	
	public   boolean  onKeyDown(int keyCode, KeyEvent event){
		return false;
	};
	
	public void showProgress(String msg) {
		if (this.getActivity()!=null && !this.isDetached()) {
			if (pDialog == null) {
				pDialog = createLoadingDialog(getActivity(),msg); // 创建ProgressDialog对象
			}
			pDialog.setCancelable(false); // 设置ProgressDialog 是否可以按退回按键取消
			if (!pDialog.isShowing()) {
				pDialog.show();// 让ProgressDialog显示
			}
			pDialog.setOnKeyListener(new DialogInterface.OnKeyListener(){
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
				Toast.makeText(getActivity(), "网络请求超时，请检查网络～", Toast.LENGTH_SHORT).show();
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
	 Dialog createLoadingDialog(Context context, String msg) {  

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
