package com.truthso.ip360.activity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.utils.FileSizeUtil;

/**
 * 
 * 执行现场录音的界面
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-6-13下午4:39:51
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class LiveRecordImplementationActivity extends Activity implements OnClickListener {	
	
    private TextView mRecordTime;
    private boolean isPaused = false;  
    private String timeUsed;  
    private int timeUsedInsec;  
     
    private Handler uiHandle = new Handler(){  
        public void handleMessage(android.os.Message msg) {  
            switch(msg.what){  
            case 1:  
                if(!isPaused) {  
                    addTimeUsed();  
                    updateClockUI();  
                }  
                uiHandle.sendEmptyMessageDelayed(1, 1000);  
                break;  
            default: break;  
            }  
        }  
    }; 
    
	private Button mButton;
	/**录音控件*/
	private  MediaRecorder  mediaRecorder = null;
	/**路径名*/
	private String filePath;
	
	private boolean isRecording = true;
	@Override                                                      
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_liverecord_implement);
		filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
		initView();
	}
	@Override  
    protected void onPause() {  
        super.onPause();  
        isPaused = true;  
    }  
      
      
 
      
    private void startTime(){  
        uiHandle.sendEmptyMessageDelayed(1, 1000);  
    }  
      
    /** 
     * 更新时间的显示 
     */  
    private void updateClockUI(){  
    	mRecordTime.setText(getHor()+":"+getMin()+ ":"+getSec());
       // minText.setText(getMin() + ":");  
        //secText.setText(getSec());  
    }  
      
    public void addTimeUsed(){  
        timeUsedInsec = timeUsedInsec + 1;  
        timeUsed = this.getMin() + ":" + this.getSec();  
    }  
    public CharSequence getHor(){  
    	int hor= timeUsedInsec / 3600;
        return hor < 10 ? "0"+ hor :String.valueOf(hor);  
        
    }  
    public CharSequence getMin(){  
    	int min= timeUsedInsec / 60;
        return min < 10 ? "0"+ min :String.valueOf(min);  
        
    }  
      
    public CharSequence getSec(){  
        int sec = timeUsedInsec % 60;  
        return sec < 10? "0" + sec :String.valueOf(sec);  
    }  

	private void initView() {
		mButton = (Button) findViewById(R.id.btn_record);
		mButton.setOnClickListener(this);
		
		mRecordTime=(TextView) findViewById(R.id.tv_record_time);
//		mBack=(ImageView) findViewById(R.id.iv_back);
//		mBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_record:
			if(isRecording){
				isRecording = !isRecording;
				mButton.setText("按下停止录音");
				recordVoice();
				
				  uiHandle.removeMessages(1);  
	                startTime();  
	                isPaused = false;  
				
			}else{
				isRecording = !isRecording;
				mButton.setText("按下开始录音");
				stoprecordVoice();
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy年MM月dd日    HH:mm:ss     ");
				Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
				String date = formatter.format(curDate);
				String	fileSize = FileSizeUtil.getAutoFileOrFilesSize(filePath);
				String fileName = filePath.substring(filePath.lastIndexOf("_")+1, filePath.lastIndexOf("."));
				String recTotalTime =  mRecordTime.getText().toString().trim();//录音时长
				
				isPaused = true;  
	            timeUsedInsec = 0;
	            saveData(date, fileSize, fileName, filePath,recTotalTime);
	             finish();
				
			}
			break;
//		case R.id.iv_back:
//				Intent intent1 = new Intent(this,MainActivity.class);
//				startActivity(intent1);
//				
//			break;
		default:
			break;
		}
	}
	
	/**开始录音*/
	private void recordVoice() {
		mediaRecorder = null;
		mediaRecorder = new MediaRecorder();
		
		filePath = filePath + "/record_" + new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".3gp";
		//设置录音的编码格式,即数据源的格式,这里设置什么格式主要根据录音的用途来判断
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		//设置录音文件的格式,这里是指文件的后缀名格式,这个设置的3GP
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		//设置录音的解码格式,这个必须在setOutputFile方法前设置,否则无效
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mediaRecorder.setOutputFile(filePath);//设置录音文件的输出路径
		try {
			mediaRecorder.prepare();
		} catch (IllegalStateException e) {
			stoprecordVoice();
			e.printStackTrace();
		} catch (IOException e) {
			stoprecordVoice();
			e.printStackTrace();
		}
		mediaRecorder.start(); //开始录音
//		Toast.makeText(LiveRecordActivity.this, "开始录音", 0).show();
	}
	
	/**停止录音*/
	private void stoprecordVoice() {
		if(mediaRecorder != null){
			mediaRecorder.stop();    //停止录音
			mediaRecorder.reset();   // 在释放资源时,必须要重置一下,不然下一步释放时可能会出错
			mediaRecorder.release(); // 这个是否录音控件的,不然会一直占据资源
//			Toast.makeText(LiveRecordActivity.this, "停止录音", 0).show();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		isRecording = true;
		isPaused = false; 
	}
	/**
	 * 将数据保存到数据库
	 */
	private void saveData(String date, String fileSize, String name, String path,String recordTime) {
		SqlDao sqlDao = new SqlDao(this);
		DbBean dbBean = new DbBean();
		dbBean.setType(MyConstants.RECODE);// 文件类别
		dbBean.setCreateTime(date);// 生成时间
		dbBean.setFileSize(fileSize);// 文件大小
		dbBean.setResourceUrl(path);
		dbBean.setTitle(name);// 名称
		dbBean.setRecordTime(recordTime);
		sqlDao.save(dbBean, "IP360_media_detail");// 存入数据库
	}

}
