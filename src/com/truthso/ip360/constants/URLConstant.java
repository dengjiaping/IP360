package com.truthso.ip360.constants;
/**
 * @despriction :存放接口Url的常量类
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-9-30上午11:24:01
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class URLConstant {
//	static String str = "192.168.1.20:8081";
//	static String str = "101.201.74.230:9091";
   // static String str = "appapi.ip360.net.cn";
	// static String str = "59.110.44.147:9020";//3.25测试环境

//	static  String str ="101.201.112.127:9020";
	static String str = "60.205.86.209:9020";
	/**`
	 * 登录
	 */
	public  final  static String LoginPath = "http://"+str+"/api/v1/user/login";
	/**
	 * 登录，注册，忘记密码获取验证码
	 */
	public final static String GetVertCode = "http://"+str+"/api/v1/vcode/getVcodeNotToken";
	
	/**
	 * 除登录注册外的获取验证码
	 */
	public final static String GetVCode = "http://"+str+"/api/v1/vcode/getVcode";
	/**
	 * 注册
	 * 
	 */
	public final static String RegistUser = "http://"+str+"/api/v1/user/registUser";
	/**
	 * 找回密码
	 * 
	 */
	public final static String FindPwd ="http://"+str+"/api/v1/security/resetPwd";

	/**
	 * 绑定手机号/更改绑定手机号
	 * 
	 */
	public final static String BindPhonum = "http://"+str+"/api/v1/security/bindMobile";
	/**
	 * 绑定邮箱/更改绑定邮箱
	 * 
	 */
	public final static String BindEmail = "http://"+str+"/api/v1/security/bindEmail";
	/**
	 * 验证码校验(解绑旧邮箱或者手机号，下一步的操作时用)
	 * 
	 */
	public final static String GetCapVerCode = "http://"+str+"/api/v1/vcode/getValidateVcode";
	
	/**
	 * 修改密码
	 * 
	 */
	public final static String ChangePwd = "http://"+str+"/api/v1/security/changePwd";
	/**
	 * 退出登录
	 */
	public final static String LogOut = "http://"+str+"/api/v1/user/logout";
	/**
	 * 获取个人信息概要
	 */
	public final static String GetPersonalMsg = "http://"+str+"/api/v1/user/getUserInfo";
	/**
	 * 获取业务计费状态（计费相关）
	 */
	public final static String GetAccountStatus = "http://"+str+"/api/v1/expense/getAccountStatus";
	/**
	 * 云端证据
	 */
	public final static String GetCloudEvidence = "http://"+str+"/api/v1/file/getCloudEvidence";
	/**
	 * 文件保全
	 */
	public final static String UploadPreserveFile = "http://"+str+"/api/v1/file/uploadPreserveFile";
	/**
	 * 设置文件备注
	 */
	public final static String SetFileRemark = "http://"+str+"/api/v1/file/setFileRemark";
	/**
	 * 获取文件备注
	 */
	public final static String getFileRemark = "http://"+str+"/api/v1/file/getFileRemark";
	/**
	 * 查看保全证书
	 */
	public final static String GetCertificateInfo = "http://"+str+"/api/v1/file/getCertificateUrl";
	/**
	 * 文件断点的位置
	 */
	public final static String GetFilePosition = "http://"+str+"/api/v1/file/getFilePosition";
	/**
	 * 上传文件
	 */
//	public final static String UploadFile = "http://101.201.74.230:9091/api/v1/file/uploadFile";
	public final static String UploadFile = "http://"+str+"/api/v1/file/uploadFile";
	
	/**
	 * 文件下载
	 */
	public final static String DownloadFile = "http://"+str+"/api/v1/file/downloadFile";
	/**
	 * 版本更新
	 */
	public final static String GetVerUpDate = "http://"+str+"/api/v1/system/getVerUp";
	/**
	 * 实名认证
	 */
	public final static String RealNameCer = "http://"+str+"/api/v1/security/setRealName";
	/**
	 * 实名认证demo
	 */
	public final static String setRealNameDemo = "http://"+str+"/api/v1/security/setRealNameDemo";
	/**
	 * 上传文件到OSS后调用
	 */
	public final static String UploadFileOssStatus = "http://"+str+"/api/v1/file/uploadFileOssStatus";
	/**
	 * 取消上传文件
	 */
	public final static String deleteFileInfo = "http://"+str+"/api/v1/file/deleteFileInfo";
	/**
	 * 扣费
	 */
	public final static String Payment = "http://"+str+"/api/v1/expense/payment";
	/**
	 * 支付宝支付,从后台获取订单信息的接口
	 */
	public final static String GetOrderInfo = "http://"+str+"/api/v1/expense/getOrderInfo";
	/**
	 * 获取轮播图图片
	 */
	public final static String getShowPicture = "http://"+str+"/api/v1/system/getShowPicture";
	/**
	 * 用户意见反馈
	 */
	public final static String userAdvice = "http://"+str+"/api/v1/**/opinionFeedback";
}