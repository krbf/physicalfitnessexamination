package com.example.physicalfitnessexamination.page.kbi;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.app.Api;
import com.example.physicalfitnessexamination.base.MyBaseActivity;
import com.example.physicalfitnessexamination.bean.ClauseBean;
import com.example.physicalfitnessexamination.bean.PersonAchievementBean;
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
 * 考核成绩记录表
 */
public class KBIAchievementTakeNotesActivity extends MyBaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageView imgRight;
    private String id;//考核id
    private String sid;//项目id
    private String gw;//岗位
    private ListView lvAchievementTakeNotes;
    private CommonAdapter<PersonAchievementBean> commonAdapter;
    private List<PersonAchievementBean> list = new ArrayList<>();

    /**
     * 跳转方法
     *
     * @param context 上下文
     */
    public static void startInstant(Context context, String id, String sid, String gw) {
        Intent intent = new Intent(context, KBIAchievementTakeNotesActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("sid", sid);
        intent.putExtra("gw", gw);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_kbi_achievement_take_notes;
    }

    @Override
    protected void initView() {
        id = getIntent().getStringExtra("id");
        sid = getIntent().getStringExtra("sid");
        gw = getIntent().getStringExtra("gw");
        tvTitle = findViewById(R.id.tv_title);
        imgRight = findViewById(R.id.iv_right);
        imgRight.setOnClickListener(this::onClick);
        lvAchievementTakeNotes = findViewById(R.id.lv_achievement_take_notes);
    }

    @Override
    protected void initData() {
        tvTitle.setText("成绩记录表");
        commonAdapter = new CommonAdapter<PersonAchievementBean>(this, R.layout.item_kbi_achievement_takes_notes, list) {
            @Override
            public void convert(ViewHolder viewHolder, PersonAchievementBean s) {
                viewHolder.setText(R.id.tv_order, viewHolder.getPosition() + 1 + "");
            }
        };
        lvAchievementTakeNotes.setAdapter(commonAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_right:
                finish();
                break;
            default:
                break;
        }
    }

    public void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("aid", id);
        map.put("sid", sid);
        map.put("gw", gw);
        OkhttpUtil.okHttpPost(Api.GETPERSONACHIEVEMENT4ASSESS, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    list.addAll(JSON.parseArray(JSON.parseObject(response).getString("data"), PersonAchievementBean.class));
                    commonAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
