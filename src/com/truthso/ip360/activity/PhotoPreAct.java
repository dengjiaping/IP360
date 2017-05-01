package com.truthso.ip360.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.linj.DisplayUtil;
import com.truthso.ip360.utils.BaiduLocationUtil;
import com.truthso.ip360.utils.FileSizeUtil;
import com.truthso.ip360.utils.FileUtil;
import com.truthso.ip360.utils.ImageLoaderUtil;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

public class PhotoPreAct extends BaseActivity {

    private ImageView image;
    private Button cancel, confirm;
    private Button btn_play;
    private String filepath,type,loc;
    private double lat,longti;

    @Override
    public void initData() {
        type = getIntent().getStringExtra("type");
        filepath = getIntent().getStringExtra("filepath");
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
            ImageLoaderUtil.displayFromSDCardopt(filepath, image, null);
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
                File newFile = new File(filepath);
                String fileSize = FileSizeUtil.getAutoFileOrFilesSize(newFile
                        .getAbsolutePath());
                long length=newFile.length();
                double fileSize_B = FileSizeUtil.FormetFileSize(length, FileSizeUtil.SIZETYPE_B);
                String date = new DateFormat().format("yyyy-MM-dd HH:mm:ss", Calendar.getInstance(Locale.CHINA)).toString();
                Intent intent = new Intent(PhotoPreAct.this, PhotoPreserved.class);
                intent.putExtra("path", newFile.getAbsolutePath());
                intent.putExtra("title", newFile.getName());
                intent.putExtra("size", fileSize);
                intent.putExtra("date", date);
                intent.putExtra("fileSize_B", fileSize_B);
                intent.putExtra("loc",loc);
                intent.putExtra("longlat",longti+","+lat);
                startActivity(intent);
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


    //获取位置
    private void getLocation(){
        BaiduLocationUtil.getLocation(this, new BaiduLocationUtil.locationListener() {

            @Override
            public void location(String s, double latitude, double longitude) {
                loc = s;
                lat = latitude;
                longti =longitude;
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
