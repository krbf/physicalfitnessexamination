package com.example.physicalfitnessexamination.page.kbi;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.base.MyBaseActivity;

/**
 * 考核分流页面
 */
public class KbiShuntActivity extends MyBaseActivity implements View.OnClickListener {
    private LinearLayout linCreate;//新建考核
    private LinearLayout linBuilt;//已建考核
    private LinearLayout linKbiWork;//考核实施
    private TextView tvTitle;
    private ImageView imgRight;

    /**
     * 跳转方法
     *
     * @param context 上下文
     */
    public static void startInstant(Context context) {
        Intent intent = new Intent(context, KbiShuntActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_kbi_shunt;
    }

    @Override
    protected void initView() {
        linCreate = findViewById(R.id.lin_create);
        linBuilt = findViewById(R.id.lin_built);
        linKbiWork = findViewById(R.id.lin_kbiWork);
        linCreate.setOnClickListener(this);
        linBuilt.setOnClickListener(this);
        linKbiWork.setOnClickListener(this);
        tvTitle = findViewById(R.id.tv_title);
        imgRight = findViewById(R.id.iv_right);
        imgRight.setOnClickListener(this::onClick);
    }

    @Override
    protected void initData() {
        tvTitle.setText("考核");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_create:
                CreateKBIActivity.startInstant(this);
                break;
            case R.id.lin_built:
                BuiltKBIActivity.startInstant(this);
                break;
            case R.id.lin_kbiWork:
                PutIntoEffectKBIActivity.startInstant(this);
                break;
            case R.id.iv_right:
                finish();
                break;
        }
    }
}
