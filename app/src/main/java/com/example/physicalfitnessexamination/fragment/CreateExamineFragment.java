package com.example.physicalfitnessexamination.fragment;

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
import android.widget.Button;

import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.activity.RosterActivity;

public class CreateExamineFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Button btnAdd;
    private String[] title = new String[]{"新建考核", "已建考核", "考核实施"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_examine, null);
        tabLayout = view.findViewById(R.id.tab_order);
        viewPager = view.findViewById(R.id.viewpage_order);
        btnAdd = view.findViewById(R.id.btn_add);

        btnAdd.setOnClickListener(v -> {
            RosterActivity.startActivity(getContext());
        });
        return view;
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
                    return new AddExamineFragment();
                case 1:
                    return new HistoryExamineFragment();
                case 2:
                    return new ImplementFragment();
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
