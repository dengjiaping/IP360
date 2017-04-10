package com.truthso.ip360.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by djj on 2016/11/10.
 */

public class SpeedView extends TextView{
    private long progress;
    private long lastProgress;
    private boolean isStart;
    public SpeedView(Context context) {
        super(context);
    }

    public SpeedView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public SpeedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setProgress(Long progress){
        if(!isStart){
            handler.sendEmptyMessage(0);
            isStart=true;
        }
        SpeedView.this.progress=progress;
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final  long speed= ( progress-lastProgress)/1024;
            if(speed >= 0){
                SpeedView.this.setText(speed+"k/s");
            }

            SpeedView.this.lastProgress=progress;
            handler.sendEmptyMessageDelayed(0,1000);
        }
    };

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeMessages(0);
    }

}
