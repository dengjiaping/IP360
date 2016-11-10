package com.truthso.ip360.bean;

/**
 * 数据库公用的bean
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016年6月7日下午3:08:53
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class DbBean {
	// id integer PRIMARY KEY autoincrement,title text,createTime text,fileSize
	// text,jsonObject text,type integer,lable text
	private int id;
	private String title;// 标题
	private String createTime;// 创建时间
	private String fileSize;// 文件大小
	private String jsonObject;// 详细信息
	private int type;// 文件类型 0照片，1视频，2录音 3云端拍照 4云端视频 5云端录音
	private String lable;// 文件标签
	private String resourceUrl;// 资源路径
	private String recordTime;// 录音专用录音时长
	private String videoTime;//录像时长
    private String remark;//备注
    private String location;
    private String status;
    private String llsize;
    
	public String getLlsize() {
		return llsize;
	}

	public void setLlsize(String llsize) {
		this.llsize = llsize;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVideoTime() {
		return videoTime;
	}

	public void setVideoTime(String videoTime) {
		this.videoTime = videoTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(String jsonObject) {
		this.jsonObject = jsonObject;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	@Override
	public String toString() {
		return "DbBean [id=" + id + ", title=" + title + ", createTime="
				+ createTime + ", fileSize=" + fileSize + ", jsonObject="
				+ jsonObject + ", type=" + type + ", lable=" + lable
				+ ", resourceUrl=" + resourceUrl + ", recordTime=" + recordTime
				+ ", videoTime=" + videoTime + ", remark=" + remark
				+ ", location=" + location + ", status=" + status + ", llsize="
				+ llsize + "]";
	}

}
