package com.example.physicalfitnessexamination.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.view.NoScrollViewPager;

public class MainActivity extends BaseActivity {
    private TabLayout tab_net;
    private NoScrollViewPager viewpage_net;
    private String[] title = new String[]{"创建考核", "考核计划", "统计分析", "英雄榜", "相关文件"};
    private int[] pics=new int[]{R.drawable.drawable_tab_image,R.drawable.drawable_tab_image2,R.drawable.drawable_tab_image3,R.drawable.drawable_tab_image4,R.drawable.drawable_tab_image5};
    private long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setScreenRoate(true);
        super.onCreate(savedInstanceState);
        setActivityContentView(R.layout.activity_main);
        getToolBar().setTitle("体能考核");
        getToolBar().setLeftVisible(false);
        getToolBar().setRightImgVislble(false);
    }

    @Override
    public void initView() {
        tab_net = findViewById(R.id.tab_order);
        viewpage_net = findViewById(R.id.viewpage_order);
    }

    @Override
    public void initBind() {

    }

    @Override
    public void initData() {
        viewpage_net.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        viewpage_net.setCurrentItem(0);
        tab_net.setupWithViewPager(viewpage_net);
        for (int i = 0; i < tab_net.getTabCount(); i++) {
            tab_net.getTabAt(i).setCustomView(getTabView(i));
        }
    }

    @Override
    public void widgetClick(View v) {

    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override

        public Fragment getItem(int position) {
            if (position == 0) {
                return new Fragment();
            } else if (position == 1) {
                return new Fragment();
            } else if (position == 2) {
                return new Fragment();
            } else if (position == 3) {
                return new Fragment();
            } else if (position == 4) {
                return new Fragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return title.length;

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_tab, null);
        ImageView imageView=view.findViewById(R.id.img_src);
        imageView.setBackgroundResource(pics[position]);
        TextView textView = view.findViewById(R.id.tv_title);
        textView.setText(title[position]);
        return view;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - time > 2000) {
                toast(this, "再按一次退出程序");
                time = System.currentTimeMillis();
            } else {
                finish();
            }
        }
        return false;
    }
}
