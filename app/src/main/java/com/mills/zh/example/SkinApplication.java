package com.mills.zh.example;

import android.app.Application;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.mills.zh.skin.ResourceManager;
import com.mills.zh.skin.SkinManager;
import com.mills.zh.skin.attr.SkinAttrType;
import com.mills.zh.skin.utils.Logger;

/**
 * Created by zhangmd on 2018/12/29.
 */

public class SkinApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.setDebug(true);
        SkinManager.addSkinAttribute(new SkinAttrType("textSize") {
            @Override
            public void apply(View view, String resName, ResourceManager resourceManager) {
                if(view instanceof TextView){
                    try {
                        int dimen = resourceManager.getDimension(resName);
                        ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, dimen);
                    } catch (Exception e) {
                        Logger.e("ResourceManager", e.getMessage());
                    }
                }
            }
        });
        SkinManager.getInstance().init(this);

    }
}
