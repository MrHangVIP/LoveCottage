package com.zln.lovecottage.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.ImageView;


import com.szh.commonBase.BaseActivity;
import com.szh.commonBase.ui.widget.CustomViewPager;
import com.zln.lovecottage.R;
import com.zln.lovecottage.ui.fragment.LoginFragment;
import com.zln.lovecottage.ui.fragment.RegistFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * @discription 登录activity
 * @autor   songzhihang
 * @time   2017/10/13  下午5:24
 **/
public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";

    private ArrayList<Fragment> fragments;
    private ArrayList<String> titles;
    private ImageView closeIV;
    private TabLayout tabLayout;
    private CustomViewPager viewPager;
    private LoginAdapter homeAdapter;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_login_layout);
    }

    @Override
    protected void findViews() {
        closeIV=(ImageView)findViewById(R.id.iv_close);
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        viewPager = (CustomViewPager)findViewById(R.id.viewpager);
    }

    @Override
    protected void initData() {
        fragments = new ArrayList<>();
        fragments.add(new LoginFragment());
        fragments.add(new RegistFragment());
        titles = new ArrayList<>();
        titles.add("登录");
        titles.add("注册");

        viewPager.setPagingEnabled(false);
        homeAdapter = new LoginAdapter(getSupportFragmentManager(), fragments, titles);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        for(int i = 0; i < titles.size(); i ++){
            tabLayout.addTab(tabLayout.newTab().setText(titles.get(i)));
        }
        viewPager.setAdapter(homeAdapter);
        tabLayout.setupWithViewPager(viewPager);//绑定 ViewPager 和tabLayout
    }

    @Override
    protected void setListener() {
        closeIV.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_close:
                finish();
                break;
        }
    }

    public void tabFragment(){
        TabLayout.Tab tab= tabLayout.getTabAt(0);
        tab.select();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public class LoginAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;
        private List<String> titles;

        public LoginAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
            super(fm);
            this.fragments = fragments;
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            if (fragments != null && fragments.size() > position){
                return fragments.get(position);
            }
            return null;
        }

        @Override
        public int getCount() {
            if (titles != null){
                return titles.size();
            }
            return 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (titles != null && titles.size() > position)
            {
                return titles.get(position);
            }
            return super.getPageTitle(position);
        }
    }
}

