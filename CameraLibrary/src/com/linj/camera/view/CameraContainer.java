package com.linj.camera.view;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import com.linj.FileOperateUtil;
import com.linj.camera.view.CameraView.FlashMode;
import com.linj.cameralibrary.R;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;



/**
 * @ClassName: CameraContainer
 * @Description:  相机界面的容器 包含相机绑定的surfaceview、拍照后的临时图片View和聚焦View
 * @author LinJ
 * @date 2014-12-31 上午9:38:52
 *
 */
public class CameraContainer extends RelativeLayout implements CameraOperation{

	public final static String TAG="CameraContainer";

	/** ����󶨵�SurfaceView  */
	private CameraView mCameraView;

	/** �������ɵ�ͼƬ������һ�����Ƶ����½ǵĶ���Ч�������� */
	private TempImageView mTempImageView;

	/** ������Ļʱ��ʾ�ľ۽�ͼ��  */
	private FocusImageView mFocusImageView;

	/** ��ʾ¼����ʱ��TextView  */
	private TextView mRecordingInfoTextView;

	/** ��ʾˮӡͼ��  */
	private ImageView mWaterMarkImageView;

	/** �����Ƭ�ĸ�Ŀ¼ */
	private String mSavePath;

	/** ��Ƭ�ֽ���������  */
	private DataHandler mDataHandler;

	/** ���ռ����ӿڣ����������տ�ʼ�ͽ�����ִ����Ӧ����  */
	private TakePictureListener mListener;

	/** ���ż����϶��� */
	private SeekBar mZoomSeekBar;

