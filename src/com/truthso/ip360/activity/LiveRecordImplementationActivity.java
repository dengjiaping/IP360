package com.truthso.ip360.activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.utils.BaiduLocationUtil;
import com.truthso.ip360.utils.FileSizeUtil;
import com.truthso.ip360.utils.BaiduLocationUtil.locationListener;
import com.truthso.ip360.view.VoiceLineView;
import com.truthso.ip360.view.xrefreshview.LogUtils;

/**
 * 
 * 执行现场录音的界面
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-6-13下午4:39:51
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class LiveRecordImplementationActivity extends BaseActivity implements
		OnClickListener {
	private String loc;
	private TextView mRecordTime;
	private String timeUsed;
	private double lat,longti;
	private int timeUsedInsec;
	private boolean isPause;
	private Handler uiHandle = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (!isPause) {
					addTimeUsed();
					updateClockUI();
				}
				uiHandle.sendEmptyMessageDelayed(1, 1000);
				break;
			default:
			case 2:
				if (mediaRecorder == null)
					return;
				double ratio = (double) mediaRecorder.getMaxAmplitude() / 100;
				double db = 0;// 分贝
				// 默认的最大音量是100,可以修改，但其实默认的，在测试过程中就有不错的表现
				// 你可以传自定义的数字进去，但需要在一定的范围内，比如0-200，就需要在xml文件中配置maxVolume
				// 同时，也可以配置灵敏度sensibility
				if (ratio > 1)
					db = 40 * Math.log10(ratio);
				// 只要有一个线程，不断调用这个方法，就可以使波形变化
				// 主要，这个方法必须在ui线程中调用
				voiceLineView.setVolume((int) (db));
				break;
			}
		}
	};

	private Button mButton, btn_cancle, btn_save;
	/** 录音控件 */
	private MediaRecorder mediaRecorder = null;
	/** 路径名 */
	private String fileDir;
	// 录音文件名
	private String filePath;

	private boolean isRecording = false;
	private SimpleDateFormat formatter;
	private Date curDate;
	private String date;
	private String fileSize;
	private String fileName;
	private String recTotalTime;
	private VoiceLineView voiceLineView;
	private boolean isAlive = true;
	private int hor;
	private int min;
	private int sec;
	private int mintime;
	private int i;



	private void startTime() {
		uiHandle.sendEmptyMessageDelayed(1, 1000);
	}

	/**
	 * 更新时间的显示
	 */
	private void updateClockUI() {
		mRecordTime.setText(getHor() + ":" + getMin() + ":" + getSec());
	
	}

	public void addTimeUsed() {
		timeUsedInsec = timeUsedInsec + 1;
		timeUsed = this.getMin() + ":" + this.getSec();
	}

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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_record:
			if (!isRecording) {
				isAlive = true;
				isRecording = true;
				mButton.setSelected(true);
				recordVoice();
				uiHandle.removeMessages(1);
				startTime();
				isPause = false;
				new Thread(new Runnable() {

					@Override
					public void run() {
						while (isAlive) {
							uiHandle.sendEmptyMessage(2);
						
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}).start();

			} else {
				isAlive = false;
				isRecording = false;
				mButton.setSelected(false);
				mButton.setVisibility(View.GONE);
				stoprecordVoice();

				formatter = new SimpleDateFormat("yyyy-MM-dd   HH:mm:ss");
				curDate = new Date(System.currentTimeMillis());
				date = formatter.format(curDate);
				fileSize = FileSizeUtil.getAutoFileOrFilesSize(filePath);
				File file = new File(filePath);
				   long length = file.length();
				   double fileSize_B = FileSizeUtil.FormetFileSize(length, FileSizeUtil.SIZETYPE_B);
				   fileName = filePath.substring(filePath.lastIndexOf("/")+1);
//				   LogUtils.e(fileName+"录音的文件名");
				recTotalTime = mRecordTime.getText().toString().trim();
				i = timeUsedInsec%60;
				if (i > 0) {
					mintime = timeUsedInsec/60+1;
				}else{
					mintime = timeUsedInsec/60;
				}
				
				isPause = true;
				timeUsedInsec = 0;
				Intent intent = new Intent(this,LiveRecordPreActivity.class);
				intent.putExtra("fileTime", recTotalTime);
				intent.putExtra("date", date);
				intent.putExtra("fileSize", fileSize);
				intent.putExtra("fileSize_B", fileSize_B);
				intent.putExtra("fileName", fileName);
				intent.putExtra("filePath", filePath);
				intent.putExtra("mintime", mintime);
				intent.putExtra("loc", loc);
				intent.putExtra("longlat",longti+","+lat);
				startActivity(intent);
				finish();		
			}
			break;

		default:
			break;
		}
	}

	/** 开始录音 */
	private void recordVoice() {
		mediaRecorder = null;
		mediaRecorder = new MediaRecorder();

		filePath = fileDir +"/"+ new DateFormat().format("yyyyMMdd_HHmmss",
						Calendar.getInstance(Locale.CHINA)) + ".amr";
		LogUtils.e(filePath+"录音的路径");
		// 设置录音的编码格式,即数据源的格式,这里设置什么格式主要根据录音的用途来判断
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		// 设置录音文件的格式,这里是指文件的后缀名格式,这个设置的3GP
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		// 设置录音的解码格式,这个必须在setOutputFile方法前设置,否则无效
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mediaRecorder.setOutputFile(filePath);// 设置录音文件的输出路径
		try {
			mediaRecorder.prepare();
		} catch (IllegalStateException e) {
			// stoprecordVoice();
			e.printStackTrace();
		} catch (IOException e) {
			// stoprecordVoice();
			e.printStackTrace();
		}
		mediaRecorder.start(); // 开始录音
	}

	/** 停止录音 */
/*	private void stoprecordVoice() {
		if (mediaRecorder != null) {
			mediaRecorder.stop(); // 停止录音
		//	mediaRecorder.reset(); // 在释放资源时,必须要重置一下,不然下一步释放时可能会出错
			mediaRecorder.release(); // 这个是否录音控件的,不然会一直占据资源
			mediaRecorder=null;
		}
	}*/
	/** 停止录音 */
	private void stoprecordVoice() {
		if (mediaRecorder != null) {
			mediaRecorder.stop(); // 停止录音
		//	mediaRecorder.reset(); // 在释放资源时,必须要重置一下,不然下一步释放时可能会出错
			mediaRecorder.release(); // 这个是否录音控件的,不然会一直占据资源
			mediaRecorder=null;
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
	}

	

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		fileDir =MyConstants.RECORD_PATH;
		File f=new File(fileDir);
		if(!f.exists()){
			f.mkdirs();
		}
	}

	@Override
	public void initView() {
		getLocation();
				

		mButton = (Button) findViewById(R.id.btn_record);
		voiceLineView = (VoiceLineView) findViewById(R.id.voicLine);
		mButton.setOnClickListener(this);

		mRecordTime = (TextView) findViewById(R.id.tv_record_time);

	}

	@Override
	public int setLayout() {
		return R.layout.activity_liverecord_implement;
	}

	@Override
	public String setTitle() {
		return "录音取证";
	}
	private void getLocation(){
		BaiduLocationUtil.getLocation(this, new locationListener() {

			@Override
			public void location(String s, double latitude, double longitude) {
				loc = s;
				lat = latitude;
				longti =longitude;
//				Message message = handler .obtainMessage();
//				message.what = 1;
//				handler.sendMessage(message);
			}


		});
	}
}
