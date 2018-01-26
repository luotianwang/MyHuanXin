package com.app.huanxin;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.app.huanxin.db.DemoDBManager;
import com.app.huanxin.db.InviteMessgeDao;
import com.app.huanxin.domain.InviteMessage;
import com.app.huanxin.runtimepermissions.PermissionsManager;
import com.app.huanxin.runtimepermissions.PermissionsResultAction;
import com.app.huanxin.util.HXHelper;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.exceptions.HyphenateException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends EaseBaseActivity implements View.OnClickListener {
    EaseTitleBar titleBar;
    TextView tvLiaotian;
    TextView tvLianxiren;
    private Fragment currentFragment;
    TextView tv_loginout;
    ContactFragment contactFragment = null;
    ConversationFragment conversationFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        requestPermissions();
        initview();
        initDate();
        conversationFragment = new ConversationFragment();
        changeFragement(getSupportFragmentManager().beginTransaction(), conversationFragment);
    }

    //获取联系人列表
    private void initDate() {
        EaseUI.getInstance().pushActivity(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    if (HXHelper.getInstance().getContactList().size() == 0) {
                        for (int i = 0; i < usernames.size(); i++) {
                            HXHelper.getInstance().saveContact(new EaseUser(usernames.get(i)));
                        }
                    }
                } catch (HyphenateException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "获取好友列表失败", Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        }).start();
    }

    private void initview() {
        titleBar = findViewById(R.id.title_bar);
        tvLiaotian = findViewById(R.id.tv_liaotian);
        tvLianxiren = findViewById(R.id.tv_lianxiren);
        tv_loginout = findViewById(R.id.tv_loginout);
        tvLiaotian.setOnClickListener(this);
        tvLianxiren.setOnClickListener(this);
        tv_loginout.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
    }


    @Override
    protected void onStop() {
        super.onStop();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
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
        }

        //收到透传消息
        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "收到透传消息", Toast.LENGTH_LONG).show();
                }
            });
        }

        //收到已读回执
        @Override
        public void onMessageRead(List<EMMessage> list) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "收到已读回执", Toast.LENGTH_LONG).show();
                }
            });
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
    public class MyContactListener implements EMContactListener {
        //收到好友邀请
        @Override
        public void onContactInvited(final String username, String reason) {
            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
            for (InviteMessage inviteMessage : msgs) {
                if (inviteMessage.getGroupId() == null && inviteMessage.getFrom().equals(username)) {
                    inviteMessgeDao.deleteMessage(username);
                }
            }
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
        public void onFriendRequestAccepted(final String username) {
            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
            for (InviteMessage inviteMessage : msgs) {
                if (inviteMessage.getFrom().equals(username)) {
                    return;
                }
            }
            // 自己封装的javabean
            InviteMessage msg = new InviteMessage();
            msg.setFrom(username);
            msg.setTime(System.currentTimeMillis());
            msg.setStatus(InviteMessage.InviteMessageStatus.BEAGREED);
            notifyNewIviteMessage(msg);
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "好友同意申请：+" + username, Toast.LENGTH_SHORT).show();
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
        public void onContactDeleted(final String username) {
            //被删除时回调此方法
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "被删除时回调此方法", Toast.LENGTH_LONG).show();
                }
            });
            // 被删除
            Map<String, EaseUser> localUsers = HXHelper.getInstance().getContactList();
            localUsers.remove(username);
            DemoDBManager.getInstance().deleteContact(username);
            inviteMessgeDao.deleteMessage(username);

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "删除联系人：+" + username, Toast.LENGTH_SHORT).show();
                }


            });

        }

        @Override
        public void onContactAdded(final String username) {
            // 保存增加的联系人
            Map<String, EaseUser> localUsers = HXHelper.getInstance().getContactList();
            Map<String, EaseUser> toAddUsers = new HashMap<String, EaseUser>();
            EaseUser user = new EaseUser(username);
            // 添加好友时可能会回调added方法两次
            if (!localUsers.containsKey(username)) {
                HXHelper.getInstance().saveContact(user);
            }
            toAddUsers.put(username, user);
            localUsers.putAll(toAddUsers);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "增加联系人：+" + username, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    ;

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
                if (conversationFragment == null) {
                    conversationFragment = new ConversationFragment();
                }
                conversationFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
                    @Override
                    public void onListItemClicked(EMConversation conversation) {
                        startActivity(new Intent(HomeActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId()));
                    }
                });
                changeFragement(transaction, conversationFragment);
                break;
            case R.id.tv_lianxiren:
                if (contactFragment == null) {
                    contactFragment = new ContactFragment();
                }
                //需要设置联系人列表才能启动fragment
                contactFragment.setContactsMap(DemoDBManager.getInstance().getContactList());
                //设置item点击事件
                contactFragment.setContactListItemClickListener(new EaseContactListFragment.EaseContactListItemClickListener() {
                    @Override
                    public void onListItemClicked(EaseUser user) {
                        startActivity(new Intent(HomeActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, user.getUsername()));
                    }
                });
                changeFragement(transaction, contactFragment);
                break;

            case R.id.tv_loginout:
                logout();
                break;
        }
    }

    @TargetApi(23)
    private void requestPermissions() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
            }

            @Override
            public void onDenied(String permission) {
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }


    private void logout() {
        final ProgressDialog pd = new ProgressDialog(HomeActivity.this);
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        HXHelper.getInstance().logout(false, new EMCallBack() {

            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        // 重新显示登陆页面
                        finish();
                        HXHelper.getInstance().setCurrentUserName(null);
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));

                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        pd.dismiss();
                        Toast.makeText(HomeActivity.this, "unbind devicetokens failed", Toast.LENGTH_SHORT).show();


                    }
                });
            }
        });
    }
}
