package com.truthso.ip360.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by Administrator on 2017/5/2.
 */

public class TimeUtile {
    private static int time;
    private static Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            time+=1;
            handler.sendEmptyMessageDelayed(0,1000);
        }
    };

    public static void startTime(){
        time=0;
        handler.sendEmptyMessageDelayed(0,1000);
    }
    public static int getCurrentTime(){
        handler.removeMessages(0);
        return time;
    }
    public static void cancelTime(){
        handler.removeMessages(0);
    }
}
