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

public class MyHistoryKbiFragment extends Fragment {
    private ListView lvMyHistory;
    private CommonAdapter<String> commonAdapter;
    private List<String> listHistory = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_history_kbi, null);
        lvMyHistory = view.findViewById(R.id.lv_my_history);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        commonAdapter = new CommonAdapter<String>(getContext(), R.layout.item_my_history_kbi, listHistory) {
            @Override
            public void convert(ViewHolder viewHolder, String s) {
                viewHolder.setText(R.id.tv_assessment_time, "");
                viewHolder.setText(R.id.tv_organizational_unit,"");
                viewHolder.setText(R.id.tv_achievement,"");
                viewHolder.setText(R.id.tv_ranking,"");
            }
        };
        lvMyHistory.setAdapter(commonAdapter);
    }
}
