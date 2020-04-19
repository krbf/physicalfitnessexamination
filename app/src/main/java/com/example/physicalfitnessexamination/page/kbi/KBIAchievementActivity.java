package com.example.physicalfitnessexamination.page.kbi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.base.MyBaseActivity;

/**
 * 考核成绩页
 */
public class KBIAchievementActivity extends MyBaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageView imgRight;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String[] title = new String[]{"成绩汇总", "成绩录入"};
    private String id;//考核id

    /**
     * 跳转方法
     *
     * @param context 上下文
     */
    public static void startInstant(Context context, String id) {
        Intent intent = new Intent(context, KBIAchievementActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_kbi_achievement;
    }

    @Override
    protected void initView() {
        id = getIntent().getStringExtra("id");
        tvTitle = findViewById(R.id.tv_title);
        imgRight = findViewById(R.id.iv_right);
        imgRight.setOnClickListener(this::onClick);
        tabLayout = findViewById(R.id.tab_order);
        viewPager = findViewById(R.id.viewpage_order);
    }

    @Override
    protected void initData() {
        tvTitle.setText("考核成绩表");
        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);
    }

    public class PageAdapter extends FragmentStatePagerAdapter {
        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    KBIGatherFragment kbiGatherFragment = new KBIGatherFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("id", id);
                    kbiGatherFragment.setArguments(bundle);
                    return kbiGatherFragment;
                case 1:
                    KBIResultInputFragment kbiResultInputFragment = new KBIResultInputFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("id", id);
                    kbiResultInputFragment.setArguments(bundle1);
                    return kbiResultInputFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return title.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_right:
                finish();
                break;
            default:
                break;
        }
    }
}
