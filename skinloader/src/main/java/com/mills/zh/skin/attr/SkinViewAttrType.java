package com.mills.zh.skin.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mills.zh.skin.ResourceManager;
import com.mills.zh.skin.SkinManager;

/**
 * Created by zhangmd on 2018/12/28.
 */

public enum SkinViewAttrType {

    BACKGROUND("background") {
        @Override
        public void apply(View view, String resName) {
            Drawable drawable = null;
            try {
                drawable = getResourceManager().getDrawable(resName);
            } catch (Exception e) {
                e.printStackTrace();
                drawable = null;
            }

            if (drawable != null) {
                view.setBackgroundDrawable(drawable);
            } else {
                try {
                    int color = getResourceManager().getColor(resName);
                    view.setBackgroundColor(color);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    },

    COLOR("textColor") {
        @Override
        public void apply(View view, String resName) {
            ColorStateList colorList = getResourceManager().getColorStateList(resName);
            if (colorList == null) return;
            ((TextView) view).setTextColor(colorList);
        }
    },

    SRC("src") {
        @Override
        public void apply(View view, String resName) {
            if (view instanceof ImageView) {
                Drawable drawable = getResourceManager().getDrawable(resName);
                if (drawable == null) return;
                ((ImageView) view).setImageDrawable(drawable);
            }

        }
    },

    TEXT("text") {
        @Override
        public void apply(View view, String resName) {
            if (view instanceof TextView) {
                String str = getResourceManager().getString(resName);
                if (str == null) return;
                ((TextView) view).setText(str);
            }
        }
    };

    String attrType;

    SkinViewAttrType(String attrType) {
        this.attrType = attrType;
    }

    public String getAttrType() {
        return attrType;
    }

    public abstract void apply(View view, String resName);

    public ResourceManager getResourceManager() {
        return SkinManager.getInstance().getResourceManager();
    }
}
