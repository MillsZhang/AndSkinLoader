package com.mills.zh.skin.attr;

import android.view.View;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by zhangmd on 2018/12/28.
 */

public class SkinView {

    public WeakReference<View> mViewRef;
    public List<SkinViewAttr> mViewAttrs;

    public SkinView(View view, List<SkinViewAttr> attrs){
        if(view == null || attrs == null || attrs.size() <= 0){
            return;
        }
        this.mViewRef = new WeakReference<View>(view);
        this.mViewAttrs = attrs;
    }

    public void apply(){
        if(mViewRef != null && mViewRef.get() != null){
            for(SkinViewAttr attr : mViewAttrs){
                attr.apply(mViewRef.get());
            }
        }
    }
}
