package com.truthso.ip360.updownload;

public class FileInfo {

	private String filePath;//下载成功的文件路径
	private String fileName;
	private String fileUrlFormatName;//
	private String fileSize;
	private String fileLoc;
	private String llsize;
	private int type;//文件类型 
	private int mobiletype;//现场取证的文件类型
	private String fileCreatetime;
	private String fileTime;//文件时长
	private int pkValue;
    private String fileFormat;
	private int status;//成功0失败1等待2
	private int dataType;//文件类型1-url确权  2-其他确权  3-现场取证 4-pc取证
	private String reMark;
	private String upLoadFilePath;//上传的文件路径

	public String getUpLoadFilePath() {
		return upLoadFilePath;
	}

	public void setUpLoadFilePath(String upLoadFilePath) {
		this.upLoadFilePath = upLoadFilePath;
	}

	public String getReMark() {
		return reMark;
	}

	public void setReMark(String reMark) {
		this.reMark = reMark;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public String getFileUrlFormatName() {
		return fileUrlFormatName;
	}

	public void setFileUrlFormatName(String fileUrlFormatName) {
		this.fileUrlFormatName = fileUrlFormatName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}

	public int getPkValue() {
		return pkValue;
	}

	public void setPkValue(int pkValue) {
		this.pkValue = pkValue;
	}

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
		return "FileInfo{" +
				"filePath='" + filePath + '\'' +
				", fileName='" + fileName + '\'' +
				", fileUrlFormatName='" + fileUrlFormatName + '\'' +
				", fileSize='" + fileSize + '\'' +
				", fileLoc='" + fileLoc + '\'' +
				", llsize='" + llsize + '\'' +
				", type=" + type +
				", mobiletype=" + mobiletype +
				", fileCreatetime='" + fileCreatetime + '\'' +
				", fileTime='" + fileTime + '\'' +
				", pkValue=" + pkValue +
				", fileFormat='" + fileFormat + '\'' +
				", status=" + status +
				", dataType=" + dataType +
				", reMark='" + reMark + '\'' +
				", position=" + position +
				", resourceId=" + resourceId +
				", objectKey='" + objectKey + '\'' +
				'}';
	}
}
