package com.example.ts.news.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.ts.news.Activity.CollectionActivity;
import com.example.ts.news.Activity.EditMineActivity;
import com.example.ts.news.Activity.EditQm;
import com.example.ts.news.Activity.LoginOrRegisterActivity;
import com.example.ts.news.Activity.SearchActivity;
import com.example.ts.news.Activity.User;
import com.example.ts.news.R;
import com.example.ts.news.Utils.AlbumUtil;
import com.example.ts.news.Utils.SharedPreUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MineFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView edit_mine, exit_login, collection, mine_user_name,user_qm;
    private ImageView my_head;
    private static final int CHOSSE_PHOTO = 1;
    private Context mContext;
    private Button go;
    private EditText search_ed;
    private SearchView search;
    public Handler myHandler=new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//            JSON listJson = (JSON) JSON.toJSON(msg.obj);
//            JSONArray arr = JSONArray.parseArray(String.valueOf(listJson));
//            for (int i = 0; i < arr.size(); i++) {
//                JSONObject temp = (JSONObject) arr.get(i);
//                // Log.d("json", temp.getInt("id")+temp.getString("exp_name")+temp.getString("exp_tech"));
//                User u = new User();
//                u.setqm(temp.getString("qm"));
//            }
            String qms=msg.obj.toString();
            user_qm.setText(qms);
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView();
        edit_mine.setOnClickListener(this);
        exit_login.setOnClickListener(this);
        collection.setOnClickListener(this);
        my_head.setOnClickListener(this);
        user_qm.setOnClickListener(this);

        final String user_qms = getActivity().getIntent().getStringExtra("user_qm");
        user_qm.setText(user_qms);

        try {
//            String username_login = getArguments().getString("username_bundle");
//            mine_user_name.setText(username_login);
//            final String nickname_login = getActivity().getIntent().getStringExtra("nickname_extra");
            String username_login = (String) SharedPreUtil.getParam(getContext(), SharedPreUtil.LOGIN_DATA, "");
            String username_str=username_login;
            String nickname_data = (String) SharedPreUtil.getNickName(getContext(), SharedPreUtil.NICKNAME_DATA, "");
            String nickname_str=nickname_data;
            mine_user_name.setText(nickname_str);
            //edit qm----
            //edit qm----
            this.mContext = getActivity();
            //当未传递值过来时
//            if(username_login==null){
            if(username_str!="") {
                //个签----------
                String url = "http://47.242.191.232:8080/UserGetQm.do?username=" + username_str;
                final OkHttpClient okHttpClient = new OkHttpClient();
                final Request request = new Request.Builder().url(url).get().build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Looper.prepare();
                        Toast.makeText(mContext, "网络连接异常", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        //获取服务器端返回的json数据
                        final String result = response.body().string();
                        System.out.println("从服务器获取的数据是:" + result);
                        //此时得到的只是一个JSON格式的字符串，需要将该字符串解析成对象
                        JSONObject object = JSONObject.parseObject(result);
//                    JSONArray jsonItems = object.getJSONArray("qmm");
//                    for (int i = 0; i <2; i++) {
//                        JSONArray jsonData = jsonItems.getJSONObject(i).getJSONArray("qm");
//                             }
                        //从json数据中判断result_code的值
                        String data = object.toString();
                        System.out.println("数据为：" + data);
                        JSONObject node = object.getJSONObject("qmm");
                        User u = JSONObject.toJavaObject(node, User.class);
                        if(u.getqm()!=null) {
                            System.out.println("签名：" + u.getqm());
                            Message message = new Message();
                            message.obj = u.getqm();
                            myHandler.sendMessage(message);
                        }else{
                            //新用户没有签名，设置一个默认的签名
                            Message message = new Message();
                            message.obj = "这个人很懒，还没有描述~";
                            myHandler.sendMessage(message);
                        }
                    }
                });
            }else{
                Message message = new Message();
                message.obj = "这个人很懒，还没有描述~";
                myHandler.sendMessage(message);
            }
            //-------------
            String path = getContext().getCacheDir().getPath();
            String fileName = "user_head";
            File file = new File(path, fileName);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                Bitmap round = AlbumUtil.toRoundBitmap(bitmap);
                my_head.setImageBitmap(round);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;

    }


    private void initView() {
        mine_user_name = view.findViewById(R.id.mine_user_name);
        edit_mine = view.findViewById(R.id.edit_mine);
        exit_login = view.findViewById(R.id.exit_login);
        collection = view.findViewById(R.id.collection);
        my_head = view.findViewById(R.id.my_head);
        user_qm=view.findViewById(R.id.user_qm);
        go=view.findViewById(R.id.go);
        search_ed=view.findViewById(R.id.search_ed);
        search=view.findViewById(R.id.search_view);
//        edit_userqm=view.findViewById(R.id.edit_userqm);
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
                    String imgPath = AlbumUtil.handleImageOnKitKat(getContext(), data);
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
                String path = getContext().getCacheDir().getPath();
                File file = new File(path, "user_head");
                round.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            my_head.setImageBitmap(round);
        } else {
            Toast.makeText(getContext(), "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_qm:{
                String username_login = (String) SharedPreUtil.getParam(getContext(), SharedPreUtil.LOGIN_DATA, "");
                String username_str=username_login;
                Intent intent =new Intent(getContext(), EditQm.class);
                intent.putExtra("username_extra",username_str);
                startActivity(intent);
            }
            break;
            case R.id.edit_mine: {
                String username_login = (String) SharedPreUtil.getParam(getContext(), SharedPreUtil.LOGIN_DATA, "");
                String username_str=username_login;
                Intent intent = new Intent(getContext(), EditMineActivity.class);
                intent.putExtra("username_extra",username_str);
                startActivity(intent);
            }
            break;
            case R.id.exit_login: {
                SharedPreUtil.setParam(getContext(), SharedPreUtil.IS_LOGIN, false);
                SharedPreUtil.removeParam(getContext(), SharedPreUtil.LOGIN_DATA);

                //重新跳转到登录页面
                Intent intent = new Intent(getContext(), LoginOrRegisterActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
            break;
            case R.id.collection: {
                Intent intent = new Intent(getContext(), CollectionActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.go:{
                Intent intent = new Intent(getContext(), SearchActivity.class);
                String title=search_ed.getText().toString();
                intent.putExtra("title_search",title);
                startActivity(intent);
            }
            break;
            case R.id.my_head: {
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
            break;
            default:
                break;

        }
    }
}

