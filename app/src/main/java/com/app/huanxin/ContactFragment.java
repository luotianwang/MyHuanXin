package com.app.huanxin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.huanxin.util.HXHelper;
import com.app.huanxin.widget.ContactItemView;
import com.hyphenate.easeui.ui.EaseContactListFragment;

/**
 * Created by apple on 2018/1/24.
 */

public class ContactFragment extends EaseContactListFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @SuppressLint("InflateParams")
    @Override
    protected void initView() {
        super.initView();
        @SuppressLint("InflateParams") View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_contact, null);
        HeaderItemClickListener clickListener = new HeaderItemClickListener();
        ContactItemView applicationItem = (ContactItemView) headerView.findViewById(R.id.application_item);
        applicationItem.setOnClickListener(clickListener);
        listView.addHeaderView(headerView);
        titleBar.setRightImageResource(R.drawable.ease_blue_add);
        titleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddContactActivity.class));
            }
        });
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        setContactsMap(HXHelper.getInstance().getContactList());
    }

    protected class HeaderItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.application_item:
                    // 进入申请与通知页面
                    startActivity(new Intent(getActivity(), NewFriendsMsgActivity.class));
                    break;
            }
        }
    }
}
