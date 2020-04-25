package com.example.physicalfitnessexamination.page.myKbi;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.base.MyBaseActivity;

/**
 * 我的考核页面
 */
public class MyKbiActivity extends MyBaseActivity implements View.OnClickListener {
    private LinearLayout linMyAchievement;//我的成绩
    private LinearLayout linHistoryKbi;//历史考核
    private LinearLayout linTrainingAnalysis;//训练分析
    private TextView tvTitle;
    private ImageView imgRight;

    /**
     * 跳转方法
     *
     * @param context 上下文
     */
    public static void startInstant(Context context) {
        Intent intent = new Intent(context, MyKbiActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_my_kbi;
    }

    @Override
    protected void initView() {
        linMyAchievement = findViewById(R.id.lin_my_achievement);
        linHistoryKbi = findViewById(R.id.lin_history_kbi);
        linTrainingAnalysis = findViewById(R.id.lin_training_analysis);
        linMyAchievement.setOnClickListener(this::onClick);
        linHistoryKbi.setOnClickListener(this::onClick);
        linTrainingAnalysis.setOnClickListener(this::onClick);
        tvTitle = findViewById(R.id.tv_title);
        imgRight = findViewById(R.id.iv_right);
        imgRight.setOnClickListener(this::onClick);
    }

    @Override
    protected void initData() {
        tvTitle.setText("我的考核");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_my_achievement:
                break;
            case R.id.lin_history_kbi:
                break;
            case R.id.lin_training_analysis:
                break;
            case R.id.iv_right:
                finish();
                break;
        }
    }
}
