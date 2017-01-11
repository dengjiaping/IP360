package com.truthso.ip360.event;

/**
 * Created by Administrator on 2017/1/10.
 */

public class DownEvent {
    private boolean flag;
    public DownEvent(boolean flag){
        this.flag=flag;
    }
    public boolean getFlag(){
        return flag;
    }
}
