package com.app.huanxin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.app.huanxin.db.InviteMessgeDao;
import com.app.huanxin.domain.InviteMessage;
import com.app.huanxin.util.HXHelper;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.easeui.widget.EaseTitleBar;

import java.util.List;

public class HomeActivity extends EaseBaseActivity implements View.OnClickListener {
    EaseTitleBar titleBar;
    TextView tvLiaotian;
    TextView tvLianxiren;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initview();
        changeFragement(getSupportFragmentManager().beginTransaction(), new ConversationFragment());
    }

    private void initview() {
        titleBar = findViewById(R.id.title_bar);
        tvLiaotian = findViewById(R.id.tv_liaotian);
        tvLianxiren = findViewById(R.id.tv_lianxiren);
        tvLiaotian.setOnClickListener(this);
        tvLianxiren.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
        EMClient.getInstance().contactManager().setContactListener(contactListener);
    }


    @Override
    protected void onStop() {
        super.onStop();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        EMClient.getInstance().contactManager().removeContactListener(contactListener);
    }

    /**
     * 监听消息状态
     */
    EMMessageListener msgListener = new EMMessageListener() {
        //收到消息
        @Override
        public void onMessageReceived(List<EMMessage> messages) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "收到消息", Toast.LENGTH_LONG).show();
                }
            });


            for (EMMessage message : messages) {
                String username = null;
                // 群组消息
                if (message.getChatType() == EMMessage.ChatType.GroupChat || message.getChatType() == EMMessage.ChatType.ChatRoom) {
                    username = message.getTo();
                } else {
                    // 单聊消息
                    username = message.getFrom();
                }
                // 如果是当前会话的消息，刷新聊天页面
                if (username.equals(ChatActivity.)) {
                    msgList.addAll(messages);
                    adapter.notifyDataSetChanged();
                    if (msgList.size() > 0) {
                        et_content.setSelection(listView.getCount() - 1);

                    }

                }
            }

        }

        //收到透传消息
        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {
            Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_LONG).show();
        }

        //收到已读回执
        @Override
        public void onMessageRead(List<EMMessage> list) {
            Toast.makeText(getApplicationContext(), "3", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {
            Toast.makeText(getApplicationContext(), "4", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {
            Toast.makeText(getApplicationContext(), "5", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {
            Toast.makeText(getApplicationContext(), "6", Toast.LENGTH_LONG).show();
        }
    };
    InviteMessgeDao inviteMessgeDao;

    /**
     * 保存并提示消息的邀请消息
     *
     * @param msg
     */
    private void notifyNewIviteMessage(InviteMessage msg) {
        if (inviteMessgeDao == null) {
            inviteMessgeDao = new InviteMessgeDao(HomeActivity.this);
        }
        inviteMessgeDao.saveMessage(msg);
        //保存未读数，这里没有精确计算
        inviteMessgeDao.saveUnreadMessageCount(1);
        // 提示有新消息
        //响铃或其他操作
    }

    /**
     * 监听好友添加状态
     */
    EMContactListener contactListener = new EMContactListener() {
        //收到好友邀请
        @Override
        public void onContactInvited(final String username, String reason) {
            InviteMessage msg = new InviteMessage();
            msg.setFrom(username);
            msg.setTime(System.currentTimeMillis());
            msg.setReason(reason);
            // 设置相应status
            msg.setStatus(InviteMessage.InviteMessageStatus.BEINVITEED);
            notifyNewIviteMessage(msg);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "好友申请同意：+" + username, Toast.LENGTH_SHORT).show();
                }
            });

        }

        //好友请求被同意
        @Override
        public void onFriendRequestAccepted(String s) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "好友请求被同意", Toast.LENGTH_LONG).show();
                }

            });
        }

        @Override
        public void onFriendRequestDeclined(String s) {
            //好友请求被拒绝
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "好友请求被拒绝", Toast.LENGTH_LONG).show();
                }

            });

        }

        @Override
        public void onContactDeleted(String username) {
            //被删除时回调此方法
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "被删除时回调此方法", Toast.LENGTH_LONG).show();
                }

            });

        }

        @Override
        public void onContactAdded(String username) {

            //增加了联系人时回调此方法
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "增加了联系人时回调此方法", Toast.LENGTH_LONG).show();
                }

            });
        }
    };

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


    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (v.getId()) {

            case R.id.tv_liaotian:
                ConversationFragment conversationListFragment = new ConversationFragment();
                conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
                    @Override
                    public void onListItemClicked(EMConversation conversation) {
                        startActivity(new Intent(HomeActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId()));
                    }
                });
                changeFragement(transaction, conversationListFragment);
                break;
            case R.id.tv_lianxiren:

                ContactFragment contactListFragment = new ContactFragment();
//需要设置联系人列表才能启动fragment
                contactListFragment.setContactsMap(HXHelper.getInstance().getContactList());
//设置item点击事件
                contactListFragment.setContactListItemClickListener(new EaseContactListFragment.EaseContactListItemClickListener() {

                    @Override
                    public void onListItemClicked(EaseUser user) {
                        startActivity(new Intent(HomeActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, user.getUsername()));
                    }
                });

                changeFragement(transaction, contactListFragment);
                break;
        }
    }
}
