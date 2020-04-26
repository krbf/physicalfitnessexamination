package com.example.physicalfitnessexamination.page.kbi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.activity.UserManager;
import com.example.physicalfitnessexamination.app.Api;
import com.example.physicalfitnessexamination.base.MyBaseActivity;
import com.example.physicalfitnessexamination.bean.BuiltKBIListBean;
import com.example.physicalfitnessexamination.bean.MessageEvent;
import com.example.physicalfitnessexamination.common.adapter.CommonAdapter;
import com.example.physicalfitnessexamination.okhttp.CallBackUtil;
import com.example.physicalfitnessexamination.okhttp.OkhttpUtil;
import com.example.physicalfitnessexamination.view.MyListView;
import com.example.physicalfitnessexamination.viewholder.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 考核分流页面
 */
public class KbiShuntActivity extends MyBaseActivity implements View.OnClickListener {
    private LinearLayout linCreate;//新建考核
    private LinearLayout linBuilt;//已建考核
    private LinearLayout linKbiWork;//考核实施

    private MyListView lvBuildKbi;//已建考核
    private List<BuiltKBIListBean> listBuildKbi = new ArrayList<>();
    private CommonAdapter<BuiltKBIListBean> commonAdapterBuildKbi;

    private MyListView lvPutIntoKbi;
    private List<BuiltKBIListBean> listPutIntoKbi = new ArrayList<>();
    private CommonAdapter<BuiltKBIListBean> commonAdapterPutIntoKbi;
    private TextView tvTitle;
    private ImageView imgRight;
    private boolean showBuildKbi, showPutIntoKbi;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
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
        lvBuildKbi = findViewById(R.id.lv_build_kbi);
        lvPutIntoKbi = findViewById(R.id.lv_put_into_kbi);
    }

    @Override
    protected void initData() {
        tvTitle.setText("考核");
        commonAdapterBuildKbi = new CommonAdapter<BuiltKBIListBean>(this, R.layout.item_built_kbi, listBuildKbi) {
            @Override
            public void convert(ViewHolder viewHolder, BuiltKBIListBean s) {
                viewHolder.setText(R.id.tv_order, viewHolder.getPosition() + 1 + "");
                viewHolder.setText(R.id.tv_name, s.getNAME());
                viewHolder.setText(R.id.tv_data, s.getCREATETIME());
                viewHolder.setText(R.id.tv_org, s.getORG_NAME());
                viewHolder.getConvertView().setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View view) {
                        BuiltKBIDetailActivity.startInstant(KbiShuntActivity.this, s.getID());
                    }
                });
            }
        };
        lvBuildKbi.setAdapter(commonAdapterBuildKbi);
        commonAdapterPutIntoKbi = new CommonAdapter<BuiltKBIListBean>(this, R.layout.item_built_kbi, listPutIntoKbi) {
            @Override
            public void convert(ViewHolder viewHolder, BuiltKBIListBean s) {
                viewHolder.setText(R.id.tv_order, viewHolder.getPosition() + 1 + "");
                viewHolder.setText(R.id.tv_name, s.getNAME());
                viewHolder.setText(R.id.tv_data, s.getCREATETIME());
                viewHolder.setText(R.id.tv_org, s.getORG_NAME());
                viewHolder.getConvertView().setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View view) {
                        KBIPutIntoEffectDetailActivity.startInstant(KbiShuntActivity.this, s.getID());
                    }
                });
            }
        };
        lvPutIntoKbi.setAdapter(commonAdapterPutIntoKbi);
        getData("0");
        getData("1");
    }

    public void getData(String status) {
        Map<String, String> map = new HashMap<>();
        map.put("org_id", UserManager.getInstance().getUserInfo(this).getOrg_id());
        map.put("status", status);//0 已建考核  1 考核实施  2 历史考核
        OkhttpUtil.okHttpPost(Api.BUILTKBILIST, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    switch (status) {
                        case "0":
                            listBuildKbi.addAll(JSON.parseArray(JSON.parseObject(response).getString("data"), BuiltKBIListBean.class));
                            commonAdapterBuildKbi.notifyDataSetChanged();
                            break;
                        case "1":
                            listPutIntoKbi.addAll(JSON.parseArray(JSON.parseObject(response).getString("data"), BuiltKBIListBean.class));
                            commonAdapterPutIntoKbi.notifyDataSetChanged();
                            break;
                    }

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_create:
                CreateKBIActivity.startInstant(this);
                break;
            case R.id.lin_built:
                if (!showBuildKbi) {
                    lvBuildKbi.setVisibility(View.VISIBLE);
                    showBuildKbi = true;
                } else {
                    lvBuildKbi.setVisibility(View.GONE);
                    showBuildKbi = false;
                }
                break;
            case R.id.lin_kbiWork:
                if (!showPutIntoKbi) {
                    lvPutIntoKbi.setVisibility(View.VISIBLE);
                    showPutIntoKbi = true;
                } else {
                    lvPutIntoKbi.setVisibility(View.GONE);
                    showPutIntoKbi = false;
                }
                break;
            case R.id.iv_right:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        switch (messageEvent.getMessage()) {
            case "已建考核列表页刷新":
                listBuildKbi.clear();
                getData("0");
                listPutIntoKbi.clear();
                getData("1");
                break;
            case "考核实施列表页刷新":
                listPutIntoKbi.clear();
                getData("1");
                break;
            default:
                break;
        }
    }
}
