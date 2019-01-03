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
import android.view.LayoutInflater;
import android.view.View;

import com.mills.zh.skin.attr.SkinView;
import com.mills.zh.skin.attr.SkinViewAttr;
import com.mills.zh.skin.utils.Logger;
import com.mills.zh.skin.utils.ReflectUtils;

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

    protected static void setFactory(Application application) {
        LayoutInflater inflater = LayoutInflater.from(application);
        LayoutInflaterCompat.setFactory(inflater, new SkinInflaterFactory(application.hashCode()));
    }

    protected static void setFactory(Activity activity){
        Logger.i(TAG, "setFactory activity:"+activity);

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
        Logger.d(TAG, "onCreateView name:"+name);

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

            assertInflaterContext(inflater, context);

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
            Logger.e(TAG, "createView(), create view failed:", e);
            view = null;
        }
        return view;
    }

    //在低版本系统中会出inflaterContext为空的问题， 因此需要处理inflaterContext为空的情况
    private void assertInflaterContext(LayoutInflater inflater, Context context) {
        Context inflaterContext = inflater.getContext();
        if (inflaterContext == null) {
            ReflectUtils.setField(inflater, "mContext", context);
        }

        //设置mConstructorArgs的第一个参数context
        Object[] constructorArgs = (Object[]) ReflectUtils.getField(inflater, "mConstructorArgs");
        if (null == constructorArgs || constructorArgs.length < 2) {
            //异常，一般不会发生
            constructorArgs = new Object[2];
            ReflectUtils.setField(inflater, "mConstructorArgs", constructorArgs);
        }

        //如果mConstructorArgs的第一个参数为空，则设置为mContext
        if (null == constructorArgs[0]) {
            constructorArgs[0] = inflater.getContext();
        }
    }

    private void saveSkinView(View view, List<SkinViewAttr> attrs){
        SkinManager.getInstance().saveSkinViews(mCode, new SkinView(view, attrs));
    }
}
