package com.app.huanxin;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.app.huanxin.util.HXHelper;
// ============== fabric start
//import com.crashlytics.android.Crashlytics;
//import io.fabric.sdk.android.Fabric;
// ============== fabric end

public class MyApplication extends Application {
    private static MyApplication instance;

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        instance = this;
        HXHelper.getInstance().init(this);

    }

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
