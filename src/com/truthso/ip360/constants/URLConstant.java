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
	/**
	 * 登录
	 */
	public  final  static String LoginPath = "http://101.201.74.230:9091/api/v1/user/login";
	/**
	 * 登录，注册获取验证码
	 */
	public final static String GetVertCode = "http://101.201.74.230:9091/api/v1/vcode/getVcodeNotToken";
	
	/**
	 * 除登录注册外的获取验证码
	 */
	public final static String GetVCode = "http://101.201.74.230:9091/api/v1/vcode/getVcode";
	/**
	 * 注册
	 * 
	 */
	public final static String RegistUser = "http://101.201.74.230:9091/api/v1/user/registUser";
	/**
	 * 找回密码
	 * 
	 */
	public final static String FindPwd = "http://101.201.74.230:9091/api/v1/security/resetPwd";

	/**
	 * 绑定手机号/更改绑定手机号
	 * 
	 */
	public final static String BindPhonum = "http://101.201.74.230:9091/api/v1/security/bindMobile";
	/**
	 * 绑定邮箱/更改绑定邮箱
	 * 
	 */
	public final static String BindEmail = "http://101.201.74.230:9091/api/v1/security/bindEmail";
	/**
	 * 修改密码
	 * 
	 */
	public final static String ChangePwd = "http://101.201.74.230:9091/api/v1/security/changePwd";

}