package com.example.ts.news.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.ts.news.R;
import com.example.ts.news.Utils.ApplicationUtil;
import com.example.ts.news.Utils.SharedPreUtil;


public class LoginOrRegisterActivity extends AppCompatActivity {
    private Button login, register;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginorregister);
        login = (Button) findViewById(R.id.btn_login);
        register = (Button) findViewById(R.id.btn_register);

        //判断用户是否登录
        Boolean userIsLogin =(Boolean)SharedPreUtil.getParam(LoginOrRegisterActivity.this,
                        SharedPreUtil.IS_LOGIN,false);
        if (userIsLogin) {
            Intent intent = new Intent(LoginOrRegisterActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
//            Intent intent = new Intent(LoginOrRegisterActivity.this, LoginOrRegisterActivity.class);
        }

        ApplicationUtil.getInstance().addActivity(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginOrRegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginOrRegisterActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ApplicationUtil.getInstance().addActivity(this);
    }
}
