package com.mills.zh.skin;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

/**
 * Created by zhangmd on 2018/12/28.
 */

public class ResourceManager {

    private static final String RES_TYPE_DRAWABLE = "drawable";
    private static final String RES_TYPE_COLOR = "color";
    private static final String RES_TYPE_STRING = "string";

    private Resources mResources;
    private String mSkinPkgName;
    private String mSkinResSuffix;

    public ResourceManager(Resources resources, String pkgname, String suffix){
        mResources = resources;
        mSkinPkgName = pkgname;
        mSkinResSuffix = suffix;
    }

    private String appendSkinResSuffix(String name){
        if (TextUtils.isEmpty(mSkinResSuffix)){
            return name;
        }
        return name + "_" + mSkinResSuffix;
    }

    public Drawable getDrawable(String name){
        name = appendSkinResSuffix(name);

        try {
            return mResources.getDrawable(mResources.getIdentifier(name, RES_TYPE_DRAWABLE, mSkinPkgName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getColor(String name) throws Exception{
        name = appendSkinResSuffix(name);
        return mResources.getColor(mResources.getIdentifier(name, RES_TYPE_COLOR, mSkinPkgName));
    }

    public ColorStateList getColorStateList(String name){
        name = appendSkinResSuffix(name);
        try {
            return mResources.getColorStateList(mResources.getIdentifier(name, RES_TYPE_COLOR, mSkinPkgName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getString(String name){
        name = appendSkinResSuffix(name);
        try {
            return mResources.getString(mResources.getIdentifier(name, RES_TYPE_STRING, mSkinPkgName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
