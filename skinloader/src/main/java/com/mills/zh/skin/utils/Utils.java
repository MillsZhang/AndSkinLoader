package com.mills.zh.skin.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by zhangmd on 2018/12/28.
 */

public class Utils {

    public static String getPackageName(Context context, String apkPath){
        PackageManager manager = context.getPackageManager();
        PackageInfo info = manager.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        if(info != null){
            return info.packageName;
        }
        return null;
    }
}
