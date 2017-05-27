package com.truthso.ip360.bean;

import com.truthso.ip360.net.BaseHttpResponse;
/**
 * @despriction :申请公证人的信息
 *
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2017/5/27 16:47
 * @version 1.3
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class NotarAccountBean extends BaseHttpResponse {
//    iscertified	是否已实名认	Integer	0-未实名 1-已实名	非空
//    requestName	申请人姓名	String(20)	申请人姓名	可空	未实名认证时可空
//    requestCardId	申请人身份证号	String(20)	申请人身份证号	可空	未实名认证时可空
//    requestPhoneNum	申请人电话号码	String(20)	申请人电话号码	可空	账号为邮箱，且未绑定电话时
//    requestEmail	申请人邮箱	String(20)	申请人邮箱	可空	账号为手机号，且未绑定邮箱时
    public Notar datas;

    public Notar getDatas() {
        return datas;
    }

    public void setDatas(Notar datas) {
        this.datas = datas;
    }

    public class Notar{
      private int iscertified;
      private String requestName;
      private String requestCardId;
      private String requestPhoneNum;
      private String requestEmail;

        public String getRequestName() {
            return requestName;
        }

        public void setRequestName(String requestName) {
            this.requestName = requestName;
        }

        public String getRequestCardId() {
            return requestCardId;
        }

        public void setRequestCardId(String requestCardId) {
            this.requestCardId = requestCardId;
        }

        public String getRequestPhoneNum() {
            return requestPhoneNum;
        }

        public void setRequestPhoneNum(String requestPhoneNum) {
            this.requestPhoneNum = requestPhoneNum;
        }

        public String getRequestEmail() {
            return requestEmail;
        }

        public void setRequestEmail(String requestEmail) {
            this.requestEmail = requestEmail;
        }

        public int getIscertified() {
            return iscertified;
        }

        public void setIscertified(int iscertified) {
            this.iscertified = iscertified;
        }
    }
}
