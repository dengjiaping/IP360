package com.truthso.ip360.ossupload;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.RequestHandle;
import com.truthso.ip360.activity.MainActivity;
import com.truthso.ip360.activity.R;
import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.bean.AccountStatusBean;
import com.truthso.ip360.bean.ExpenseBean;
import com.truthso.ip360.bean.UpLoadBean;
import com.truthso.ip360.dao.WaituploadDao;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.FileUtil;
import com.truthso.ip360.utils.NetStatusUtil;
import com.truthso.ip360.utils.SecurityUtil;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2017/5/5.
 */

public class FileUploadHelper {
    private Activity activity;
    private FileInfo info;
    private boolean filePreIsok;
    private Dialog alertDialog;

    private  RequestHandle requestHandle;
    private int upload_type;//上传操作的类型
    private int UPLOAD_FILEINFO=0;//上传文件信息
    private int UPLOAD_FILE=1;//上传文件
    private int UPLOAD_FILEINFO_AGAIN=2;//重新上传文件信息
    private int UPLOAD_FILE_AGAIN=3;//重新上传文件


    public FileUploadHelper(Activity activity) {
        this.activity = activity;
    }
    //上传文件信息
    public void uploadFileInfo(FileInfo info){
        this.info=info;
        upload_type=UPLOAD_FILEINFO;
        if(CheckUtil.isEmpty(activity)||CheckUtil.isEmpty(info)){
            return ;
        }
        if(CheckUtil.isEmpty(info.getHashCode())){
           getHashCode(info.getFilePath());
        }else{
            uploadInfo();
        }
    }

    //上传文件  获取交易金额--是否支付--支付成功--上传文件
    public void uploadFile(){
        upload_type=UPLOAD_FILE;
        getport();
    }

    //上传文件  获取交易金额--是否支付--支付成功--上传文件
    public void uploadFileAgain(FileInfo info){
         upload_type=UPLOAD_FILE_AGAIN;
         this.info=info;
         getport();
    }

    //重新上传文件信息
    public void uploadFileInfoAgain(FileInfo info){
        upload_type=UPLOAD_FILEINFO_AGAIN;

        this.info=info;
        if(CheckUtil.isEmpty(info.getHashCode())){
            getHashCode(info.getFilePath());
        }else {
            uploadInfo();
        }
    }


    public boolean isFileInfoUpload(){
        return filePreIsok;
    }

