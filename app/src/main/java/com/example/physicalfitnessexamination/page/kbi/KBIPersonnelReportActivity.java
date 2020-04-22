package com.example.physicalfitnessexamination.page.kbi;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.app.Api;
import com.example.physicalfitnessexamination.base.MyBaseActivity;
import com.example.physicalfitnessexamination.bean.MessageEvent;
import com.example.physicalfitnessexamination.bean.PersonBean;
import com.example.physicalfitnessexamination.okhttp.CallBackUtil;
import com.example.physicalfitnessexamination.okhttp.OkhttpUtil;
import com.example.physicalfitnessexamination.util.Tool;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 考核附件-人员报送-报送人员具体信息填写
 */
public class KBIPersonnelReportActivity extends MyBaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageView imgRight;
    private String id;//考核id
    private PersonBean personBean;
    private TextView tvName;//姓名
    private TextView tvSex;//性别
    private TextView tvAge;//年龄
    private TextView tvJob;//职务
    private TextView tvType;//类型
    private TextView tv_unit;//单位
    private TextView tvPost;//岗位
    private EditText edtMatter;//事项
    private EditText edtExplain;//说明
    private EditText edtRemarks;//备注
    private TextView tvCommit;//提交

    /**
     * 跳转方法
     *
     * @param context 上下文
     */
    public static void startInstant(Context context, String id, PersonBean personBean) {
        Intent intent = new Intent(context, KBIPersonnelReportActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("person", personBean);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_personnel_report;
    }

    @Override
    protected void initView() {
        id = getIntent().getStringExtra("id");
        personBean = getIntent().getParcelableExtra("person");
        tvTitle = findViewById(R.id.tv_title);
        imgRight = findViewById(R.id.iv_right);
        imgRight.setOnClickListener(this::onClick);
        tvName = findViewById(R.id.tv_name);
        tvSex = findViewById(R.id.tv_sex);
        tvAge = findViewById(R.id.tv_age);
        tvJob = findViewById(R.id.tv_job);
        tvType = findViewById(R.id.tv_type);
        tv_unit = findViewById(R.id.tv_unit);
        tvPost = findViewById(R.id.tv_post);
        edtMatter = findViewById(R.id.edt_matter);
        edtExplain = findViewById(R.id.edt_explain);
        edtRemarks = findViewById(R.id.edt_remarks);
        tvCommit = findViewById(R.id.tv_commit);
        tvCommit.setOnClickListener(this::onClick);
    }

    @Override
    protected void initData() {
        tvTitle.setText("人员报送");
        tvName.setText(personBean.getNAME());
        tvSex.setText(personBean.getSEX());
        tvAge.setText(personBean.getAGE() + "");
        tvJob.setText(personBean.getZW());
        tvType.setText(personBean.getTYPE());
        tv_unit.setText(personBean.getORG_NAME());
        tvPost.setText(personBean.getJ_TYPE());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_right:
                finish();
                break;
            case R.id.tv_commit:
                if (check()) {
                    ReferTo();
                }
                break;
            default:
                break;
        }
    }

    public void ReferTo() {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("userid", personBean.getID());
        map.put("username", personBean.getNAME());
        map.put("age", personBean.getAGE() + "");
        map.put("sex", personBean.getSEX());
        map.put("zw", personBean.getZW());
        map.put("org_id", personBean.getORG_ID());
        map.put("bz", edtRemarks.getText().toString());//备注
        map.put("type", personBean.getTYPE());//类型
        map.put("org_name", personBean.getORG_NAME());
        map.put("j_type", personBean.getJ_TYPE());//岗位
        map.put("remark", edtExplain.getText().toString());//说明
        map.put("matter", edtMatter.getText().toString());//事项
        map.put("photo", "");
        OkhttpUtil.okHttpPost(Api.SETLEAVEPERSONFORASSESSMENT, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    showToast("提交成功");
                    EventBus.getDefault().post(new MessageEvent("请假人员列表刷新"));
                    finish();
                }
            }
        });
    }

    public boolean check() {
        if (Tool.isEmpty(edtMatter.getText().toString())) {
            showToast("请填写事项");
            return false;
        } else if (Tool.isEmpty(edtExplain.getText().toString())) {
            showToast("请填写说明");
            return false;
        }
//        else if (Tool.isEmpty(edtRemarks.getText().toString())) {
//            showToast("请填写备注");
//            return false;
//        }
        return true;
    }
}
