package com.truthso.ip360.pager;


import com.truthso.ip360.activity.MainActivity;

import android.content.Context;
import android.view.View;


public abstract class BasePager {

	private View view;
	protected Context ctx;
	protected MainActivity activity;
	public BasePager(Context ctx){
		this.ctx = ctx;
		activity = (MainActivity) ctx;
		view = initView();
	}
	
	public View getView(){
		return view;//构造方法已经执行initView()了 直接返回view就行；
	}
	
	/**
	 * 初始化view
	 * @return
	 */
	public abstract View initView();
	
	
	/**
	 * 初始化数据
	 *  根据逻辑，动态调用
	 * @return
	 */
	public abstract void initData(int position);
	
	
	/**
	 * 联接网络请求
	 * @param method GET 或 post 请求方式
	 * @param url	要联网的url 
	 * @param params 要发送的参数
//	 * @param callBack 联网完成后的回调
//	 */
//	public <T> void requestUrl(HttpMethod method, String url, RequestParams params, RequestCallBack<T> callBack){
//		/*
//		 * 常见的联网方法 
//		 * 1\ httpUrlconnection
//		 * 2\ httpClent 
//		 * 3\ xUtils httpUtils
//		 */
//		HttpUtils httpUtils = new HttpUtils() ;
		
//		RequestParams params = new RequestParams();
//		params.addBodyParameter("name", "tom");
//		params.addBodyParameter("age", "18");
//		params.addBodyParameter("school", "itcast");
		
//		params.addBodyParameter("req", "{name:'tom',age:'18'}");
		
//		NameValuePair nameValuePairs = new BasicNameValuePair("name", "tom");
//		
//		List<NameValuePair> nameValues = new ArrayList<NameValuePair>();
//		nameValues.add(nameValuePairs);
//		
//		params.addBodyParameter(nameValues);
		
		// 异步的联网接求
//		httpUtils.send(method, url, params, callBack);
		
		// 发送同步请求
//		httpUtils.sendSync(method, url, params)
//	}
	
	/**
	 * 打印错误日志
	 * @param error
	 */
	public void printfErrorLog(Exception error){
		error.printStackTrace();
	}
	
	
	public abstract void setAllSelect(boolean b);
	public abstract void setChoice(boolean b);

}
