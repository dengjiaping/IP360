package com.truthso.ip360.bean;

import com.truthso.ip360.net.BaseHttpResponse;
/**
 * 上传（保全）文件前，调本地的接口的响应bean
 * @author summer
 *
 */
public class UpLoadBean  extends BaseHttpResponse{
	private Upload datas;
	public Upload getDatas() {
		return datas;
		
	}

	public void setDatas(Upload datas) {
		this.datas = datas;
	}

	public class Upload{
		private int pkValue;//证据记录唯一标识（主键）。mobileRight id
		private String fileUrl;//上传文件到阿里云的objectKey
		
		public String getFileUrl() {
			return fileUrl;
		}

		public void setFileUrl(String fileUrl) {
			this.fileUrl = fileUrl;
		}

		public int getPkValue() {
			return pkValue;
		}

		public void setPkValue(int pkValue) {
			this.pkValue = pkValue;
		}
		
	}
}
