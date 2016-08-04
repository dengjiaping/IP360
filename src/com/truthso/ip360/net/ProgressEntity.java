package com.truthso.ip360.net;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.http.entity.ByteArrayEntity;

/**
 * 
 * @author wsx_summer
 * @date 创建时间：2016年5月26日下午3:26:33
 * @version 1.0  
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class ProgressEntity extends ByteArrayEntity{
	private ProgressListener progressListener;

	  public ProgressEntity(byte[] b, ProgressListener progressListener)
	  {
	    super(b);
	    this.progressListener = progressListener;
	  }

	  public void writeTo(OutputStream outstream) throws IOException
	  {
	    super.writeTo(new ProgressFilterOutputStream(outstream, this.progressListener));
	  }
	  class ProgressFilterOutputStream extends FilterOutputStream {
	    private ProgressEntity.ProgressListener progressListener;
	    private int progress;

	    public ProgressFilterOutputStream(OutputStream out, ProgressEntity.ProgressListener progressListener) {
	      super(out);
	      this.progressListener = progressListener;
	      this.progress = 0;
	    }

	    public void write(byte[] buffer, int offset, int length) throws IOException
	    {
	      super.write(buffer, offset, length);
	      this.progress += length;
	      if (this.progressListener != null)
	        this.progressListener.onProgress(this.progress);
	    }

	    public void write(int oneByte) throws IOException
	    {
	      super.write(oneByte);
	      this.progress += 1;
	      if (this.progressListener != null)
	        this.progressListener.onProgress(this.progress);
	    }
	  }

	  public static abstract interface ProgressListener
	  {
	    public abstract void onProgress(int paramInt);
	  }
}
