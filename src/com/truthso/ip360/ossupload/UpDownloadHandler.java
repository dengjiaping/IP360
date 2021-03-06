package com.truthso.ip360.ossupload;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.format.DateFormat;

import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.dao.UpDownLoadDao;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.utils.FileUtil;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Administrator on 2017/1/13.
 */

public class UpDownloadHandler extends Handler {

    private final int DOWNLOAD_CODE=101,UPLOAD_CODE=201,SUCCESS=102;
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
        String date = new DateFormat().format("yyyy-MM-dd HH:mm:ss", Calendar.getInstance(Locale.CHINA)).toString();
         switch (msg.what){
             case DOWNLOAD_CODE:
                 if(msg.arg1==SUCCESS){
                     DbBean dbBean=(DbBean)msg.obj;
                     SqlDao.getSQLiteOpenHelper().save(dbBean, MyConstants.TABLE_MEDIA_DETAIL);
                     UpDownLoadDao.getDao().updateStatusByResourceId("0",dbBean.getPkValue(),date);
                 }else{
                     String pkvalue=(String)msg.obj;
                     UpDownLoadDao.getDao().updateStatusByResourceId("3",pkvalue,date);
                 }
                 break;
             case UPLOAD_CODE:
                 FileInfo info=(FileInfo)msg.obj;
                 if(msg.arg1==SUCCESS){
                   //  SqlDao.getSQLiteOpenHelper().deleteByPkValue(MyConstants.TABLE_MEDIA_DETAIL,info.getPkValue());
                     UpDownLoadDao.getDao().updateUploadInfoByUrl(info.getFilePath(),0,date);
                     try {
                         FileUtil.deleteFile(info.getFilePath());
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }else{
                     UpDownLoadDao.getDao().updateUploadInfoByUrl(info.getFilePath(),3,date);
                 }
                 break;
         }
    }
}
