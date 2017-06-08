package com.truthso.ip360.bean;

import com.truthso.ip360.net.BaseHttpResponse;

/**
 * Created by Administrator on 2017/6/8.
 */

public class GetLinkCountBean extends BaseHttpResponse {
    private GetLinkCount datas;

    public GetLinkCount getDatas() {
        return datas;
    }

    public void setDatas(GetLinkCount datas) {
        this.datas = datas;
    }
}
