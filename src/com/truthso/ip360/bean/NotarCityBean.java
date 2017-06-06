package com.truthso.ip360.bean;

import com.truthso.ip360.net.BaseHttpResponse;

import java.util.List;

/**
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @version 1.3
 * @despriction :获取公证城市及名称
 * @date 创建时间：2017/6/1 11:42
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class NotarCityBean extends BaseHttpResponse {
    private NotarCity datas;

    public NotarCity getDatas() {
        return datas;
    }

    public void setDatas(NotarCity datas) {
        this.datas = datas;
    }
}
