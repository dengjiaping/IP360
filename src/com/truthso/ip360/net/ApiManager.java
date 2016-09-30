package com.truthso.ip360.net;

import java.util.HashMap;

import com.loopj.android.http.RequestHandle;
import com.truthso.ip360.bean.LoginBean;
import com.truthso.ip360.constants.URLConstant;
import com.truthso.ip360.utils.CheckUtil;

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
	 * @param userAccount
	 * @param userPwd
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public RequestHandle doLogin(String userAccount,
			String userPwd,ApiCallback callback) {
		BaseHttpRequest<LoginBean> request = new BaseHttpRequest<LoginBean>(
				LoginBean.class, this);

		request.setPath(URLConstant.LoginPath);
		request.params().add("userAccount",userAccount);
		request.params().add("userPwd",userPwd);
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.post();
		
		requestHashMap.put(requestHandle, request);

		return requestHandle;
	}


    
}
