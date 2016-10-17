package com.truthso.ip360.bean;

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
	/*fileSize	文件大小	String(10)	文件大小	非空	28K
	fileTitle	文件名称	String(30)	文件名称	非空	W20160913.jpg
	fileDate	时间	文件取证的时间。格式：
	yyyy-MM-dd HH:mm:ss	非空	2016-09-13 10:44:06	文件取证的时间。格式：
	yyyy-MM-dd HH:mm:ss
	fileTime	时长	String(10)	文件时长，拍照类型的没有	可空	23:07:00
	fileLocation	地点	String(20)	取证的地点	可空	
	fileMode	取证方式	String(15)	是手机端取证还是pc端取证	非空	手机取证
	imei	IMEI码	String(20)	取证手机的IMEI码	非空	
	fileFormat	证据格式	String(6)	取证文件的格式	非空	JPG
	cardTitle	证书标题	String(30)	证书标题	非空	IP360手机取证数据保全证书
	fileUrl	文件路径	String(255)	云URL。用于在线播放场景	非空	
	pkValue	证据记录主键值	Integer	证据记录唯一标识（主键）。修改备注的时候用	非空	*/
	private String fileSize;//文件大小
	private String fileTitle;//文件名称
	private String fileDate;//时间 格式 HH:mm:ss
	private String fileTime;//时长
	private String fileLocation;//取证地点
	private String fileMode;//取证方式
	private String imei;//IMEI码
	private String fileFormat;//证据格式
	private String cardTitle;//证书标题
	private String fileUrl;//文件路径
	private int pkValue;//证据记录主键值
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public String getFileTitle() {
		return fileTitle;
	}
	public void setFileTitle(String fileTitle) {
		this.fileTitle = fileTitle;
	}
	public String getFileDate() {
		return fileDate;
	}
	public void setFileDate(String fileDate) {
		this.fileDate = fileDate;
	}
	public String getFileTime() {
		return fileTime;
	}
	public void setFileTime(String fileTime) {
		this.fileTime = fileTime;
	}
	public String getFileLocation() {
		return fileLocation;
	}
	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}
	public String getFileMode() {
		return fileMode;
	}
	public void setFileMode(String fileMode) {
		this.fileMode = fileMode;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getFileFormat() {
		return fileFormat;
	}
	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}
	public String getCardTitle() {
		return cardTitle;
	}
	public void setCardTitle(String cardTitle) {
		this.cardTitle = cardTitle;
	}
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