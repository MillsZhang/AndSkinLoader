package com.mills.zh.skin.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhangmd on 2018/12/28.
 */

public class SharePrefs {

    private static final String NAME = "skin_res_pref";
    private static final String KEY_PKGNAME = "skin_res_pkgname";
    private static final String KEY_PATH = "skin_res_path";
    private static final String KEY_SUFFIX = "skin_res_suffix";

    private SharedPreferences mPrefs;

    public SharePrefs(Context context){
        mPrefs = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public String getSkinResPkgName(){
        return mPrefs.getString(KEY_PKGNAME, "");
    }

    public String getSkinResPath(){
        return mPrefs.getString(KEY_PATH, "");
    }

    public String getSkinResSuffix(){
        return mPrefs.getString(KEY_SUFFIX, "");
    }

    public void saveSkinResInfo(String pkgname, String path, String suffix){
        mPrefs.edit()
                .putString(KEY_PKGNAME, pkgname)
                .putString(KEY_PATH, path)
                .putString(KEY_SUFFIX, suffix)
                .commit();
    }

    public void clear(){
        mPrefs.edit().clear().commit();
    }
}
