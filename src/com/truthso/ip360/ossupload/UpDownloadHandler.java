package com.truthso.ip360.ossupload;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.dao.UpDownLoadDao;

/**
 * Created by Administrator on 2017/1/13.
 */

public class UpDownloadHandler extends Handler {

    private final int DOWNLOAD_CODE=101,SUCCESS=102;
    private static UpDownloadHandler mHandler;
    private UpDownloadHandler(Looper looper) {
        super(looper);
    }

    public static UpDownloadHandler getInstance(){
        if(mHandler==null){
            mHandler=new UpDownloadHandler(Looper.getMainLooper());
        }
        return mHandler;
    }

    @Override
    public void handleMessage(Message msg) {
         switch (msg.what){
             case DOWNLOAD_CODE:
                 if(msg.arg1==SUCCESS){
                     DbBean dbBean=(DbBean)msg.obj;
                     SqlDao.getSQLiteOpenHelper().save(dbBean, MyConstants.TABLE_MEDIA_DETAIL);
                     UpDownLoadDao.getDao().deleteDownInfoByResourceId(dbBean.getPkValue());
                 }
                 break;
         }
    }
}