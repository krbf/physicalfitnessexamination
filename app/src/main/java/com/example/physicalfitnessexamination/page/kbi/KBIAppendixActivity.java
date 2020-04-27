package com.example.physicalfitnessexamination.page.kbi;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.base.MyBaseActivity;

/**
 * 考核附件
 */
public class KBIAppendixActivity extends MyBaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageView imgRight;
    private LinearLayout linReport;//人员报送
    private LinearLayout linAppeal;//考核申诉
    private LinearLayout linMeans;//考核资料
    private String id;//考核id
    private int flag;//1 已经考核进入  2 考核实施进入  3 历史考核进入

    /**
     * 跳转方法
     *
     * @param context 上下文
     */
    public static void startInstant(Context context, String id, int flag) {
        Intent intent = new Intent(context, KBIAppendixActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("flag", flag);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_kbi_appendix;
    }

    @Override
    protected void initView() {
        id = getIntent().getStringExtra("id");
        flag = getIntent().getIntExtra("flag", 0);
        tvTitle = findViewById(R.id.tv_title);
        imgRight = findViewById(R.id.iv_right);
        imgRight.setOnClickListener(this::onClick);
        linReport = findViewById(R.id.lin_report);
        linReport.setOnClickListener(this::onClick);
        linAppeal = findViewById(R.id.lin_appeal);
        linAppeal.setOnClickListener(this::onClick);
        linMeans = findViewById(R.id.lin_means);
        linMeans.setOnClickListener(this::onClick);
    }

    @Override
    protected void initData() {
        tvTitle.setText("考核附件");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_right:
                finish();
                break;
            case R.id.lin_report:
                if (flag == 3) {
                    showToast("考核结束无法进行报送");
                    return;
                }
                KBIPersonnelReportShowActivity.startInstant(this, id);
                break;
            case R.id.lin_appeal:
                showToast("暂未开放考核申诉");
                break;
            case R.id.lin_means:
                showToast("暂未开放");
                break;
            default:
                break;
        }
    }
}
