package com.heima.plugs;

import android.app.Activity;
import android.os.Bundle;

/**
 * 符合加载标准（Activity生命周期）
 */
public interface PlugInterface {

    void onCreate(Bundle saveInstance);

    void onStart();

    void onResume();

    void onRestart();

    void onDestroy();

    void onStop();

    void onPause();

    /**
     * 注入context
     * @param context
     */
    void attachContext(Activity context);
}
