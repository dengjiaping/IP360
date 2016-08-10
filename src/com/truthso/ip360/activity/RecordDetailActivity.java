package com.truthso.ip360.activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.SimpleFormatter;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
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
	private CheckBox btn_start;
	private TextView tv_current,tv_total;
	private SeekBar sb_recorddetail;
	private Timer mTimer;
	private boolean isChanging,isFirst=true;
	private int duration;
	private SimpleDateFormat formatter;
	private boolean isPlaying;
	@Override
	public void initData() {

		url=getIntent().getStringExtra("url");
          if(url!=null){
      		try {
      			mp.setDataSource(url);
      			mp.prepare();
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
  		duration = mp.getDuration();
	}

	@Override
	public void initView() {
		btn_start=(CheckBox) findViewById(R.id.btn_start);
		tv_current=(TextView) findViewById(R.id.tv_current);
		tv_total=(TextView) findViewById(R.id.tv_total);
		formatter = new SimpleDateFormat("HH:mm:ss");//初始化Formatter的转换格式。
		formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		tv_total.setText(formatter.format(duration)) ;  
		sb_recorddetail=(SeekBar) findViewById(R.id.sb_recorddetail);
		sb_recorddetail.setMax(duration);
		
		sb_recorddetail.setOnSeekBarChangeListener(this);
		btn_start.setOnClickListener(this);
		mp.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer arg0) {
				btn_start.setChecked(false);
			}
		});
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
					mTimer=new Timer();
					mTimer.schedule(new TimerTask() {
						
						@Override
						public void run() {
							if(isChanging){
								return;
							}
							 int position = mp.getCurrentPosition();
							sb_recorddetail.setProgress(position);
							Message msg=new Message();
							msg.obj=position;
							handler.sendMessage(msg);
						}
					}, 0, 100);
					isFirst=false;
				}		
				mp.start();
			}
			break;
		}
	}
	
	private Handler handler=new Handler(){
	   public void handleMessage(android.os.Message msg) {
		   tv_current.setText(formatter.format(msg.obj)) ;
	   };
	};
	
	
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
		if(mTimer!=null){
			mTimer.cancel();			
		}
		if(mp!=null){
			mp.release();
		}
		
		super.onDestroy();
	}

	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
		if(mp.isPlaying()&&isChanging){
			
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
		mp.seekTo(arg0.getProgress());
		isChanging=false;
	}
}
