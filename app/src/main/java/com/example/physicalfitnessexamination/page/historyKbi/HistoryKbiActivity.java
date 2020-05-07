package com.example.physicalfitnessexamination.page.historyKbi;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.activity.UserManager;
import com.example.physicalfitnessexamination.app.Api;
import com.example.physicalfitnessexamination.base.MyBaseActivity;
import com.example.physicalfitnessexamination.bean.BuiltKBIListBean;
import com.example.physicalfitnessexamination.common.adapter.CommonAdapter;
import com.czy.module_common.okhttp.CallBackUtil;
import com.czy.module_common.okhttp.OkhttpUtil;
import com.example.physicalfitnessexamination.view.excel.SpinnerParentView;
import com.example.physicalfitnessexamination.viewholder.ViewHolder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 历史考核列表页
 */
public class HistoryKbiActivity extends MyBaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageView imgRight;
    private ListView lvKBI;
    private List<BuiltKBIListBean> list = new ArrayList<>();
    private CommonAdapter<BuiltKBIListBean> commonAdapter;
    private SpinnerParentView spvOrganization, spvType;
    private TextView tvHintTime;
    private TextView tvSearch;
    private EditText edt_search;
    private LinearLayout linTime;
    private TextView tvTime;
    private Calendar ca = Calendar.getInstance();
    private int mYear = ca.get(Calendar.YEAR);
    private int mMonth = ca.get(Calendar.MONTH);
    private int mDay = ca.get(Calendar.DAY_OF_MONTH);

    /**
     * 跳转方法
     *
     * @param context 上下文
     */
    public static void startInstant(Context context) {
        Intent intent = new Intent(context, HistoryKbiActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_history_kbi;
    }

    @Override
    protected void initView() {
        tvTitle = findViewById(R.id.tv_title);
        imgRight = findViewById(R.id.iv_right);
        lvKBI = findViewById(R.id.lv_kbi);
        imgRight.setOnClickListener(this::onClick);
        spvOrganization = findViewById(R.id.spv_organization);
        spvType = findViewById(R.id.spv_type);
        tvHintTime = findViewById(R.id.tv_hint_time);
        tvSearch = findViewById(R.id.tv_search);
        edt_search = findViewById(R.id.edt_search);
        linTime = findViewById(R.id.lin_time);
        linTime.setOnClickListener(this::onClick);
        tvTime = findViewById(R.id.tv_time);
    }

    @Override
    protected void initData() {
        tvTitle.setText("历史考核");
        spvOrganization.setName("单位");
        spvType.setName("类型");
        Drawable drawable = getResources().getDrawable(R.mipmap.ic_down_arrow);
        // 设置图片的大小
        drawable.setBounds(0, 0, 60, 60);
        // 设置图片的位置，左、上、右、下
        tvHintTime.setCompoundDrawables(null, null, drawable, null);
        commonAdapter = new CommonAdapter<BuiltKBIListBean>(this, R.layout.item_built_kbi, list) {
            @Override
            public void convert(ViewHolder viewHolder, BuiltKBIListBean s) {
                viewHolder.setText(R.id.tv_order, viewHolder.getPosition() + 1 + "");
                viewHolder.setText(R.id.tv_name, s.getNAME());
                viewHolder.setText(R.id.tv_data, s.getCREATETIME());
                viewHolder.setText(R.id.tv_org, s.getORG_NAME());
                viewHolder.getConvertView().setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View view) {
                         HistoryKbiDetailActivity.startInstant(HistoryKbiActivity.this, s.getID());
                    }
                });
            }
        };
        lvKBI.setAdapter(commonAdapter);
        getData();
    }

    public void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("org_id", UserManager.getInstance().getUserInfo(this).getOrg_id());
        map.put("status", "2");//0 已建考核  1 考核实施 2 历史考核
        OkhttpUtil.okHttpPost(Api.BUILTKBILIST, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    list.addAll(JSON.parseArray(JSON.parseObject(response).getString("data"), BuiltKBIListBean.class));
                    commonAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_right:
                finish();
                break;
            case R.id.lin_time:
                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                mYear = year;
                                mMonth = month;
                                mDay = dayOfMonth;
                                final String data = (month + 1) + "月-" + dayOfMonth + "日 ";
                                tvTime.setText(data);
                            }
                        },
                        mYear, mMonth, mDay);
                datePickerDialog.show();
                break;
            default:
                break;
        }
    }
}
