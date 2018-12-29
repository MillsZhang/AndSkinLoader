package com.mills.zh.skin;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.mills.zh.skin.attr.SkinView;
import com.mills.zh.skin.attr.SkinViewAttr;

import java.util.List;

/**
 * Created by zhangmd on 2018/12/28.
 */

public class SkinInflaterFactory implements LayoutInflaterFactory {
    private static final String TAG = "SkinInflaterFactory";

    private int mCode;
    private LayoutInflaterFactory mFactoryDelegate;

    private SkinInflaterFactory(int code){
        mCode = code;
    }

    public static void setFactory(Application application) {
        LayoutInflater inflater = LayoutInflater.from(application);
        LayoutInflaterCompat.setFactory(inflater, new SkinInflaterFactory(application.hashCode()));
    }

    public static void setFactory(Activity activity){
        if(Consts.DEBUG) Log.d(TAG, "setFactory activity:"+activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        SkinInflaterFactory factory = new SkinInflaterFactory(activity.hashCode());
        if(activity instanceof AppCompatActivity){
            final AppCompatDelegate delegate = ((AppCompatActivity)activity).getDelegate();
            factory.setFactoryDelegate(new LayoutInflaterFactory() {
                @Override
                public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                    return delegate.createView(parent, name, context, attrs);
                }
            });
        }
        LayoutInflaterCompat.setFactory(inflater, factory);
    }

    private void setFactoryDelegate(LayoutInflaterFactory delegate){
        mFactoryDelegate = delegate;
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        if(Consts.DEBUG) Log.d(TAG, "onCreateView name:"+name);

        View view = null;
        if (mFactoryDelegate != null){
            view = mFactoryDelegate.onCreateView(parent, name, context, attrs);
        }

        String attrstr = attrs.getAttributeValue(Consts.SKIN_XML_NAMESPACE, Consts.SKIN_XML_ATTRS);
        if(!TextUtils.isEmpty(attrstr)){
            List<SkinViewAttr> list = SkinManager.parseViewSkinAttr(attrstr);
            if(list != null && list.size() > 0){
                if(view == null){
                    view = createView(name, context, attrs);
                }

                if(view != null){
                    saveSkinView(view, list);
                }
            }
        }

        return view;
    }

    private View createView(String name, Context context, AttributeSet attrs){
        View view = null;
        try {
            LayoutInflater inflater = LayoutInflater.from(context);
            // TODO
            if (-1 == name.indexOf('.')) {
                if ("View".equals(name) || "ViewStub".equals(name) || "ViewGroup".equals(name)) {
                    view = inflater.createView(name, "android.view.", attrs);
                }
                if (view == null) {
                    view = inflater.createView(name, "android.widget.", attrs);
                }
                if (view == null) {
                    view = inflater.createView(name, "android.webkit.", attrs);
                }
            } else {
                view = inflater.createView(name, null, attrs);
            }
        } catch (Exception e) {
            Log.e(TAG, "createView(), create view failed", e);
            view = null;
        }
        return view;
    }

    private void saveSkinView(View view, List<SkinViewAttr> attrs){
        SkinManager.getInstance().saveSkinViews(mCode, new SkinView(view, attrs));
    }
}
