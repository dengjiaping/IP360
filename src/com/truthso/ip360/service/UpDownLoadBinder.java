package com.truthso.ip360.service;

import android.os.Binder;
import android.util.Log;

/**
 * Created by Administrator on 2017/2/12.
 */

public class UpDownLoadBinder extends Binder {
    public void startDownload(){
         new Thread(){
             @Override
             public void run() {
                 for (int i=0;i<100;i++){
                     Log.i("djj","haha"+i);
                     try {
                         Thread.sleep(1000);
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
                 }
             }
         }.start();
    }

    public void startUpload(){

    }
}
