package com.example.ts.news.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.ts.news.R;
import com.example.ts.news.Utils.SharedPreUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RegisterToLogin extends AppCompatActivity {
    private EditText username_edit;
    private EditText password_edit;
    private CheckBox cbPd;
    private Handler myHandler=new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.obj.equals(404)) {
                Toast.makeText(getApplicationContext(), "账号或密码错误！", Toast.LENGTH_SHORT).show();
            } else {
                User u = (User) msg.obj;
                //在此处更新主线程中的UI
                Toast.makeText(getApplicationContext(), u.getUsername() + ",登陆成功！", Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        username_edit=(EditText)findViewById(R.id.login_username);
        password_edit=(EditText)findViewById(R.id.login_password);
        ImageView login = (ImageView) findViewById(R.id.login);
        cbPd=(CheckBox)findViewById(R.id.cbPd);
        Intent it=getIntent();
        //从MainActivity中获取username
        String name=it.getStringExtra("username_extra");
        String password=it.getStringExtra("password_extra");
        username_edit.setText(name);
        password_edit.setText(password);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //从Android 6.0之后，访问网络的代码不能放在主线程中
                if (username_edit.length() == 0) {
                    Toast.makeText(getApplicationContext(), "请输入用户名", Toast.LENGTH_SHORT).show();
                } else {
                    //判断密码是否输入，如果未输入，提示“请输入密码”
                    if (password_edit.length() == 0) {
                        Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //启动子线程要做的事情全部放在run方法中
                                String urlAddress = "http://47.242.191.232:8080/login.do?username=" + username_edit.getText().toString() + "&password=" + password_edit.getText().toString();
                                try {
                                    URL url = new URL(urlAddress);
                                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                    connection.setRequestMethod("GET");
                                    connection.setConnectTimeout(5000);
                                    connection.connect();
                                    int code = connection.getResponseCode();
                                    if (code == 200) {
                                        InputStream is = connection.getInputStream();
                                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                                        String line = "";
                                        StringBuilder stringBuilder = new StringBuilder();
                                        //循环从输入流管道读取数据并保存到StringBuilder对象中
                                        while ((line = br.readLine()) != null) {
                                            stringBuilder.append(line);
                                        }
                                        System.out.println("从服务器获取的数据是:" + stringBuilder.toString());
                                        //此时得到的只是一个JSON格式的字符串，需要将该字符串解析成对象
                                        JSONObject object = JSONObject.parseObject(stringBuilder.toString());
                                        int result_code = object.getInteger("result_code");
                                        if (result_code == 200) {
                                            //登录数据保存方法
                                            String username_str = username_edit.getText().toString();
                                            String password_str = password_edit.getText().toString();
                                            if (cbPd.isChecked()) {
                                                //若已经勾选，将EditText中输入的数据转换成String类型并读取
                                                //调用下面写的save_userMes方法在app目录中创建一个user.xml,存入读取的数据
                                                boolean flag = save_userMes(RegisterToLogin.this, username_str, password_str);
                                            }else{
                                                boolean flag =remove_userMes(RegisterToLogin.this);
                                            }
                                            JSONObject node = object.getJSONObject("user");
                                            User u = JSONObject.toJavaObject(node, User.class);
                                            SharedPreUtil.setParam(RegisterToLogin.this, SharedPreUtil.IS_LOGIN, true);
                                            SharedPreUtil.setParam(RegisterToLogin.this, SharedPreUtil.LOGIN_DATA, username_str);
                                            SharedPreUtil.setNickNameData(RegisterToLogin.this,SharedPreUtil.NICKNAME_DATA,u.getNickname());
                                            System.out.println(u.getUsername() + "---->" + u.getPassword() + "--->" + u.getNickname());
                                            //子线程更新主线程中的UI,不能将更新UI的操作放在子线程中
                                            //通过消息，把要更新的数据发送出去，主线程再到消息队列中去取改数据，然后在线程中更新
                                            Message message = new Message();
                                            message.obj = u;
                                            //通过Handler发送消息
                                            myHandler.sendMessage(message);
//                                            show.setText(u.getUsername());
//                                            String username=u.getUsername();
                                            Intent intent = new Intent(RegisterToLogin.this, MainActivity.class);
                                            intent.putExtra("nickname_login",u.getNickname());
                                            intent.putExtra("username_extra",u.getUsername());
                                            startActivity(intent);
                                            finish();
                                        }else{
                                            Message message = new Message();
                                            message.obj = 404;
                                            //通过Handler发送消息
                                            myHandler.sendMessage(message);
                                        }
                                    }

                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }
            }
        });
    }


    //封装的保存数据方法
    private boolean save_userMes(Context context,String username,String password){
        SharedPreferences sharedPreferences=context.getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("username_cd",username);
        editor.putString("password_cd",password);
        editor.commit();
        return true;
    }
    //封装的清楚数据方法，这里用的是editor.clear();直接清楚所有数据
    private boolean remove_userMes(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.commit();
        return true;
    }
    //封装的获取数据方法
    private Map<String,String>get_userMes(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences("user",MODE_PRIVATE);
        String username=sharedPreferences.getString("username_cd",null);
        String password=sharedPreferences.getString("password_cd",null);
        Map<String,String> user=new HashMap<String,String>();
        user.put("username_cd",username);
        user.put("password_cd",password);
        return user;
    }

}