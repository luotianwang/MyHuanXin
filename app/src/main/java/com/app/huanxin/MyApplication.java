package com.app.huanxin;

import android.app.Application;

import com.hyphenate.easeui.EaseUI;

/**
 * Created by apple on 2018/1/24.
 */

public class MyApplication extends Application {

    private static final String TAG = "LMC";

    @Override
    public void onCreate() {
        super.onCreate();
        EaseUI.getInstance().init(this, null);
    }
}
