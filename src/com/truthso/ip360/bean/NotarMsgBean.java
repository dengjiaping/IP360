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
//            "notaryOfficeAddress": "公证处地址",
//            "receiver": "本人领取 或者 其他自然人领取",
//            "notarDate": "申请公正的时间",
//            "notaryNum": "公证编号",
//            "requestName": "申请人姓名",
//            "applicantCard": "申请人证件号",
//            "receiverName": "领取者姓名",
//            "receiverCard": "领取者证件号",
//            "notarStatus": 1,
//            "noReason": "公证名称已经存在",
//            "fileSize": "3.3M",
//            "fileMount": "申请公证的文件个数",
//            "monery": "待付费用",
//            "receiverDate": "证书领取日期",
//            "notaryPageNum": "2",
//            "pkValue": "此条公证服务id"

    private List<NotarMsg> datas;

    public List<NotarMsg> getDatas() {
        return datas;
    }

    public void setDatas(List<NotarMsg> datas) {
        this.datas = datas;
    }
}
