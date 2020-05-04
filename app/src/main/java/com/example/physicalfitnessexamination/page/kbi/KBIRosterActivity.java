package com.example.physicalfitnessexamination.page.kbi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.activity.UserManager;
import com.example.physicalfitnessexamination.app.Api;
import com.example.physicalfitnessexamination.base.MyBaseActivity;
import com.example.physicalfitnessexamination.bean.AssessmentInfoBean;
import com.example.physicalfitnessexamination.bean.MessageEvent;
import com.example.physicalfitnessexamination.bean.ParticipatingInstitutionsBean;
import com.example.physicalfitnessexamination.bean.ReferencePersonnelBean;
import com.example.physicalfitnessexamination.common.adapter.CommonAdapter;
import com.example.physicalfitnessexamination.okhttp.CallBackUtil;
import com.example.physicalfitnessexamination.okhttp.OkhttpUtil;
import com.example.physicalfitnessexamination.view.excel.SpinnerParentView;
import com.example.physicalfitnessexamination.viewholder.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 考核花名册
 */
public class KBIRosterActivity extends MyBaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageView imgRight;
    private TextView tvEnroll;//报名 普考不显示,考核单位没本单位也不显示
    private SpinnerParentView spvOrganization;
    private ListView lvLookRoster;
    private CommonAdapter<ReferencePersonnelBean> commonAdapter;
    private List<ReferencePersonnelBean> listRoster = new ArrayList<>();
    private List<ParticipatingInstitutionsBean> listPI = new ArrayList<>();
    private String id;//考核id
    private AssessmentInfoBean assessmentInfoBean;
    private boolean unit;
    private int flag;//1 已建考核进入  2 考核实施进入  3 历史考核进入

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    /**
     * 跳转方法
     *
     * @param context 上下文
     */
    public static void startInstant(Context context, String id, int flag) {
        Intent intent = new Intent(context, KBIRosterActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("flag", flag);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_kbi_roster;
    }

    @Override
    protected void initView() {
        id = getIntent().getStringExtra("id");
        flag = getIntent().getIntExtra("flag", 0);
        tvTitle = findViewById(R.id.tv_title);
        imgRight = findViewById(R.id.iv_right);
        imgRight.setOnClickListener(this::onClick);
        spvOrganization = findViewById(R.id.spv_organization);
        lvLookRoster = findViewById(R.id.lv_look_roster);
        tvEnroll = findViewById(R.id.tv_enroll);
        tvEnroll.setOnClickListener(this::onClick);
    }

    @Override
    protected void initData() {
        switch (flag){
            case 1:
                tvTitle.setText("已建考核 - 考核花名册");
                break;
            case 2:
                tvTitle.setText("考核实施 - 考核花名册");
                break;
        }
        spvOrganization.setName("单位");
        getData();
        commonAdapter = new CommonAdapter<ReferencePersonnelBean>(this, R.layout.item_kbi_roster, listRoster) {
            @Override
            public void convert(ViewHolder viewHolder, ReferencePersonnelBean s) {
                if ("0".equals(s.getSTATUS())) {
                    viewHolder.getView(R.id.lin_content).setBackgroundColor(Color.parseColor("#E6ECFA"));
                } else {
                    viewHolder.getView(R.id.lin_content).setBackgroundColor(Color.parseColor("#EF7D65"));
                }
                viewHolder.setText(R.id.tv_order, viewHolder.getPosition() + 1 + "");
                viewHolder.setText(R.id.tv_name, s.getUSERNAME());
                viewHolder.setText(R.id.tv_sex, s.getSEX());
                viewHolder.setText(R.id.tv_age, s.getAGE());
                viewHolder.setText(R.id.tv_job, s.getZW());
                viewHolder.setText(R.id.tv_post, s.getGW());
                viewHolder.setText(R.id.tv_unit, s.getORG_NAME());
            }
        };
        lvLookRoster.setAdapter(commonAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_right:
                finish();
                break;
            case R.id.tv_enroll:
                getPersonList1();
                break;
            default:
                break;
        }
    }

    public void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        OkhttpUtil.okHttpPost(Api.GETCREATAORG, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    listPI.addAll(JSON.parseArray(JSON.parseObject(response).getString("data"), ParticipatingInstitutionsBean.class));
                    for (int i = 0; i < listPI.size(); i++) {
                        if (listPI.get(i).getORG_ID().equals(UserManager.getInstance().getUserInfo(KBIRosterActivity.this).getOrg_id())) {
                            unit = true;
                        }
                    }
                    getAssessmentInfo();
                    HashSet<Integer> defSet = new HashSet();
                    defSet.add(0);
                    spvOrganization.setSpinner(listPI.toArray(), new SpinnerParentView.OnGetStrListener() {
                        @NotNull
                        @Override
                        public String getStr(Object bean) {
                            ParticipatingInstitutionsBean participatingInstitutionsBean = (ParticipatingInstitutionsBean) bean;
                            return participatingInstitutionsBean.getORG_NAME();
                        }
                    }, new SpinnerParentView.OnCheckListener() {
                        @Override
                        public void onConfirmAndChangeListener(@NotNull SpinnerParentView view, @NotNull List selectBeanList) {
                            getPersonList(((ParticipatingInstitutionsBean) selectBeanList.get(0)).getORG_ID());
                        }
                    }, true, defSet);
                    getPersonList(((ParticipatingInstitutionsBean) spvOrganization.getSelectList().get(0)).getORG_ID());
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void getPersonList(String org_id) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("org_id", org_id);
        OkhttpUtil.okHttpPost(Api.GETASSESSMENTPERSONLIST, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    listRoster.clear();
                    listRoster.addAll(JSON.parseArray(JSON.parseObject(response).getString("data"), ReferencePersonnelBean.class));
                    commonAdapter.notifyDataSetChanged();
                } else {
                    listRoster.clear();
                    commonAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void getAssessmentInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        OkhttpUtil.okHttpPost(Api.GETASSESSMENTINFO, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    assessmentInfoBean = JSON.parseObject(JSON.parseObject(response).getString("data"), AssessmentInfoBean.class);
                    if (!"0".equals(assessmentInfoBean.getPERSON_TYPE()) && unit) {
                        tvEnroll.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public void getPersonList1() {//用于反选已报名人员
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("org_id", UserManager.getInstance().getUserInfo(this).getOrg_id());
        OkhttpUtil.okHttpPost(Api.GETASSESSMENTPERSONLIST, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                List<ReferencePersonnelBean> list = new ArrayList<>();
                if (success) {
                    list.addAll(JSON.parseArray(JSON.parseObject(response).getString("data"), ReferencePersonnelBean.class));
                    KBIRosterEnrollActivity.startInstant(KBIRosterActivity.this, assessmentInfoBean.getREQUIREMENT_PERSON(), assessmentInfoBean.getPERSON_TYPE(), id, list);
                } else {
                    KBIRosterEnrollActivity.startInstant(KBIRosterActivity.this, assessmentInfoBean.getREQUIREMENT_PERSON(), assessmentInfoBean.getPERSON_TYPE(), id, list);
                }
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        switch (messageEvent.getMessage()) {
            case "刷新参赛人员列表":

                break;
            default:
                break;
        }
    }
}
