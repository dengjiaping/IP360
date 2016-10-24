package com.truthso.ip360.bean;

public class DownLoadInfo {

	private int resourceId;
	private String fileName;
	private String fileSize;
	private String downLoadUrl;
	private int position;
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public int getResourceId() {
		return resourceId;
	}
	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
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
	public String getDownLoadUrl() {
		return downLoadUrl;
	}
	public void setDownLoadUrl(String downLoadUrl) {
		this.downLoadUrl = downLoadUrl;
	}
	
}
