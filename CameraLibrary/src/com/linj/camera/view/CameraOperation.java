package com.linj.camera.view;

import android.graphics.Bitmap;
import android.hardware.Camera.PictureCallback;

import com.linj.camera.view.CameraContainer.TakePictureListener;
import com.linj.camera.view.CameraView.FlashMode;

/** 
 * @ClassName: CameraOperation 
 * @Description:?????????????????CameraContainer??CameraView?????
 * @author LinJ
 * @date 2015-1-26 ????2:41:31 
 *  
 */
public interface CameraOperation {
	/**  
	 *  ??????
	 *  @return  ???????????
	 */
	public boolean startRecord();

	/**  
	 *  ?????
	 *  @return ????????
	 */
	public String stopRecord();
	/**  
	 *   ?��???��???????
	 */
	public void switchCamera();
	/**  
	 *  ?????????????
	 *  @return   
	 */
	public FlashMode getFlashMode();
	/**  
	 *  ???????????
	 *  @param flashMode   
	 */
	public void setFlashMode(FlashMode flashMode);
	/**  
	 *  ????
	 *  @param callback ?????????? 
	 *  @param listener ???????????????  
	 */
	public void takePicture(PictureCallback callback,TakePictureListener listener);
	/**  
	 *  ?????????????
	 *  @return   
	 */
	public int getMaxZoom();
	/**  
	 *  ?????????????
	 *  @param zoom   
	 */
	public void setZoom(int zoom);
	/**  
	 *  ?????????????
	 *  @return   
	 */
	public int getZoom();
}
