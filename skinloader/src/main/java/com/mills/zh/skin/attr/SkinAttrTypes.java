package com.mills.zh.skin.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mills.zh.skin.ResourceManager;
import com.mills.zh.skin.utils.Logger;

import java.util.HashMap;

/**
 * Created by zhangmd on 2019/1/3.
 */

public class SkinAttrTypes {
    private static final String TAG = "SkinAttrTypes";

    public static final String BACKGROUND = "background";
    public static final String COLOR = "textColor";
    public static final String SRC = "src";
    public static final String TEXT = "text";


    private static HashMap<String, SkinAttrType> sTypes = new HashMap<>();

    static {
        add(new SkinAttrType(BACKGROUND) {
            @Override
            public void apply(View view, String resName, ResourceManager resourceManager) {
                Drawable drawable = resourceManager.getDrawable(resName);

                if (drawable != null) {
                    view.setBackgroundDrawable(drawable);
                } else {
                    try {
                        int color = resourceManager.getColor(resName);
                        view.setBackgroundColor(color);
                    } catch (Exception e) {
                        Logger.e("ResourceManager", e.getMessage());
                    }
                }
            }
        });

        add(new SkinAttrType(COLOR) {
            @Override
            public void apply(View view, String resName, ResourceManager resourceManager) {
                if(view instanceof TextView) {
                    ColorStateList colorList = resourceManager.getColorStateList(resName);
                    if (colorList == null) return;
                    ((TextView) view).setTextColor(colorList);
                }
            }
        });

        add(new SkinAttrType(SRC) {
            @Override
            public void apply(View view, String resName, ResourceManager resourceManager) {
                if (view instanceof ImageView) {
                    Drawable drawable = resourceManager.getDrawable(resName);
                    if (drawable == null) return;
                    ((ImageView) view).setImageDrawable(drawable);
                }
            }
        });

        add(new SkinAttrType(TEXT) {
            @Override
            public void apply(View view, String resName, ResourceManager resourceManager) {
                if (view instanceof TextView) {
                    String str = resourceManager.getString(resName);
                    if (str == null) return;
                    ((TextView) view).setText(str);
                }
            }
        });
    }


    public static boolean add(SkinAttrType attrType){
        if(attrType == null){
            Logger.e(TAG, "putSkinAttrType skin attr type is null!");
            return false;
        }
        String type = attrType.getAttrType();
        if(TextUtils.isEmpty(type)){
            Logger.e(TAG, "putSkinAttrType skin attr type is empty!");
            return false;
        }
        if(sTypes.containsKey(type)){
            Logger.e(TAG, "putSkinAttrType skin attr type:"+type+" is exist!");
            return false;
        }
        sTypes.put(type, attrType);
        return true;
    }

    public static SkinAttrType get(String attrType){
        if(TextUtils.isEmpty(attrType)){
            return null;
        }
        return sTypes.get(attrType);
    }
}