	/** ����ִ�ж�ʱ�����Handler����*/
	private Handler mHandler;
	private long mRecordStartTime;
	private SimpleDateFormat mTimeFormat;
	public CameraContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		mHandler=new Handler();
		mTimeFormat=new SimpleDateFormat("mm:ss",Locale.getDefault());
		setOnTouchListener(new TouchListener());
	}

	/**
	 *  ��ʼ���ӿؼ�
	 *  @param context
	 */
	private void initView(Context context) {
		inflate(context, R.layout.cameracontainer, this);
		mCameraView=(CameraView) findViewById(R.id.cameraView);

		mTempImageView=(TempImageView) findViewById(R.id.tempImageView);

		mFocusImageView=(FocusImageView) findViewById(R.id.focusImageView);

		mRecordingInfoTextView=(TextView) findViewById(R.id.recordInfo);

		mWaterMarkImageView=(ImageView) findViewById(R.id.waterMark);

		mZoomSeekBar=(SeekBar) findViewById(R.id.zoomSeekBar);
		//��ȡ��ǰ�����֧�ֵ�������ż���ֵС��0��ʾ��֧�����š���֧������ʱ�������϶�����
		int maxZoom=mCameraView.getMaxZoom();
		if(maxZoom>0){
			mZoomSeekBar.setMax(maxZoom);
			mZoomSeekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
		}
	}


	@Override
	public boolean startRecord(){
		mRecordStartTime=SystemClock.uptimeMillis();
		mRecordingInfoTextView.setVisibility(View.VISIBLE);
		mRecordingInfoTextView.setText("00:00:00");
		if(mCameraView.startRecord()){
			mHandler.postAtTime(recordRunnable, mRecordingInfoTextView, SystemClock.uptimeMillis()+1000);
			return true;
		}else {
			return false;
		}
	}

	Runnable recordRunnable=new Runnable() {
		@Override
		public void run() {

			if(mCameraView.isRecording()){
				long recordTime=SystemClock.uptimeMillis()-mRecordStartTime;
				mRecordingInfoTextView.setText("00:"+mTimeFormat.format(new Date(recordTime)));
				mHandler.postAtTime(this,mRecordingInfoTextView, SystemClock.uptimeMillis()+500);
			}else {
				mRecordingInfoTextView.setVisibility(View.GONE);
			}
		}
	};

	public String stopRecord(TakePictureListener listener){
		mListener=listener;
		return stopRecord();
	}

	@Override
	public String stopRecord(){
		mRecordingInfoTextView.setVisibility(View.GONE);
		String path=mCameraView.stopRecord();
		/*if(thumbnailBitmap!=null){
			mTempImageView.setListener(mListener);
			mTempImageView.isVideo(true);
			mTempImageView.setImageBitmap(thumbnailBitmap);
			mTempImageView.startAnimation(R.anim.tempview_show);
		}*/
		return path;
	}

	/**
	 *  �ı����ģʽ ������ģʽ��¼��ģʽ���л� ����ģʽ�ĳ�ʼ���ż���ͬ
	 *  @param zoom   ���ż���
	 */
	public void switchMode(int zoom){
		mZoomSeekBar.setProgress(zoom);
		mCameraView.setZoom(zoom);
		//�Զ��Խ�
		mCameraView.onFocus(new Point(getWidth()/2, getHeight()/2), autoFocusCallback);
		//����ˮӡ
		mWaterMarkImageView.setVisibility(View.GONE);
	}



	public void setWaterMark(){
		if (mWaterMarkImageView.getVisibility()==View.VISIBLE) {
			mWaterMarkImageView.setVisibility(View.GONE);
		}else {
			mWaterMarkImageView.setVisibility(View.VISIBLE);
		}
	}

	/**
	 *   ǰ�á���������ͷת��
	 */
	@Override
	public void switchCamera(){
		mCameraView.switchCamera();
	}
	//獲取是否是前置攝像頭
	public boolean getIsFrontCamera(){
		return mCameraView.getIsFrontCamera();
	}
	/**
	 *  ��ȡ��ǰ���������
	 *  @return
	 */
	@Override
	public FlashMode getFlashMode() {
		return mCameraView.getFlashMode();
	}

	/**
	 *  �������������
	 *  @param flashMode
	 */
	@Override
	public void setFlashMode(FlashMode flashMode) {
		mCameraView.setFlashMode(flashMode);
	}

	/**
	 * �����ļ�����·��
	 * @param rootPath
	 */
	public void setRootPath(String rootPath){
		this.mSavePath=rootPath;
		mCameraView.setVideoPath(rootPath);
	}



	/**
	 * ���շ���
	 * @param callback
	 */
	public void takePicture(){
		takePicture(pictureCallback,mListener);
	}

	/**
	 * @Description: ���շ���
	 * @param @param listener ���ռ����ӿ�
	 * @return void
	 * @throws
	 */
	public void takePicture(TakePictureListener listener){
		this.mListener=listener;
		takePicture(pictureCallback, mListener);
	}


	@Override
	public void takePicture(PictureCallback callback,
			TakePictureListener listener) {
		mCameraView.takePicture(callback,listener);
	}

	@Override
	public int getMaxZoom() {
		// TODO Auto-generated method stub
		return mCameraView.getMaxZoom();
	}

	@Override
	public void setZoom(int zoom) {
		// TODO Auto-generated method stub
		mCameraView.setZoom(zoom);
	}

	@Override
	public int getZoom() {
		// TODO Auto-generated method stub
		return mCameraView.getZoom();
	}

	private final OnSeekBarChangeListener onSeekBarChangeListener=new OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			mCameraView.setZoom(progress);
			mHandler.removeCallbacksAndMessages(mZoomSeekBar);
			//ZOOMģʽ�� �ڽ������������seekbar ����tokenΪmZoomSeekBar�������������ʱ�Ƴ�ǰһ����ʱ����
			mHandler.postAtTime(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					mZoomSeekBar.setVisibility(View.GONE);
				}
			}, mZoomSeekBar,SystemClock.uptimeMillis()+2000);
		}



		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}



		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}
	};

	private final AutoFocusCallback autoFocusCallback=new AutoFocusCallback() {

		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			if (success) {
				mFocusImageView.onFocusSuccess();
			}else {
				mFocusImageView.onFocusFailed();

			}
		}
	};

	private final PictureCallback pictureCallback=new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			if(mSavePath==null) throw new RuntimeException("mSavePath is null");
			if(mDataHandler==null) mDataHandler=new DataHandler();
			mDataHandler.setMaxSize(200);
			String filePath=mDataHandler.save(data);
			//���´�Ԥ��ͼ��������һ�ε�����׼��
			camera.startPreview();
			if(mListener!=null) mListener.onTakePictureEnd(filePath);
		}
	};

	 /**
     * ��ͼƬ����ת�Ƕ���Ϊ0  ���˷������Խ��ĳЩ�������պ�ͼ�񣬳�������ת���
     *
     * @Title: setPictureDegreeZero
     * @param path
     * @return void
     * @date 2012-12-10 ����10:54:46
     */
    private void setPictureDegreeZero(String path,int degress) {
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            // ����ͼƬ����ת�Ƕȣ������䲻��ת������Ҳ������������ת�ĽǶȣ����Դ�ֵ��ȥ��
            // ������ת90�ȣ���ֵExifInterface.ORIENTATION_ROTATE_90����Ҫ�����ֵת��ΪString���͵�
           // exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "no");
            exifInterface.setAttribute(String.valueOf(ExifInterface.ORIENTATION_ROTATE_90), String.valueOf(degress));
            exifInterface.saveAttributes();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * ��ȡͼƬ����ת�Ƕ�
     * @Title: readPictureDegree
     * @param path
     * @return
     * @return int
     * @date 2012-12-4 ����9:22:33
     */
    private  int readPictureDegree(String path) {
        int degree  = 0;
        try {
                ExifInterface exifInterface = new ExifInterface(path);

                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                default:
                   degree = 0;
                }
        } catch (Exception e) {
                e.printStackTrace();
        }
        return degree;
    }

	private final class TouchListener implements OnTouchListener {

		/** ��¼��������Ƭģʽ���ǷŴ���С��Ƭģʽ */

		private static final int MODE_INIT = 0;
		/** �Ŵ���С��Ƭģʽ */
		private static final int MODE_ZOOM = 1;
		private int mode = MODE_INIT;// ��ʼ״̬

		/** ���ڼ�¼����ͼƬ�ƶ�������λ�� */

		private float startDis;


		@Override
		public boolean onTouch(View v, MotionEvent event) {
			/** ͨ�������㱣������λ MotionEvent.ACTION_MASK = 255 */
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			// ��ָѹ����Ļ
			case MotionEvent.ACTION_DOWN:
				mode = MODE_INIT;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				//���mZoomSeekBarΪnull ��ʾ���豸��֧������ ֱ����������mode Moveָ��Ҳ�޷�ִ��
				if(mZoomSeekBar==null) return true;
				//�Ƴ�token����ΪmZoomSeekBar����ʱ����
				mHandler.removeCallbacksAndMessages(mZoomSeekBar);
				mZoomSeekBar.setVisibility(View.VISIBLE);

				mode = MODE_ZOOM;
				/** ����������ָ��ľ��� */
				startDis = distance(event);
				break;
			case MotionEvent.ACTION_MOVE:
				if (mode == MODE_ZOOM) {
					//ֻ��ͬʱ�����������ʱ���ִ��
					if(event.getPointerCount()<2) return true;
					float endDis = distance(event);// ��������
					//ÿ�仯10f zoom��1
					int scale=(int) ((endDis-startDis)/10f);
					if(scale>=1||scale<=-1){
						int zoom=mCameraView.getZoom()+scale;
						//zoom���ܳ�����Χ
						if(zoom>mCameraView.getMaxZoom()) zoom=mCameraView.getMaxZoom();
						if(zoom<0) zoom=0;
						mCameraView.setZoom(zoom);
						mZoomSeekBar.setProgress(zoom);
						//�����һ�εľ�����Ϊ��ǰ����
						startDis=endDis;
					}
				}
				break;
				// ��ָ�뿪��Ļ
			case MotionEvent.ACTION_UP:
				if(mode!=MODE_ZOOM){
					//���þ۽�
					Point point=new Point((int)event.getX(), (int)event.getY());
					mCameraView.onFocus(point,autoFocusCallback);
					mFocusImageView.startFocus(point);
				}else {
					//ZOOMģʽ�� �ڽ������������seekbar ����tokenΪmZoomSeekBar�������������ʱ�Ƴ�ǰһ����ʱ����
					mHandler.postAtTime(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							mZoomSeekBar.setVisibility(View.GONE);
						}
					}, mZoomSeekBar,SystemClock.uptimeMillis()+2000);
				}
				break;
			}
			return true;
		}
		/** ����������ָ��ľ��� */
		private float distance(MotionEvent event) {
			float dx = event.getX(1) - event.getX(0);
			float dy = event.getY(1) - event.getY(0);
			/** ʹ�ù��ɶ���������֮��ľ��� */
			return (float) Math.sqrt(dx * dx + dy * dy);
		}

	}

	/**
	 * ���շ��ص�byte���ݴ�����
	 * @author linj
	 *
	 */
	private final class DataHandler{
		/** ��ͼ���·��  */
		//private String mThumbnailFolder;
		/** Сͼ���·�� */
		//private String mImageFolder;
		/** ѹ�����ͼƬ���ֵ ��λKB*/
		private int maxSize=200;

		public DataHandler(){
			//mImageFolder=FileOperateUtil.getFolderPath(getContext(), FileOperateUtil.TYPE_IMAGE, mSavePath);
		//	mThumbnailFolder=FileOperateUtil.getFolderPath(getContext(),  FileOperateUtil.TYPE_THUMBNAIL, mSavePath);
			File folder=new File(mSavePath);
			if(!folder.exists()){
				folder.mkdirs();
			}
			/*folder=new File(mThumbnailFolder);
			if(!folder.exists()){
				folder.mkdirs();
			}*/
		}

		/**
		 * ����ͼƬ
		 * @param ������ص��ļ���
		 * @return ���������ɵ�����ͼ
		 */
		public String save(byte[] data)  {
			if(data!=null){
				//�����µ��ļ���
				String imgName=FileOperateUtil.createFileNmae(".jpg");
				File folder=new File(mSavePath+"/photo");
				if(!folder.isDirectory()){
					folder.delete();
				}
				if(!folder.exists()){
					folder.mkdirs();
					Log.e(TAG, "folder");
				}
			//	String imagePath=mSavePath+File.separator+"photo"+File.separator+imgName;
				Log.e(TAG, "exists"+folder.exists());
				File file=new File(mSavePath+File.separator+"photo",imgName);
				if(!file.exists()){
					Log.e(TAG, "createNewFile");
					try {
						file.createNewFile();
					} catch (IOException e) {
						Log.e(TAG, "IOException"+e.getMessage());
						e.printStackTrace();
					}
				}
				try{
					//��ͼƬ��ͼ
					FileOutputStream fos=new FileOutputStream(file);
					fos.write(data);
					fos.flush();
					fos.close();
					return file.getAbsolutePath();
				}catch(Exception e){
					Log.e(TAG, e.toString());
					Toast.makeText(getContext(), "解析相机返回流失败", Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(getContext(), "拍照失败，请重试", Toast.LENGTH_SHORT).show();
			}
			return null;
		}

		private Bitmap getBitmapWithWaterMark(Bitmap bm) {
			// TODO Auto-generated method stub
			if(!(mWaterMarkImageView.getVisibility()==View.VISIBLE)){
				return bm;
			}
			Drawable mark=mWaterMarkImageView.getDrawable();
			Bitmap wBitmap=drawableToBitmap(mark);
			int w = bm.getWidth();

			int h = bm.getHeight();

			int ww = wBitmap.getWidth();

			int wh = wBitmap.getHeight();
			Bitmap newb = Bitmap.createBitmap( w, h, Config.ARGB_8888 );
			Canvas canvas=new Canvas(newb);
			//draw src into

			canvas.drawBitmap( bm, 0, 0, null );//�� 0��0���꿪ʼ����src
			canvas.drawBitmap( wBitmap, w - ww + 5, h - wh + 5, null );//��src�����½ǻ���ˮӡ
			//save all clip

			canvas.save( Canvas.ALL_SAVE_FLAG );//����

			//store

			canvas.restore();//�洢
			bm.recycle();
			bm=null;
			wBitmap.recycle();
			wBitmap=null;
			return newb;

		}
		public  Bitmap drawableToBitmap(Drawable drawable) {
			Bitmap bitmap = Bitmap.createBitmap(
					drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight(),
					drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
							: Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
			drawable.draw(canvas);
			return bitmap;
		}
		/**
		 * ͼƬѹ������
		 * @param bitmap ͼƬ�ļ�
		 * @param max �ļ���С���ֵ
		 * @return ѹ������ֽ���
		 * @throws Exception
		 */
		public ByteArrayOutputStream compress(Bitmap bitmap){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// ����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��
			int options = 99;
			while ( baos.toByteArray().length / 1024 > maxSize) { // ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���ڼ���ѹ��
				options -= 3;// ÿ�ζ�����10
				//ѹ����С��0������ѹ��
				if (options<0) {
					break;
				}
				Log.i(TAG,baos.toByteArray().length / 1024+"");
				baos.reset();// ����baos�����baos
				bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// ����ѹ��options%����ѹ��������ݴ�ŵ�baos��
			}
			return baos;
		}

		public void setMaxSize(int maxSize) {
			this.maxSize = maxSize;
		}
	}

	/**
	 * @ClassName: TakePictureListener
	 * @Description:  ���ռ����ӿڣ����������տ�ʼ�ͽ�����ִ����Ӧ����
	 * @author LinJ
	 * @date 2014-12-31 ����9:50:33
	 *
	 */
	public static interface TakePictureListener{
		/**
		 *���ս���ִ�еĶ������÷�������onPictureTaken����ִ�к󴥷�
		 *  @param bm �������ɵ�ͼƬ
		 */
		public void onTakePictureEnd(String filePath);

		/**  ��ʱͼƬ���������󴥷�
		 * @param bm �������ɵ�ͼƬ
		 * @param isVideo true����ǰΪ¼������ͼ false:Ϊ��������ͼ
		 * */
		public void onAnimtionEnd(Bitmap bm,boolean isVideo);
	}


	/**
	 * dipתpx
	 *  @param dipValue
	 *  @return
	 */
	private  int dip2px(float dipValue){
		final float scale = getResources().getDisplayMetrics().density;
		return (int)(dipValue * scale + 0.5f);
	}
}