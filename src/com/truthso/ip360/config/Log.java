package com.truthso.ip360.config;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.truthso.ip360.activity.BuildConfig;

import android.annotation.SuppressLint;

/**
 * 封装的输出日志的类
 * @author wsx_summer
 * @date 创建时间：2016年5月27日下午2:32:25
 * @version 1.0  
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class Log {
    /**
     * 是否允许输出日志
     */
    public static final boolean ENABLE_LOG = BuildConfig.DEBUG;

    /**
     * 日志标志
     */
    public static final String TAG = "ic-";

    /**
     * 包名
     */
    public static final String PACKAGE_NAME = "com.truthso.ip360";

    protected Log() {
    }

    public static void o(String message) {
        if (!ENABLE_LOG) {
            return;
        }
        oTime(TAG, "<" + getCallMethodName() + "> " + message);
    }

    public static void d(String message) {
        if (!ENABLE_LOG) {
            return;
        }
        if (android.util.Log.isLoggable(TAG, android.util.Log.DEBUG)) {
            dTime(TAG, "<" + getCallMethodName() + "> " + message);
        }
    }

    public static void d(String variableName, boolean value) {
        if (!ENABLE_LOG) {
            return;
        }
        if (android.util.Log.isLoggable(TAG, android.util.Log.DEBUG)) {
            dTime(TAG, "<" + getCallMethodName() + "> " + variableName + ":"
                    + ((value) ? "true" : "false"));
        }
    }

    public static void v(String message) {
        if (!ENABLE_LOG) {
            return;
        }
        if (android.util.Log.isLoggable(TAG, android.util.Log.VERBOSE)) {
            vTime(TAG, "<" + getCallMethodName() + "> " + message);
        }
    }

    public static void e(String message) {
        if (!ENABLE_LOG) {
            return;
        }
        if (android.util.Log.isLoggable(TAG, android.util.Log.ERROR)) {
            eTime(TAG, "<" + getCallMethodName() + "> " + message);
        }
    }

    public static void e(Throwable t) {
        if (!ENABLE_LOG) {
            return;
        }
        if (android.util.Log.isLoggable(TAG, android.util.Log.ERROR)) {
            eTime(TAG, "<" + getCallMethodName() + "> ", t);
        }
    }

    public static void i(String message) {
        if (!ENABLE_LOG) {
            return;
        }
        if (android.util.Log.isLoggable(TAG, android.util.Log.INFO)) {
            iTime(TAG, "<" + getCallMethodName() + "> " + message);
        }
    }

    public static void w(String message) {
        if (!ENABLE_LOG) {
            return;
        }
        if (android.util.Log.isLoggable(TAG, android.util.Log.WARN)) {
            wTime(TAG, "<" + getCallMethodName() + "> " + message);
        }
    }

    public static void trace() {
        if (!ENABLE_LOG) {
            return;
        }
        if (android.util.Log.isLoggable(TAG, android.util.Log.INFO)) {
            iTime(TAG, "<" + getCallMethodName() + ">");
        }
    }

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat SDF = new SimpleDateFormat("HH:mm:ss");

    private static void oTime(String tag, String message) {
        Date d = new Date();
        System.out.println(tag + ":[" + SDF.format(d) + "] " + message);
    }

    private static void dTime(String tag, String message) {
        Date d = new Date();
        android.util.Log.d(tag, "[" + SDF.format(d) + "] " + message);
    }

    private static void vTime(String tag, String message) {
        Date d = new Date();
        android.util.Log.v(tag, "[" + SDF.format(d) + "] " + message);
    }

    private static void eTime(String tag, String message) {
        Date d = new Date();
        android.util.Log.e(tag, "[" + SDF.format(d) + "] " + message);
    }

    private static void eTime(String tag, String message, Throwable t) {
        Date d = new Date();
        android.util.Log.e(tag, "[" + SDF.format(d) + "] " + message, t);
    }

    private static void iTime(String tag, String message) {
        Date d = new Date();
        android.util.Log.i(tag, "[" + SDF.format(d) + "] " + message);
    }

    private static void wTime(String tag, String message) {
        Date d = new Date();
        android.util.Log.v(tag, "[" + SDF.format(d) + "] " + message);
    }

    private static String getCallMethodName() {
        Throwable t = new Throwable();
        StackTraceElement element = t.getStackTrace()[2];
        String className = element.getClassName().replace(PACKAGE_NAME, "");
        String methodName = element.getMethodName();
        return className + "/" + methodName;
    }

}
