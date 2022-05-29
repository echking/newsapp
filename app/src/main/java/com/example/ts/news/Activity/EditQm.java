package com.example.ts.news.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.ts.news.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EditQm extends AppCompatActivity {
    private EditText edit_qm;
    private ImageView back;
    private TextView update_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_qm);
        edit_qm=(EditText) findViewById(R.id.edit_qm);
        back=(ImageView) findViewById(R.id.img_back);
        update_user=(TextView) findViewById(R.id.update_user);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditQm.this.finish();
            }
        });
        update_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String user_qm=edit_qm.getText().toString();
                Intent it=getIntent();
                //从MainActivity中获取username，password，mail
                String username=it.getStringExtra("username_extra");
                if(!user_qm.equals("")) {
                        String url = "http://47.242.191.232:8080/UserQmEdit.do?username=" + username + "&qm=" + user_qm;
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
                                    Intent intent = new Intent(EditQm.this, MainActivity.class);
                                    intent.putExtra("user_qm",user_qm);
                                    startActivity(intent);
                                    EditQm.this.finish();
                                }else{
                                    Looper.prepare();
                                        Toast.makeText(getApplicationContext(), "网络错误！", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            }
                        });
                    }
            }
        });
    }
}