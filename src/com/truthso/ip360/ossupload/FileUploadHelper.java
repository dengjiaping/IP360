package com.truthso.ip360.ossupload;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.truthso.ip360.activity.MainActivity;
import com.truthso.ip360.activity.PhotoPreserved;
import com.truthso.ip360.activity.R;
import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.bean.AccountStatusBean;
import com.truthso.ip360.bean.ExpenseBean;
import com.truthso.ip360.bean.UpLoadBean;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.WaituploadDao;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.ProgressUtil;
import com.truthso.ip360.utils.SecurityUtil;
import com.truthso.ip360.utils.SharePreferenceUtil;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2017/5/5.
 */

public class FileUploadHelper {
    private Activity activity;
    private FileInfo info;
    private boolean filePreIsok;
    private Dialog alertDialog;
    private ProgressUtil progressUtil;
    private boolean isUploadAgain;


    public FileUploadHelper(Activity activity) {
        this.activity = activity;
        progressUtil=new ProgressUtil(activity);
    }
    public void uploadFileInfo(FileInfo info){
        if(CheckUtil.isEmpty(activity)||CheckUtil.isEmpty(info)){
            return ;
        }
        this.info=info;
        isUploadAgain=false;
        if(CheckUtil.isEmpty(info.getHashCode())){
           getHashCode(info.getFilePath());
        }else{
            uploadInfo();
        }
    }

    //上传文件  获取交易金额--是否支付--支付成功--上传文件
    public void uploadFile(){
        getport();
    }

    //重新上传 需要加签
    public void uploadFileAgain(FileInfo info){
        this.info=info;
        isUploadAgain=true;
        uploadInfo();
    }

    public boolean isFileInfoUpload(){
        return filePreIsok;
    }

