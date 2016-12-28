package com.truthso.ip360.bean;

import com.truthso.ip360.net.BaseHttpResponse;

import java.util.List;

/**
 * Created by summer on 2016/12/28.
 */

public class ShowPictureBean extends BaseHttpResponse{
    private List<ShowPicture> datas;

    public List<ShowPicture> getDatas() {
        return datas;
    }

    public void setDatas(List<ShowPicture> datas) {
        this.datas = datas;
    }
}
