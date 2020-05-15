package com.example.physicalfitnessexamination.page.rank.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.czy.module_common.glide.ImageLoaderUtils;
import com.czy.module_common.okhttp.CallBackUtil;
import com.czy.module_common.okhttp.OkhttpUtil;
import com.example.physicalfitnessexamination.Constants;
import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.app.Api;
import com.example.physicalfitnessexamination.bean.AllAroundBean;
import com.example.physicalfitnessexamination.bean.UnitBean;
import com.example.physicalfitnessexamination.common.adapter.CommonAdapter;
import com.example.physicalfitnessexamination.util.ShowUnitView;
import com.example.physicalfitnessexamination.view.excel.SpinnerParentView;
import com.example.physicalfitnessexamination.viewholder.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Rank4Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Rank4Fragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private GridView gvUnit_5;
    private List<AllAroundBean> list = new ArrayList<>();
    private CommonAdapter<AllAroundBean> commonAdapter;
    private LinearLayout linUnit;//机构
    private TextView tvUnitHint;
    private TextView tvUnit;//机构
    private String org_id;

    public Rank4Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Rank4Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Rank4Fragment newInstance(String param1, String param2) {
        Rank4Fragment fragment = new Rank4Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rank4, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gvUnit_5 = view.findViewById(R.id.gv_unit_5);
        linUnit = view.findViewById(R.id.lin_unit);
        linUnit.setOnClickListener(this::onClick);
        tvUnitHint = view.findViewById(R.id.tv_unit_hint);
        tvUnit = view.findViewById(R.id.tv_unit);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        HashSet<Integer> defSet = new HashSet();
        Drawable drawable = getResources().getDrawable(R.mipmap.ic_down_arrow);
        // 设置图片的大小
        drawable.setBounds(0, 0, 60, 60);
        // 设置图片的位置，左、上、右、下
        tvUnitHint.setCompoundDrawables(null, null, drawable, null);
        commonAdapter = new CommonAdapter<AllAroundBean>(getContext(), R.layout.item_rank4_fragment, list) {
            @Override
            public void convert(ViewHolder viewHolder, AllAroundBean allAroundBean) {
                viewHolder.setText(R.id.tv_unit, allAroundBean.getORG_NAME());
                ImageView imagePhoto = viewHolder.getView(R.id.img_photo);
                ImageLoaderUtils.display(getContext(), imagePhoto, Constants.IP + allAroundBean.getPHOTO());
                ImageView imageIcon = viewHolder.getView(R.id.img_icon);
                ImageLoaderUtils.display(getContext(), imageIcon, Constants.IP + allAroundBean.getICON());
            }
        };
        gvUnit_5.setAdapter(commonAdapter);
    }

    public void getData(String org_id) {
        Map<String, String> map = new HashMap<>();
        map.put("org_id", org_id);
        OkhttpUtil.okHttpPost(Api.GETORGSCORESUMLIST4BSYM, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    list.clear();
                    list.addAll(JSON.parseArray(JSON.parseObject(response).getString("data"), AllAroundBean.class));
                    commonAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_unit:
                new ShowUnitView(getContext(), new ShowUnitView.Listener() {
                    @Override
                    public void data(UnitBean unitBean) {
                        tvUnit.setText(unitBean.getName());
                        org_id = unitBean.getId();
                        getData(org_id);
                    }
                }, false).showUnitView();
                break;
        }
    }
}
