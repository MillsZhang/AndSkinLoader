package com.mills.zh.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;

import com.mills.zh.skin.SkinManager;
import com.mills.zh.skin.SkinManager.SkinSwitchCallback;

/**
 * Created by zhangmd on 2019/1/2.
 */

public class SecondActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SkinManager.getInstance().register(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_second);

        FragmentManager manager = getSupportFragmentManager();

        Fragment fragment = manager.findFragmentByTag("secondfragment");
        if(fragment == null){
            fragment = new SecondFragment();

            manager.beginTransaction().replace(R.id.fragment_container, fragment).commitAllowingStateLoss();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_MENU){

            Consts.skinidx = Consts.skinidx % 4;
            if(Consts.skinidx == 0){
                SkinManager.getInstance().switchSkinInner("green");
            } else if(Consts.skinidx == 1){
                SkinManager.getInstance().switchSkinInner("blue");
            } else if(Consts.skinidx == 2){
                SkinManager.getInstance().switchSkin(
                        "com.mills.zh.skinplugin",
                        Consts.skinpluginpath,
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
                SkinManager.getInstance().restoreSkin();
            }

            Consts.skinidx++;

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().unregister(this);
    }
}
