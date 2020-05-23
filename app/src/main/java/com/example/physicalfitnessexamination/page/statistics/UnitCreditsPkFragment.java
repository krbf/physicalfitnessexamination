package com.example.physicalfitnessexamination.page.statistics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.czy.module_common.okhttp.CallBackUtil;
import com.czy.module_common.okhttp.OkhttpUtil;
import com.example.physicalfitnessexamination.Constants;
import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.activity.UserManager;
import com.example.physicalfitnessexamination.app.Api;
import com.example.physicalfitnessexamination.bean.ParticipatingInstitutionsBean;
import com.example.physicalfitnessexamination.bean.PostBean;
import com.example.physicalfitnessexamination.bean.UnitCreditsPkBean;
import com.example.physicalfitnessexamination.bean.UserInfo;
import com.example.physicalfitnessexamination.view.HistogramView;
import com.example.physicalfitnessexamination.view.excel.SpinnerParentView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UnitCreditsPkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnitCreditsPkFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SpinnerParentView spvUnit1, spvUnit2;
    private UserInfo userInfo;
    private List<PostBean> listType = new ArrayList<>();
    private List<ParticipatingInstitutionsBean> listPI = new ArrayList<>();
    private List<UnitCreditsPkBean> listData = new ArrayList<>();
    private HistogramView histogramView;

    public UnitCreditsPkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UnitCreditsPkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UnitCreditsPkFragment newInstance(String param1, String param2) {
        UnitCreditsPkFragment fragment = new UnitCreditsPkFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo = UserManager.getInstance().getUserInfo(getContext());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_unit_credits_pk, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spvUnit1 = view.findViewById(R.id.spv_unit1);
        spvUnit2 = view.findViewById(R.id.spv_unit2);
        histogramView = view.findViewById(R.id.hv_pk);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        spvUnit1.setName("层级");
        spvUnit2.setName("机构");
        switch (userInfo.getRole_id()) {
            case Constants.RoleIDStr.COMM:
                PostBean postBean = new PostBean();
                postBean.setName("消防站");
                postBean.setType("3");
                listType.add(postBean);
                break;
            case Constants.RoleIDStr.MANAGE:
            case Constants.RoleIDStr.MANAGE_BRIGADE:
                PostBean postBean1 = new PostBean();
                postBean1.setName("大队");
                postBean1.setType("2");
                listType.add(postBean1);
                PostBean postBean2 = new PostBean();
                postBean2.setName("消防站");
                postBean2.setType("3");
                listType.add(postBean2);
                break;
            default:
                break;
        }
        HashSet<Integer> defSet = new HashSet();
        //defSet.add(0);
        spvUnit1.setSpinner(listType.toArray(), new SpinnerParentView.OnGetStrListener() {
            @NotNull
            @Override
            public String getStr(Object bean) {
                return ((PostBean) bean).getName();
            }
        }, new SpinnerParentView.OnCheckListener() {
            @Override
            public void onConfirmAndChangeListener(@NotNull SpinnerParentView view, @NotNull List selectBeanList) {
                getGroup(((PostBean) (selectBeanList.get(0))).getType());
            }
        }, true, defSet);
    }

    public void getGroup(String type) {
        Map<String, String> map = new HashMap<>();
        map.put("org_id", userInfo.getOrg_id());
        map.put("hierarchy_restriction", "1");
        map.put("type", type);
        OkhttpUtil.okHttpPost(Api.GETORGFORASSESSMENT, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    listPI.clear();
                    listPI.addAll(JSON.parseArray(JSON.parseObject(response).getString("data"), ParticipatingInstitutionsBean.class));
                    HashSet<Integer> defSet = new HashSet();
                    spvUnit2.setSpinner(listPI.toArray(), new SpinnerParentView.OnGetStrListener() {
                        @NotNull
                        @Override
                        public String getStr(Object bean) {
                            ParticipatingInstitutionsBean participatingInstitutionsBean = (ParticipatingInstitutionsBean) bean;
                            return participatingInstitutionsBean.getORG_NAME();
                        }
                    }, new SpinnerParentView.OnCheckListener() {
                        @Override
                        public void onConfirmAndChangeListener(@NotNull SpinnerParentView view, @NotNull List selectBeanList) {
                            List<ParticipatingInstitutionsBean> list = (List<ParticipatingInstitutionsBean>) selectBeanList;
                            String org_id = "";
                            for (ParticipatingInstitutionsBean participatingInstitutionsBean : list) {
                                org_id = participatingInstitutionsBean.getORG_ID() + "," + org_id;
                            }
                            getPk(org_id);
                        }
                    }, false, defSet);
                }
            }
        });
    }

    public void getPk(String org_id) {
        Map<String, String> map = new HashMap<>();
        map.put("org_id", org_id);
        OkhttpUtil.okHttpPost(Api.ORGSCOREPK, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    listData.clear();
                    listData.addAll(JSON.parseArray(JSON.parseObject(response).getString("data"), UnitCreditsPkBean.class));
                    histogramView.setDataList(listData);
                    histogramView.startAnimation();
                }
            }
        });
    }
}
