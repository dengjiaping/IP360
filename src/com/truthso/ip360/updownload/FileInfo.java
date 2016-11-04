package com.truthso.ip360.updownload;

public class FileInfo {

	private String filePath;
	private String fileName;
	private String fileSize;
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
