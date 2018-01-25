package com.app.huanxin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.easeui.widget.EaseTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends FragmentActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.tv_liaotian)
    TextView tvLiaotian;
    @BindView(R.id.tv_lianxiren)
    TextView tvLianxiren;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


    }

    /**
     * <>更换页面</>
     *
     * @param transaction
     * @param fragment
     */
    private void changeFragement(FragmentTransaction transaction, Fragment fragment) {
        if (fragment == null)
            return;
        if (fragment.isAdded()) {
            transaction.show(fragment).hide(currentFragment).commitAllowingStateLoss();
        } else {
            transaction.add(R.id.layout_cententview, fragment).commitAllowingStateLoss();
        }
        currentFragment = fragment;
    }


    @OnClick({R.id.tv_liaotian, R.id.tv_lianxiren})
    public void onViewClicked(View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (view.getId()) {

            case R.id.tv_liaotian:
                EaseConversationListFragment conversationListFragment = new EaseConversationListFragment();
                conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
                    @Override
                    public void onListItemClicked(EMConversation conversation) {
                        startActivity(new Intent(HomeActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId()));
                    }
                });
                changeFragement(transaction, conversationListFragment);
                break;
            case R.id.tv_lianxiren:

//                EaseContactListFragment contactListFragment = new EaseContactListFragment();
//
//
////需要设置联系人列表才能启动fragment
//                contactListFragment.setContactsMap(getContacts());
////设置item点击事件
//                contactListFragment.setContactListItemClickListener(new EaseContactListFragment.EaseContactListItemClickListener() {
//
//                    @Override
//                    public void onListItemClicked(EaseUser user) {
//                        startActivity(new Intent(HomeActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, user.getUsername()));
//                    }
//                });
//
//                changeFragement(transaction, contactListFragment);
                break;
        }
    }
}
