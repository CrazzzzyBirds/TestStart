package com.heima.teststart;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import com.heima.plugs.PlugInterface;


/**
 * 承载页---加载第三方activity页面
 */
public class ProxyActivity extends Activity {


    private PlugInterface pluginInterface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //待启动的第三方Activity
        String className = getIntent().getStringExtra("className");
        try {
            //加载该Activity的字节码对象
            Class<?> aClass = PlugManager.getInstance().getPluginDexClassLoader().loadClass(className);
            //创建该Activity的实例
            Object newInstance = aClass.newInstance();
            //程序健壮性检查
            if (newInstance instanceof PlugInterface) {
                pluginInterface = (PlugInterface) newInstance;
                //将代理Activity的实例传递给三方Activity
                pluginInterface.attachContext(this);
                //创建bundle用来与三方apk传输数据
                Bundle bundle = new Bundle();
                //调用三方Activity的onCreate，
                pluginInterface.onCreate(bundle);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重新，通过className拿到类名
     *
     * @return
     */
    @Override
    public ClassLoader getClassLoader() {
        return PlugManager.getInstance().getPluginDexClassLoader();
    }


    /**
     * 注意：三方调用拿到对应加载的三方Resources
     *
     * @return
     */
    @Override
    public Resources getResources() {
        return PlugManager.getInstance().getPluginResources();
    }

    @Override
    public void startActivity(Intent intent) {
        Intent intent1 = new Intent(this, ProxyActivity.class);
       String className = intent.getStringExtra("className");
        //intent1.putExtra("className", intent.getComponent().getClassName());
        intent1.putExtra("className",className);
        super.startActivity(intent1);
    }

    @Override
    public void onStart() {
        if (pluginInterface != null)
            pluginInterface.onStart();
        super.onStart();
    }

    @Override
    public void onResume() {
        if (pluginInterface != null)
            pluginInterface.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        if (pluginInterface != null)
            pluginInterface.onPause();
        super.onPause();
    }

    @Override
    public void onRestart() {
        if (pluginInterface != null)
            pluginInterface.onRestart();
        super.onRestart();
    }

    @Override
    public void onStop() {
        if (pluginInterface != null)
            pluginInterface.onStop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (pluginInterface != null)
            pluginInterface.onDestroy();
        super.onDestroy();
    }

}
