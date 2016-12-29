package com.truthso.ip360.bean;

import java.util.List;

/**
 * Created by summer on 2016/12/28.
 */

public class ShowPicture {
    private List<PictureList> pictureList ;

    public void setPictureList(List<PictureList> pictureList){
        this.pictureList = pictureList;
    }
    public List<PictureList> getPictureList(){
        return this.pictureList;
    }

}
