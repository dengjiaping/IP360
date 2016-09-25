package com.truthso.ip360.net;

import java.util.HashMap;

import com.loopj.android.http.RequestHandle;

public class ApiManager implements BaseHttpRequestCallBack {

	@SuppressWarnings("rawtypes")
	private HashMap<RequestHandle, BaseHttpRequest> requestHashMap = new HashMap<RequestHandle, BaseHttpRequest>();
	private static ApiManager mInstance = null;


	private ApiManager() {
	}

	public static ApiManager getInstance() {
		if (mInstance == null) {
			mInstance = new ApiManager();
		}
		return mInstance;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onRequestResult(RequestHandle requestHandle, int errorCode,
			String message, BaseHttpResponse response) {

		if (response.getCode() == 92) {
			/*SharePreferenceUtil.saveOrUpdateAttribute(App.getInstance(),
					KeyConstant.SP_USER, KeyConstant.isTokenFail, true);
			SharePreferenceUtil.saveOrUpdateAttribute(App.getInstance(),
					KeyConstant.SP_USER, KeyConstant.tokenId, null);
			SharePreferenceUtil.saveOrUpdateAttribute(App.getInstance(),
					KeyConstant.SP_USER, KeyConstant.imgSmallUrl, null);
			SharePreferenceUtil.saveOrUpdateAttribute(App.getInstance(),
					KeyConstant.SP_USER, KeyConstant.imgLargeUrl, null);
			SharePreferenceUtil.saveOrUpdateAttribute(App.getInstance(),
					KeyConstant.SP_USER, KeyConstant.imgMiddleUrl, null);

			SharePreferenceUtil.saveOrUpdateAttribute(App.getInstance(),
					KeyConstant.SP_USER, KeyConstant.accountName, null);
			SharePreferenceUtil.saveOrUpdateAttribute(App.getInstance(),
					KeyConstant.SP_USER, KeyConstant.certificateType, null);
			SharePreferenceUtil.saveOrUpdateAttribute(App.getInstance(),
					KeyConstant.SP_USER, KeyConstant.certificateNumber, null);

			Toast.makeText(App.getInstance(), response.getMessage(),
					Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(App.getInstance(), LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			App.getInstance().startActivity(intent);*/
			return;
		}

		if (requestHandle != null) {
			BaseHttpRequest request = requestHashMap.get(requestHandle);
			// WeakReference callback = request.getApiCallback();
			ApiCallback callback = request.getApiCallback();

			if (callback != null) {
				callback.onApiResult(errorCode, message, response);
			}
			requestHashMap.remove(requestHandle);
		}
	}

	
	
	

	/**
	 * 登录
	 * 
	 * @param callback
	 * @param accountCode
	 * @param accountPassword
	 * @param verificationCode
	 * @return
	 *//*
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public RequestHandle doLogin(ABApiCallback callback, String accountCode,
			String accountPassword, String verificationCode) {
		ABBaseHttpRequest request = new ABBaseHttpRequest<LoginBean>(
				LoginBean.class, this, SystemConstant.LOGIN);

		request.setPath(URLConstant.getLoginPath());
		request.params().add("accountCode", CheckUtil.isEmpty(accountCode)?"":AESUtil.encode(key, accountCode));
		request.params().add("accountPassword",
				CheckUtil.isEmpty(accountPassword)?"":AESUtil.encode(key, accountPassword));
		request.params().add("verificationCode", verificationCode);
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.post();
		
		requestHashMap.put(requestHandle, request);

		return requestHandle;
	}*/

	
//	//获取天气
//    public RequestHandle getWeatherInfo(String cityName,ApiCallback call){
//    	
//    	BaseHttpRequest<WeatherBean> request=new BaseHttpRequest<WeatherBean>(WeatherBean.class, this);
//    	
//    	request.setPath("http://apis.baidu.com/apistore/weatherservice/cityname");
//    	request.params().add("cityname", cityName);
//    	
//    	
//    	request.setApiCallback(call);
//    	RequestHandle requestHandle = request.get();
//    	
//    	requestHashMap.put(requestHandle, request);
//    	return requestHandle;
//    }
//	
////微信热门
//    public RequestHandle getWeChatData(String word,String src,int num,int rand,int page,ApiCallback callback){
//		BaseHttpRequest<WeChatBean> request = new BaseHttpRequest<WeChatBean>(WeChatBean.class, this);
//		request.setPath("http://apis.baidu.com/txapi/weixin/wxhot");
//		request.params().add("word", word);
//		request.params().add("src", src);
//		request.params().add("num", num+"");
//		request.params().add("rand", rand+"");
//		request.params().add("page", page+"");
//
//		request.setApiCallback(callback);
//		RequestHandle requestHandle = request.get();
//		requestHashMap.put(requestHandle, request);
//    	
//    	
//    	
//    	return requestHandle;
//		
//		
//		
//		
//    	
//    }
//    
    
}
