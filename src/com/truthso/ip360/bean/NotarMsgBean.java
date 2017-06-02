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
//    "notarName": "申请公证时，公正包的名称",
//            "notarOfficeName": "公证处名称",
//            "notarDate": "申请公正的时间",
//            "notaryNum": "公证编号",
//            "requestName": "申请人姓名",
//            "receiverName": "领取者姓名"
// "notaryOfficeAddress": "公证处地址",
//            "notarStatus": 1,
//            "noReason": "公证名称已经存在",
//            "fileSize": "3.3M",
//            "fileMount": "申请公证的文件个数",
//            "monery": "待付费用",
//            "receiverDate": "证书领取日期",
//            "pkValue": "此条公证服务id"
//    notaryPageNum:公证书个数
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
        private String notaryOfficeAddress;
        private String notarDate;
        private String notaryNum;
        private String requestName;
        private String receiverName;
        private String notaryPageNum;
        private int notarStatus;//0审核拒绝1等待提交2等待审核3等待付费4等待制证5已公证
       private String noReason;//审核没有通过的原因
        private String fileSize;
        private String fileMount;
        private String monery;
        private String receiverDate;
        private String pkValue;//此条公证服务的id
//        private List<FileBean> fileList;


        public String getNotaryPageNum() {
            return notaryPageNum;
        }

        public void setNotaryPageNum(String notaryPageNum) {
            this.notaryPageNum = notaryPageNum;
        }

        public String getNotaryOfficeAddress() {
            return notaryOfficeAddress;
        }

        public void setNotaryOfficeAddress(String notaryOfficeAddress) {
            this.notaryOfficeAddress = notaryOfficeAddress;
        }

        public String getNoReason() {
            return noReason;
        }

        public void setNoReason(String noReason) {
            this.noReason = noReason;
        }

        public String getPkValue() {
            return pkValue;
        }

        public void setPkValue(String pkValue) {
            this.pkValue = pkValue;
        }

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


    }
}
