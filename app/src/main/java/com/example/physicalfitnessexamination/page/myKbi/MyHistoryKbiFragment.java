package com.example.physicalfitnessexamination.page.myKbi;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.activity.UserManager;
import com.example.physicalfitnessexamination.app.Api;
import com.example.physicalfitnessexamination.bean.MyClauseBean;
import com.example.physicalfitnessexamination.bean.MyHistoryKbiBean;
import com.example.physicalfitnessexamination.bean.ParticipatingInstitutionsBean;
import com.example.physicalfitnessexamination.bean.ReferencePersonnelBean;
import com.example.physicalfitnessexamination.bean.UserInfo;
import com.example.physicalfitnessexamination.common.adapter.CommonAdapter;
import com.example.physicalfitnessexamination.okhttp.CallBackUtil;
import com.example.physicalfitnessexamination.okhttp.OkhttpUtil;
import com.example.physicalfitnessexamination.page.kbi.KBIRosterActivity;
import com.example.physicalfitnessexamination.view.excel.SpinnerParentView;
import com.example.physicalfitnessexamination.viewholder.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class MyHistoryKbiFragment extends Fragment {
    private ListView lvMyHistory;
    private CommonAdapter<MyHistoryKbiBean> commonAdapter;
    private List<MyHistoryKbiBean> listHistory = new ArrayList<>();
    private List<MyClauseBean> listClause = new ArrayList<>();
    private SpinnerParentView spvClause;//项目
    private UserInfo userInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo = UserManager.getInstance().getUserInfo(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_history_kbi, null);
        lvMyHistory = view.findViewById(R.id.lv_my_history);
        spvClause = view.findViewById(R.id.spv_clause);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        spvClause.setName("项目");
        getData();
        commonAdapter = new CommonAdapter<MyHistoryKbiBean>(getContext(), R.layout.item_my_history_kbi, listHistory) {
            @Override
            public void convert(ViewHolder viewHolder, MyHistoryKbiBean myHistoryKbiBean) {
                viewHolder.setText(R.id.tv_assessment_time, myHistoryKbiBean.getCREATETIME());
                viewHolder.setText(R.id.tv_organizational_unit, myHistoryKbiBean.getNAME());
                viewHolder.setText(R.id.tv_achievement, myHistoryKbiBean.getACHIEVE());
                viewHolder.setText(R.id.tv_ranking, myHistoryKbiBean.getDENSE_RANK());
            }
        };
        lvMyHistory.setAdapter(commonAdapter);
    }

    public void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("org_id", userInfo.getOrg_id());
        map.put("username", userInfo.getUsername());
        OkhttpUtil.okHttpPost(Api.GETSUBJECT4MINE, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    listClause.addAll(JSON.parseArray(JSON.parseObject(response).getString("data"), MyClauseBean.class));
                    HashSet<Integer> defSet = new HashSet();
                    defSet.add(0);
                    spvClause.setSpinner(listClause.toArray(), new SpinnerParentView.OnGetStrListener() {
                        @NotNull
                        @Override
                        public String getStr(Object bean) {
                            MyClauseBean myClauseBean = (MyClauseBean) bean;
                            return myClauseBean.getNAME();
                        }
                    }, new SpinnerParentView.OnCheckListener() {
                        @Override
                        public void onConfirmAndChangeListener(@NotNull SpinnerParentView view, @NotNull List selectBeanList) {
                            getMyKbiList(((MyClauseBean) selectBeanList.get(0)).getID());
                        }
                    }, true, defSet);
                    getMyKbiList(((MyClauseBean) spvClause.getSelectList().get(0)).getID());
                }
            }
        });
    }

    public void getMyKbiList(String sid) {
        Map<String, String> map = new HashMap<>();
        map.put("org_id", userInfo.getOrg_id());
        map.put("username", userInfo.getUsername());
        map.put("sid", sid);
        OkhttpUtil.okHttpPost(Api.GETOLDASSESSMENT4MINE, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    listHistory.clear();
                    listHistory.addAll(JSON.parseArray(JSON.parseObject(response).getString("data"), MyHistoryKbiBean.class));
                    commonAdapter.notifyDataSetChanged();
                } else {
                    listHistory.clear();
                    commonAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