    //获取文件hashcode
    private void getHashCode(final String filePath) {
       showProgress("正在加载...");
        new Thread() {
            @Override
            public void run() {
                super.run();
              String hashCode = SecurityUtil.SHA512(filePath);
                if (hashCode != null) {
                    hideProgress();
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

    //上传文件信息
    private void uploadInfo(){
        //判断网络
        if(!NetStatusUtil.isNetValid(activity)){

            showDialogNoNet("网络超时，是否重试？");
            return;
        }
        showProgress("努力加载中...");
        String imei = MyApplication.getInstance().getDeviceImei();
        //	 * @param fileType文件类型 文件类型 （拍照（50001）、录像（50003）、录音（50002） 非空 fileSize 文件大小，单位为BhashCode哈希值 非空
        // fileDate 取证时间 fileUrl 上传oss的文件路径 fileLocation 取证地点 可空 fileTime 取证时长 录像 录音不为空 imei手机的IMEI码

         requestHandle = ApiManager.getInstance().uploadPreserveFile(info.getFileName(), info.getType(),
                info.getFileSize(), info.getHashCode(), info.getFileCreatetime(), info.getFileLoc(), info.getFileTime(), imei, info.getLatitudeLongitude(), info.getEncrypte(), info.getRsaId(),
                new ApiCallback() {
                    @Override
                    public void onApiResultFailure(int statusCode,
                                                   Header[] headers, byte[] responseBody,
                                                   Throwable error) {
                        hideProgress();
                        //网络超时请重试
                        showDialogNoNet("网络超时，是否重试？");

                    }

                    @Override
                    public void onApiResult(int errorCode, String message,
                                            BaseHttpResponse response) {
                        hideProgress();
                        UpLoadBean bean = (UpLoadBean) response;
                        if (!CheckUtil.isEmpty(bean)) {
                            if (bean.getCode() == 200) {
                                filePreIsok = true;
                                UpLoadBean.Upload datas = bean.getDatas();
                                info.setResourceId(datas.getPkValue());
                                info.setObjectKey(datas.getFileUrl());//文件上传的objectKey
                                //如果是重新上传 直接调上传文件的接口
                                if(upload_type==UPLOAD_FILEINFO_AGAIN){
                                    uploadFileAgain(info);
                                }
                            } else if (bean.getCode() == 502) {
                                showDialogFileTamper("当前文件被篡改，不可保全");
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

    //文件被篡改
    private void showDialogFileTamper(String msg) {
        alertDialog = new AlertDialog.Builder(activity).setTitle("温馨提示")
                .setMessage(msg).setIcon(R.drawable.ww)
                .setCancelable(false)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    /*    if(upload_type==UPLOAD_FILEINFO_AGAIN){
                            uploadFileInfoAgain(info);
                        }else{
                            uploadFileInfo(info);//上传文件信息
                        }*/
                    }
                }).setPositiveButton("确定删除", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       //删除文件
                        try {
                            FileUtil.deletefile(info.getFilePath());
                            WaituploadDao.getDao().delete(info.getId());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).create();
        alertDialog.show();
    }

    // 调接口获取当次交易的金额
    private void getport() {
        //判断网络
        if(!NetStatusUtil.isNetValid(activity)){
            showDialogNoNet("网络超时，是否重试？");
            return;
        }
        showProgress("正在加载...");
        requestHandle=ApiManager.getInstance().getAccountStatus(info.getType(),info.getMinTime(),
                new ApiCallback() {

                    @Override
                    public void onApiResultFailure(int statusCode,
                                                   Header[] headers, byte[] responseBody,
                                                   Throwable error) {
                        hideProgress();
                        showDialogNoNet("网络超时，是否重试？");
                    }

                    @Override
                    public void onApiResult(int errorCode, String message,
                                            BaseHttpResponse response) {
                        hideProgress();
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
                        //上传文件信息失败 重试
                        if(upload_type==UPLOAD_FILEINFO){
                            uploadFileInfo(info);
                        }else if (upload_type==UPLOAD_FILE){//上传文件失败重试
                            uploadFile();
                        }else if(upload_type==UPLOAD_FILEINFO_AGAIN){//重新上传文件信息失败重试
                            uploadFileInfoAgain(info);
                        }else{
                            uploadFileAgain(info);
                        }
                    }
                }).setNegativeButton("稍后保全", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //上传文件信息失败，保存数据库等待重新上传
                        if(upload_type==UPLOAD_FILEINFO||upload_type==UPLOAD_FILE){
                            saveToDb();
                            Toaster.showToast(activity, "已进入上传列表等待上传");
                            Intent intent = new Intent(activity, MainActivity.class);
                            activity.startActivity(intent);
                            activity.finish();
                        }
                    }
                }).create();
        alertDialog.show();
    }


    // 保存照片信息加签后到数据库
    private void saveToDb() {
        if(upload_type==UPLOAD_FILE){
            //无需加签 保存数据库
            Log.i("djj","UPLOAD_FILE");
                WaituploadDao.getDao().save(info);
        }else {
            //文件加签 保存数据库
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
                        //判断网络
                        if(!NetStatusUtil.isNetValid(activity)){
                            showDialogNoNet("网络超时，是否重试？");
                            return;
                        }
                        //调扣费的接口
                        GetPayment(info.getResourceId(), info.getType(), 1);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        cancelUploadFile();
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
        showProgress("正在加载...");
        requestHandle =ApiManager.getInstance().Payment(pkValue, type, count, new ApiCallback() {
            @Override
            public void onApiResult(int errorCode, String message, BaseHttpResponse response) {
                ExpenseBean bean = (ExpenseBean) response;
                if (!CheckUtil.isEmpty(bean)) {
                    if (bean.getCode() == 200) {
                        hideProgress();
                        Toaster.showToast(activity, "文件正在上传，请在传输列表查看");
                        //上传文件
                        boolean resuambleUpload = UpLoadManager.getInstance().resuambleUpload(info);
                        if(!resuambleUpload){
                            return;
                        }
                        if(upload_type==UPLOAD_FILE){
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
                showDialogNoNet("网络超时，是否重试？");
            }
        });
    }

    /**
     * 取消上传文件
     */
    public void cancelUploadFile() {
        WaituploadDao.getDao().delete(info.getId());
        requestHandle=ApiManager.getInstance().DeleteFileInfo(info.getResourceId(), new ApiCallback() {
            @Override
            public void onApiResult(int errorCode, String message, BaseHttpResponse response) {
                if (response.getCode() == 200) {

                }
            }

            @Override
            public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }


    private Dialog pDialog;
    public void showProgress(String msg) {
        if (!activity.isFinishing()) {
            if (pDialog == null) {
                pDialog = createLoadingDialog(activity,msg); // 创建ProgressDialog对象
            }
            pDialog.setCancelable(false); // 设置ProgressDialog 是否可以按退回按键取消
            if (!pDialog.isShowing()) {
                pDialog.show();// 让ProgressDialog显示
            }
            pDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if(pDialog.isShowing()){
                            hideProgress();
                        }
                    }
                    return false;
                }
            });
        }

    }

    public void hideProgress() {
        if (null != pDialog && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        if(null!=requestHandle){
            requestHandle.cancel(true);
        }

    }

    public Dialog createLoadingDialog(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字

        tipTextView.setText(msg);// 设置加载信息
        final Dialog loadingDialog = new Dialog(context,R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(true);// 不可以用“返回键”取消
        loadingDialog.setContentView(v);

        return loadingDialog;
    }

}
