package com.app.huanxin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.huanxin.util.HXHelper;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by apple on 2018/1/24.
 */

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.edt_username)
    EditText edtUsername;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_register)
    Button btnRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_login, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                final String userName = edtUsername.getText().toString();
                String passWord = edtPassword.getText().toString();
                EMClient.getInstance().login(userName, passWord, new EMCallBack() {//回调
                    @Override
                    public void onSuccess() {
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                HXHelper.getInstance().setCurrentUserName(userName);
                                Toast.makeText(getApplicationContext(), "登录聊天服务器成功", Toast.LENGTH_LONG).show();
                            }
                        });

                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }

                    @Override
                    public void onError(int code, String message) {
                        Log.d("main", "登录聊天服务器失败！");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "登录聊天服务器失败", Toast.LENGTH_LONG).show();
                            }
                        });

                    }

                });
                break;
            case R.id.btn_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }
}
