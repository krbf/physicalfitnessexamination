package com.example.physicalfitnessexamination.page.kbi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.activity.UserManager;
import com.example.physicalfitnessexamination.app.Api;
import com.example.physicalfitnessexamination.base.MyBaseActivity;
import com.example.physicalfitnessexamination.bean.BuiltKBIListBean;
import com.example.physicalfitnessexamination.common.adapter.CommonAdapter;
import com.example.physicalfitnessexamination.okhttp.CallBackUtil;
import com.example.physicalfitnessexamination.okhttp.OkhttpUtil;
import com.example.physicalfitnessexamination.viewholder.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
/**
 * 已建考核列表页
 */
public class BuiltKBIActivity extends MyBaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageView imgRight;
    private ListView lvKBI;
    private List<BuiltKBIListBean> list = new ArrayList<>();
    private CommonAdapter<BuiltKBIListBean> commonAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 跳转方法
     *
     * @param context 上下文
     */
    public static void startInstant(Context context) {
        Intent intent = new Intent(context, BuiltKBIActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_built_kbi;
    }

    @Override
    protected void initView() {
        tvTitle = findViewById(R.id.tv_title);
        imgRight = findViewById(R.id.iv_right);
        lvKBI = findViewById(R.id.lv_kbi);
        imgRight.setOnClickListener(this::onClick);
    }

    @Override
    protected void initData() {
        tvTitle.setText("已建考核");
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
                        BuiltKBIDetailActivity.startInstant(BuiltKBIActivity.this,s.getID());
                    }
                });
            }
        };
        lvKBI.setAdapter(commonAdapter);
        getData();
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

    public void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("org_id", UserManager.getInstance().getUserInfo(this).getOrg_id());
        map.put("status","0");//0 已建考核  1 考核实施
        OkhttpUtil.okHttpGet(Api.BUILTKBILIST, map, new CallBackUtil.CallBackString() {
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
}
