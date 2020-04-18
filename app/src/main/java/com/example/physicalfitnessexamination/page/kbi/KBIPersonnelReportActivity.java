package com.example.physicalfitnessexamination.page.kbi;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.base.MyBaseActivity;
/**
 * 考核附件-人员报送-报送人员具体信息填写
 */
public class KBIPersonnelReportActivity extends MyBaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageView imgRight;
    private String id;//考核id

    /**
     * 跳转方法
     *
     * @param context 上下文
     */
    public static void startInstant(Context context, String id) {
        Intent intent = new Intent(context, KBIPersonnelReportActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }
    @Override
    protected int initLayout() {
        return R.layout.activity_personnel_report;
    }

    @Override
    protected void initView() {
        id = getIntent().getStringExtra("id");
        tvTitle = findViewById(R.id.tv_title);
        imgRight = findViewById(R.id.iv_right);
        imgRight.setOnClickListener(this::onClick);
    }

    @Override
    protected void initData() {
        tvTitle.setText("人员报送");
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
