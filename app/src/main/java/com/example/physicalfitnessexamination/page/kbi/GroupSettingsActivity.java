package com.example.physicalfitnessexamination.page.kbi;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.base.MyBaseActivity;
import com.example.physicalfitnessexamination.bean.GroupSettingsBean;
import com.example.physicalfitnessexamination.common.adapter.CommonAdapter;
import com.example.physicalfitnessexamination.view.excel.SpinnerParentView;
import com.example.physicalfitnessexamination.viewholder.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GroupSettingsActivity extends MyBaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageView imgRight;
    private TextView tvNumber;
    private SpinnerParentView spvGroup;
    private ListView lvGroup;
    private CommonAdapter<GroupSettingsBean> commonAdapter;
    private List<GroupSettingsBean> listGroup = new ArrayList<>();
    private List<String> list = new ArrayList<>();

    /**
     * 跳转方法
     *
     * @param context 上下文
     */
    public static void startInstant(Context context) {
        Intent intent = new Intent(context, GroupSettingsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_group_settings;
    }

    @Override
    protected void initView() {
        tvTitle = findViewById(R.id.tv_title);
        imgRight = findViewById(R.id.iv_right);
        imgRight.setOnClickListener(this::onClick);
        tvNumber = findViewById(R.id.tv_number);
        spvGroup = findViewById(R.id.spv_group);
        lvGroup = findViewById(R.id.lv_group);
    }

    @Override
    protected void initData() {
        tvTitle.setText("组别设置");
        spvGroup.setName("分组");
        for (int i = 1; i < 6; i++) {
            list.add(i + "");
        }
        HashSet<Integer> defSet = new HashSet<>();
//        defSet.add(0);
        spvGroup.setSpinner(list.toArray(), new SpinnerParentView.OnGetStrListener() {
            @NotNull
            @Override
            public String getStr(Object bean) {
                return bean.toString();
            }
        }, new SpinnerParentView.OnCheckListener() {
            @Override
            public void onConfirmAndChangeListener(@NotNull SpinnerParentView view, @NotNull List selectBeanList) {
                int number = Integer.parseInt(selectBeanList.get(0).toString());
                int result = 32 / number;
                int remainder = 32 % number;
                listGroup.clear();
                for (int i = 1; i < number + 1; i++) {
                    GroupSettingsBean groupSettingsBean = new GroupSettingsBean();
                    groupSettingsBean.setName(i + "组");
                    if (i != number) {
                        groupSettingsBean.setStartOrder(result * i - result + 1);
                        groupSettingsBean.setEndOrder(result * i);
                    } else {
                        groupSettingsBean.setStartOrder(result * i - result + 1);
                        groupSettingsBean.setEndOrder(result * i + remainder);
                    }
                    listGroup.add(groupSettingsBean);
                }


                commonAdapter.notifyDataSetChanged();
            }
        }, true, defSet);

        commonAdapter = new CommonAdapter<GroupSettingsBean>(this, R.layout.item_group_settings, listGroup) {
            @Override
            public void convert(ViewHolder viewHolder, GroupSettingsBean s) {
                viewHolder.setText(R.id.tv_group, s.getName());
                viewHolder.setText(R.id.tv_order, "序号：" + s.getStartOrder() + " - " + s.getEndOrder());
            }
        };
        lvGroup.setAdapter(commonAdapter);
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
