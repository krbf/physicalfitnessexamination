package com.example.physicalfitnessexamination.page.kbi;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.base.MyBaseActivity;

/**
 * 考核分流页面
 */
public class KbiShuntActivity extends MyBaseActivity implements View.OnClickListener {

    private Button btnCreate;
    private Button btnBuilt;
    private Button btnKbiWork;

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
        btnCreate = findViewById(R.id.btn_create);
        btnBuilt = findViewById(R.id.btn_built);
        btnKbiWork = findViewById(R.id.btn_kbiWork);
        btnCreate.setOnClickListener(this);
        btnBuilt.setOnClickListener(this);
        btnKbiWork.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create:
                CreateKBIActivity.startInstant(this);
                break;
            case R.id.btn_built:
                BuiltKBIActivity.startInstant(this);
                break;
            case R.id.btn_kbiWork:
                break;
        }
    }
}
