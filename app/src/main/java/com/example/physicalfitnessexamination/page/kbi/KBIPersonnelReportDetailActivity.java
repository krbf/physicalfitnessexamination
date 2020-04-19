package com.example.physicalfitnessexamination.page.kbi;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.app.Api;
import com.example.physicalfitnessexamination.base.MyBaseActivity;
import com.example.physicalfitnessexamination.bean.ReferencePersonnelBean;
import com.example.physicalfitnessexamination.bean.ReportPersonBean;
import com.example.physicalfitnessexamination.okhttp.CallBackUtil;
import com.example.physicalfitnessexamination.okhttp.OkhttpUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 考核附件-报送人员详情
 */
public class KBIPersonnelReportDetailActivity extends MyBaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageView imgRight;
    private TextView tvName;//姓名
    private TextView tvSex;//性別
    private TextView tvAge;//年齡
    private TextView tvJob;//职务
    private TextView tvType;//类型
    private TextView tvUnit;//单位
    private TextView tvPost;//岗位
    private TextView tvMatter;//事项
    private TextView tvExplain;//说明
    private TextView tvRemarks;//备注
    private String id;//考核id
    private ReferencePersonnelBean referencePersonnelBean;
    private ReportPersonBean reportPersonBean;

    /**
     * 跳转方法
     *
     * @param context 上下文
     */
    public static void startInstant(Context context, String id, ReferencePersonnelBean referencePersonnelBean) {
        Intent intent = new Intent(context, KBIPersonnelReportDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("referencePersonnelBean", referencePersonnelBean);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_kbi_personnel_report_detail;
    }

    @Override
    protected void initView() {
        id = getIntent().getStringExtra("id");
        referencePersonnelBean = getIntent().getParcelableExtra("referencePersonnelBean");
        tvTitle = findViewById(R.id.tv_title);
        imgRight = findViewById(R.id.iv_right);
        imgRight.setOnClickListener(this::onClick);
        tvName = findViewById(R.id.tv_name);
        tvSex = findViewById(R.id.tv_sex);
        tvAge = findViewById(R.id.tv_age);
        tvJob = findViewById(R.id.tv_job);
        tvType = findViewById(R.id.tv_type);
        tvUnit = findViewById(R.id.tv_unit);
        tvPost = findViewById(R.id.tv_post);
        tvMatter = findViewById(R.id.tv_matter);
        tvExplain = findViewById(R.id.tv_explain);
        tvRemarks = findViewById(R.id.tv_remarks);
    }

    @Override
    protected void initData() {
        tvTitle.setText("报送人员详情");
        tvName.setText(referencePersonnelBean.getUSERNAME());
        tvSex.setText(referencePersonnelBean.getSEX());
        tvAge.setText(referencePersonnelBean.getAGE());
        tvJob.setText(referencePersonnelBean.getZW());
        tvType.setText(referencePersonnelBean.getTYPE());
        tvUnit.setText(referencePersonnelBean.getORG_NAME());
        tvPost.setText(referencePersonnelBean.getGW());
        getPersonInfo();
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

    public void getPersonInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("userid", referencePersonnelBean.getUSERID());
        OkhttpUtil.okHttpGet(Api.GETLEAVEPERSONINFOFORASSESSMENT, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    reportPersonBean = JSON.parseObject(JSON.parseObject(response).getString("data"), ReportPersonBean.class);
                    tvMatter.setText(reportPersonBean.getMATTER());
                    tvExplain.setText(reportPersonBean.getREMARK());
                    tvRemarks.setText(reportPersonBean.getBZ());
                }
            }
        });
    }

}
