package com.app.huanxin.util;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by apple on 2018/1/25.
 */

public class HXhelp {
    private Map<String, EaseUser> contactList;//联系人存储

    public boolean isLoggedIn() {
        return EMClient.getInstance().isLoggedInBefore();
    }

    /**
     * 联系人列表
     *
     * @return
     */

    public Map<String, EaseUser> getContactList() {
        if (isLoggedIn() && contactList == null) {
//            contactList = demoModel.getContactList();
        }

        // return a empty non-null object to avoid app crash
        if (contactList == null) {
            return new Hashtable<String, EaseUser>();
        }

        return contactList;
    }
}
