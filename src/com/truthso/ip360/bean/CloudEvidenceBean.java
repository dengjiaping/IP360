package com.truthso.ip360.bean;

import java.util.List;

import com.truthso.ip360.net.BaseHttpResponse;
/**
 * @despriction :云端证据的Bean
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-10-14下午4:42:17
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class CloudEvidenceBean extends BaseHttpResponse{
	
	private List<CloudEviItemBean> datas;

	public List<CloudEviItemBean> getDatas() {
		return datas;
	}

	public void setDatas(List<CloudEviItemBean> datas) {
		this.datas = datas;
	}
	
	
	
}
