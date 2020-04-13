package com.example.physicalfitnessexamination.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.fragment.RosterFragment;

public class RosterActivity extends BaseActivity {

    /**
     * 页面跳转
     *
     * @param mContext 上下文
     */
    public static void startActivity(Context mContext) {
        Intent intent = new Intent(mContext, RosterActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setSteepStatusBar(true);
        super.onCreate(savedInstanceState);
        setActivityContentView(R.layout.activity_roster);
    }

    @Override
    public void initView() {
        {
            getToolBar().setTitle("人员花名册");
            getToolBar().setLeftVisible(false);
            getToolBar().setRightImgVislble(true);
            getToolBar().setRightImg(R.mipmap.back1);
            getToolBar().setToolBarRightOnClickListener(this::finish);
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.cl_content, RosterFragment.newInstance("", ""))
                .commit();
    }

    @Override
    public void initBind() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void widgetClick(View v) {

    }
}
