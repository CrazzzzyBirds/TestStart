package com.heima.teststart;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * DexClassLoader加载第三方app
 *
 */
public class PlugManager {
    public  static  final  String TAG="PlugManager";
    private static PlugManager ourInstance;
    private Context context;
    private DexClassLoader pluginDexClassLoader;
    private Resources pluginResources;
    private PackageInfo pluginPackageArchiveInfo;
    private String entryActivityName;

    private PlugManager() {
    }

    public static PlugManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new PlugManager();
        }
        return ourInstance;
    }


    public void setContext(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * 获取Plugin的字节码文件对象
     * 加载外部apk，重写getDexClassLoader（）与getResources()
     * @param dexPath Plugin的路径
     */
    public void loadApk(String dexPath) {
        /*  optimizedDirectory   Plugin的缓存路径
         *  libraryPath          可以为null
         *  parent              为父类加载器
         */
        File dexOutFile = context.getDir("dex", Context.MODE_PRIVATE);
        pluginDexClassLoader = new DexClassLoader(dexPath, dexOutFile.getAbsolutePath(), null, context.getClassLoader());

        // 获取包名
        PackageManager packageManager = context.getPackageManager();
        pluginPackageArchiveInfo = packageManager.getPackageArchiveInfo(dexPath, PackageManager.GET_ACTIVITIES);
        //activity集合跟App-B的Manifest中注册的activity有关 顺序也有关
        entryActivityName = pluginPackageArchiveInfo.activities[1].name;
        for (int i=0;i<pluginPackageArchiveInfo.activities.length;i++){
            Log.e(TAG, pluginPackageArchiveInfo.activities[i].name);
        }


      /*  实例化resources
        Resources(AssetManager assets, DisplayMetrics metrics, Configuration config) */
        AssetManager assets = null;
        try {
            assets = AssetManager.class.newInstance();
            Method addAssetPath = AssetManager.class.getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assets, dexPath);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //获取Plugin的Resources
        pluginResources = new Resources(assets, context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());
    }

    public PackageInfo getPluginPackageArchiveInfo() {
        return pluginPackageArchiveInfo;
    }

    public DexClassLoader getPluginDexClassLoader() {
        return pluginDexClassLoader;
    }

    public Resources getPluginResources() {
        return pluginResources;
    }

    public String getEntryActivityName() {
        return entryActivityName;
    }


}
