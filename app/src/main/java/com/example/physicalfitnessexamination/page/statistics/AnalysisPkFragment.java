package com.example.physicalfitnessexamination.page.statistics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.physicalfitnessexamination.R;

/**
 * 训练分析——PK分析
 */
public class AnalysisPkFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String[] title = new String[]{"单位总积分PK", "单位单项PK", "个人全能PK","单位岗位Pk","个人单项Pk"};

    /**
     * 训练分析Fragment实例化方法
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AnalysisPkFragment.
     */
    public static AnalysisPkFragment newInstance(String param1, String param2) {
        AnalysisPkFragment fragment = new AnalysisPkFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_analysis_pk, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = view.findViewById(R.id.tab_order);
        viewPager = view.findViewById(R.id.viewpage_order);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewPager.setAdapter(new PageAdapter(getChildFragmentManager()));
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
                    return UnitCreditsPkFragment.newInstance("", "");
                case 1:
                case 2:
                case 3:
                case 4:
                    return new Fragment();
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
}
