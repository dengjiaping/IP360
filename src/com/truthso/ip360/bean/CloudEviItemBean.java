package com.truthso.ip360.bean;
/**
 * 云端证据条目的数据bean
 * @author summer
 *
 */
public class CloudEviItemBean {
	private String fileName;//文件名
	private String createTime;// 创建时间
	private String fileSize;// 文件大小
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	
}
