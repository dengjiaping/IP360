package com.truthso.ip360.activity;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.VideoView;

import com.truthso.ip360.utils.MediaController;

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
	VideoView viv;
	int progress = 0;
	private String path;

	@Override
	public void initData() {

	}

	@Override
	public void initView() {
		path = getIntent().getStringExtra("url");
		viv = (VideoView) findViewById(R.id.videoView);
		mController = new MediaController(this);
		viv.setMediaController(mController);
		viv.setVideoPath(path);
		viv.requestFocus();
		viv.start();
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
