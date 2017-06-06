package com.truthso.ip360.net;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import android.content.Intent;
import com.loopj.android.http.RequestHandle;
import com.truthso.ip360.activity.LoginActivity;
import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.bean.AccountStatusBean;
import com.truthso.ip360.bean.CertificateInfoBean;
import com.truthso.ip360.bean.CloudEvidenceBean;
import com.truthso.ip360.bean.DefraymentBean;
import com.truthso.ip360.bean.DownLoadFileBean;
import com.truthso.ip360.bean.ExpenseBean;
import com.truthso.ip360.bean.FileBean;
import com.truthso.ip360.bean.FilePositionBean;
import com.truthso.ip360.bean.FileRemarkBean;
import com.truthso.ip360.bean.LoginBean;
import com.truthso.ip360.bean.NotarAccountBean;
import com.truthso.ip360.bean.NotarCityBean;
import com.truthso.ip360.bean.NotarMsgBean;
import com.truthso.ip360.bean.PersonalMsgBean;
import com.truthso.ip360.bean.ShowPictureBean;
import com.truthso.ip360.bean.UpLoadBean;
import com.truthso.ip360.bean.VerUpDateBean;
import com.truthso.ip360.bean.ZfbPayBean;
import com.truthso.ip360.constants.URLConstant;
import com.truthso.ip360.utils.CheckUtil;

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
		//被挤掉的时候，调证据列表的接口会闪退一下，因为response为空
		if (CheckUtil.isEmpty(response)||response.getCode() == 405||response.getCode()==400) {//其他设备
			Intent intent = new Intent(MyApplication.getInstance(), LoginActivity.class);
			intent.putExtra("tag","otherlogin");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			MyApplication.getInstance().startActivity(intent);
			return;
		}else if(response.getCode() == 501){//登陆失效
			Intent intent = new Intent(MyApplication.getInstance(), LoginActivity.class);
			intent.putExtra("tag","ineffic");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			MyApplication.getInstance().startActivity(intent);
			return;
		}

		if (requestHandle != null) {
			BaseHttpRequest request = requestHashMap.get(requestHandle);
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
	 * 登录注册找回密码不带token获取验证码的接口
	 * @param type
	 * @param acount
	 * @param callback
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public RequestHandle getRegVerCode(String type, String acount,String vcode,ApiCallback callback) {
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
	public RequestHandle getVerCode(String type, String acount,String vcode,ApiCallback callback) {
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
		BaseHttpRequest<AccountStatusBean> request = new BaseHttpRequest<AccountStatusBean>(
				AccountStatusBean.class, this);
		request.setPath(URLConstant.GetAccountStatus);
		request.params().add("type", type+"");
		request.params().add("count", count+"");
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.get();
		requestHashMap.put(requestHandle, request);
		return requestHandle;
		
	}

	/**
	 * 实名认证
	 * @param  picControl 人像控制版本
	 * @param terminalId 终端号
	 * @param idCardNum 身份证号
	 * @param userRealName 真实姓名
	 * @param file 已采集成功的照片
	 * @param callback
	 * @return
	 */
	public RequestHandle setRealName(String picControl,String terminalId,String idCardNum,String userRealName,File file,ApiCallback callback){
		//得返回认证是否成功
		BaseHttpRequest<BaseHttpResponse> request = new BaseHttpRequest<BaseHttpResponse>(
				BaseHttpResponse.class, this);
		request.setPath(URLConstant.RealNameCer);
		request.params().add("picControl", picControl);
		request.params().add("terminalId", terminalId);
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
	/**
	 * 实名认证
	 * @param  picControl 人像控制版本
	 * @param terminalId 终端号
	 * @param idCardNum 身份证号
	 * @param userRealName 真实姓名
	 * @param file 已采集成功的照片
	 * @param callback
	 * @return
	 */
	public RequestHandle setRealNameDemo(String picControl,String terminalId,String idCardNum,String userRealName,File file,ApiCallback callback){
		//得返回认证是否成功
		BaseHttpRequest<BaseHttpResponse> request = new BaseHttpRequest<BaseHttpResponse>(
				BaseHttpResponse.class, this);
		request.setPath(URLConstant.setRealNameDemo);
		request.params().add("picControl", picControl);
		request.params().add("terminalId", terminalId);
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
	/**
	 * 云端证据
	 * @param keywork 搜索内容 （确权和现场取证:文件名，remark pc取证:证据名称，remark） 可空
	 * @param type 展示类别 1-确权  2-现场取证 3-pc取证 非空   1-确权 2-现场取证 3-pc取证 4-全部(除了确权其他全包含)
	 * @param mobileType 取证类型 现场取证 （拍照（50001）、录像（50003）、录音（50002）可空
	 * @param pageNumber 当前第几页  非空
	 * @param pageSize 每页显示条数 非空
	 * @param callback
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public RequestHandle getCloudEvidence(String keywork,int type,int mobileType,int pageNumber,int pageSize,int versionCode,ApiCallback callback){
		BaseHttpRequest<CloudEvidenceBean> request = new BaseHttpRequest<CloudEvidenceBean>(
				CloudEvidenceBean.class, this);
		request.setPath(URLConstant.GetCloudEvidence);
		request.params().add("keywork", keywork);
		request.params().add("type", type+"");
		request.params().add("mobileType", mobileType+"");
		request.params().add("pageNumber", pageNumber+"");
		request.params().add("pageSize", pageSize+"");
		request.params().add("versionCode", versionCode+"");
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.get();
		requestHashMap.put(requestHandle, request);
		return requestHandle;
	}
	/**
	 * 文件保全（这个接口只传文件hashcode等信息，不上传文件）
	 * @param fileType 文件类型   文件类型 （拍照（50001）、录像（50003）、录音（50002） 非空
	 * @param fileSize 文件大小，单位为B
	 * @param hashCode 哈希值 非空
	 * @param fileDate 取证时间	
	 * @param fileLocation 取证地点 可空
	 * @param fileTime 取证时长 录像 录音不为空
	 * @param  encrypte 加密的需要传密文  摘要规则: (文件名称+取证时间+取证地点+哈希值+秘钥) 拼接成字符串做一下 sha512
	 * @param	//code返回508,表示文件信息不正确(可能被篡改)String encrypted ,
	 * @param imei 手机的IMEI码
	 * @param callback
	 * @return
	 */
	public RequestHandle uploadPreserveFile(String fileTitle,int fileType,String fileSize,String hashCode,String fileDate,String fileLocation,String fileTime,String imei,String latitudeLongitude,String encrypte,int rsaId,ApiCallback callback){
		BaseHttpRequest<UpLoadBean> request = new BaseHttpRequest<UpLoadBean>(
				UpLoadBean.class, this);
		request.setPath(URLConstant.UploadPreserveFile);
		request.params().add("fileTitle", fileTitle);
		request.params().add("fileType", fileType+"");
		request.params().add("fileSize", fileSize);
		request.params().add("hashCode", hashCode);
		request.params().add("fileDate", fileDate);
//		request.params().add("fileUrl", fileUrl);
		request.params().add("fileLocation", fileLocation);
		request.params().add("fileTime", fileTime);
		request.params().add("imei", imei);
		request.params().add("latitudeLongitude", latitudeLongitude);
		request.params().add("encrypte", encrypte);
		request.params().add("rsaId", rsaId+"");
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.post();
		requestHashMap.put(requestHandle, request);
		return requestHandle;
		
	}
	
	/**
	 * 设置备注
	 * @param remarkText 备注的文本
	 * @param pkValue 证据记录主键值
	 * @param type  请求展示类别  1-确权  2-现场取证 3-pc取证
	 * @param callback
	 * @return
	 */
	public RequestHandle setFileRemark(String remarkText,int pkValue,int type,int dataType,ApiCallback callback){
		BaseHttpRequest<BaseHttpResponse> request = new BaseHttpRequest<BaseHttpResponse>(
				BaseHttpResponse.class, this);
		request.setPath(URLConstant.SetFileRemark);
		request.params().add("remarkText", remarkText);
		request.params().add("pkValue", pkValue+"");
		request.params().add("dataType", dataType+"");
		request.params().add("type", type+"");
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.post();
		requestHashMap.put(requestHandle, request);
		return requestHandle;
	}
	/**
	 * 查看 保全证书
	 * @param pkValue
	 * @param type 1-确权  2-现场取证 3-pc取证
	 * @param callback
	 * @return
	 */
	public RequestHandle getCertificateInfo(int pkValue,int type,int dataType,ApiCallback callback){
		//接口要返回Url，后台改好再换Bean
		BaseHttpRequest<CertificateInfoBean> request = new BaseHttpRequest<CertificateInfoBean>(
				CertificateInfoBean.class, this);
		request.setPath(URLConstant.GetCertificateInfo);
		request.params().add("pkValue", pkValue+"");
		request.params().add("type", type+"");
		request.params().add("dataType", dataType+"");
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.get();
		requestHashMap.put(requestHandle, request);
		return requestHandle;
	}
	/**
	 * 上传文件:(调完保全接口调，返回可用再上传)
	 * @param resourceId 唯一标示，就是调完保全接口给返回的keyValue
	 * @param position 文件断点的位置，为0 表示还没传过
	 * @param file 要上传的文件
	 * @param callback
	 * @return
	 */
	public RequestHandle uploadFile(int resourceId,int position,File file,ApiCallback callback){
		
		BaseHttpRequest<BaseHttpResponse> request = new BaseHttpRequest<BaseHttpResponse>(
				BaseHttpResponse.class, this);
		request.setPath(URLConstant.UploadFile);
		request.params().add("resourceId", resourceId+"");
		request.params().add("position", position+"");
		try {
			request.params().put("file", file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.post();
		requestHashMap.put(requestHandle, request);
		return requestHandle;
	}
	/**
	 * 文件断点位置
	 * @param resourceId 文件唯一标示，调完保全接口返回的valueKey
	 * @param callback
	 * @return
	 */
	public RequestHandle getFilePosition(int resourceId,ApiCallback callback){
		
		BaseHttpRequest<FilePositionBean> request = new BaseHttpRequest<FilePositionBean>(
				FilePositionBean.class, this);
		request.setPath(URLConstant.GetFilePosition);
		request.params().add("resourceId", resourceId+"");
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.post();
		requestHashMap.put(requestHandle, request);
		return requestHandle;
	}
	/**
	 * 文件的下载
	 * @param pkValue 唯一标示（云端证据接口返回的keyValue）
	 * @param type 1-确权  2-现场取证 3-pc取证
	 * @param callback
	 * @return
	 */
	public RequestHandle downloadFile(int pkValue,int type,int dataType,ApiCallback callback){
		BaseHttpRequest<DownLoadFileBean> request = new BaseHttpRequest<DownLoadFileBean>(
				DownLoadFileBean.class, this);
		request.setPath(URLConstant.DownloadFile);
		request.params().add("pkValue", pkValue+"");
		request.params().add("type", type+"");
		request.params().add("dataType", dataType+"");
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.post();
		requestHashMap.put(requestHandle, request);
		return requestHandle;
	}
	/**
	 * 版本更新
	 * @param versionCode 版本号
	 * @param callback
	 * @return
	 */
	public RequestHandle getVerUpDate(String  versionCode,ApiCallback callback){
		BaseHttpRequest<VerUpDateBean> request = new BaseHttpRequest<VerUpDateBean>(
				VerUpDateBean.class, this);
		request.setPath(URLConstant.GetVerUpDate);
		request.params().add("versionCode", versionCode);
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.get();
		requestHashMap.put(requestHandle, request);
		return requestHandle;
	}
	/**
	 * 文件上传到oss后用，返回码200该文件存在，404未找到
	 * @param resourceId 保全文件接口返回的keyValue
	 * @param callback
	 * @return
	 */
	public RequestHandle uploadFileOssStatus(int resourceId,ApiCallback callback){
		BaseHttpRequest<BaseHttpResponse> request = new BaseHttpRequest<BaseHttpResponse>(BaseHttpResponse.class,this);
		request.setPath(URLConstant.UploadFileOssStatus);
		request.params().add("resourceId", resourceId+"");
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.get();
		requestHashMap.put(requestHandle, request);
		
		return requestHandle;
		
	}
	/**
	 * 备注页，获取备注时调用
	 * @param pkValue
	 * @param type
	 * @param callback
	 * @return
	 */
	public RequestHandle getFileRemark(int pkValue,int type,ApiCallback callback){
		BaseHttpRequest<FileRemarkBean> request = new BaseHttpRequest<FileRemarkBean>(FileRemarkBean.class,this);
		request.setPath(URLConstant.getFileRemark);
		request.params().add("pkValue", pkValue+"");
		request.params().add("type", type+"");
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.get();
		requestHashMap.put(requestHandle, request);
		return requestHandle;
	}
	/**
	 * 取消上传文件
	 * @param pkValue
	 * @param callback
	 * @return
	 */
	public RequestHandle DeleteFileInfo(int pkValue,ApiCallback callback){
		BaseHttpRequest<BaseHttpResponse> request = new BaseHttpRequest<BaseHttpResponse>(BaseHttpResponse.class,this);
		request.setPath(URLConstant.deleteFileInfo);
		request.params().add("pkValue", pkValue+"");
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.get();
		requestHashMap.put(requestHandle, request);
		return requestHandle;
	}

	/**
	 * 扣费
	 * @param type （拍照（50001）、录像（50003）、录音（50002）
	 * @param count 无论B、C类用户，均表示使用的业务量的条数、分钟数。照片按次，录像、录音按时长（分钟）。可为0。
	 * @param callback
     * @return
     */
	public RequestHandle Payment(int pkValue,int type,int count,ApiCallback callback){
		BaseHttpRequest<ExpenseBean> request = new BaseHttpRequest<ExpenseBean>(ExpenseBean.class,this);
		request.setPath(URLConstant.Payment);
		request.params().add("pkValue", pkValue+"");
		request.params().add("type", type+"");
		request.params().add("count", count+"");
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.get();
		requestHashMap.put(requestHandle, request);
		return requestHandle;
	}

	/**
	 *支付宝支付接口
	 * @param money
	 * @param
	 * @param callback
     * @return
     */
	public RequestHandle getOrderInfo(String money,ApiCallback callback){
		BaseHttpRequest<ZfbPayBean> request = new BaseHttpRequest<ZfbPayBean>(ZfbPayBean.class,this);
		request.setPath(URLConstant.GetOrderInfo);
		request.params().add("money", money);
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.get();
		requestHashMap.put(requestHandle, request);
		return requestHandle;
	}

	/**
	 * 轮播图
	 * @param callback
     * @return
     */
	public RequestHandle getShowPicture(ApiCallback callback){
		BaseHttpRequest<ShowPictureBean> request = new BaseHttpRequest<ShowPictureBean>(ShowPictureBean.class,this);
		request.setPath(URLConstant.getShowPicture);
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.get();
		requestHashMap.put(requestHandle, request);
		return requestHandle;
	}

	/**
	 * 用户意见反馈
	 * @param content
	 * @param contacts 反馈内容
	 * @param callback 联系方式
     * @return
     */
	public RequestHandle UserAdvice(String content,String contacts,String name,ApiCallback callback){
		BaseHttpRequest<BaseHttpResponse> request = new BaseHttpRequest<BaseHttpResponse>(BaseHttpResponse.class,this);
		request.setPath(URLConstant.userAdvice);
		request.params().add("name", name);
		request.params().add("content", content);
		request.params().add("contacts", contacts);
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.post();
		requestHashMap.put(requestHandle, request);
		return requestHandle;
	}

	/**
	 * 公证信息提交
	 * @param notarName 公证名称
	 * @param notaryId 公证处id
	 * @param notarCopies 公证书所需份数
	 * @param receiver 领取类型 1-本人领取；2-其他人自然领取
	 * @param domicileLoc 申请人户籍所在地
	 * @param currentAddress 申请人现居地址
	 * @param pkValue
	 * @param receiverName 领取者姓名
	 * @param receiverCardId 领取者身份证号
	 * @param receiverPhoneNum 领取者手机号
     * @param receiverEmail 领取者邮箱
     * @param callback
     * @return  code 503 公证名称已存在
     */
	public RequestHandle commitNotarMsg(String notarName,int notaryId,int notarCopies,String receiver,String domicileLoc,String currentAddress,String pkValue,String receiverName,String receiverCardId,String receiverPhoneNum,String receiverEmail,ApiCallback callback){
		BaseHttpRequest<BaseHttpResponse> request = new BaseHttpRequest<BaseHttpResponse>(BaseHttpResponse.class,this);
		request.setPath(URLConstant.commitNotarMsg);
		request.params().add("notarName", notarName);
		request.params().add("notaryId", notaryId +"");
		request.params().add("notarCopies", notarCopies+"");
		request.params().add("receiver", receiver);
		request.params().add("domicileLoc", domicileLoc);
		request.params().add("currentAddress", currentAddress);
		request.params().add("pkValue", pkValue);
		request.params().add("receiverName", receiverName);
		request.params().add("receiverCardId", receiverCardId);
		request.params().add("receiverPhoneNum", receiverPhoneNum);
		request.params().add("receiverEmail", receiverEmail);
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.post();
		requestHashMap.put(requestHandle, request);
		return requestHandle;
	}

	/**
	 * 申请公证人账号信息 点公证按钮时调用
	 * @param callback
	 * @return
     */
	public RequestHandle getAccountMsg(ApiCallback callback){
		BaseHttpRequest<NotarAccountBean> request = new BaseHttpRequest<NotarAccountBean>(NotarAccountBean.class,this);
		request.setPath(URLConstant.getAccountMsg);
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.get();
		requestHashMap.put(requestHandle, request);
		return requestHandle;
	}

	/**
	 * 申请公证支付费用  申请公证第三步。确认支付时调用
	 * @param notaryNum
	 * @param callback
     * @return
     */
	public RequestHandle defrayment(String notaryNum,ApiCallback callback){
		BaseHttpRequest<DefraymentBean> request = new BaseHttpRequest<DefraymentBean>(DefraymentBean.class,this);
		request.setPath(URLConstant.defrayment);
		request.params().add("notaryNum", notaryNum);
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.post();
		requestHashMap.put(requestHandle, request);
		return requestHandle;
	}

	/**
	 * 我的公证列表
	 * @param pageNumber 页码值
	 * @param pageSize 每页数据条数
	 * @param callback
     * @return
     */
	public RequestHandle getNotarMsg(int pageNumber,int pageSize,ApiCallback callback){
		BaseHttpRequest<NotarMsgBean> request = new BaseHttpRequest<NotarMsgBean>(NotarMsgBean.class,this);
		request.setPath(URLConstant.getNotarMsg);
		request.params().add("pageNumber", pageNumber+"");
		request.params().add("pageSize", pageSize+"");
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.get();
		requestHashMap.put(requestHandle, request);
		return requestHandle;
	}

	/**
	 * 撤销公证
	 * @param notaryNum
	 * @param callback
     * @return
     */
	public RequestHandle backoutNotary(String notaryNum,ApiCallback callback){
		BaseHttpRequest<BaseHttpResponse> request = new BaseHttpRequest<BaseHttpResponse>(BaseHttpResponse.class,this);
		request.setPath(URLConstant.backoutNotary);
		request.params().add("notaryNum", notaryNum);
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.post();
		requestHashMap.put(requestHandle, request);
		return requestHandle;
	}

	/**
	 * 获取公证处以及所在城市
	 * @param callback
	 * @return
     */
	public RequestHandle getNotaryCity(ApiCallback callback){
		BaseHttpRequest<NotarCityBean> request = new BaseHttpRequest<NotarCityBean>(NotarCityBean.class,this);
		request.setPath(URLConstant.getNotaryCity);
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.post();
		requestHashMap.put(requestHandle, request);
		return requestHandle;
	}

	/**
	 * 公证服务详情
	 * @param pageNumber 页码值
	 * @param pageSize 每页数据条数
	 * @param pkValue 公证服务的id
	 * @param callback
     * @return
     */
	public RequestHandle getNotarInfo(int pageNumber,int pageSize,int pkValue, ApiCallback callback){
		BaseHttpRequest<FileBean> request = new BaseHttpRequest<FileBean>(FileBean.class,this);
		request.setPath(URLConstant.getNotarInfo);
		request.params().add("pageNumber", pageNumber+"");
		request.params().add("pageSize", pageSize+"");
		request.params().add("pkValue", pkValue+"");
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.post();
		requestHashMap.put(requestHandle, request);
		return requestHandle;
	}
	/**
	 * 云端证据 全部数据
	 * @param keywork 搜索内容 （确权和现场取证:文件名，remark pc取证:证据名称，remark） 可空
	 * @param pageNumber 当前第几页  非空
	 * @param pageSize 每页显示条数 非空
	 * @param callback
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public RequestHandle getCloudEvidenceAll(String keywork,int pageNumber,int pageSize,ApiCallback callback){
		BaseHttpRequest<CloudEvidenceBean> request = new BaseHttpRequest<CloudEvidenceBean>(
				CloudEvidenceBean.class, this);
		request.setPath(URLConstant.getCloudEvidenceAll);
		request.params().add("keywork", keywork);
		request.params().add("pageNumber", pageNumber+"");
		request.params().add("pageSize", pageSize+"");
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.get();
		requestHashMap.put(requestHandle, request);
		return requestHandle;
	}
	/**
	 * 云端证据 二级里面的证据
	 * @param pageNumber 当前第几页  非空
	 * @param pageSize 每页显示条数 非空
	 * @param callback
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public RequestHandle getSubEvidence(int type,int pkValue,int pageNumber,int pageSize,ApiCallback callback){
		BaseHttpRequest<CloudEvidenceBean> request = new BaseHttpRequest<CloudEvidenceBean>(
		CloudEvidenceBean.class, this);
		request.setPath(URLConstant.getSubEvidence);
		request.params().add("type", type+"");
		request.params().add("pkValue", pkValue+"");
		request.params().add("pageNumber", pageNumber+"");
		request.params().add("pageSize", pageSize+"");
		request.setApiCallback(callback);
		RequestHandle requestHandle = request.get();
		requestHashMap.put(requestHandle, request);
		return requestHandle;
	}

	/**
	 * 公证审核被拒，重新提交
	 * @param notarCopies 公证书所需份数
	 * @param domicileLoc 申请人户籍所在地
	 * @param currentAddress 申请人现居地址
	 * @param receiverPhoneNum 领取者手机号
	 * @param receiverEmail 领取者邮箱
	 * @param pkValue 此条公证服务id
     * @param callback
     * @return
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public RequestHandle commitAgainNotarMsg(int notarCopies,String domicileLoc,String currentAddress,String receiverPhoneNum,String receiverEmail,String pkValue,ApiCallback callback){
		BaseHttpRequest<CloudEvidenceBean> request = new BaseHttpRequest<CloudEvidenceBean>(
				CloudEvidenceBean.class, this);
		request.setPath(URLConstant.commitAgainNotarMsg);
		request.params().add("notarCopies", notarCopies+"");
		request.params().add("pkValue", pkValue+"");
		request.params().add("domicileLoc", domicileLoc);
		request.params().add("currentAddress", currentAddress);
		request.params().add("receiverPhoneNum", receiverPhoneNum);
		request.params().add("receiverEmail", receiverEmail);
		request.params().add("pkValue", pkValue);

		request.setApiCallback(callback);
		RequestHandle requestHandle = request.post();
		requestHashMap.put(requestHandle, request);
		return requestHandle;
	}

}
