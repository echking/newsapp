package com.example.ts.news.Fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ts.news.R;
import com.example.ts.news.Utils.ApplicationUtil;
import com.example.ts.news.Utils.FileCacheUtils;




public class SettingFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;

    private TextView exit_app, about_app, check_version, welcome_app, clear_cache, app_notice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        initView();
        app_notice.setOnClickListener(this);
        welcome_app.setOnClickListener(this);
        check_version.setOnClickListener(this);
        about_app.setOnClickListener(this);
        clear_cache.setOnClickListener(this);
        exit_app.setOnClickListener(this);
        exit_app.setOnClickListener(this);
        return view;
    }

    private void clearCache() {
        try {
            String cacheSize = FileCacheUtils.getCacheSize(context.getCacheDir());
            FileCacheUtils.cleanInternalCache(context);
            Toast.makeText(context, "本次清理" + cacheSize + "缓存", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initView() {
        welcome_app = view.findViewById(R.id.welcome_app);
        check_version = view.findViewById(R.id.check_version);
        about_app = view.findViewById(R.id.about_app);
        clear_cache = view.findViewById(R.id.clear_cache);
        exit_app = view.findViewById(R.id.exit_app);
        app_notice = view.findViewById(R.id.app_notice);
        context = getContext();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.app_notice: {
            }
            break;
            case R.id.check_version: {
                Toast.makeText(getContext(), "当前已为最新版1.0", Toast.LENGTH_SHORT).show();
            }
            break;
            case R.id.about_app: {
                Toast.makeText(getContext(), "@News news.com version 1.0", Toast.LENGTH_SHORT).show();
            }
            break;
            case R.id.clear_cache: {
                clearCache();
            }
            break;
            case R.id.exit_app: {
                ApplicationUtil.getInstance().exit();
            }
            break;
            default:
                break;

        }
    }
}
