package com.example.physicalfitnessexamination.page.myKbi;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.activity.UserManager;
import com.example.physicalfitnessexamination.app.Api;
import com.example.physicalfitnessexamination.bean.MyAchievementBean;
import com.example.physicalfitnessexamination.bean.UserInfo;
import com.example.physicalfitnessexamination.common.adapter.CommonAdapter;
import com.example.physicalfitnessexamination.okhttp.CallBackUtil;
import com.example.physicalfitnessexamination.okhttp.OkhttpUtil;
import com.example.physicalfitnessexamination.viewholder.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 我的考核-我的成绩页面
 */
public class MyAchievementFragment extends Fragment {
    private ListView lvMyAchievement;
    private CommonAdapter<MyAchievementBean.MyAchievement> commonAdapter;
    private List<MyAchievementBean.MyAchievement> listMyAchievement = new ArrayList<>();
    private UserInfo userInfo;
    private MyAchievementBean myAchievementBean;
    private TextView tvRemark;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo = UserManager.getInstance().getUserInfo(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_achievement, null);
        lvMyAchievement = view.findViewById(R.id.lv_my_achievement);
        tvRemark = view.findViewById(R.id.tv_remark);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        commonAdapter = new CommonAdapter<MyAchievementBean.MyAchievement>(getContext(), R.layout.item_my_achievement, listMyAchievement) {
            @Override
            public void convert(ViewHolder viewHolder, MyAchievementBean.MyAchievement s) {
                viewHolder.setText(R.id.tv_clause, s.getNAME());
                viewHolder.setText(R.id.tv_detachment_record, s.getZAC());
                viewHolder.setText(R.id.tv_my_best_achievement, s.getWAC());
                viewHolder.setText(R.id.tv_detachment_ranking, s.getDENSE_RANK());
                viewHolder.setText(R.id.tv_single_integral, s.getWSCORE());
                viewHolder.setText(R.id.tv_average_points_of_detachment, s.getZSCORE());
            }
        };
        lvMyAchievement.setAdapter(commonAdapter);
        getData();
    }

    public void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("org_id", userInfo.getOrg_id());
        map.put("username", userInfo.getUsername());
        OkhttpUtil.okHttpPost(Api.GETACHIEVEMENT4MINE, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    myAchievementBean = JSON.parseObject(JSON.parseObject(response).getString("data"), MyAchievementBean.class);
                    if (myAchievementBean.getList() != null) {
                        listMyAchievement.addAll(myAchievementBean.getList());
                        commonAdapter.notifyDataSetChanged();
                    }
                    tvRemark.setText(myAchievementBean.getRemark());
                }
            }
        });
    }
}
