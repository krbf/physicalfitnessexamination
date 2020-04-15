package com.example.physicalfitnessexamination.page.kbi;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.base.MyBaseActivity;
import com.example.physicalfitnessexamination.common.adapter.CommonAdapter;
import com.example.physicalfitnessexamination.view.excel.SpinnerParentView;
import com.example.physicalfitnessexamination.viewholder.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 考核花名册
 */
public class KBIRosterActivity extends MyBaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageView imgRight;
    private TextView tvEnroll;//报名
    private SpinnerParentView spvOrganization;
    private ListView lvLookRoster;
    private CommonAdapter<String> commonAdapter;
    private List<String> listRoster = new ArrayList<>();

    /**
     * 跳转方法
     *
     * @param context 上下文
     */
    public static void startInstant(Context context) {
        Intent intent = new Intent(context, KBIRosterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_kbi_roster;
    }

    @Override
    protected void initView() {
        tvTitle = findViewById(R.id.tv_title);
        imgRight = findViewById(R.id.iv_right);
        imgRight.setOnClickListener(this::onClick);
        spvOrganization = findViewById(R.id.spv_organization);
        lvLookRoster = findViewById(R.id.lv_look_roster);
        tvEnroll = findViewById(R.id.tv_enroll);
        tvEnroll.setOnClickListener(this::onClick);
    }

    @Override
    protected void initData() {
        tvTitle.setText("考核花名册 - 查看");
        spvOrganization.setName("单位");
        spvOrganization.setSpinner(getResources().getStringArray(R.array.perSelect), new SpinnerParentView.OnGetStrListener() {
            @NotNull
            @Override
            public String getStr(Object bean) {
                return bean.toString();
            }
        }, new SpinnerParentView.OnCheckListener() {
            @Override
            public void onConfirmAndChangeListener(@NotNull SpinnerParentView view, @NotNull List selectBeanList) {

            }
        }, true);
        listRoster.add("");
        listRoster.add("");
        listRoster.add("");
        listRoster.add("");
        listRoster.add("");
        listRoster.add("");
        listRoster.add("");
        listRoster.add("");
        listRoster.add("");
        listRoster.add("");
        commonAdapter = new CommonAdapter<String>(this, R.layout.item_kbi_roster, listRoster) {
            @Override
            public void convert(ViewHolder viewHolder, String s) {

            }
        };
        lvLookRoster.setAdapter(commonAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_right:
                finish();
                break;
            case R.id.tv_enroll:
                KBIRosterEnrollActivity.startInstant(this);
                break;
            default:
                break;
        }
    }
}
