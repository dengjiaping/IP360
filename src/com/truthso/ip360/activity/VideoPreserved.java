package com.truthso.ip360.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.ossupload.FileUploadHelper;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.utils.BaiduLocationUtil;
import com.truthso.ip360.utils.BaiduLocationUtil.locationListener;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.GetFileSizeUtil;
import com.truthso.ip360.utils.SharePreferenceUtil;

/**
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @version 1.0
 * @despriction :录像保全的界面
 * @date 创建时间：2016-7-21下午5:21:25
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class VideoPreserved extends BaseActivity implements OnClickListener {
    private Dialog alertDialog;
    private String mVideoPath;
    private String mVideoName;
    private ImageView iv_video;
    private String mVideoSize, size, title, longlat;
    private String mDate, loc, currLoc, currLonglat, time;
    private Button btn_preserved, btn_title_right;
    private ImageButton btn_title_left;
    private TextView tv_filename, tv_loc, tv_date, tv_filesize, tv_time;
    private double video_fileSize_B;
    private long ll;
    private RelativeLayout rl_video;
    private FileInfo info;
    private FileUploadHelper fileUploadHelper;
    private int minTime;

    @Override
    public void initData() {
        mVideoPath = getIntent().getStringExtra("filePath");
        mDate = getIntent().getStringExtra("date");
        loc = getIntent().getStringExtra("loc");
        longlat = getIntent().getStringExtra("longlat");
        time = getIntent().getStringExtra("time");
        size = getIntent().getStringExtra("size");
        title = getIntent().getStringExtra("title");
        minTime=getIntent().getIntExtra("minTime",0);
        video_fileSize_B = getIntent().getDoubleExtra("video_fileSize_B", 0);
        ll = Math.round(video_fileSize_B);
    }

    @Override
    public void initView() {
        //更新位置
        getLocation();
        btn_title_left = (ImageButton) findViewById(R.id.btn_title_left);
        btn_title_left.setOnClickListener(this);
        btn_preserved = (Button) findViewById(R.id.btn_preserved);
        btn_preserved.setOnClickListener(this);
        btn_title_right = (Button) findViewById(R.id.btn_title_right);
        btn_title_right.setVisibility(View.VISIBLE);
        btn_title_right.setOnClickListener(this);
        btn_title_right.setText("放弃");
        btn_title_right.setTextColor(getResources().getColor(R.color.white));
        rl_video = (RelativeLayout) findViewById(R.id.rl_video);
        rl_video.setOnClickListener(this);
        iv_video = (ImageView) findViewById(R.id.iv_video);
        tv_filename = (TextView) findViewById(R.id.tv_filename);
        tv_loc = (TextView) findViewById(R.id.tv_loc);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_date.setText(mDate);
        tv_filesize = (TextView) findViewById(R.id.tv_filesize);
        tv_time = (TextView) findViewById(R.id.tv_time);

        mVideoName = mVideoPath.substring(mVideoPath.lastIndexOf("/") + 1);
        iv_video.setImageBitmap(getVideoThumbnail(mVideoPath, 80, 80,
                MediaStore.Images.Thumbnails.MICRO_KIND));
        mVideoSize = GetFileSizeUtil.FormatFileSize(mVideoPath);
        tv_filename.setText(mVideoName);
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

        tv_filesize.setText(mVideoSize);
        tv_time.setText(time.toString().trim());
        //上传文件信息
        // filePre();
        info = new FileInfo();
        info.setFileName(title);
        info.setFilePath(mVideoPath);
        info.setFileSize(ll + "");
        info.setType(MyConstants.VIDEOTYPE);
        info.setFileCreatetime(mDate);
        info.setFileLoc(loc);
        info.setPriKey((String) SharePreferenceUtil.getAttributeByKey(this, MyConstants.RSAINFO, MyConstants.PRIKEY, SharePreferenceUtil.VALUE_IS_STRING));
        info.setRsaId((int) SharePreferenceUtil.getAttributeByKey(this, MyConstants.RSAINFO, MyConstants.RSAID, SharePreferenceUtil.VALUE_IS_INT));
        info.setLatitudeLongitude(longlat);
        info.setMinTime(minTime);
        fileUploadHelper = new FileUploadHelper(this);
        fileUploadHelper.uploadFileInfo(info);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_videopreserved;
    }

    @Override
    public String setTitle() {
        return "录像完成";
    }

    /**
     * 获取视频的缩略图 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     *
     * @param videoPath 视频的路径
     * @param width     指定输出视频缩略图的宽度
     * @param height    指定输出视频缩略图的高度度
     * @param kind      参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *                  其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     * @author wsx_summer
     */
    @SuppressWarnings("unused")
    private Bitmap getVideoThumbnail(String videoPath, int width, int height,
                                     int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_video://点击到播放详情
                Intent intent = new Intent(VideoPreserved.this, VideoDetailActivity.class);
                intent.putExtra("url", mVideoPath);
                startActivity(intent);
                break;
            case R.id.btn_title_right://标题右上角放弃
                showDialogIsCancel("是否确认放弃保全？");
                break;
            case R.id.btn_preserved://保全
                if (!fileUploadHelper.isFileInfoUpload()) {//保全的接口调不成功，再掉一次
                    fileUploadHelper.uploadFileInfo(info);
                }
                //上传文件
                fileUploadHelper.uploadFile();
                break;
            case R.id.btn_title_left://标题上的返回键
                showDialogIsCancel("是否确认放弃保全？");
                break;

            default:
                break;
        }
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
     * 监听系统的返回键
     */
    @Override
    public void onBackPressed() {
//		super.onBackPressed();//不注释掉会立即返回，不提示弹框
        showDialogIsCancel("是否确认放弃保全？");
        //取消上传文件
        fileUploadHelper.cancelUploadFile();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 定位
     */
    private void getLocation() {
        BaiduLocationUtil.getLocation(VideoPreserved.this, new locationListener() {

            @Override
            public void location(String s, double latitude, double longitude) {
                currLoc = s;
                currLonglat = longitude + "," + latitude;
            }


        });
    }
}
