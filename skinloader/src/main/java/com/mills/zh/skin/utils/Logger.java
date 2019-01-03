package com.mills.zh.skin.utils;

import android.util.Log;

import com.mills.zh.skin.Consts;

/**
 * Created by zhangmd on 2019/1/2.
 */

public class Logger {
    private static final String TAG = "Skin";

    public static void d(String tag, String msg) {
        if (Consts.DEBUG) Log.i(TAG, "[" + tag + "]" + msg);
    }

    public static void i(String tag, String msg) {
        Log.i(TAG, "[" + tag + "]" + msg);
    }

    public static void w(String tag, String msg) {
        Log.e(TAG, "[" + tag + "]" + msg);
    }

    public static void e(String tag, String msg) {
        Log.e(TAG, "[" + tag + "]" + msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        Log.e(TAG, "[" + tag + "]" + msg, tr);
    }
}
