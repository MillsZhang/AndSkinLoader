package com.mills.zh.skin.attr;

import android.view.View;

import com.mills.zh.skin.ResourceManager;

/**
 * Created by zhangmd on 2019/1/3.
 */

public abstract class SkinAttrType {

    private String mAttrType;

    public SkinAttrType(String attrType){
        mAttrType = attrType;
    }

    public String getAttrType() {
        return mAttrType;
    }

    public abstract void apply(View view, String resName, ResourceManager resourceManager);



}