    //获取文件hashcode
    private void getHashCode(final String filePath) {
        progressUtil.showProgress("正在加载...");
        new Thread() {
            @Override
            public void run() {
                super.run();
              String hashCode = SecurityUtil.SHA512(filePath);
                if (hashCode != null) {
                    progressUtil.hideProgress();
                    info.setHashCode(hashCode);
                    handler.sendEmptyMessage(0);
                }
            }
        }.start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            uploadInfo();
        }
    };

    private void uploadInfo(){
        progressUtil.showProgress("努力加载中...");
        String imei = MyApplication.getInstance().getDeviceImei();
        //	 * @param fileType文件类型 文件类型 （拍照（50001）、录像（50003）、录音（50002） 非空 fileSize 文件大小，单位为BhashCode哈希值 非空
        // fileDate 取证时间 fileUrl 上传oss的文件路径 fileLocation 取证地点 可空 fileTime 取证时长 录像 录音不为空 imei手机的IMEI码
        ApiManager.getInstance().uploadPreserveFile(info.getFileName(), info.getType(),
                info.getFileSize(), info.getHashCode(), info.getFileCreatetime(), info.getFileLoc(), info.getFileTime(), imei, info.getLatitudeLongitude(), info.getEncrypte(), info.getRsaId(),
                new ApiCallback() {
                    @Override
                    public void onApiResultFailure(int statusCode,
                                                   Header[] headers, byte[] responseBody,
                                                   Throwable error) {
                        progressUtil. hideProgress();
                        //网络超时请重试
                        showDialogNoNet("网络链接超时，是否重试？");

                    }

                    @Override
                    public void onApiResult(int errorCode, String message,
                                            BaseHttpResponse response) {
                        progressUtil.hideProgress();
                        UpLoadBean bean = (UpLoadBean) response;
                        if (!CheckUtil.isEmpty(bean)) {
                            if (bean.getCode() == 200) {
                                filePreIsok = true;
                                UpLoadBean.Upload datas = bean.getDatas();
                                info.setResourceId(datas.getPkValue());
                                info.setObjectKey(datas.getFileUrl());//文件上传的objectKey
                              //  info.setFileCreatetime(datas.getFileDate());//从服务器获取的保全时间
                                if(isUploadAgain){
                                    getport();
                                }
                            } else {
                                Toaster.showToast(activity,
                                        bean.getMsg());
                            }
                        } else {
                            Toaster.showToast(activity, "请求失败");
                        }
                    }
                });
    }

    // 调接口获取当次交易的金额
    private void getport() {
        progressUtil. showProgress("正在加载...");
        ApiManager.getInstance().getAccountStatus(info.getType(),info.getMinTime(),
                new ApiCallback() {

                    @Override
                    public void onApiResultFailure(int statusCode,
                                                   Header[] headers, byte[] responseBody,
                                                   Throwable error) {
                        progressUtil.hideProgress();
                    }

                    @Override
                    public void onApiResult(int errorCode, String message,
                                            BaseHttpResponse response) {
                        progressUtil.hideProgress();
                        AccountStatusBean bean = (AccountStatusBean) response;
                        if (!CheckUtil.isEmpty(bean)) {
                            if (bean.getCode() == 200) {
                                showDialog(bean.getDatas().getShowText());
                            } else {
                                Toaster.showToast(activity,
                                        bean.getMsg());
                            }
                        } else {
                            Toaster.showToast(activity, "请重试");
                        }
                    }
                });
    }
    /**
     * 网络加载失败，稍后重试
     *
     * @param msg
     */
    private void showDialogNoNet(String msg) {
        alertDialog = new AlertDialog.Builder(activity).setTitle("温馨提示")
                .setMessage(msg).setIcon(R.drawable.ww)
                .setCancelable(false)
                .setPositiveButton("重试", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        uploadFileInfo(info);//上传文件信息

                    }
                }).setNegativeButton("稍后保全", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //上传列表等待上传
                        saveToDb();
                        Toaster.showToast(activity, "已进入上传列表等待上传");
                        Intent intent = new Intent(activity, MainActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                    }
                }).create();
        alertDialog.show();
    }


    // 保存照片信息加签后到数据库
    private void saveToDb() {
       /* FileInfo info = new FileInfo();
        info.setFilePath(path);
        info.setFileName(title);
        info.setType(MyConstants.PHOTOTYPE);
        info.setFileSize(size);
        info.setHashCode(hashCode);
        info.setFileCreatetime(date);
        info.setFileLoc(loc);
        info.setPriKey((String) SharePreferenceUtil.getAttributeByKey(this, MyConstants.RSAINFO, MyConstants.PRIKEY, SharePreferenceUtil.VALUE_IS_STRING));
        info.setRsaId((int) SharePreferenceUtil.getAttributeByKey(this, MyConstants.RSAINFO, MyConstants.RSAID, SharePreferenceUtil.VALUE_IS_INT));
        info.setLatitudeLongitude(longlat);*/
        String priKey = info.getPriKey();
        try {
            String encrypte = SecurityUtil.RSAFileInfo(info.getFileName() + info.getFileCreatetime() + info.getFileLoc() + info.getHashCode(), priKey);
            info.setEncrypte(encrypte);
            WaituploadDao.getDao().save(info);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("djj",e.getMessage());
        }
    }

    /**
     * 弹出框
     *
     * @param msg
     */
    private void showDialog(String msg) {
        if(activity.isFinishing()){
            return;
        }
        alertDialog = new AlertDialog.Builder(activity).setTitle("温馨提示")
                .setMessage(msg).setIcon(R.drawable.ww)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //调扣费的接口
                        GetPayment(info.getResourceId(), info.getType(), 1);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelUploadFile();
                    }
                }).create();
        alertDialog.show();
    }

    /**
     * 扣费
     *
     * @param type  （拍照（50001）、录像（50003）、录音（50002）
     * @param count 当次消费业务量
     */
    private void GetPayment(int pkValue, int type, int count) {
        progressUtil.showProgress("正在加载...");
        ApiManager.getInstance().Payment(pkValue, type, count, new ApiCallback() {
            @Override
            public void onApiResult(int errorCode, String message, BaseHttpResponse response) {
                ExpenseBean bean = (ExpenseBean) response;
                if (!CheckUtil.isEmpty(bean)) {
                    if (bean.getCode() == 200) {
                        progressUtil. hideProgress();
                        Toaster.showToast(activity, "文件正在上传，请在传输列表查看");
                        //上传文件
                        boolean resuambleUpload = UpLoadManager.getInstance().resuambleUpload(info);
                        if(!resuambleUpload){
                            return;
                        }
                        if(!isUploadAgain){
                            activity.finish();
                        }else {
                            WaituploadDao.getDao().delete(info.getId());
                        }
                    } else {
                        Toaster.showToast(activity, bean.getMsg());
                    }
                } else {
                    Toaster.showToast(activity, "保全失败，请点保全按钮重试！");
                }
            }
            @Override
            public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    /**
     * 取消上传文件
     */
    public void cancelUploadFile() {
        ApiManager.getInstance().DeleteFileInfo(info.getResourceId(), new ApiCallback() {
            @Override
            public void onApiResult(int errorCode, String message, BaseHttpResponse response) {
                if (response.getCode() == 200) {
                    progressUtil.hideProgress();
                }
            }

            @Override
            public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

}
