package com.mills.zh.skin;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.mills.zh.skin.utils.Logger;

/**
 * Created by zhangmd on 2018/12/28.
 */

public class ResourceManager {
    private static final String TAG = "ResourceManager";

    private static final String RES_TYPE_DRAWABLE = "drawable";
    private static final String RES_TYPE_COLOR = "color";
    private static final String RES_TYPE_STRING = "string";
    private static final String RES_TYPE_DIMENSION = "dimen";


    private Resources mResources;
    private String mSkinPkgName;
    private String mSkinResSuffix;

    public ResourceManager(Resources resources, String pkgname, String suffix){
        mResources = resources;
        mSkinPkgName = pkgname;
        mSkinResSuffix = suffix;
    }

    public void setSkinResSuffix(String suffix){
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
            int id = mResources.getIdentifier(name, RES_TYPE_DRAWABLE, mSkinPkgName);
            if(id != 0){
                return mResources.getDrawable(id);
            }
            Logger.e(TAG, "getDrawable not found id:"+name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getColor(String name) {
        name = appendSkinResSuffix(name);
        try {
            int id = mResources.getIdentifier(name, RES_TYPE_COLOR, mSkinPkgName);
            if(id != 0){
                return mResources.getColor(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new NotFoundException("getColor not found id:"+name);
    }

    public ColorStateList getColorStateList(String name){
        name = appendSkinResSuffix(name);
        try {
            int id = mResources.getIdentifier(name, RES_TYPE_COLOR, mSkinPkgName);
            if(id != 0){
                return mResources.getColorStateList(id);
            }
            Logger.e(TAG, "getColorStateList not found id:"+name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getString(String name){
        name = appendSkinResSuffix(name);
        try {
            int id = mResources.getIdentifier(name, RES_TYPE_STRING, mSkinPkgName);
            if(id != 0){
                return mResources.getString(id);
            }
            Logger.e(TAG, "getString not found id:"+name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getDimension(String name){
        name = appendSkinResSuffix(name);
        try {
            int id = mResources.getIdentifier(name, RES_TYPE_DIMENSION, mSkinPkgName);
            if(id != 0){
                return mResources.getDimensionPixelOffset(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new NotFoundException("getDimension not found id:"+name);
    }
}
