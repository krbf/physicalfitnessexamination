package com.example.physicalfitnessexamination.page.kbi;

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

public class KBIResultInputFragment extends Fragment {
    private ListView lvResultInput;
    private CommonAdapter<String> commonAdapter;
    private List<String> list = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kbi_resultinput, null);
        lvResultInput = view.findViewById(R.id.lv_result_input);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        commonAdapter = new CommonAdapter<String>(getContext(), R.layout.item_kbi_resultinput, list) {
            @Override
            public void convert(ViewHolder viewHolder, String s) {

            }
        };
        lvResultInput.setAdapter(commonAdapter);
    }
}
