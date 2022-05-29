package com.example.ts.news.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.ts.news.Activity.SearchGo;
import com.example.ts.news.Adapter.FragmentAdapter;
import com.example.ts.news.R;
import com.example.ts.news.Utils.TimeCount;

import java.util.ArrayList;
import java.util.List;



public class MainFragment extends Fragment {

    private View view;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SearchView search_view;

    private List<String> titleList;
    private List<Fragment> fragmentList;

    private FragmentAdapter fragmentAdapter;

    private TechFragment tech_fragment;
    private EnteFragment ente_fragment;
    private SportFragment sport_fragment;
    private MiliFragment mili_fragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        initView();
        fragmentChange();
        TimeCount.getInstance().setTime(System.currentTimeMillis());
        return view;
    }

    private void initView() {
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        search_view=view.findViewById(R.id.search_view);
        search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(getContext(), SearchGo.class);
                startActivity(it);
            }
        });

    }

    private void fragmentChange() {
        fragmentList = new ArrayList<>();

        tech_fragment = new TechFragment();
        ente_fragment = new EnteFragment();
        sport_fragment = new SportFragment();
        mili_fragment = new MiliFragment();


        fragmentList.add(tech_fragment);
        fragmentList.add(ente_fragment);
        fragmentList.add(sport_fragment);
        fragmentList.add(mili_fragment);

        titleList = new ArrayList<>();
        titleList.add("安卓");
        titleList.add("学习");
        titleList.add("网页");
        titleList.add("教程");

        fragmentAdapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(fragmentAdapter);

        //将tabLayout与viewPager连起来
        tabLayout.setupWithViewPager(viewPager);
    }


}
