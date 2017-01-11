package com.truthso.ip360.bean;

import com.truthso.ip360.net.BaseHttpResponse;
/**
 * @despriction :查看保全证书的bean
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-10-21下午2:17:10
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class CertificateInfoBean extends BaseHttpResponse{
	private CertificateInfo datas;


	public CertificateInfo getDatas() {
		return datas;
	}

	public void setDatas(CertificateInfo datas) {
		this.datas = datas;
	}

	public class CertificateInfo{
		private String arreaStatus;//0是欠费，1是不欠费
		private String certificateUrl;//Url

		public String getArreaStatus() {
			return arreaStatus;
		}

		public void setArreaStatus(String arreaStatus) {
			this.arreaStatus = arreaStatus;
		}

		public String getCertificateUrl() {
			return certificateUrl;
		}

		public void setCertificateUrl(String certificateUrl) {
			this.certificateUrl = certificateUrl;
		}
		
	}
}
