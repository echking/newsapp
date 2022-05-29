package com.example.ts.news.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.ts.news.R;
import com.example.ts.news.Utils.AlbumUtil;
import com.example.ts.news.Utils.ApplicationUtil;
import com.example.ts.news.Utils.MyDatabaseHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;

    private TextView save_user;
    private ImageView shangchuan_head,back;
    private EditText username,nickname, userpassword, repassword;
    private CheckBox checkBox;

    private static final int CHOSSE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        save_user =(TextView) findViewById(R.id.save_user);
        shangchuan_head =(ImageView) findViewById(R.id.shangchuan_head);
        username =(EditText) findViewById(R.id.register_username);
        nickname=(EditText)findViewById(R.id.register_nickname);
        userpassword =(EditText) findViewById(R.id.register_password);
        repassword =(EditText) findViewById(R.id.register_repassword);
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(RegisterActivity.this,LoginOrRegisterActivity.class);
                startActivity(it);
            }
        });
        shangchuan_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(RegisterActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RegisterActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
        });

        save_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username_str = username.getText().toString();
                final String nickname_str = nickname.getText().toString();
                String userpassword_str = userpassword.getText().toString();
                final String repassword_str = repassword.getText().toString();
                    if(!TextUtils.isEmpty(username_str)) {
                        if(userpassword_str.equals(repassword_str)&userpassword_str.length()>6){
                            String url = "http://47.242.191.232:8080/Register.do?username=" + username_str + "&password=" + repassword_str + "&nickname=" + nickname_str;
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
                                        String username_str = username.getText().toString();
                                        JSONObject node = object.getJSONObject("user");
                                        User u = JSONObject.toJavaObject(node, User.class);
                                        System.out.println(u.getUsername() + "---->" + u.getPassword() + "--->" + u.getNickname());
                                    Looper.prepare();
                                    Toast.makeText(getApplicationContext(), u.getUsername() +"注册成功！", Toast.LENGTH_SHORT).show();
                                    //通过putExtra方法将用户名和密码传递到登录界面
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    intent.putExtra("username_extra",u.getUsername());
                                    intent.putExtra("password_extra",u.getPassword());
                                    //跳转到登录界面
                                    startActivity(intent);
                                    }else{
                                        Looper.prepare();
                                        if(TextUtils.isEmpty(nickname_str)){
                                            Toast.makeText(getApplicationContext(), "请输入昵称！", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(getApplicationContext(), "用户名已存在，请重新填写！", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(), "请输入用户名！", Toast.LENGTH_SHORT).show();
                    }
                }


        });



        ApplicationUtil.getInstance().addActivity(this);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOSSE_PHOTO);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOSSE_PHOTO:
                if (resultCode == -1) {
                    String imgPath = AlbumUtil.handleImageOnKitKat(this, data);
                    setHead(imgPath);
                }
                break;
            default:
                break;
        }
    }
    private void setHead(String imgPath) {
        if (imgPath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
            Bitmap round = AlbumUtil.toRoundBitmap(bitmap);
            try {
                String path = getCacheDir().getPath();
                File file = new File(path,"user_head");
                round.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            shangchuan_head.setImageBitmap(round);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }
}
