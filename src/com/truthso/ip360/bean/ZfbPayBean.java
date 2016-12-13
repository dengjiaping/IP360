package com.truthso.ip360.bean;

import com.truthso.ip360.net.BaseHttpResponse;

/**
 * 支付宝的接口bean ，获取支付宝签名后的订单信息
 * Created by summer on 2016/12/13.
 */

public class ZfbPayBean extends BaseHttpResponse{
    private  Zhifubao datas;

    public Zhifubao getDatas() {
        return datas;
    }

    public void setDatas(Zhifubao datas) {
        this.datas = datas;
    }

    public class Zhifubao{
    private String text;//签名后的订单

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
