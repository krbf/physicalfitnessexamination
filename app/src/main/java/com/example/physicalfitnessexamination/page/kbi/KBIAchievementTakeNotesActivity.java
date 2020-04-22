package com.example.physicalfitnessexamination.page.kbi;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.example.physicalfitnessexamination.view.DMDialog;
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
    private TextView tvClause;
    private String id;//考核id
    private ClauseBean.Clause clause;//项目实体类
    private String GW;//岗位
    private ListView lvAchievementTakeNotes;
    private CommonAdapter<PersonAchievementBean> commonAdapter;
    private List<PersonAchievementBean> list = new ArrayList<>();

    /**
     * 跳转方法
     *
     * @param context 上下文
     */
    public static void startInstant(Context context, String id, ClauseBean.Clause clause, String GW) {
        Intent intent = new Intent(context, KBIAchievementTakeNotesActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("clause", (Parcelable) clause);
        intent.putExtra("GW", GW);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_kbi_achievement_take_notes;
    }

    @Override
    protected void initView() {
        id = getIntent().getStringExtra("id");
        clause = getIntent().getParcelableExtra("clause");
        GW = getIntent().getStringExtra("GW");
        tvTitle = findViewById(R.id.tv_title);
        imgRight = findViewById(R.id.iv_right);
        imgRight.setOnClickListener(this::onClick);
        lvAchievementTakeNotes = findViewById(R.id.lv_achievement_take_notes);
        tvClause = findViewById(R.id.tv_clause);
    }

    @Override
    protected void initData() {
        tvTitle.setText("成绩记录表");
        tvClause.setText(clause.getNAME());
        getData();
        commonAdapter = new CommonAdapter<PersonAchievementBean>(this, R.layout.item_kbi_achievement_takes_notes, list) {
            @Override
            public void convert(ViewHolder viewHolder, PersonAchievementBean s) {
                viewHolder.setText(R.id.tv_order, viewHolder.getPosition() + 1 + "");
                viewHolder.setText(R.id.tv_number, s.getNO());
                viewHolder.setText(R.id.tv_name, s.getUSERNAME());
                viewHolder.setText(R.id.tv_unit, s.getORG_NAME());
                viewHolder.setText(R.id.tv_post, s.getGW());
                viewHolder.setText(R.id.tv_achievement, s.getACHIEVEMENT());
                viewHolder.getView(R.id.tv_achievement).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DMDialog.builder(KBIAchievementTakeNotesActivity.this, R.layout.dialog_achievement_input)
                                .onDialogInitListener((helper, dialog) ->
                                {
                                    helper.setText(R.id.tv_name, s.getUSERNAME());
                                    helper.setText(R.id.tv_unit, s.getORG_NAME());
                                    helper.setText(R.id.tv_measurement, clause.getDW());
                                    EditText edtAchievement = helper.getView(R.id.edt_achievement);
                                    switch (clause.getATYPE()) {
                                        case "0"://时间计数
                                            edtAchievement.setFocusable(false);
                                            edtAchievement.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                }
                                            });
                                            break;
                                        case "1"://次数
                                            break;
                                        default:
                                            break;
                                    }

                                    helper.setOnClickListener(R.id.tv_ok, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            submission(s.getUSERID(), edtAchievement.getText().toString());
                                            dialog.dismiss();
                                        }
                                    });
                                })
                                .setGravity(Gravity.CENTER)
                                .show();
                    }
                });
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
        map.put("sid", clause.getSID());
        map.put("gw", GW);
        map.put("type", clause.getTYPE());
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

    public void submission(String userid, String achievement) {
        Map<String, String> map = new HashMap<>();
        map.put("aid", id);
        map.put("sid", clause.getSID());
        map.put("userid", userid);
        map.put("achievement", achievement);
        OkhttpUtil.okHttpPost(Api.SETPERSONACHIEVEMENT4ASSESS, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    list.clear();
                    getData();
                }
            }
        });
    }
}
