package com.kys.player.example.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kys.player.example.R;
import com.kys.player.example.base.BaseActivity;
import com.kys.player.example.base.OperateSharePreferences;
import com.kys.player.example.tools.LoginHelperPro;

/**
 * Created by bsy on 2016/8/5.
 */
public class LoginZX extends BaseActivity {

    private EditText phone, password;
    private TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_login_register);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        phone = (EditText) findViewById(R.id.phone);
        password = (EditText) findViewById(R.id.password);
        login = (TextView) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        if (isNull(phone.getText().toString(), password.getText().toString())) {//有空值
            Toast.makeText(LoginZX.this, "用户名及密码不能为空", Toast.LENGTH_SHORT).show();
        } else {
            OperateSharePreferences operateSharePreferences = OperateSharePreferences.getInstance(LoginZX.this);
            operateSharePreferences.saveUserName(phone.getText().toString());
            operateSharePreferences.savePassword(password.getText().toString());
            LoginHelperPro.getInstance(getApplicationContext()).login(new LoginHelperPro.LoginHelperListener() {
                @Override
                public void isLogin() {

                }

                @Override
                public void isReLogin() {
                    finish();
                }

                @Override
                public void isLogout() {

                }

                @Override
                public void goLogin() {

                }
            });
        }
    }

    private Boolean isNull(String phone, String password) {
        Boolean isNull;
        if (phone.equals("") || password.equals("")) {
            isNull = true;
        } else isNull = false;
        return isNull;
    }
}
