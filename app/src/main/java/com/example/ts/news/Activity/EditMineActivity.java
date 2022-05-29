package com.example.ts.news.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.ts.news.R;
import com.example.ts.news.Utils.MyDatabaseHelper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EditMineActivity extends AppCompatActivity {
    private EditText update_nickname, update_password, update_repassword;
    private TextView update_user;
    private MyDatabaseHelper dbHelper;
    private ImageView img_back;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mine);
        initView();
    }

    private void initView() {
        update_user =(TextView) findViewById(R.id.update_user);
        update_nickname = (EditText) findViewById(R.id.update_nickname);
        update_password = (EditText) findViewById(R.id.update_password);
        update_repassword = (EditText) findViewById(R.id.update_repassword);
        img_back=(ImageView)findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditMineActivity.this.finish();
            }
        });
        update_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=getIntent();
                //从MainActivity中获取username，password，mail
                String username=it.getStringExtra("username_extra");
                final String nickname_str = update_nickname.getText().toString();
                String userpassword_str = update_password.getText().toString();
                final String repassword_str = update_repassword.getText().toString();
                if(!TextUtils.isEmpty(nickname_str)) {
                    if(userpassword_str.equals(repassword_str)&userpassword_str.length()>6){
                        String url = "http://47.242.191.232:8080/UserEdit.do?username=" + username+"&nickname="+nickname_str + "&password=" + repassword_str;
                        final OkHttpClient okHttpClient = new OkHttpClient();
                        final Request request = new Request.Builder().url(url).get().build();
                        Call call =okHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                Looper.prepare();
                                Toast.makeText(getApplicationContext(), "请检查网络状态！", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                //获取服务器端返回的json数据
                                final String result = response.body().string();
                                System.out.println("从服务器获取的数据是:" + result);
                                //此时得到的只是一个JSON格式的字符串，需要将该字符串解析成对象
                                JSONObject object = JSONObject.parseObject(result);
                                int result_code = object.getInteger("result_code");
                                //从json数据中判断result_code的值
                                if (result_code == 200) {
                                    String username_str = update_nickname.getText().toString();
                                    Looper.prepare();
                                    Toast.makeText(getApplicationContext(), "您的新昵称："+nickname_str, Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getApplicationContext(), "您的新密码："+repassword_str, Toast.LENGTH_SHORT).show();


//                                    boolean flag = remove_userMes(EditMineActivity.this);
//                                    boolean flag2 = save_userMes(EditMineActivity.this, u.getUsername(), u.getPassword());
                                    Intent intent = new Intent(EditMineActivity.this, LoginActivity.class);
//                                            newInstance(u.getUsername());
//                                    intent.putExtra("nickname_extra",repassword_str);
                                    startActivity(intent);
                                    EditMineActivity.this.finish();

                                }else{
                                    Looper.prepare();
                                    if(TextUtils.isEmpty(nickname_str)){
                                        Toast.makeText(getApplicationContext(), "请输入昵称！", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getApplicationContext(), "修改失败！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                Looper.loop();
                            }
                        });
                    }else if(!userpassword_str.equals(repassword_str)){
                        Toast.makeText(getApplicationContext(), "两次密码输入不一致！", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "请输入六位以上密码！", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "请输入昵称！", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private boolean remove_userMes(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.commit();
        return true;
    }
    private boolean save_userMes(Context context,String username,String password){
        SharedPreferences sharedPreferences=context.getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("username_cd",username);
        editor.putString("password_cd",password);
        editor.commit();
        return true;
    }

}
