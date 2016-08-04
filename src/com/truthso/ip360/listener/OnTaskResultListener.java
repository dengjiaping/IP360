package com.truthso.ip360.listener;

/**
 * 异步任务执行完后回调接口
 * @author wsx_summer
 * @date 创建时间：2016年5月27日下午5:57:03
 * @version 1.0  
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public interface OnTaskResultListener {
    /**
     * 回调函数
     * 
     * @param success 是否成功
     * @param error 错误信息，[成功的时候错误信息为空]
     * @param result 获取到的结果
     */
    void onResult(final boolean success, final String error, final Object result);

}
