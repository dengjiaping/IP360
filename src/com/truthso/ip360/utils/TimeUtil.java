package com.truthso.ip360.utils;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Administrator on 2017/5/2.
 */

public class TimeUtil {
    private static int time;
    private static Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            time+=1;
        }
    };

    public static void startTime(){
        handler.removeMessages(0);
        time=0;
        handler.sendEmptyMessageDelayed(0,1000);
    }
    public static int getCurrentTime(){
        handler.removeMessages(0);
        return time;
    }

}
