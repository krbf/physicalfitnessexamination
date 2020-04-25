package com.example.physicalfitnessexamination.page.myKbi;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.common.adapter.CommonAdapter;
import com.example.physicalfitnessexamination.viewholder.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MyAchievementFragment extends Fragment {
    private ListView lvMyAchievement;
    private CommonAdapter<String> commonAdapter;
    private List<String> listMyAchievement = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_achievement, null);
        lvMyAchievement = view.findViewById(R.id.lv_my_achievement);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        commonAdapter = new CommonAdapter<String>(getContext(), R.layout.item_my_achievement, listMyAchievement) {
            @Override
            public void convert(ViewHolder viewHolder, String s) {
                viewHolder.setText(R.id.tv_clause, "");
                viewHolder.setText(R.id.tv_detachment_record,"");
                viewHolder.setText(R.id.tv_my_best_achievement,"");
                viewHolder.setText(R.id.tv_detachment_ranking,"");
                viewHolder.setText(R.id.tv_single_integral,"");
                viewHolder.setText(R.id.tv_average_points_of_detachment,"");
            }
        };
        lvMyAchievement.setAdapter(commonAdapter);
    }
}
