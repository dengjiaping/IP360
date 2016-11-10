package com.truthso.ip360.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by djj on 2016/11/10.
 */

public class SpeedView extends TextView{

    private Timer timer=new Timer();
    private long progress;
    private long lastProgress;
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
        SpeedView.this.progress=progress;
    }

    public TimerTask task=new TimerTask() {
        @Override
        public void run() {
        final  long speed= ( progress-lastProgress)/1024;
            SpeedView.this.post(new Runnable() {
                @Override
                public void run() {
                    SpeedView.this.setText(speed+"k/s");
                }
            });
            SpeedView.this.lastProgress=progress;
        }
    };

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        timer.schedule(task,0,1000);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        timer.cancel();
    }
}
