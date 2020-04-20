package com.example.physicalfitnessexamination.page.kbi;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.activity.UserManager;
import com.example.physicalfitnessexamination.app.Api;
import com.example.physicalfitnessexamination.bean.BuiltKBIListBean;
import com.example.physicalfitnessexamination.bean.ClauseBean;
import com.example.physicalfitnessexamination.common.adapter.CommonAdapter;
import com.example.physicalfitnessexamination.okhttp.CallBackUtil;
import com.example.physicalfitnessexamination.okhttp.OkhttpUtil;
import com.example.physicalfitnessexamination.util.ToastUtil;
import com.example.physicalfitnessexamination.viewholder.ViewHolder;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 成绩录入
 */
public class KBIResultInputFragment extends Fragment {
    private ListView lvResultInput;
    private CommonAdapter<ClauseBean> commonAdapterClause;
    private CommonAdapter<ClauseBean.Clause> commonAdapterPost;
    private List<ClauseBean> listClause = new ArrayList<>();
    private String id;//考核id

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getString("id");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kbi_resultinput, null);
        lvResultInput = view.findViewById(R.id.lv_result_input);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getData();
        commonAdapterClause = new CommonAdapter<ClauseBean>(getContext(), R.layout.item_kbi_resultinput, listClause) {
            @Override
            public void convert(ViewHolder viewHolder, ClauseBean s) {
                viewHolder.setText(R.id.tv_clause, s.getGW());
                String gw = s.getGW();
                ListView listView = viewHolder.getView(R.id.lv_post);
                final boolean[] isShow = {false};
                commonAdapterPost = new CommonAdapter<ClauseBean.Clause>(getContext(), R.layout.item_kbi_resultinput_child, s.getSUBJECT()) {
                    @Override
                    public void convert(ViewHolder viewHolder, ClauseBean.Clause s) {
                        viewHolder.setText(R.id.tv_post, s.getNAME());
                        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                KBIAchievementTakeNotesActivity.startInstant(getContext(), id, s.getSID(), gw,s.getTYPE(),s.getNAME());
                            }
                        });
                    }
                };
                listView.setAdapter(commonAdapterPost);
                viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isShow[0]) {
                            listView.setVisibility(View.VISIBLE);
                            isShow[0] = true;
                        } else {
                            listView.setVisibility(View.GONE);
                            isShow[0] = false;
                        }

                    }
                });
            }
        };
        lvResultInput.setAdapter(commonAdapterClause);
    }

    public void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("ID", id);
        OkhttpUtil.okHttpPost(Api.GETSUBJECTFORASSESSMENT, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    listClause.addAll(JSON.parseArray(JSON.parseObject(response).getString("data"), ClauseBean.class));
                    commonAdapterClause.notifyDataSetChanged();
                }
            }
        });
    }
}
