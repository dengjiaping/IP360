package com.truthso.ip360.bean;

import com.truthso.ip360.net.BaseHttpResponse;

/**
 * @despriction :申请公证支付费用，费用不足时的弹框
 *
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2017/5/27 17:21
 * @version  1.3
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class DefraymentBean extends BaseHttpResponse{
    private Defrayment datas;


    public Defrayment getDatas() {
        return datas;
    }

    public void setDatas(Defrayment datas) {
        this.datas = datas;
    }

    public class Defrayment{
        private String status;//status:0-正常扣费 1-欠费 2-余额不足（0,1正常，2弹框提示去充值）
        private String showText;
        private String balance;//余额
        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getShowText() {
            return showText;
        }

        public void setShowText(String showText) {
            this.showText = showText;
        }
    }}
