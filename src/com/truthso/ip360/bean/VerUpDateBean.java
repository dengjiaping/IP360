package com.truthso.ip360.bean;

import com.truthso.ip360.net.BaseHttpResponse;
/**
 * @despriction :版本更新的bean
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-10-20下午5:37:30
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class VerUpDateBean extends BaseHttpResponse{
	private VerUPDate datas;
	
	public VerUPDate getDatas() {
		return datas;
	}

	public void setDatas(VerUPDate datas) {
		this.datas = datas;
	}

	public class VerUPDate{
		private String iVersionCode;//服务器上的版本号
		private String apkURl;//apk的下载路径
		public String getiVersionCode() {
			return iVersionCode;
		}
		public void setiVersionCode(String iVersionCode) {
			this.iVersionCode = iVersionCode;
		}
		public String getApkURl() {
			return apkURl;
		}
		public void setApkURl(String apkURl) {
			this.apkURl = apkURl;
		}
		
	}
}
