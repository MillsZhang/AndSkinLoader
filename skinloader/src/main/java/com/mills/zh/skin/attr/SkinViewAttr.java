package com.mills.zh.skin.attr;

import android.view.View;

/**
 * Created by zhangmd on 2018/12/28.
 */

public class SkinViewAttr {
    public String mResName;
    public SkinViewAttrType mAttrType;

    public SkinViewAttr(String resName, SkinViewAttrType attrType) {
        mResName = resName;
        mAttrType = attrType;
    }

    public void apply(View view){
        mAttrType.apply(view, mResName);
    }
}
