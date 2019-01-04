package com.mills.zh.skin.attr;

import android.view.View;

import com.mills.zh.skin.ResourceManager;

/**
 * Created by zhangmd on 2018/12/28.
 */

public class SkinViewAttr {
    public String mResName;
    public SkinAttrType mAttrType;

    public SkinViewAttr(String resName, SkinAttrType attrType) {
        mResName = resName;
        mAttrType = attrType;
    }

    public void apply(View view, ResourceManager resourceManager){
        mAttrType.apply(view, mResName, resourceManager);
    }
}
