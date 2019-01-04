# AndSkinLoader
Android版简单可用的换肤框架

## **AndSkinLoader使用方法**

### **初始化**
首先在`Application`的`onCreate`中进行初始化：
```
    SkinManager.getInstance().init(this);
```
init接口中已经为Application的LayoutInflater设置了自定义的Factory，所以使用Application的LayoutInflater加
载的View也默认支持换肤功能。

### **Activity中注册**
需要支持换肤功能的Activity，需要在onCreate中注册：
```
    protected void onCreate(Bundle savedInstanceState) {
        SkinManager.getInstance().register(this);
        super.onCreate(savedInstanceState);
        ...
    }
```
在onDestroy中需要取消注册：
```
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().unregister(this);
    }
```
(Fragment使用的是Activity的LayoutInflater来加载View，所以只要Activity注册了换肤功能，Fragment自动支持换肤)

### **xml中添加换肤属性**
1.添加skin命名空间
xmlns:skin="http://schemas.android.com/android/skin"

2.View控件添加skin属性
skin:attrs

```
    <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:skin="http://schemas.android.com/android/skin"
        android:id="@+id/root"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@color/home_bg"
        skin:attrs="background:home_bg">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_centerHorizontal="true"
            android:textSize="30sp"
            skin:attrs="textColor:txt_color|text:txt_str"
            android:text="Hello World!" />

        <ImageView
            skin:attrs="src:btn_img"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_centerHorizontal="true"
            android:layout_marginTop="200px"
            android:src="@drawable/btn_img"/>

    </RelativeLayout>
```
3.skin:attrs格式说明
```
    skin:attrs="textColor:txt_color|text:txt_str"
    格式： 属性名称:资源名称|属性名称:资源名称|...
    默认支持原生background、textColor、src、text属性，可自己扩展支持的属性
```

### **扩展换肤支持的属性**
使用SkinManager.addSkinAttribute来扩展皮肤属性，实现apply接口来处理具体的View属性操作

注意：扩展属性最好在SkinManager.getInstance().init之前，否则会不生效
```
    SkinManager.addSkinAttribute(new SkinAttrType("textSize") {
        @Override
        public void apply(View view, String resName, ResourceManager resourceManager) {
            if(view instanceof TextView){
                try {
                    int dimen = resourceManager.getDimension(resName);
                    ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, dimen);
                } catch (Exception e) {
                    Logger.e("ResourceManager", e.getMessage());
                }
            }
        }
    });
```

### **代码中动态添加的View支持换肤**
使用SkinManager.getInstance().register来注册View：
```
    SkinManager.getInstance().register(MainActivity.this,
                                    btn,
                                    "text:btn_txt_str|textColor:btn_txt_color");
```

### **切换皮肤**

```
    切换内置皮肤资源
    SkinManager.getInstance().switchSkinInner("green");

    切换外部皮肤插件资源
    SkinManager.getInstance().switchSkin(pkgname, path, suffix, callback);

    恢复到默认皮肤
    SkinManager.getInstance().restoreSkin();
```

### **皮肤资源生成**
说明：皮肤资源通过皮肤后缀符来区分，所有的皮肤资源名称必须带上对应的后缀，否则会加载不成功

1.内置皮肤资源

drawable、string、color等资源中拷贝复制需要换肤的资源，添加上对应的皮肤后缀即可。

2.外部皮肤插件

参考skintest模块  
将需要换肤的资源拷贝到皮肤插件模块，执行模块gradle build任务assembleRelease，生成皮肤包

TODO
