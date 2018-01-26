package com.app.huanxin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.app.huanxin.util.HXHelper;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.ui.EaseBaseActivity;

/**
 * Created by apple on 2018/1/24.
 */

public class SplashActivity extends EaseBaseActivity {


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        EMClient.getInstance().groupManager().loadAllGroups();
        EMClient.getInstance().chatManager().loadAllConversations();
        String username = HXHelper.getInstance().getCurrentUsernName();
        if (!TextUtils.isEmpty(username)) {
            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }
    }
}
