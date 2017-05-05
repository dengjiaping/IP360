package com.truthso.ip360.updownload;

public class FileInfo {

	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private String filePath;//下载成功的文件路径
	private String fileName;
	private String fileUrlFormatName;//
	private String fileSize;
	private String fileLoc;
	private String llsize;//byte单位的文件大小
	private int type;//文件类型 
	private int mobiletype;//现场取证的文件类型
	private String fileCreatetime;
	private String fileTime;//文件时长
    private String fileFormat;
	private int status;//成功0失败1等待2
	private int dataType;//文件类型1-url确权  2-其他确权  3-现场取证 4-pc取证
	private String reMark;
	private String upLoadFilePath;//上传的文件路径
	private String completeDate;
	private int position;
	private int resourceId;
	private String latitudeLongitude;//经纬度
	private String priKey;//私钥
	private int rsaId;//私钥ID
	private String hashCode;//hashcode
	private String encrypte;//文件加签后的字符串
	private int minTime;//视频录音的计费时长，照片为1

	public int getMinTime() {
		return minTime;
	}

	public void setMinTime(int minTime) {
		this.minTime = minTime;
	}

	public String getLlsize() {
		return llsize;
	}

	public void setLlsize(String llsize) {
		this.llsize = llsize;
	}

	public String getEncrypte() {
		return encrypte;
	}

	public void setEncrypte(String encrypte) {
		this.encrypte = encrypte;
	}

	public String getLatitudeLongitude() {
		return latitudeLongitude;
	}

	public void setLatitudeLongitude(String latitudeLongitude) {
		this.latitudeLongitude = latitudeLongitude;
	}

	public String getPriKey() {
		return priKey;
	}

	public void setPriKey(String priKey) {
		this.priKey = priKey;
	}

	public int getRsaId() {
		return rsaId;
	}

	public void setRsaId(int rsaId) {
		this.rsaId = rsaId;
	}

	public String getHashCode() {
		return hashCode;
	}

	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}

	public String getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(String completeDate) {
		this.completeDate = completeDate;
	}

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

/*	public int getPkValue() {
		return pkValue;
	}

	public void setPkValue(int pkValue) {
		this.pkValue = pkValue;
	}*/

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
