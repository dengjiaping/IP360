package com.truthso.ip360.activity;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.truthso.ip360.utils.CheckUtil;

/**
 * @despriction :录音播放的页面
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-8-8下午3:46:58
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class RecordDetailActivity extends BaseActivity implements OnClickListener, OnSeekBarChangeListener {

	private MediaPlayer mp=new MediaPlayer();
	private String url,recordTime;
	private Button btn_start;
	private TextView tv_current,tv_total;
	private SeekBar sb_recorddetail;
	private Timer mTimer;
	private boolean isFirst=true,isChanging;
	@Override
	public void initData() {

		url=getIntent().getStringExtra("url");
		recordTime=getIntent().getStringExtra("recordTime");
	}

	@Override
	public void initView() {
		btn_start=(Button) findViewById(R.id.btn_start);
		tv_current=(TextView) findViewById(R.id.tv_current);
		tv_total=(TextView) findViewById(R.id.tv_total);
		tv_total.setText(recordTime);
		sb_recorddetail=(SeekBar) findViewById(R.id.sb_recorddetail);

		sb_recorddetail.setOnSeekBarChangeListener(this);
		btn_start.setOnClickListener(this);
		/*mp.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer arg0) {
				mp.release();
			}
		});*/
	}

	@Override
	public int setLayout() {
		return R.layout.activity_recorddetail;
	}

	@Override
	public String setTitle() {
		return "证据查看";
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_start:
			if(mp.isPlaying()){
				mp.pause();
			}else{
				if(isFirst){
					if(!CheckUtil.isEmpty(url)){
						try {
							mp.setDataSource(url);
							mp.prepare();
							sb_recorddetail.setMax(mp.getDuration());
	                        mTimer=new Timer();
	                        mTimer.schedule(new TimerTask() {
								
								@Override
								public void run() {
									if(isChanging){
										return;
									}

										sb_recorddetail.setProgress(mp.getCurrentPosition());								
								}
							}, 0, 10);
							
							mp.start();
							isFirst=false;
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}				
				}else{
					mp.start();
				}
			}
			break;

		default:
			break;
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(mp!=null&&mp.isPlaying()){
			mp.pause();
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(mp!=null){
			if(mp.isPlaying()){
				mp.stop();
			}
			mp.release();
		}
		mTimer.cancel();
		super.onDestroy();
	}

	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
		if(mp!=null){
			mp.seekTo(arg1);
		}
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		isChanging=true;
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		isChanging=false;
	}
}
