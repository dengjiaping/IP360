package com.truthso.ip360.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.ossupload.FileUploadHelper;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.utils.BaiduLocationUtil;
import com.truthso.ip360.utils.BaiduLocationUtil.locationListener;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.SharePreferenceUtil;

/**
 * 录音保全界面
 *
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @version 1.0
 * @date 创建时间：2016-6-13上午10:48:56
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class LiveRecordPreActivity extends BaseActivity implements
        OnClickListener {
    private TextView tv_filename, tv_loc, tv_date, tv_filesize, tv_time;
    private String date, fileName, loc, currLoc, fileSize, time, filePath, currLonglat, longlat;
    private Button btn_title_right, btn_save;
    private ImageButton btn_title_left;
    private double fileSize_B;
    private long ll;
    private Dialog alertDialog;
    private RelativeLayout rl_record;
    private FileUploadHelper fileUploadHelper;
    private FileInfo info;
    private int minTime;//文件计费时长

    @Override
    public void initData() {
        fileName = getIntent().getStringExtra("fileName");
        date = getIntent().getStringExtra("date");
        fileSize = getIntent().getStringExtra("fileSize");
        minTime = getIntent().getIntExtra("mintime", 0);
        time = getIntent().getStringExtra("fileTime");
        filePath = getIntent().getStringExtra("filePath");
        loc = getIntent().getStringExtra("loc");
        longlat = getIntent().getStringExtra("longlat");
        fileSize_B = getIntent().getDoubleExtra("fileSize_B", 0);
        ll = Math.round(fileSize_B);
    }

    @Override
    public void initView() {
        //更新位置
        getLocation();
        btn_title_left = (ImageButton) findViewById(R.id.btn_title_left);
        btn_title_left.setOnClickListener(this);
        btn_title_right = (Button) findViewById(R.id.btn_title_right);
        btn_title_right.setVisibility(View.VISIBLE);
        btn_title_right.setOnClickListener(this);
        btn_title_right.setText("放弃");
        btn_title_right.setTextColor(getResources().getColor(R.color.white));
        tv_filename = (TextView) findViewById(R.id.tv_filename);
        tv_loc = (TextView) findViewById(R.id.tv_loc);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_date.setText(date);
        tv_filesize = (TextView) findViewById(R.id.tv_filesize);
        tv_time = (TextView) findViewById(R.id.tv_time);

        rl_record = (RelativeLayout) findViewById(R.id.rl_record);
        rl_record.setOnClickListener(this);
        btn_save = (Button) findViewById(R.id.btn_preserved);
        btn_save.setOnClickListener(this);
        tv_filename.setText(fileName);

        if (!CheckUtil.isEmpty(currLoc) && !currLoc.equals("nullnull")) {//当前能获取位置用当前的位置，
            loc = currLoc;
            longlat = currLonglat;
            tv_loc.setText(loc);

        } else {//当前没有位置,用取证前时候的位置

            if (!CheckUtil.isEmpty(loc) && !loc.equals("nullnull")) {
                tv_loc.setText(loc);
            } else {
                tv_loc.setText("获取位置信息失败");
                loc = "获取位置信息失败";
            }
        }
        tv_date.setText(date);
        tv_filesize.setText(fileSize);
        tv_time.setText(time);

        //上传文件的
        //filePre();
        info = new FileInfo();
        info.setFileName(fileName);
        info.setFilePath(filePath);
        info.setFileSize(ll + "");
        info.setType(MyConstants.RECORDTYPE);
        info.setFileCreatetime(date);
        info.setFileTime(time);
        info.setFileLoc(loc);
        info.setPriKey((String) SharePreferenceUtil.getAttributeByKey(this, MyConstants.RSAINFO, MyConstants.PRIKEY, SharePreferenceUtil.VALUE_IS_STRING));
        info.setRsaId((int) SharePreferenceUtil.getAttributeByKey(this, MyConstants.RSAINFO, MyConstants.RSAID, SharePreferenceUtil.VALUE_IS_INT));
        info.setLatitudeLongitude(longlat);
        info.setMinTime(minTime);
        //上传文件信息
        //filePre();
        fileUploadHelper = new FileUploadHelper(this);
        fileUploadHelper.uploadFileInfo(info);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_prerecord;
    }

    @Override
    public String setTitle() {
        return "录音完成";
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_record://录音
                Intent intent = new Intent(LiveRecordPreActivity.this, RecordDetailActivity.class);
                intent.putExtra("url", filePath);
                startActivity(intent);
                break;
            case R.id.btn_title_right://放弃
                showDialogIsCancel("是否确认放弃保全？");
                break;
            case R.id.btn_preserved://保全
                if (!fileUploadHelper.isFileInfoUpload()) {//保全的接口调不成功，再掉一次
                    fileUploadHelper.uploadFileInfo(info);
                }
                //调获取本次保全费用，及是否可用的接口
                fileUploadHelper.uploadFile();
                break;
            case R.id.btn_title_left://返回键
                showDialogIsCancel("是否确认放弃保全？");
                break;
            default:
                break;

        }
    }

    /**
     * 监听系统的返回键
     */
    @Override
    public void onBackPressed() {
//		super.onBackPressed();
        //取消上传文件
        showDialogIsCancel("是否确认放弃保全？");
    }

    /**
     * 是否确认放弃保全
     *
     * @param msg
     */
    private void showDialogIsCancel(String msg) {
        alertDialog = new AlertDialog.Builder(this).setTitle("温馨提示")
                .setMessage(msg).setIcon(R.drawable.ww)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //取消上传文件
                        fileUploadHelper.cancelUploadFile();
                        finish();


                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fileUploadHelper.cancelUploadFile();
                    }
                }).create();
        alertDialog.show();
    }

    /**
     * 定位
     */
    private void getLocation() {
        BaiduLocationUtil.getLocation(LiveRecordPreActivity.this, new locationListener() {

            @Override
            public void location(String s, double latitude, double longitude) {
                currLoc = s;
                currLonglat = longitude + "," + latitude;
            }


        });
    }
}
