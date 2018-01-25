package com.app.huanxin.util;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
// ============== fabric start
//import com.crashlytics.android.Crashlytics;
//import io.fabric.sdk.android.Fabric;
// ============== fabric end

public class DemoApplication extends Application {
    private static DemoApplication instance;
    // login user name
    public final String PREF_USERNAME = "username";

    /**
     * nickname for current user, the nickname instead of ID be shown when user receive notification from APNs
     */
    public static String currentUserNick = "";

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
//        applicationContext = this;
        instance = this;
        HXHelper.getInstance().init(this);

    }

    public static DemoApplication getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
