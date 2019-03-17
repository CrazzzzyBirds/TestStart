package com.heima.otherapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * 待加载app主界面
 * 此app没有安装，仅存.apk文件在内存卡
 */
public class MainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void start(View view) {
        //使用注入的context
        startActivity(new Intent(thatActivity, SecondActivity.class));
    }
}
