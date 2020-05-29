package com.example.physicalfitnessexamination.util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.physicalfitnessexamination.R;

import java.util.ArrayList;
import java.util.List;

public class ShowSingleView<T> {
    private List<T> options1Items = new ArrayList<>();
    private OptionsPickerView pvOptions;
    private Listener<T> listener;
    private Context context;

    public ShowSingleView(Context context, List<T> options1Items, Listener listener) {
        this.context = context;
        this.options1Items = options1Items;
        this.listener = listener;
    }

    public interface Listener<T> {
        void data(T t);
    }

    public void show() {// 弹出选择器
        pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if (options1Items.size()>0){
                    listener.data(options1Items.get(options1));
                }

            }
        }).setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
            @Override
            public void onOptionsSelectChanged(int options1, int options2, int options3) {

            }
        }).setLayoutRes(R.layout.view_show_single, new CustomListener() {
            @Override
            public void customLayout(View v) {
                TextView tvSubmit = v.findViewById(R.id.tv_finish);

                TextView tvCancel = v.findViewById(R.id.tv_cancel);
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                .setTitleText("问题场景")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();
        pvOptions.setPicker(options1Items);

        pvOptions.show();
    }
}
