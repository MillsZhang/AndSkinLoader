package com.mills.zh.example;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.mills.zh.skin.SkinManager;
import com.mills.zh.skin.SkinManager.SkinSwitchCallback;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private String skinpluginpath = Environment.getExternalStorageDirectory() + File.separator + "skintest.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SkinManager.getInstance().register(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().unregister(this);
    }

    int idx = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_MENU){

            idx = idx % 4;
            if(idx == 0){
                SkinManager.getInstance().switchSkinInner("green");
            } else if(idx == 1){
                SkinManager.getInstance().switchSkinInner("blue");
            } else if(idx == 2){
                SkinManager.getInstance().switchSkin(
                        "com.mills.zh.skinplugin",
                        skinpluginpath,
                        "test",
                        new SkinSwitchCallback() {
                            @Override
                            public void onStart() {
                                Log.d("test", "skin switch start");
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.d("test", "skin switch error:"+e);
                            }

                            @Override
                            public void onComplete() {
                                Log.d("test", "skin switch complete");
                            }
                        });
            } else {
                SkinManager.getInstance().disableSkinPlugin();
            }

            idx++;

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
