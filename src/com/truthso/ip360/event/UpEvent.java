package com.truthso.ip360.event;

/**
 * Created by Administrator on 2017/1/10.
 */

public class UpEvent {
    private boolean flag;
    public UpEvent(boolean flag){
        this.flag=flag;
    }
    public boolean getFlag(){
        return flag;
    }
}
