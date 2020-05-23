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
import com.example.physicalfitnessexamination.bean.ClauseEnrollBean;
import com.example.physicalfitnessexamination.bean.ReferencePersonnelBean;
import com.example.physicalfitnessexamination.bean.UserInfo;
import com.example.physicalfitnessexamination.common.adapter.CommonAdapter;
import com.example.physicalfitnessexamination.viewholder.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 按项目报名前置列表页
 */
public class ClauseEnrollListActivity extends MyBaseActivity implements View.OnClickListener {
    private String id;//考核id
    private TextView tvTitle;
    private ImageView imgRight;
    private ListView lvClause;
    private List<ClauseEnrollBean> list = new ArrayList<>();
    private CommonAdapter<ClauseEnrollBean> commonAdapter;
    private UserInfo userInfo;
    private String requirements;//人员要求
    private String sid;//项目id
    private int personNum1, personNum2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 跳转方法
     *
     * @param context 上下文
     */
    public static void startInstant(Context context, String id) {
        Intent intent = new Intent(context, ClauseEnrollListActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_clause_enroll_list;
    }

    @Override
    protected void initView() {
        userInfo = UserManager.getInstance().getUserInfo(this);
        id = getIntent().getStringExtra("id");
        tvTitle = findViewById(R.id.tv_title);
        imgRight = findViewById(R.id.iv_right);
        imgRight.setOnClickListener(this::onClick);
        lvClause = findViewById(R.id.lv_clause);
    }

    @Override
    protected void initData() {
        tvTitle.setText("考核花名册 - 项目报送");
        commonAdapter = new CommonAdapter<ClauseEnrollBean>(this, R.layout.item_clause_enroll_list, list) {
            @Override
            public void convert(ViewHolder viewHolder, ClauseEnrollBean clauseEnrollBean) {
                viewHolder.setText(R.id.tv_clause_name, clauseEnrollBean.getName());
                viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sid = clauseEnrollBean.getSid();
                        personNum1 = Integer.parseInt(clauseEnrollBean.getRequirment().get(0).getPSUM());
                        personNum2 = Integer.parseInt(clauseEnrollBean.getRequirment().get(1).getPSUM());
                        requirements = "";
                        for (ClauseEnrollBean.ClauseEnroll clauseEnroll : clauseEnrollBean.getRequirment()) {
                            requirements = clauseEnroll.getGW() + ":" + clauseEnroll.getPSUM() + "人    " + requirements;
                        }
                        getPersonList1();
                    }
                });
            }
        };
        lvClause.setAdapter(commonAdapter);
        getClauseList();
    }

    public void getClauseList() {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("org_id", userInfo.getOrg_id());
        OkhttpUtil.okHttpPost(Api.GETPERSONREQUIREMENT, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    list.clear();
                    list.addAll(JSON.parseArray(JSON.parseObject(response).getString("data"), ClauseEnrollBean.class));
                    commonAdapter.notifyDataSetChanged();
                } else {
                    list.clear();
                    commonAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void getPersonList1() {//用于反选已报名人员
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("org_id", userInfo.getOrg_id());
        map.put("sid", sid);
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
                    ClauseEnrollActivity.startInstant(ClauseEnrollListActivity.this, requirements, id, sid, list, personNum1, personNum2);
                } else {
                    ClauseEnrollActivity.startInstant(ClauseEnrollListActivity.this, requirements, id, sid, list, personNum1, personNum2);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_right:
                finish();
                break;
        }
    }
}
