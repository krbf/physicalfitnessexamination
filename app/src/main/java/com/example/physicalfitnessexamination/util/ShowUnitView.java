package com.example.physicalfitnessexamination.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.czy.module_common.okhttp.CallBackUtil;
import com.czy.module_common.okhttp.OkhttpUtil;
import com.example.physicalfitnessexamination.Constants;
import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.activity.UserManager;
import com.example.physicalfitnessexamination.bean.UnitBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class ShowUnitView {
    private List<UnitBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<UnitBean>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<UnitBean>>> options3Items = new ArrayList<>();
    private List<UnitBean> list = new ArrayList<>();
    private OptionsPickerView pvOptions;
    private int flag;
    private Listener listener;
    private Context context;
    private boolean firehouse = true;

    public ShowUnitView(Context context, Listener listener, boolean firehouse) {
        this.context = context;
        this.listener = listener;
        this.firehouse = firehouse;
    }

    public interface Listener {
        void data(UnitBean unitBean);
    }

    public void setView() {// 弹出选择器
        pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String opt1tx = options1Items.size() > 0 ?
                        options1Items.get(options1).getName() : "";

                String opt2tx = options2Items.size() > 0
                        && options2Items.get(options1).size() > 0 ?
                        options2Items.get(options1).get(options2).getName() : "";

                String opt3tx = options2Items.size() > 0
                        && options3Items.get(options1).size() > 0
                        && options3Items.get(options1).get(options2).size() > 0 ?
                        options3Items.get(options1).get(options2).get(options3).getName() : "";

                String tx = opt1tx + opt2tx + opt3tx;
                switch (flag) {
                    case 1:
                        listener.data(options1Items.size() > 0 ? options1Items.get(options1) : new UnitBean());
                        break;
                    case 2:
                        listener.data(options2Items.size() > 0
                                && options2Items.get(options1).size() > 0 ?
                                options2Items.get(options1).get(options2) : new UnitBean());
                        break;
                    case 3:
                        listener.data(options2Items.size() > 0
                                && options3Items.get(options1).size() > 0
                                && options3Items.get(options1).get(options2).size() > 0 ?
                                options3Items.get(options1).get(options2).get(options3) : new UnitBean());
                        break;
                }
            }
        }).setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
            @Override
            public void onOptionsSelectChanged(int options1, int options2, int options3) {

            }
        }).setLayoutRes(R.layout.view_three_level_linkage, new CustomListener() {
            @Override
            public void customLayout(View v) {
                TextView tvSubmit1 = v.findViewById(R.id.tv_finish1);
                tvSubmit1.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                TextView tvSubmit2 = v.findViewById(R.id.tv_finish2);
                tvSubmit2.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                TextView tvSubmit3 = v.findViewById(R.id.tv_finish3);
                tvSubmit3.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                if (!firehouse){
                    tvSubmit3.setVisibility(View.INVISIBLE);
                }
                TextView tvCancel = v.findViewById(R.id.tv_cancel);
                tvSubmit1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        flag = 1;
                        pvOptions.returnData();
                        pvOptions.dismiss();
                    }
                });
                tvSubmit2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        flag = 2;
                        pvOptions.returnData();
                        pvOptions.dismiss();
                    }
                });
                tvSubmit3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        flag = 3;
                        pvOptions.returnData();
                        pvOptions.dismiss();
                    }
                });
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvOptions.dismiss();
                    }
                });
            }
        })
                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();
        if (firehouse) {
            pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        }else {
            pvOptions.setPicker(options1Items, options2Items);//二级选择器
        }
        pvOptions.show();
    }

    public void showUnitView() {
        Map<String, String> map = new HashMap<>();
        map.put("role_id", "manage");
        map.put("org_id", Constants.UNITID);
        OkhttpUtil.okHttpPost(Constants.IP + "sysorg/doListSysOrg", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                list.addAll(JSON.parseArray(response, UnitBean.class));
                options1Items.add(list.get(0));
                for (int j = 0; j < options1Items.size(); j++) {
                    ArrayList<UnitBean> options2Items_child = new ArrayList<>();
                    for (UnitBean unitBean : list) {
                        if (unitBean.getpId().equals(options1Items.get(j).getId())) {
                            options2Items_child.add(unitBean);
                        }
                    }
                    options2Items.add(options2Items_child);
                }

                for (int i = 0; i < options2Items.size(); i++) {
                    ArrayList<ArrayList<UnitBean>> options3Items_child = new ArrayList<>();
                    for (int k = 0; k < options2Items.get(i).size(); k++) {
                        ArrayList<UnitBean> options3Items_child_child = new ArrayList<>();
                        for (UnitBean unitBean : list) {
                            if (unitBean.getpId().equals(options2Items.get(i).get(k).getId())) {
                                options3Items_child_child.add(unitBean);
                            }
                        }
                        options3Items_child.add(options3Items_child_child);
                    }
                    options3Items.add(options3Items_child);
                }
                setView();
            }
        });
    }
}
