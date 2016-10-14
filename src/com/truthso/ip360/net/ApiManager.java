package com.truthso.ip360.net;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

import com.loopj.android.http.RequestHandle;
import com.truthso.ip360.bean.LoginBean;
import com.truthso.ip360.bean.PersonalMsgBean;
import com.truthso.ip360.constants.URLConstant;

import cz.msebera.android.httpclient.Header;

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

	
	@Override
	public void onFaile(RequestHandle requestHandle,int statusCode, Header[] headers,
			byte[] responseBody, Throwable error) {
		// TODO Auto-generated method stub
		if (requestHandle != null) {
			BaseHttpRequest request = requestHashMap.get(requestHandle);
			// WeakReference callback = request.getApiCallback();
			ApiCallback callback = request.getApiCallback();

			if (callback != null) {
				callback.onApiResultFailure(statusCode, headers, responseBody,error);
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
	/**
	 * 登录注册不带token获取验证码的接口
	 * @param type
	 * @param acount
	 * @param callback
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public RequestHandle getRegVerCode(String type,
			String acount,String vcode,ApiCallback callback) {
		BaseHttpRequest<BaseHttpResponse> request = new BaseHttpRequest<BaseHttpResponse>(
				BaseHttpResponse.class, this);

		request.setPath(URLConstant.GetVertCode);
		request.params().add("type",type);
		request.params().add("acount",acount);
		request.params().add("vcode", vcode);
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.get();
		
		requestHashMap.put(requestHandle, request);

		return requestHandle;
	}
	/**
	 * 带token获取验证码的接口
	 * @param type
	 * @param acount
	 * @param callback
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public RequestHandle getVerCode(String type,
			String acount,String vcode,ApiCallback callback) {
		BaseHttpRequest<BaseHttpResponse> request = new BaseHttpRequest<BaseHttpResponse>(
				BaseHttpResponse.class, this);

		request.setPath(URLConstant.GetVCode);
		request.params().add("type",type);
		request.params().add("acount",acount);
		request.params().add("vcode", vcode);
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.get();
		
		requestHashMap.put(requestHandle, request);

		return requestHandle;
	}
	/**
	 * 注册
	 * @param type
	 * @param acount
	 * @param vcode
	 * @param callback
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public RequestHandle registUser(String userAccount,
			String userPwd,String vcode,ApiCallback callback) {
		BaseHttpRequest<BaseHttpResponse> request = new BaseHttpRequest<BaseHttpResponse>(
				BaseHttpResponse.class, this);

		request.setPath(URLConstant.RegistUser);
		request.params().add("userAccount",userAccount);
		request.params().add("userPwd",userPwd);
		request.params().add("vcode", vcode);
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.post();
		
		requestHashMap.put(requestHandle, request);

		return requestHandle;
	}
	/**
	 * 重置密码
	 * @param type
	 * @param acount
	 * @param vcode
	 * @param callback
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public RequestHandle ResetPwd(String userAccount,
			String newPwd,String vcode,ApiCallback callback) {
		BaseHttpRequest<BaseHttpResponse> request = new BaseHttpRequest<BaseHttpResponse>(
				BaseHttpResponse.class, this);

		request.setPath(URLConstant.FindPwd);
		request.params().add("userAccount",userAccount);
		request.params().add("newPwd",newPwd);
		request.params().add("vcode", vcode);
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.post();
		
		requestHashMap.put(requestHandle, request);

		return requestHandle;
	}
	/**
	 * 绑定手机号
	 * @param mobile
	 * @param vcode
	 * @param callback
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public RequestHandle BindPhonum(String mobile,
			String vcode,ApiCallback callback) {
		BaseHttpRequest<BaseHttpResponse> request = new BaseHttpRequest<BaseHttpResponse>(
				BaseHttpResponse.class, this);

		request.setPath(URLConstant.BindPhonum);
		request.params().add("mobile", mobile);
		request.params().add("vcode", vcode);
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.post();
		
		requestHashMap.put(requestHandle, request);

		return requestHandle;
	}
	/**
	 * 更改绑定的手机号
	 * @param mobile
	 * @param oldVcode
	 * @param newVcode
	 * @param callback
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public RequestHandle BindNewPhonum(String mobile,
			String vcode,ApiCallback callback) {
		BaseHttpRequest<BaseHttpResponse> request = new BaseHttpRequest<BaseHttpResponse>(
				BaseHttpResponse.class, this);

		request.setPath(URLConstant.BindPhonum);
		request.params().add("mobile", mobile);
//		request.params().add("oldVcode", oldVcode);
		request.params().add("vcode", vcode);
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.post();
		
		requestHashMap.put(requestHandle, request);

		return requestHandle;
	}
	/**
	 * 验证校验码（解绑旧手机号，旧邮箱时，点下一步操作时用）
	 * @param type
	 * @param acount
	 * @param vcode
	 * @param callback
	 * @return
	 */
	public RequestHandle getCapVerCode(String type, String acount,String vcode,ApiCallback callback){
		BaseHttpRequest<BaseHttpResponse> request = new BaseHttpRequest<BaseHttpResponse>(
				BaseHttpResponse.class, this);
		request.setPath(URLConstant.GetCapVerCode);
		request.params().add("type", type);
		request.params().add("acount", acount);
		request.params().add("vcode", vcode);
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.get();
		requestHashMap.put(requestHandle, request);
		return requestHandle;
		
	}
	/**
	 * 绑定邮箱
	 * @param email
	 * @param vcode
	 * @param callback
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public RequestHandle BindEmail(String email,
			String vcode,ApiCallback callback) {
		BaseHttpRequest<BaseHttpResponse> request = new BaseHttpRequest<BaseHttpResponse>(
				BaseHttpResponse.class, this);

		request.setPath(URLConstant.BindEmail);
		request.params().add("email", email);
		request.params().add("vcode", vcode);
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.post();
		
		requestHashMap.put(requestHandle, request);

		return requestHandle;
	}
	/**
	 * 更改绑定的邮箱
	 * @param newEmail
	 * @param oldVcode
	 * @param newVcode
	 * @param callback
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public RequestHandle BindNewEmail(String email,String vcode,ApiCallback callback) {
		BaseHttpRequest<BaseHttpResponse> request = new BaseHttpRequest<BaseHttpResponse>(
				BaseHttpResponse.class, this);

		request.setPath(URLConstant.BindEmail);
		request.params().add("email", email);
		request.params().add("vcode", vcode);
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.post();
		
		requestHashMap.put(requestHandle, request);

		return requestHandle;
	}
	/**
	 * 修改密码
	 * @param oldPwd
	 * @param newPwd
	 * @param callback
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public RequestHandle ChangePwd(String oldPwd,
			String newPwd,ApiCallback callback) {
		BaseHttpRequest<BaseHttpResponse> request = new BaseHttpRequest<BaseHttpResponse>(
				BaseHttpResponse.class, this);

		request.setPath(URLConstant.ChangePwd);
		request.params().add("oldPwd", oldPwd);
		request.params().add("newPwd", newPwd);
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.post();
		
		requestHashMap.put(requestHandle, request);

		return requestHandle;
	}
	/**
	 * 退出登录
	 * @param callback
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public RequestHandle LogOut(ApiCallback callback){
    	BaseHttpRequest<BaseHttpResponse> request = new BaseHttpRequest<BaseHttpResponse>(
				BaseHttpResponse.class, this);
		request.setPath(URLConstant.LogOut);
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.post();
		requestHashMap.put(requestHandle, request);
		return requestHandle;
    	
    }
	/**
	 * 获取个人信息概要
	 * @param callback
	 * @return
	 */
	public RequestHandle getPersonalMsg(ApiCallback callback){
    	BaseHttpRequest<PersonalMsgBean> request = new BaseHttpRequest<PersonalMsgBean>(
    			PersonalMsgBean.class, this);
		request.setPath(URLConstant.GetPersonalMsg);
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.get();
		requestHashMap.put(requestHandle, request);
		return requestHandle;
		
	}
	/**
	 * 获取业务计费状态（计费相关）
	 * @param type 业务类型()50001:现场取证拍照 50002:现场取证录音 50003：现场取证录像
	 * @param count 可空  当次使用业务量 无论B、C类用户，均表示使用的业务量的条数、分钟数。照片按次，录像、录音按时长（分钟）。可为0。
	 * @param callback
	 * @return
	 */
	public RequestHandle getAccountStatus(int type,int count,ApiCallback callback){
    	
		//改bean
		BaseHttpRequest<PersonalMsgBean> request = new BaseHttpRequest<PersonalMsgBean>(
    			PersonalMsgBean.class, this);
		request.setPath(URLConstant.GetPersonalMsg);
		request.params().add("type", type+"");
		request.params().add("count", count+"");
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.get();
		requestHashMap.put(requestHandle, request);
		return requestHandle;
		
	}

	/**
	 * 实名认证
	 * @param idCardNum 身份证号
	 * @param userRealName 真实姓名
	 * @param file 已采集成功的照片
	 * @param callback
	 * @return
	 */
	public RequestHandle setRealName(String idCardNum,String userRealName,File file,ApiCallback callback){
		//得返回认证是否成功
		BaseHttpRequest<BaseHttpResponse> request = new BaseHttpRequest<BaseHttpResponse>(
				BaseHttpResponse.class, this);
		request.setPath(URLConstant.GetPersonalMsg);
		request.params().add("idCardNum", idCardNum);
		request.params().add("userRealName", userRealName);
		try {
			request.params().put("file", file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.post();
		requestHashMap.put(requestHandle, request);
		return requestHandle;
		
	}

}
