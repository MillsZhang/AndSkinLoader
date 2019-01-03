package com.mills.zh.example;

import android.app.Application;

import com.mills.zh.skin.SkinManager;

/**
 * Created by zhangmd on 2018/12/29.
 */

public class SkinApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.setDebug(true);
        SkinManager.getInstance().init(this);
    }
}
