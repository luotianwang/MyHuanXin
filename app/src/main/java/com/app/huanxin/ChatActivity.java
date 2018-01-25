package com.app.huanxin;

import android.os.Bundle;

import com.app.huanxin.util.Constant;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.hyphenate.easeui.ui.EaseChatFragment;

/**
 * Created by apple on 2018/1/25.
 */

public class ChatActivity extends EaseBaseActivity {

    public static String toUserName = "";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_chat);

        String userName = getIntent().getStringExtra(Constant.EXTRA_USER_ID);
        toUserName = userName;
        //new出EaseChatFragment或其子类的实例
        EaseChatFragment chatFragment = new EaseChatFragment();
        //传入参数
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
        args.putString(EaseConstant.EXTRA_USER_ID, userName);
        chatFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.layout_content, chatFragment).commit();

    }

}
