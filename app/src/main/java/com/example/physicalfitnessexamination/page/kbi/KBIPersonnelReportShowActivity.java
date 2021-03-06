package com.example.physicalfitnessexamination.page.kbi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.czy.module_common.okhttp.CallBackUtil;
import com.czy.module_common.okhttp.OkhttpUtil;
import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.activity.UserManager;
import com.example.physicalfitnessexamination.app.Api;
import com.example.physicalfitnessexamination.base.MyBaseActivity;
import com.example.physicalfitnessexamination.bean.MessageEvent;
import com.example.physicalfitnessexamination.bean.ParticipatingInstitutionsBean;
import com.example.physicalfitnessexamination.bean.PersonBean;
import com.example.physicalfitnessexamination.bean.ReferencePersonnelBean;
import com.example.physicalfitnessexamination.common.adapter.CommonAdapter;
import com.example.physicalfitnessexamination.view.RosterDialogFragment;
import com.example.physicalfitnessexamination.viewholder.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 考核附件-人员报送
 */
public class KBIPersonnelReportShowActivity extends MyBaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageView imgRight;
    private TextView tvEnroll;//人员报送
    // private SpinnerParentView spvOrganization;
    private ListView lvLookLeave;
    private CommonAdapter<ReferencePersonnelBean> commonAdapter;
    private List<ReferencePersonnelBean> listLeave = new ArrayList<>();
    private List<ParticipatingInstitutionsBean> listPI = new ArrayList<>();
    private String id;//考核id
    private ArrayList<PersonBean> listPerson = new ArrayList<>();
    private int flag;//1 已建考核进入  2 考核实施进入  3 历史考核进入

    /**
     * 跳转方法
     *
     * @param context 上下文
     */
    public static void startInstant(Context context, String id, int flag) {
        Intent intent = new Intent(context, KBIPersonnelReportShowActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("flag", flag);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_kbi_personnel_report_show;
    }

    @Override
    protected void initView() {
        id = getIntent().getStringExtra("id");
        flag = getIntent().getIntExtra("flag", 0);
        tvTitle = findViewById(R.id.tv_title);
        imgRight = findViewById(R.id.iv_right);
        imgRight.setOnClickListener(this::onClick);
        // spvOrganization = findViewById(R.id.spv_organization);
        lvLookLeave = findViewById(R.id.lv_look_leave);
        tvEnroll = findViewById(R.id.tv_enroll);
        tvEnroll.setOnClickListener(this::onClick);
    }

    @Override
    protected void initData() {
        tvTitle.setText("人员报送");
        //spvOrganization.setName("单位");
        getData();
        getPersonList();
        commonAdapter = new CommonAdapter<ReferencePersonnelBean>(this, R.layout.item_kbi_person_report_show, listLeave) {
            @Override
            public void convert(ViewHolder viewHolder, ReferencePersonnelBean s) {
                viewHolder.setText(R.id.tv_order, viewHolder.getPosition() + 1 + "");
                viewHolder.setText(R.id.tv_name, s.getUSERNAME());
                viewHolder.setText(R.id.tv_sex, s.getSEX());
                viewHolder.setText(R.id.tv_age, s.getAGE());
                viewHolder.setText(R.id.tv_type, s.getTYPE());
                viewHolder.setText(R.id.tv_post, s.getGW());
                viewHolder.setText(R.id.tv_job, s.getZW());
                viewHolder.setText(R.id.tv_unit, s.getORG_NAME());
                viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        KBIPersonnelReportDetailActivity.startInstant(KBIPersonnelReportShowActivity.this, id, s);
                    }
                });
            }
        };
        lvLookLeave.setAdapter(commonAdapter);
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
                        if (listPI.get(i).getORG_ID().equals(UserManager.getInstance().getUserInfo(KBIPersonnelReportShowActivity.this).getOrg_id())) {
                            tvEnroll.setVisibility(View.VISIBLE);
                        }
                    }
                    if (flag == 3 || flag == 2) {
                        tvEnroll.setVisibility(View.GONE);
                    }
//                    HashSet<Integer> defaultIndex = new HashSet<>();
//                    defaultIndex.add(0);
//                    spvOrganization.setSpinner(listPI.toArray(), new SpinnerParentView.OnGetStrListener() {
//                        @NotNull
//                        @Override
//                        public String getStr(Object bean) {
//                            ParticipatingInstitutionsBean participatingInstitutionsBean = (ParticipatingInstitutionsBean) bean;
//                            return participatingInstitutionsBean.getORG_NAME();
//                        }
//                    }, new SpinnerParentView.OnCheckListener() {
//                        @Override
//                        public void onConfirmAndChangeListener(@NotNull SpinnerParentView view, @NotNull List selectBeanList) {
//                            getPersonList(((ParticipatingInstitutionsBean) selectBeanList.get(0)).getORG_ID());
//                        }
//                    }, true, defaultIndex);
//                    getPersonList(((ParticipatingInstitutionsBean) spvOrganization.getSelectList().get(0)).getORG_ID());
                }
            }
        });
    }

    public void getPersonList() {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        // map.put("org_id", org_id);
        OkhttpUtil.okHttpPost(Api.GETLEAVEPERSONFORASSESSMENT, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    listLeave.clear();
                    listLeave.addAll(JSON.parseArray(JSON.parseObject(response).getString("data"), ReferencePersonnelBean.class));
                    commonAdapter.notifyDataSetChanged();
                } else {
                    listLeave.clear();
                    commonAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_right:
                finish();
                break;
            case R.id.tv_enroll:
                RosterDialogFragment.newInstance(UserManager.getInstance().getUserInfo(context).getOrg_id(),
                        null, listPerson,
                        list -> KBIPersonnelReportActivity.startInstant(KBIPersonnelReportShowActivity.this, id, list.get(0)),
                        id, 1).show(getSupportFragmentManager(), "");
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        switch (messageEvent.getMessage()) {
            case "请假人员列表刷新":
                getPersonList();
                break;
            default:
                break;
        }
    }
}
