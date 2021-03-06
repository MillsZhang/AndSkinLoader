package com.mills.zh.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.mills.zh.skin.SkinManager;
import com.mills.zh.skin.SkinManager.SkinSwitchCallback;

public class MainActivity extends AppCompatActivity {

    private ViewGroup root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SkinManager.getInstance().register(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        root = (ViewGroup) findViewById(R.id.root);

        root.postDelayed(new Runnable() {
            @Override
            public void run() {
                Button btn = new Button(MainActivity.this);
                btn.setText(R.string.btn_txt_str);
                btn.setTextColor(getResources().getColor(R.color.btn_txt_color));
                RelativeLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);

                btn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.this.startActivity(new Intent(MainActivity.this, SecondActivity.class));
                    }
                });
                root.addView(btn, params);

                btn.requestFocus();

                SkinManager.getInstance().register(MainActivity.this, btn, "text:btn_txt_str|textColor:btn_txt_color");
            }
        }, 5000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().unregister(this);
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
}
