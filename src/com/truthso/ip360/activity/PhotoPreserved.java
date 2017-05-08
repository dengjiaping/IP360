package com.truthso.ip360.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.ossupload.FileUploadHelper;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.utils.BaiduLocationUtil;
import com.truthso.ip360.utils.BaiduLocationUtil.locationListener;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.ImageLoaderUtil;
import com.truthso.ip360.utils.SharePreferenceUtil;

/**
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @version 1.0
 * @despriction :照片保全的界面
 * @date 创建时间：2016-7-21下午5:18:47
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class PhotoPreserved extends BaseActivity implements OnClickListener {
    private Button btn_title_right, btn_preserved;
    private ImageButton btn_title_left;
    private ImageView iv_photo;
    private String path, title, size, date, loc, currLoc, currLonglat, longlat;
    private TextView tv_filename, tv_loc, tv_date, tv_filesize;
    private double fileSize_B;
    private long ll;
    private Dialog alertDialog;
    private FileUploadHelper fileUploadHelper;
    private FileInfo info;


    @Override
    public void initData() {
        path = getIntent().getStringExtra("path");
        title = getIntent().getStringExtra("title");
        size = getIntent().getStringExtra("size");
        date = getIntent().getStringExtra("date");
        loc = getIntent().getStringExtra("loc");
        longlat = getIntent().getStringExtra("longlat");
        fileSize_B = getIntent().getDoubleExtra("fileSize_B", 0);
        ll = Math.round(fileSize_B);
        getLocation();
    }

    @Override
    public void initView() {
        btn_title_left = (ImageButton) findViewById(R.id.btn_title_left);
        btn_title_left.setOnClickListener(this);
        btn_title_right = (Button) findViewById(R.id.btn_title_right);
        btn_title_right.setVisibility(View.VISIBLE);
        btn_title_right.setText("放弃");
        btn_title_right.setTextColor(getResources().getColor(R.color.white));
        btn_preserved = (Button) findViewById(R.id.btn_preserved);
        tv_filename = (TextView) findViewById(R.id.tv_filename);
        tv_filename.setText(title);
        tv_loc = (TextView) findViewById(R.id.tv_loc);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_date.setText(date);

        tv_filesize = (TextView) findViewById(R.id.tv_filesize);
        tv_filesize.setText(size);
        btn_title_right.setOnClickListener(this);
        btn_preserved.setOnClickListener(this);
        iv_photo = (ImageView) findViewById(R.id.iv_photo);
        iv_photo.setOnClickListener(this);

        ImageLoaderUtil.displayFromSDCardopt(path, iv_photo, null);
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

        info = new FileInfo();
        info.setFileName(title);
        info.setFilePath(path);
        info.setFileTime(null);
        info.setFileSize(ll + "");
        info.setType(MyConstants.PHOTOTYPE);
        info.setFileCreatetime(date);
        info.setFileLoc(loc);
        info.setPriKey((String) SharePreferenceUtil.getAttributeByKey(this, MyConstants.RSAINFO, MyConstants.PRIKEY, SharePreferenceUtil.VALUE_IS_STRING));
        info.setRsaId((int) SharePreferenceUtil.getAttributeByKey(this, MyConstants.RSAINFO, MyConstants.RSAID, SharePreferenceUtil.VALUE_IS_INT));
        info.setLatitudeLongitude(longlat);
        info.setMinTime(1);//照片计费时长为1
        //上传文件信息
        //filePre();
        fileUploadHelper = new FileUploadHelper(this);
        fileUploadHelper.uploadFileInfo(info);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_photopreserved;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_photo://点击查看详情
                Intent intent = new Intent(PhotoPreserved.this, PhotoDetailActivity.class);
                intent.putExtra("url", path);
                startActivity(intent);
                break;
            case R.id.btn_title_right://标题右上角的放弃
                showDialogIsCancel("是否确认放弃保全？");
                break;
            case R.id.btn_preserved:
                if (!fileUploadHelper.isFileInfoUpload()) {//保全接口没调成功的话就再调一次
                    fileUploadHelper.uploadFileInfo(info);
                }
                //调获取本次保全费用，及是否可用的接口
                // getport();
                fileUploadHelper.uploadFile();
                break;
            case R.id.btn_title_left://返回键
                showDialogIsCancel("是否确认放弃保全？");
                break;
            default:
                break;
        }

    }

    @Override
    public String setTitle() {
        return "拍照完成";
    }

    /**
     * 监听系统的返回键
     */
    @Override
    public void onBackPressed() {

        showDialogIsCancel("是否确认放弃保全？");
        //取消上传文件
        fileUploadHelper.cancelUploadFile();
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
                        // CancelUploadFile();
                    }
                }).create();
        alertDialog.show();
    }

    /**
     * 定位
     */
    private void getLocation() {
        BaiduLocationUtil.getLocation(PhotoPreserved.this, new locationListener() {

            @Override
            public void location(String s, double latitude, double longitude) {
                currLoc = s;
                currLonglat = longitude + "," + latitude;
            }
        });
    }
}


