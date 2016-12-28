package com.truthso.ip360.bean;

import android.graphics.Picture;

import com.truthso.ip360.net.BaseHttpResponse;

import java.util.List;

/**
 * Created by summer on 2016/12/28.
 */

public class ShowPictureBean extends BaseHttpResponse{
    private ShowPicture datas;

    public ShowPicture getDatas() {
        return datas;
    }

    public void setDatas(ShowPicture datas) {
        this.datas = datas;
    }
}
