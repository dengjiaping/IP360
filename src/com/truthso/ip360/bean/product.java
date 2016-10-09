package com.truthso.ip360.bean;
/**
 * @despriction :获取个人概要信息中的PersonalMsgBean中的泛型bean
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-10-9下午6:01:48
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class product {
	private int type;//业务类型
	/*RECORD_TYPE_RIGHT(20000,"确权"),
	RECORD_TYPE_SNAP_SHOT(30001,"自主取证快照"),
	RECORD_TYPE_SNAP_SCREEN(30002,"自主取证录屏"),
	RECORD_TYPE_MONITOR_RIGHT(40001,"确权监测"),
	RECORD_TYPE_MONITOR_YSTW(40002,"音视图文监测"),
	RECORD_TYPE_MONITOR_YQ(40003,"舆情监测"),
	RECORD_TYPE_MONITOR_ONLINE(40004,"电商监测"),
	RECORD_TYPE_MOBILE_PICTURE(50001,"现场取证拍照"),
	RECORD_TYPE_MOBILE_SOUND(50002,"现场取证录音"),
	RECORD_TYPE_MOBILE_VIDEO(50003,"现场取证录像")
	*/
	private String unit;//数量单位 业务量的单位。如次、分钟、周。
	private int freeCount;//赠送量 大于等于0
	private int usedCount;//累计使用量
	private int balanceCount;//剩余量freeCount-usedCount。负值时为透支。
	private int limitCount;//透支阀值 透支最大量。超过会停服务。    可空
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public int getFreeCount() {
		return freeCount;
	}
	public void setFreeCount(int freeCount) {
		this.freeCount = freeCount;
	}
	public int getUsedCount() {
		return usedCount;
	}
	public void setUsedCount(int usedCount) {
		this.usedCount = usedCount;
	}
	public int getBalanceCount() {
		return balanceCount;
	}
	public void setBalanceCount(int balanceCount) {
		this.balanceCount = balanceCount;
	}
	public int getLimitCount() {
		return limitCount;
	}
	public void setLimitCount(int limitCount) {
		this.limitCount = limitCount;
	}
	
}
