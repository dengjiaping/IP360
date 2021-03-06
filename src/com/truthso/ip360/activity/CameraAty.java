package com.truthso.ip360.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.linj.DisplayUtil;
import com.linj.camera.view.CameraContainer;
import com.linj.camera.view.CameraView;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.TimeUtile;

import java.util.List;


/**
 * @despriction :自定义拍照录像
 *
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2017/5/2 14:12
 * @version
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class CameraAty extends Activity implements View.OnClickListener, CameraContainer.TakePictureListener {
    public final static String TAG = "CameraAty";
    private boolean mIsRecordMode = false;
    private CameraContainer mContainer;
    private ImageButton mCameraShutterButton;
    private ImageButton mRecordShutterButton;
    private ImageView mFlashView;
    private ImageView mSwitchCameraView;
    private ImageView mSettingView;
    private ImageView mVideoIconView;
    private View mHeaderBar;
    private Dialog alertDialog;
    private boolean isRecording = false;
    private String flag;
    private Long serviceTime;
    private String mSaveRoot = MyConstants.CACHE_PATH;
    private float previewRate = -1f;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.camera);

        flag = getIntent().getStringExtra("flag");
        serviceTime = getIntent().getLongExtra("serviceTime",0);//服務器初始時間
        mHeaderBar = findViewById(R.id.camera_header_bar);
        mContainer = (CameraContainer) findViewById(R.id.container);
        mVideoIconView = (ImageView) findViewById(R.id.videoicon);
        mCameraShutterButton = (ImageButton) findViewById(R.id.btn_shutter_camera);
        mRecordShutterButton = (ImageButton) findViewById(R.id.btn_shutter_record);
        mSwitchCameraView = (ImageView) findViewById(R.id.btn_switch_camera);
        mFlashView = (ImageView) findViewById(R.id.btn_flash_mode);


        mCameraShutterButton.setOnClickListener(this);
        mRecordShutterButton.setOnClickListener(this);
        mFlashView.setOnClickListener(this);
        mContainer.setFlashMode(CameraView.FlashMode.OFF);
        previewRate = DisplayUtil.getScreenRate(this); //默认全屏的比例预览
        mFlashView.setImageResource(R.drawable.btn_flash_off);
        mSwitchCameraView.setOnClickListener(this);


        mContainer.setRootPath(mSaveRoot);
        if (flag.equals("video")) {
            mCameraShutterButton.setVisibility(View.GONE);
            mRecordShutterButton.setVisibility(View.VISIBLE);
            mHeaderBar.setVisibility(View.GONE);
            mIsRecordMode = true;
            mContainer.switchMode(5);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_shutter_camera:
                mCameraShutterButton.setClickable(false);
                mContainer.takePicture(this);
                break;
            case R.id.btn_flash_mode:
                if (mContainer.getFlashMode() == CameraView.FlashMode.ON) {
                    mContainer.setFlashMode(CameraView.FlashMode.OFF);
                    mFlashView.setImageResource(R.drawable.btn_flash_off);
                } else if (mContainer.getFlashMode() == CameraView.FlashMode.OFF) {
                    mContainer.setFlashMode(CameraView.FlashMode.AUTO);
                    mFlashView.setImageResource(R.drawable.btn_flash_auto);
                } else if (mContainer.getFlashMode() == CameraView.FlashMode.AUTO) {
                    mContainer.setFlashMode(CameraView.FlashMode.TORCH);
                    mFlashView.setImageResource(R.drawable.btn_flash_torch);
                } else if (mContainer.getFlashMode() == CameraView.FlashMode.TORCH) {
                    mContainer.setFlashMode(CameraView.FlashMode.ON);
                    mFlashView.setImageResource(R.drawable.btn_flash_on);
                }
                break;
            case R.id.btn_shutter_record:

                    if (!isRecording) {
                        isRecording = mContainer.startRecord();
                        if (isRecording) {
                            mRecordShutterButton.setBackgroundResource(R.drawable.btn_shutter_recording);
                        }
                    } else {
                        stopRecord();
                    }


                break;
            case R.id.btn_switch_camera:
                mContainer.switchCamera();
                if(mContainer.getIsFrontCamera()){
                    mFlashView.setVisibility(View.GONE);
                }else {
                    mFlashView.setVisibility(View.VISIBLE);
                }
            /*    CameraView c = new CameraView(CameraAty.this);
                if ( c.openCamera()){
                    mFlashView.setVisibility(View.INVISIBLE);
                }*/
              /*  CameraView c = new CameraView(CameraAty.this);
                boolean   isFrontCamera = c.IsFrontCamera();
                if (isFrontCamera){
                    mFlashView.setVisibility(View.INVISIBLE);
                }*/
                break;
            default:
                break;
        }
    }


    private void stopRecord() {
        String path = mContainer.stopRecord(this);
        isRecording = false;
        mRecordShutterButton.setBackgroundResource(R.drawable.btn_shutter_record);
        int currentTime = TimeUtile.getCurrentTime();
        long date= serviceTime+currentTime*1000;//服务器返回的时间加上本地计时器时间
        Intent intent = new Intent(this, PhotoPreAct.class);
        intent.putExtra("type", "video");
        intent.putExtra("filepath", path);
//        intent.putExtra("fileTime",fileTime);
        intent.putExtra("date", date);
        startActivity(intent);
//        finish();
    }

    @Override
    public void onTakePictureEnd(String filePath) {
        mCameraShutterButton.setClickable(true);
        int currentTime = TimeUtile.getCurrentTime();
        long date= serviceTime+currentTime*1000;//服务器返回的时间加上本地计时器时间
        Intent intent = new Intent(this, PhotoPreAct.class);
        intent.putExtra("type", "photo");
        intent.putExtra("filepath", filePath);
        intent.putExtra("date", date);
        startActivity(intent);
//        finish();
    }

    @Override
    public void onAnimtionEnd(Bitmap bm, boolean isVideo) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TimeUtile.cancelTime();
    }

    /**
     * 系统返回键
     */
    @Override
    public void onBackPressed() {
        if (flag.equals("video") && isRecording) {
            showDialogIsCancel("是否确定放弃录像？");
        }else{
            finish();
        }
    }
    private void showDialogIsCancel(String msg) {
        alertDialog = new AlertDialog.Builder(this).setTitle("温馨提示")
                .setMessage(msg).setIcon(R.drawable.ww)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();


                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        alertDialog.show();
    }
}