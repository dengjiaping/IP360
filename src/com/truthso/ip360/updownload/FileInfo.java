package com.truthso.ip360.updownload;

public class FileInfo {

	private String filePath;
	private String fileName;
	private String fileSize;
	private String fileLoc;
	private String llsize;
	private int type;//文件类型 
	private int mobiletype;//现场取证的文件类型
	private String fileCreatetime;
	private String fileTime;//文件时长
	
	public String getFileTime() {
		return fileTime;
	}
	public void setFileTime(String fileTime) {
		this.fileTime = fileTime;
	}
	public int getMobiletype() {
		return mobiletype;
	}
	public void setMobiletype(int mobiletype) {
		this.mobiletype = mobiletype;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getLlsize() {
		return llsize;
	}
	public void setLlsize(String llsize) {
		this.llsize = llsize;
	}
	public String getFileLoc() {
		return fileLoc;
	}
	public void setFileLoc(String fileLoc) {
		this.fileLoc = fileLoc;
	}
	public String getFileCreatetime() {
		return fileCreatetime;
	}
	public void setFileCreatetime(String fileCreatetime) {
		this.fileCreatetime = fileCreatetime;
	}
	private int position;
	private int resourceId;

	private String objectKey;
	public String getObjectKey() {
		return objectKey;
	}
	public void setObjectKey(String objectKey) {
		this.objectKey = objectKey;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
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
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	@Override
	public String toString() {
		return "UpLoadInfo [filePath=" + filePath + ", fileName=" + fileName + ", fileSize=" + fileSize + ", position=" + position + ", resourceId=" + resourceId + "]";
	}

	
}
