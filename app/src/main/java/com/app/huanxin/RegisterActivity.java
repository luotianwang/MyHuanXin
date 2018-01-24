package com.app.huanxin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by apple on 2018/1/24.
 */

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.edt_username)
    EditText edtUsername;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.btn_register)
    Button btnRegister;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_register)
    public void onViewClicked() {
        final String userName = edtUsername.getText().toString();
        final String passWord = edtPassword.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(passWord)) {
                    try {
                        EMClient.getInstance().createAccount(userName, passWord);//同步方法
                        Log.e("LMC", "注册成功");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            }
                        });
                    } catch (final HyphenateException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                /**
                                 * 关于错误码可以参考官方api详细说明
                                 * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                                 */
                                int errorCode = e.getErrorCode();
                                String message = e.getMessage();
                                Log.d("lzan13", String.format("sign up - errorCode:%d, errorMsg:%s", errorCode, e.getMessage()));
                                switch (errorCode) {
                                    // 网络错误
                                    case EMError.NETWORK_ERROR:
                                        Toast.makeText(RegisterActivity.this, "网络错误 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    // 用户已存在
                                    case EMError.USER_ALREADY_EXIST:
                                        Toast.makeText(RegisterActivity.this, "用户已存在 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    // 参数不合法，一般情况是username 使用了uuid导致，不能使用uuid注册
                                    case EMError.USER_ILLEGAL_ARGUMENT:
                                        Toast.makeText(RegisterActivity.this, "参数不合法，一般情况是username 使用了uuid导致，不能使用uuid注册 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    // 服务器未知错误
                                    case EMError.SERVER_UNKNOWN_ERROR:
                                        Toast.makeText(RegisterActivity.this, "服务器未知错误 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    case EMError.USER_REG_FAILED:
                                        Toast.makeText(RegisterActivity.this, "账户注册失败 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    default:
                                        Toast.makeText(RegisterActivity.this, "ml_sign_up_failed code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }
                        });
                    }
                }

            }
        }).start();
    }

}