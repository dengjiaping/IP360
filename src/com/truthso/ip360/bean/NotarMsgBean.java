package com.truthso.ip360.bean;

import com.truthso.ip360.net.BaseHttpResponse;

import java.util.List;

/**
 * @despriction :我的公证
 *
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2017/5/27 17:33
 * @version 1.3
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class NotarMsgBean extends BaseHttpResponse{
//    notarName	公正包名称	String(20)	申请公证时，公正包的名称	非空
//    notarOfficeName	公证处名称	String(15)	公证处名称	非空	北京-东方公证处
//    notarDate	申请公正的时间	String(20)	申请公正的时间	非空
//    notaryNum	公证编号	String(20)	公证编号	非空
//    requestName	申请人姓名	String(20)	申请人姓名	非空
//    receiverName
//    领取者姓名	String(20)	领取者姓名	非空
//    notarStatus	公证状态	Integer	1等待审核 2等待付费3已公证	非空
//    fileSize	文件大小	String（10）	总的文件大小	非空	36.02M
//    fileMount	文件个数	String(10)	申请公证的文件个数	非空	7
//    monery	待付费用	String（10）	状态为2等待付费时候的费用	可空
//    receiverDate	证书领取日期	String(10)	状态为3已公证，且公正处给了领取时间时返回	可空
//    fileList	申请公证的所有文件的集合	List<file>	申请出证的文件的结合	非空
    private NotarMsg datas;

    public NotarMsg getDatas() {
        return datas;
    }

    public void setDatas(NotarMsg datas) {
        this.datas = datas;
    }

    public class NotarMsg{
        private String notarName;
        private String notarOfficeName;
        private String notarDate;
        private String notaryNum;
        private String requestName;
        private String receiverName;
        private int notarStatus;
        private String fileSize;
        private String fileMount;
        private String monery;
        private String receiverDate;
        private List<FileBean> fileList;

        public String getNotarName() {
            return notarName;
        }

        public void setNotarName(String notarName) {
            this.notarName = notarName;
        }

        public String getNotarOfficeName() {
            return notarOfficeName;
        }

        public void setNotarOfficeName(String notarOfficeName) {
            this.notarOfficeName = notarOfficeName;
        }

        public String getNotarDate() {
            return notarDate;
        }

        public void setNotarDate(String notarDate) {
            this.notarDate = notarDate;
        }

        public String getNotaryNum() {
            return notaryNum;
        }

        public void setNotaryNum(String notaryNum) {
            this.notaryNum = notaryNum;
        }

        public String getRequestName() {
            return requestName;
        }

        public void setRequestName(String requestName) {
            this.requestName = requestName;
        }

        public String getReceiverName() {
            return receiverName;
        }

        public void setReceiverName(String receiverName) {
            this.receiverName = receiverName;
        }

        public int getNotarStatus() {
            return notarStatus;
        }

        public void setNotarStatus(int notarStatus) {
            this.notarStatus = notarStatus;
        }

        public String getFileSize() {
            return fileSize;
        }

        public void setFileSize(String fileSize) {
            this.fileSize = fileSize;
        }

        public String getFileMount() {
            return fileMount;
        }

        public void setFileMount(String fileMount) {
            this.fileMount = fileMount;
        }

        public String getMonery() {
            return monery;
        }

        public void setMonery(String monery) {
            this.monery = monery;
        }

        public String getReceiverDate() {
            return receiverDate;
        }

        public void setReceiverDate(String receiverDate) {
            this.receiverDate = receiverDate;
        }

        public List<FileBean> getFileList() {
            return fileList;
        }

        public void setFileList(List<FileBean> fileList) {
            this.fileList = fileList;
        }
    }
}
