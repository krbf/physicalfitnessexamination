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
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.activity.UserManager;
import com.example.physicalfitnessexamination.app.Api;
import com.example.physicalfitnessexamination.base.MyBaseActivity;
import com.example.physicalfitnessexamination.bean.BuiltKBIListBean;
import com.example.physicalfitnessexamination.bean.HistoryKbiCensusBean;
import com.example.physicalfitnessexamination.bean.ParticipatingInstitutionsBean;
import com.example.physicalfitnessexamination.bean.UserInfo;
import com.example.physicalfitnessexamination.common.adapter.CommonAdapter;
import com.czy.module_common.okhttp.CallBackUtil;
import com.czy.module_common.okhttp.OkhttpUtil;
import com.example.physicalfitnessexamination.view.excel.SpinnerParentView;
import com.example.physicalfitnessexamination.viewholder.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
    private SpinnerParentView spvOrganization;
    private List<ParticipatingInstitutionsBean> listPI = new ArrayList<>();
    private TextView tvHintTime;
    private LinearLayout linTime;
    private TextView tvTime;
    private Calendar ca = Calendar.getInstance();
    private TimePickerView pvTime;//时间选择器
    private UserInfo userInfo;
    private String year;//所选年份
    private String org_id;//所选单位
    private HistoryKbiCensusBean historyKbiCensusBean;
    private TextView tv_zkh,tv_zdkh,tv_ddkh,tv_xfzkh,tv_year;

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
        tvHintTime = findViewById(R.id.tv_hint_time);
        linTime = findViewById(R.id.lin_time);
        linTime.setOnClickListener(this::onClick);
        tvTime = findViewById(R.id.tv_time);
        tv_zkh=findViewById(R.id.tv_zkh);
        tv_zdkh=findViewById(R.id.tv_zdkh);
        tv_ddkh=findViewById(R.id.tv_ddkh);
        tv_xfzkh=findViewById(R.id.tv_xfzkh);
        tv_year=findViewById(R.id.tv_year);
    }

    @Override
    protected void initData() {
        userInfo = UserManager.getInstance().getUserInfo(this);
        org_id = userInfo.getOrg_id();
        tvTitle.setText("历史考核");
        spvOrganization.setName("单位");
        getUnit();
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

    public void getUnit() {
        Map<String, String> map = new HashMap<>();
        map.put("org_id", userInfo.getOrg_id());
        map.put("hierarchy_restriction", "1");
        map.put("type", null);
        OkhttpUtil.okHttpPost(Api.GETORGFORASSESSMENT, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    listPI.clear();
                    listPI.addAll(JSON.parseArray(JSON.parseObject(response).getString("data"), ParticipatingInstitutionsBean.class));
                    HashSet<Integer> defSet = new HashSet();
                    spvOrganization.setSpinner(listPI.toArray(), new SpinnerParentView.OnGetStrListener() {
                        @NotNull
                        @Override
                        public String getStr(Object bean) {
                            ParticipatingInstitutionsBean participatingInstitutionsBean = (ParticipatingInstitutionsBean) bean;
                            return participatingInstitutionsBean.getORG_NAME();
                        }
                    }, new SpinnerParentView.OnCheckListener() {
                        @Override
                        public void onConfirmAndChangeListener(@NotNull SpinnerParentView view, @NotNull List selectBeanList) {
                            org_id = ((ParticipatingInstitutionsBean) (selectBeanList.get(0))).getORG_ID();
                            getData();
                        }
                    }, true, defSet);
                }
            }
        });
    }

    public void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("org_id", org_id);
        map.put("status", "2");//0 已建考核  1 考核实施 2 历史考核
        map.put("year", year);
        OkhttpUtil.okHttpPost(Api.BUILTKBILIST, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    historyKbiCensusBean = JSON.parseObject(JSON.parseObject(response).getString("statistics"), HistoryKbiCensusBean.class);
                    tv_zkh.setText(historyKbiCensusBean.getZKHS());
                    tv_zdkh.setText(historyKbiCensusBean.getZDKHS());
                    tv_ddkh.setText(historyKbiCensusBean.getDDKHS());
                    tv_xfzkh.setText(historyKbiCensusBean.getXFZKHS());
                    tv_year.setText(historyKbiCensusBean.getYear());
                    list.clear();
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
                pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        year = getTime(date);
                        tvTime.setText(getTime(date));
                        getData();
                        pvTime.dismiss();
                    }
                }).setType(new boolean[]{true, false, false, false, false, false})// 默认全部显示
                        .setLayoutRes(R.layout.view_timepicker, new CustomListener() {
                            @Override
                            public void customLayout(View v) {
                                TextView textViewOk = v.findViewById(R.id.tv_finish);
                                textViewOk.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        pvTime.returnData();
                                    }
                                });
                            }
                        }).setDate(ca)
                        .build();
                pvTime.show();
                break;
            default:
                break;
        }
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat formatMonth = new SimpleDateFormat("MM");
        SimpleDateFormat formatDay = new SimpleDateFormat("dd");
        SimpleDateFormat formatWeek = new SimpleDateFormat("E");
        SimpleDateFormat formatHour = new SimpleDateFormat("HH");
        SimpleDateFormat formatMin = new SimpleDateFormat("mm");
        SimpleDateFormat formatSecond = new SimpleDateFormat("ss");
        return formatYear.format(date);
    }
}
