package com.truthso.ip360.bean;
/**
 * 文件列表公用的bean
 * @author wsx_summer  Email:wangshaoxia@truthso.com
 * @date 创建时间：2016年6月7日下午4:33:57
 * @version 1.0  
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class CommonBean {
	private int id;
	private String fileName;//文件名
	private String fileSize;//文件大小
	private String createTime;//时间
	private String resourceUrl;
	private String lable;//文件标签
	private String fileType;//文件类型
	private String recordTime;//录音时长
	
	public String getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}
	public String getResourceUrl() {
		return resourceUrl;
	}
	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
//	public String getJsonObject() {
//		return jsonObject;
//	}
//	public void setJsonObject(String jsonObject) {
//		this.jsonObject = jsonObject;
//	}
	public String getLable() {
		return lable;
	}
	public void setLable(String lable) {
		this.lable = lable;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	@Override
	public String toString() {
		return "CommonBean [id=" + id + ", fileName=" + fileName + ", fileSize=" + fileSize + ", createTime=" + createTime + ", resourceUrl=" + resourceUrl + ", lable=" + lable + ", fileType="
				+ fileType + "]";
	}
	
	
}
