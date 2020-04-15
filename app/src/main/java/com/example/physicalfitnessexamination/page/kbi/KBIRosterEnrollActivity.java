package com.example.physicalfitnessexamination.page.kbi;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.base.MyBaseActivity;
import com.example.physicalfitnessexamination.common.adapter.CommonAdapter;
import com.example.physicalfitnessexamination.view.excel.SpinnerParentView;
import com.example.physicalfitnessexamination.viewholder.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 考核花名册报名
 */
public class KBIRosterEnrollActivity extends MyBaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageView imgRight;
    private SpinnerParentView spvUnit;//单位选择
    private SpinnerParentView spvPost;//岗位选择
    private SpinnerParentView spvRandom;//抽取人数选择
    private GridView gvPost, gvRandom;
    private CommonAdapter<String> commonAdapterPost;
    private CommonAdapter<String> commonAdapterRandom;
    private List<String> listPost = new ArrayList<>();
    private List<String> listRandom = new ArrayList<>();

    /**
     * 跳转方法
     *
     * @param context 上下文
     */
    public static void startInstant(Context context) {
        Intent intent = new Intent(context, KBIRosterEnrollActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_kbi_roster_enroll;
    }

    @Override
    protected void initView() {
        tvTitle = findViewById(R.id.tv_title);
        imgRight = findViewById(R.id.iv_right);
        imgRight.setOnClickListener(this::onClick);
        spvUnit = findViewById(R.id.spv_unit);
        spvPost = findViewById(R.id.spv_post);
        spvRandom = findViewById(R.id.spv_random);
        gvPost = findViewById(R.id.gv_post);
        gvRandom = findViewById(R.id.gv_random);
    }

    @Override
    protected void initData() {
        tvTitle.setText("考核花名册 - 报名");
        spvUnit.setName("单位");
        spvPost.setName("岗位");
        spvPost.setBackgroundColor(ContextCompat.getColor(this, R.color._E6ECFA));
        spvRandom.setName("抽取人数");
        spvRandom.setBackgroundColor(ContextCompat.getColor(this, R.color._E6ECFA));

        listPost.add("");
        listPost.add("");
        listPost.add("");
        listPost.add("");
        listPost.add("");
        listPost.add("");
        listPost.add("");
        listPost.add("");
        listPost.add("");
        listPost.add("");
        listPost.add("");
        listPost.add("");
        listPost.add("");
        listPost.add("");
        listPost.add("");
        listPost.add("");
        listPost.add("");
        listPost.add("");
        listPost.add("");
        listPost.add("");
        listPost.add("");
        listPost.add("");
        listPost.add("");
        listPost.add("");
        listPost.add("");
        listPost.add("");
        listPost.add("");
        listPost.add("");
        listPost.add("");
        listPost.add("");
        commonAdapterPost=new CommonAdapter<String>(this,R.layout.item_kbi_roster_enroll_post,listPost) {
            @Override
            public void convert(ViewHolder viewHolder, String s) {

            }
        };
        gvPost.setAdapter(commonAdapterPost);



        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        listRandom.add("");
        commonAdapterRandom=new CommonAdapter<String>(this,R.layout.item_kbi_roster_enroll_random,listRandom) {
            @Override
            public void convert(ViewHolder viewHolder, String s) {

            }
        };
        gvRandom.setAdapter(commonAdapterRandom);
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
