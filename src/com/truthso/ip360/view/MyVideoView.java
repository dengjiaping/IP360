package com.truthso.ip360.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class MyVideoView extends VideoView {

    private int mHeight;
    private int mWidth;

    public MyVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public MyVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public MyVideoView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }


    public void setMeasure(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
      /*  int width=getDefaultSize(getWidth(), widthMeasureSpec);
		int height=getDefaultSize(getHeight(), heightMeasureSpec);*/
        // 默认高度，为了自动获取到focus
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = width;
        // 这个之前是默认的拉伸图像
        if (this.mWidth > 0 && this.mHeight > 0) {
            width = this.mWidth;
            height = this.mHeight;
        }
        setMeasuredDimension(width, height);
    }

}
