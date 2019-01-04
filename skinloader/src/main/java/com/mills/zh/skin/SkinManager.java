package com.mills.zh.skin;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;

import com.mills.zh.skin.attr.SkinAttrType;
import com.mills.zh.skin.attr.SkinAttrTypes;
import com.mills.zh.skin.attr.SkinView;
import com.mills.zh.skin.attr.SkinViewAttr;
import com.mills.zh.skin.utils.Logger;
import com.mills.zh.skin.utils.SharePrefs;
import com.mills.zh.skin.utils.Utils;

import java.io.File;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

/**
 * Created by zhangmd on 2018/12/28.
 */

public class SkinManager {
    private static final String TAG = "SkinManager";

    private Context mContext;
    private SharePrefs mSharePrefs;
    private Resources mPluginResources;
    private ResourceManager mPluginResManager;
    private ResourceManager mSysResManager;

    private String mSkinResPkgName;
    private String mSkinResPath;
    private String mSkinResSuffix;

    private boolean mUseSkinPlugin;

    private HashMap<Integer, List<SkinView>> mSkinMap;

    private SkinManager(){
        mSkinMap = new HashMap<>();
    }

    private static class SingletonHolder {
        static SkinManager sInstance = new SkinManager();
    }

    public static SkinManager getInstance(){
        return SingletonHolder.sInstance;
    }

    public static void setDebug(boolean debug){
        Consts.DEBUG = debug;
    }

    public void init(Application application){
        Logger.i(TAG, "init");

        SkinInflaterFactory.setFactory(application);

        mContext = application.getApplicationContext();
        mSharePrefs = new SharePrefs(mContext);

        String pkgname = mSharePrefs.getSkinResPkgName();
        String path = mSharePrefs.getSkinResPath();
        String suffix = mSharePrefs.getSkinResSuffix();

        if(checkSkinResValid(pkgname, path, suffix)){
            // 外部插件资源
            try {
                loadSkinPlugin(pkgname, path, suffix);
            } catch (Exception e) {
                e.printStackTrace();
                Logger.e(TAG, "load saved skin res error!");
                mSharePrefs.clear();
            }
        } else {
            // 内部皮肤资源
            mSkinResSuffix = suffix;
        }
    }

    private boolean checkSkinResValid(String pkgname, String path, String suffix){
        if(TextUtils.isEmpty(pkgname)
                || TextUtils.isEmpty(path)
                || TextUtils.isEmpty(suffix)){
            Logger.w(TAG, "external skin res info is incompletely!");
            return false;
        }

        File file = new File(path);
        if(!file.exists() || !file.isFile()){
            Logger.e(TAG, "skin res file is not exist!");
            return false;
        }

        if(!pkgname.equals(Utils.getPackageName(mContext, path))){
            Logger.e(TAG, "skin res file contains error package name!");
            return false;
        }

        return true;
    }

    public void loadSkinPlugin(String pkgname, String path, String suffix) throws Exception{
        Logger.i(TAG, "loadSkinPlugin pkgname:"+pkgname+", path:"+path+", suffix:"+suffix);
        long current = System.currentTimeMillis();
        AssetManager assetManager = AssetManager.class.newInstance();
        Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
        addAssetPath.invoke(assetManager, path);

        mPluginResources = new Resources(assetManager,
                mContext.getResources().getDisplayMetrics(),
                mContext.getResources().getConfiguration());
        mPluginResManager = new ResourceManager(mPluginResources, pkgname, suffix);

        mSkinResPkgName = pkgname;
        mSkinResPath = path;
        mSkinResSuffix = suffix;

        mUseSkinPlugin = true;
        Logger.i(TAG, "loadSkinPlugin cost time(ms):"+(System.currentTimeMillis() - current));
    }

    private void clearSkinPluginInfo(){
        mUseSkinPlugin = false;
        mSkinResPkgName = null;
        mSkinResPath = null;
        mSkinResSuffix = null;
        mPluginResources = null;
        mPluginResManager = null;
        mSharePrefs.clear();
    }

