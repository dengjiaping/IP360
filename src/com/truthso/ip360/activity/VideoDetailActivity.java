package com.truthso.ip360.activity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;


import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.view.MyVideoView;


import java.util.HashMap;
import java.util.Map;


/**
 * @despriction :视频详情播放页面
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-8-8下午3:43:47
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class VideoDetailActivity extends BaseActivity implements OnTouchListener {
	MediaController mController;
	MyVideoView viv;
	int progress = 0;
	private String path;
	private Map<String,String> headers ;
	private ImageView iv_chuo;
	@Override
	public void initData() {

		headers=new HashMap<>();
		headers.put("Referer","http://appapi.truthso.com");

	}

	@SuppressLint("NewApi")
	public void initView() {
		path = getIntent().getStringExtra("url");
		viv = (MyVideoView) findViewById(R.id.videoView);
		mController = new MediaController(this);
		viv.setMediaController(mController);
		//iv_chuo= (ImageView) findViewById(R.id.iv_chuo);
		//viv.setVideoPath(path);

		Uri uri= Uri.parse(path);
		viv.setVideoURI(uri,headers);
		viv.requestFocus();
		viv.start();
		showProgress("正在缓冲...");
		viv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer arg0) {
				 hideProgress();
//				iv_chuo.setVisibility(View.VISIBLE);//真相保全的戳
			}
	});
		viv.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            //视频无法播放监听
            public boolean onError(MediaPlayer mp, int what, int extra) {
				Toaster.showToast(VideoDetailActivity.this,"视频无法播放");
				hideProgress();
                finish();  
                return true;  
            }  
        });
		viv.setOnInfoListener(new MediaPlayer.OnInfoListener() {
			
			@Override
			public boolean onInfo(MediaPlayer mp, int arg1, int arg2) {
				if(arg1 == MediaPlayer.MEDIA_INFO_BUFFERING_START){
                   showProgress("正在缓冲...");
                }else if(arg1 == MediaPlayer.MEDIA_INFO_BUFFERING_END){  
                    //此接口每次回调完START就回调END,若不加上判断就会出现缓冲图标一闪一闪的卡顿现象  
                	  hideProgress();
//					iv_chuo.setVisibility(View.VISIBLE);
                    if(mp.isPlaying()){  
                       hideProgress();
//						iv_chuo.setVisibility(View.VISIBLE);
                    }  
                }  
				return true;
			}
		});
	}

	@Override
	public int setLayout() {
		return R.layout.activity_video_detail;
	}

	@Override
	public String setTitle() {
		return "证据查看";
	}
	@Override
	protected void onPause() {
		super.onPause();
		progress = viv.getCurrentPosition();
	}

	@Override
	protected void onResume() {
		super.onResume();
		viv.seekTo(progress);
		viv.start();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;

	}
}
