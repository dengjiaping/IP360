package com.truthso.ip360.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.linj.DisplayUtil;
import com.truthso.ip360.utils.BaiduLocationUtil;
import com.truthso.ip360.utils.DateUtil;
import com.truthso.ip360.utils.FileSizeUtil;
import com.truthso.ip360.utils.TimeUtile;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
/**
 * @despriction :拍照录像后的页面
 *
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2017/5/3 14:03
 * @version
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class PhotoPreAct extends BaseActivity {

    private ImageView image;
    private Button cancel, confirm;
    private Button btn_play;
    private String filepath, type, loc;
    private double lat, longti;
    private double video_fileSize_B;
    private String time;
    private long date;

    @Override
    public void initData() {
        type = getIntent().getStringExtra("type");
        filepath = getIntent().getStringExtra("filepath");
        date=getIntent().getLongExtra("date",0);
        getLocation();
    }

    @Override
    public void initView() {
        image = (ImageView) findViewById(R.id.image);
        cancel = (Button) findViewById(R.id.cancel);
        confirm = (Button) findViewById(R.id.confirm);
        btn_play = (Button) findViewById(R.id.btn_play);

        if (type.equals("photo")) {
            btn_play.setVisibility(View.GONE);
            Glide.with(this).load(filepath).into(image);
        } else {
            btn_play.setVisibility(View.VISIBLE);
            btn_play.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri data = Uri.parse("file://" + filepath);
                    intent.setDataAndType(data, "video/mp4");

                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Toast.makeText(PhotoPreAct.this, "播放失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            image.setImageBitmap(getVideoThumbnail(filepath));
        }

        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (filepath == null) {
                    return;
                }
                if (type.equals("photo")) {

                    File newFile = new File(filepath);
                    String fileSize = FileSizeUtil.getAutoFileOrFilesSize(newFile
                            .getAbsolutePath());
                    long length = newFile.length();
                    double fileSize_B = FileSizeUtil.FormetFileSize(length, FileSizeUtil.SIZETYPE_B);
                    String dateStr= DateUtil.formatDate(new Date(date),"yyyy-MM-dd HH:mm:ss");
                    Log.i("djj","dateStr"+dateStr);

                    Intent intent = new Intent(PhotoPreAct.this, PhotoPreserved.class);
                    intent.putExtra("path", newFile.getAbsolutePath());
                    intent.putExtra("title", newFile.getName());
                    intent.putExtra("size", fileSize);
                    intent.putExtra("date", dateStr);
                    intent.putExtra("fileSize_B", fileSize_B);
                    intent.putExtra("loc", loc);
                    intent.putExtra("longlat", longti + "," + lat);
                    startActivity(intent);
                } else {
                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                    mmr.setDataSource(filepath);
                    String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // 播放时长单位为毫秒
                    double duration = Double.parseDouble(durationStr);
                    timeUsedInsec = (int) (duration / 1000);//秒
                    time = getHor() + ":" + getMin() + ":" + getSec();
                    if (sec > 0) {
                        minTime = hor * 60 + min + 1;
                    } else {
                        minTime = hor * 60 + min;
                    }
                    String dateStr= DateUtil.formatDate(new Date(date),"yyyy-MM-dd HH:mm:ss");

                    File file = new File(filepath);
                    long length = file.length();
                    video_fileSize_B = FileSizeUtil.FormetFileSize(length, FileSizeUtil.SIZETYPE_B);
                    long size = 0;
                    try {
                        size = FileSizeUtil.getFileSize(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(PhotoPreAct.this, VideoPreserved.class);
                    intent.putExtra("filePath", filepath);
                    intent.putExtra("date", dateStr);
                    intent.putExtra("loc", loc);
                    intent.putExtra("time", time);
                    intent.putExtra("minTime", minTime);
                    intent.putExtra("size", size);
                    intent.putExtra("video_fileSize_B", video_fileSize_B);
                    intent.putExtra("title", file.getName());
                    intent.putExtra("loc", loc);
                    intent.putExtra("longlat", longti + "," + lat);
                    startActivity(intent);
                }
                TimeUtile.cancelTime();
                finish();
            }
        });
    }

    @Override
    public int setLayout() {
        return R.layout.photopre;
    }

    @Override
    public String setTitle() {
        return null;
    }

    private int sec;
    private int hor;
    private int min;
    private int minTime;
    private int timeUsedInsec;

    public CharSequence getHor() {
        hor = timeUsedInsec / 3600;
        return hor < 10 ? "0" + hor : String.valueOf(hor);
    }

    public CharSequence getMin() {
        min = timeUsedInsec / 60;
        return min < 10 ? "0" + min : String.valueOf(min);
    }

    public CharSequence getSec() {
        sec = timeUsedInsec % 60;
        return sec < 10 ? "0" + sec : String.valueOf(sec);
    }


    //获取位置
    private void getLocation() {
        BaiduLocationUtil.getLocation(this, new BaiduLocationUtil.locationListener() {

            @Override
            public void location(String s, double latitude, double longitude) {
                loc = s;
                lat = latitude;
                longti = longitude;
            }
        });
    }

    /**
     * 获取帧缩略图，根据容器的高宽进行缩放
     *
     * @param filePath
     * @return
     */
    public Bitmap getVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime(-1);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        if (bitmap == null)
            return null;
        // Scale down the bitmap if it's too large.
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int pWidth = DisplayUtil.getScreenWidth(this);// 容器宽度
        int pHeight = DisplayUtil.getScreenHeight(this);//容器高度

        //获取宽高跟容器宽高相比较小的倍数，以此为标准进行缩放
        float scale = Math.min((float) width / pWidth, (float) height / pHeight);
        int w = Math.round(scale * pWidth);
        int h = Math.round(scale * pHeight);
        bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
        return bitmap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaiduLocationUtil.unRegisterLocationListener();
    }
}
