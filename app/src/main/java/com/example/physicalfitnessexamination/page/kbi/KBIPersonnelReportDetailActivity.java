package com.example.physicalfitnessexamination.page.kbi;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.base.MyBaseActivity;

/**
 * 考核附件-报送人员详情
 */
public class KBIPersonnelReportDetailActivity extends MyBaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageView imgRight;

    /**
     * 跳转方法
     *
     * @param context 上下文
     */
    public static void startInstant(Context context,String id,String userId) {
        Intent intent = new Intent(context, KBIPersonnelReportDetailActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("userId",userId);
        context.startActivity(intent);
    }
    @Override
    protected int initLayout() {
        return R.layout.activity_kbi_personnel_report_detail;
    }

    @Override
    protected void initView() {
        tvTitle = findViewById(R.id.tv_title);
        imgRight = findViewById(R.id.iv_right);
        imgRight.setOnClickListener(this::onClick);
    }

    @Override
    protected void initData() {
        tvTitle.setText("报送人员详情");
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
}