    public void restoreSkin(){
        Logger.d(TAG, "restoreSkin");

        clearSkinPluginInfo();

        try {
            doSkinSwitch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected boolean needSwitchSkin(){
        return mUseSkinPlugin || !TextUtils.isEmpty(mSkinResSuffix);
    }

    public void switchSkinInner(String resSuffix){
        switchSkinInner(resSuffix, null);
    }

    public void switchSkinInner(String resSuffix, SkinSwitchCallback callback){
        Logger.d(TAG, "switchSkinInner resource suffix:"+resSuffix);

        if(TextUtils.isEmpty(resSuffix)){
            throw new InvalidParameterException("switchSkinInner: skin res suffix is null!");
        }

        if(TextUtils.equals(mSkinResSuffix, resSuffix)){
            Logger.w(TAG, "switchSkinInner: skin not change.");
            return;
        }

        if(callback != null){
            callback.onStart();
        }

        clearSkinPluginInfo();

        mSkinResSuffix = resSuffix;
        mSharePrefs.saveSkinResInfo(null, null, mSkinResSuffix);

        try {
            doSkinSwitch();
        } catch (Exception e) {
            e.printStackTrace();

            if(callback != null){
                callback.onError(e);
            }

            return;
        }

        if(callback != null){
            callback.onComplete();
        }
    }

    public void switchSkin(String pkgname, String path, String suffix){
        switchSkin(pkgname, path, suffix, null);
    }

    public void switchSkin(String pkgname, String path, String suffix, SkinSwitchCallback callback){

        if(!checkSkinResValid(pkgname, path, suffix)){
            throw new InvalidParameterException("switchSkin: skin res info invalid!");
        }

        if(TextUtils.equals(mSkinResPkgName, pkgname)
                && TextUtils.equals(mSkinResPath, path)
                && TextUtils.equals(mSkinResSuffix, suffix)){
            Logger.w(TAG, "switchSkin: skin not change.");
            return;
        }

        if(callback != null){
            callback.onStart();
        }

        try {
            loadSkinPlugin(pkgname, path, suffix);
        } catch (Exception e) {
            e.printStackTrace();

            if(callback != null){
                callback.onError(e);
            }

            return;
        }

        mSharePrefs.saveSkinResInfo(pkgname, path, suffix);

        try {
            doSkinSwitch();
        } catch (Exception e) {
            e.printStackTrace();

            if(callback != null){
                callback.onError(e);
            }

            return;
        }

        if(callback != null){
            callback.onComplete();
        }
    }

    private void doSkinSwitch() throws Exception{
        Logger.i(TAG, "doSkinSwitch...");

        Iterator<Entry<Integer, List<SkinView>>> iterator = mSkinMap.entrySet().iterator();
        while (iterator != null && iterator.hasNext()){
            List<SkinView> list = iterator.next().getValue();
            for(SkinView view : list){
                view.apply(getResourceManager());
            }
        }
    }

    private ResourceManager getResourceManager(){
        if(mUseSkinPlugin){
            return mPluginResManager;
        } else {
            if(mSysResManager == null){
                mSysResManager = new ResourceManager(mContext.getResources(),
                        mContext.getPackageName(),
                        mSkinResSuffix);
            } else {
                mSysResManager.setSkinResSuffix(mSkinResSuffix);
            }
            return mSysResManager;
        }
    }


    public void register(Activity activity){
        Logger.d(TAG, "register activity:"+activity);

        if(activity != null && !activity.isFinishing()){
            int code = activity.hashCode();
            if(!mSkinMap.containsKey(code)){
                mSkinMap.put(code, new ArrayList<SkinView>());

                SkinInflaterFactory.setFactory(activity);
            }
        }
    }

    public void unregister(Activity activity){
        Logger.d(TAG, "unregister activity:"+activity);

        int code = activity.hashCode();
        List<SkinView> skinViews = mSkinMap.remove(code);
        if(skinViews != null){
            skinViews.clear();
        }
    }

    /**
     * 代码动态添加View时需要手动注册
     * @param activity
     * @param view
     * @param skinattrs
     */
    public void register(Activity activity, View view, String skinattrs){
        Logger.d(TAG, "register activity:"+activity+" view:"+view+" skinattrs:"+skinattrs);

        if(activity != null && !activity.isFinishing()){
            if(view == null || TextUtils.isEmpty(skinattrs)){
                Logger.e(TAG, "register params invalid!");
                return;
            }
            int code = activity.hashCode();
            if(!mSkinMap.containsKey(code)){
                mSkinMap.put(code, new ArrayList<SkinView>());
            }

            List<SkinViewAttr> attrs = parseViewSkinAttr(skinattrs);
            if(attrs != null && attrs.size() > 0){
                saveSkinViews(code, new SkinView(view, attrs));
            } else {
                Logger.e(TAG, "register view find invalid skin attrs!");
            }
        }
    }

    public static boolean addSkinAttribute(SkinAttrType attrType){
        return SkinAttrTypes.add(attrType);
    }

    /**
     * 解析skin属性值
     *   格式：skin:attrs="src:home_icon|backgroud:home_bg"
     * @param skinAttr
     */
    protected static List<SkinViewAttr> parseViewSkinAttr(String skinAttr){
        Logger.d(TAG, "parseViewSkinAttr skinAttr= "+skinAttr);

        if(!TextUtils.isEmpty(skinAttr)){
            List<SkinViewAttr> attrs = new ArrayList<>();
            String[] pairs = skinAttr.split("\\|");
            for(String pair : pairs){
                if(TextUtils.isEmpty(pair)){
                    continue;
                }
                String[] list = pair.split(":");
                if(list != null && list.length == 2){
                    String attrType = list[0];
                    String resName = list[1];

                    if(TextUtils.isEmpty(attrType) || TextUtils.isEmpty(resName)){
                        continue;
                    }

                    SkinAttrType skinAttrType = SkinAttrTypes.get(attrType);
                    if(skinAttrType == null){
                        Logger.e(TAG, "skin attr type " + attrType + " is not support!");
                        continue;
                    }

                    attrs.add(new SkinViewAttr(resName, skinAttrType));
                }
            }
            return attrs;
        }
        return null;
    }

    protected void saveSkinViews(int code, SkinView skinView){
        if(mSkinMap.containsKey(code)){
            mSkinMap.get(code).add(skinView);

            if(needSwitchSkin()){
                skinView.apply(getResourceManager());
            }
        }
    }

    public static interface SkinSwitchCallback {
        void onStart();
        void onError(Exception e);
        void onComplete();
    }
}
