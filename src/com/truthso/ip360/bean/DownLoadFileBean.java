package com.truthso.ip360.bean;

import com.truthso.ip360.net.BaseHttpResponse;
/**
 * @despriction :文件下载的响应bean
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-10-20下午5:20:12
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class DownLoadFileBean extends BaseHttpResponse{
	private DownLoad datas;
	
	public DownLoad getDatas() {
		return datas;
	}

	public void setDatas(DownLoad datas) {
		this.datas = datas;
	}

	public class DownLoad{
		private String fileUrl;//文件下载路径

		public String getFileUrl() {
			return fileUrl;
		}

		public void setFileUrl(String fileUrl) {
			this.fileUrl = fileUrl;
		}
		
	}
}
