package com.example.physicalfitnessexamination.page.kbi;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.physicalfitnessexamination.Constants;
import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.activity.UserManager;
import com.example.physicalfitnessexamination.app.Api;
import com.example.physicalfitnessexamination.base.MyBaseActivity;
import com.example.physicalfitnessexamination.bean.AllPersonBean;
import com.example.physicalfitnessexamination.bean.MessageEvent;
import com.example.physicalfitnessexamination.bean.PostBean;
import com.example.physicalfitnessexamination.bean.ReferencePersonnelBean;
import com.example.physicalfitnessexamination.bean.UserInfo;
import com.example.physicalfitnessexamination.common.adapter.CommonAdapter;
import com.example.physicalfitnessexamination.okhttp.CallBackUtil;
import com.example.physicalfitnessexamination.okhttp.OkhttpUtil;
import com.example.physicalfitnessexamination.util.Tool;
import com.example.physicalfitnessexamination.view.excel.SpinnerParentView;
import com.example.physicalfitnessexamination.viewholder.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.Call;

/**
 * 考核花名册报名
 */
public class KBIRosterEnrollActivity extends MyBaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageView imgRight;
    private TextView tvUnit;//单位
    private TextView tvRequirements;//人员要求
    private EditText etNumber;//随机数量
    private TextView tvOkRandom;//随机数确定
    private SpinnerParentView spvPost;//岗位选择
    private GridView gvPost, gvRandom;
    private CommonAdapter<AllPersonBean> commonAdapterPost;
    private CommonAdapter<AllPersonBean> commonAdapterRandom;
    private List<AllPersonBean> listPost = new ArrayList<>();
    private List<AllPersonBean> listPost1 = new ArrayList<>();//type-1
    private List<AllPersonBean> listPost2 = new ArrayList<>();//type-2
    private List<AllPersonBean> listRandom = new ArrayList<>();
    private List<AllPersonBean> listRandom1 = new ArrayList<>();//type-1
    private List<AllPersonBean> listRandom2 = new ArrayList<>();//type-2
    private List<PostBean> listType = new ArrayList<>();
    private List<ReferencePersonnelBean> listRoster = new ArrayList<>();
    private String requirements;//人员要求
    private String personType;//人员选取方式 1-随机抽取 2-单位报名
    private UserInfo userInfo;
    private int type = 1;
    private TextView tvCommit;//人员报名提交
    private String id;//考核id
    private TextView tvExtract;//随机抽取显示

    /**
     * 跳转方法
     *
     * @param context 上下文
     */
    public static void startInstant(Context context, String requirements, String personType, String id, List<ReferencePersonnelBean> listRoster) {
        Intent intent = new Intent(context, KBIRosterEnrollActivity.class);
        intent.putExtra("requirements", requirements);
        intent.putExtra("personType", personType);
        intent.putExtra("id", id);
        intent.putParcelableArrayListExtra("listRoster", (ArrayList<? extends Parcelable>) listRoster);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_kbi_roster_enroll;
    }

    @Override
    protected void initView() {
        userInfo = UserManager.getInstance().getUserInfo(this);
        requirements = getIntent().getStringExtra("requirements");
        personType = getIntent().getStringExtra("personType");
        id = getIntent().getStringExtra("id");
        listRoster = getIntent().getParcelableArrayListExtra("listRoster");
        tvTitle = findViewById(R.id.tv_title);
        imgRight = findViewById(R.id.iv_right);
        imgRight.setOnClickListener(this::onClick);
        tvRequirements = findViewById(R.id.tv_requirements);
        tvUnit = findViewById(R.id.tv_unit);
        spvPost = findViewById(R.id.spv_post);
        etNumber = findViewById(R.id.et_number);
        tvOkRandom = findViewById(R.id.tv_ok_random);
        tvOkRandom.setOnClickListener(this::onClick);
        gvPost = findViewById(R.id.gv_post);
        tvExtract = findViewById(R.id.tv_extract);
        gvRandom = findViewById(R.id.gv_random);
        tvCommit = findViewById(R.id.tv_commit);
        tvCommit.setOnClickListener(this::onClick);
    }

    @Override
    protected void initData() {
        tvTitle.setText("考核花名册 - 报名");
        tvRequirements.setText(requirements);
        tvUnit.setText(UserManager.getInstance().getUserInfo(this).getOrg_name());
        switch (personType) {
            case "1":
                tvExtract.setText("抽取人数");
                etNumber.setVisibility(View.VISIBLE);
                tvOkRandom.setVisibility(View.VISIBLE);
                break;
            case "2":
                tvExtract.setText("被选人员");
                etNumber.setVisibility(View.GONE);
                tvOkRandom.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        spvPost.setName("岗位");
        spvPost.setBackgroundColor(ContextCompat.getColor(this, R.color._E6ECFA));
        switch (userInfo.getRole_id()) {
            case Constants.RoleIDStr.COMM:
                setType("消防站指挥员", "消防站消防员");
                break;
            case Constants.RoleIDStr.MANAGE:
                setType("支队领导", "支队指挥员");
                break;
            case Constants.RoleIDStr.MANAGE_BRIGADE:
                setType("大队领导", "大队指挥员");
                break;
            default:
                break;
        }

        HashSet<Integer> defSet = new HashSet();
        defSet.add(0);

        spvPost.setSpinner(listType.toArray(), new SpinnerParentView.OnGetStrListener() {
            @NotNull
            @Override
            public String getStr(Object bean) {
                PostBean postBean = (PostBean) bean;
                return postBean.getName();
            }
        }, new SpinnerParentView.OnCheckListener() {
            @Override
            public void onConfirmAndChangeListener(@NotNull SpinnerParentView view, @NotNull List selectBeanList) {
                switch (((PostBean) selectBeanList.get(0)).getType()) {
                    case "1":
                        type = 1;
                        listPost.clear();
                        listPost.addAll(listPost1);
                        commonAdapterPost.notifyDataSetChanged();
                        listRandom.clear();
                        listRandom.addAll(listRandom1);
                        commonAdapterRandom.notifyDataSetChanged();
                        break;
                    case "2":
                        type = 2;
                        listPost.clear();
                        listPost.addAll(listPost2);
                        commonAdapterPost.notifyDataSetChanged();
                        listRandom.clear();
                        listRandom.addAll(listRandom2);
                        commonAdapterRandom.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        }, true, defSet);
        getPersonList("1");
        getPersonList("2");

        commonAdapterPost = new CommonAdapter<AllPersonBean>(this, R.layout.item_kbi_roster_enroll_post, listPost) {
            @Override
            public void convert(ViewHolder viewHolder, AllPersonBean s) {
                int position = viewHolder.getPosition();
                viewHolder.setText(R.id.tv_order, position + 1 + "");
                viewHolder.setText(R.id.tv_name, s.getNAME());
                viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("2".equals(personType)) {
                            switch (type) {
                                case 1:
                                    AllPersonBean allPersonBean = listPost1.get(position);
                                    listRandom1.add(allPersonBean);
                                    listPost1.remove(allPersonBean);
                                    listPost.clear();
                                    listPost.addAll(listPost1);
                                    commonAdapterPost.notifyDataSetChanged();
                                    listRandom.clear();
                                    listRandom.addAll(listRandom1);
                                    commonAdapterRandom.notifyDataSetChanged();
                                    break;
                                case 2:
                                    AllPersonBean allPersonBean1 = listPost2.get(position);
                                    listRandom2.add(allPersonBean1);
                                    listPost2.remove(allPersonBean1);
                                    listPost.clear();
                                    listPost.addAll(listPost2);
                                    commonAdapterPost.notifyDataSetChanged();
                                    listRandom.clear();
                                    listRandom.addAll(listRandom2);
                                    commonAdapterRandom.notifyDataSetChanged();
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                });
            }
        };
        gvPost.setAdapter(commonAdapterPost);


        commonAdapterRandom = new CommonAdapter<AllPersonBean>(this, R.layout.item_kbi_roster_enroll_random, listRandom) {
            @Override
            public void convert(ViewHolder viewHolder, AllPersonBean s) {
                int position = viewHolder.getPosition();
                viewHolder.setText(R.id.tv_order, position + 1 + "");
                viewHolder.setText(R.id.tv_name, s.getNAME());
                viewHolder.getView(R.id.img_delete).setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View view) {
                        switch (type) {
                            case 1:
                                AllPersonBean allPersonBean = listRandom1.get(position);
                                listPost1.add(allPersonBean);
                                listRandom1.remove(allPersonBean);
                                listPost.clear();
                                listPost.addAll(listPost1);
                                commonAdapterPost.notifyDataSetChanged();
                                listRandom.clear();
                                listRandom.addAll(listRandom1);
                                commonAdapterRandom.notifyDataSetChanged();
                                break;
                            case 2:
                                AllPersonBean allPersonBean1 = listRandom2.get(position);
                                listPost2.add(allPersonBean1);
                                listRandom2.remove(allPersonBean1);
                                listPost.clear();
                                listPost.addAll(listPost2);
                                commonAdapterPost.notifyDataSetChanged();
                                listRandom.clear();
                                listRandom.addAll(listRandom2);
                                commonAdapterRandom.notifyDataSetChanged();
                                break;
                            default:
                                break;
                        }
                    }
                });
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
            case R.id.tv_ok_random:
                HashSet<Integer> hashSet = new HashSet<>();
                Random random = new Random();
                int number = Integer.parseInt(String.valueOf(etNumber.getText()));
                int max;
                if (type == 1) {
                    max = listPost1.size();
                } else {
                    max = listPost2.size();
                }
                if (number > max) {
                    showToast("人数不足以抽取");
                    return;
                }
                do {
                    hashSet.add(random.nextInt(max));
                } while (hashSet.size() != number);
                if (type == 1) {
                    for (Integer index : hashSet) {
                        AllPersonBean bean = listPost1.get(index);
                        listRandom1.add(bean);
                    }
                    listPost1.removeAll(listRandom1);
                    listPost.clear();
                    listPost.addAll(listPost1);
                    commonAdapterPost.notifyDataSetChanged();
                    listRandom.clear();
                    listRandom.addAll(listRandom1);
                    commonAdapterRandom.notifyDataSetChanged();
                } else {
                    for (Integer index : hashSet) {
                        AllPersonBean bean = listPost2.get(index);
                        listRandom2.add(bean);
                    }
                    listPost2.removeAll(listRandom2);
                    listPost.clear();
                    listPost.addAll(listPost2);
                    commonAdapterPost.notifyDataSetChanged();
                    listRandom.clear();
                    listRandom.addAll(listRandom2);
                    commonAdapterRandom.notifyDataSetChanged();
                }
                break;
            case R.id.tv_commit:
                commitData();
                break;
            default:
                break;
        }
    }

    public void setType(String s, String s1) {
        PostBean postBean = new PostBean();
        postBean.setName(s);
        postBean.setType("1");
        PostBean postBean1 = new PostBean();
        postBean1.setName(s1);
        postBean1.setType("2");
        listType.add(postBean);
        listType.add(postBean1);
    }

    public void getPersonList(String type) {
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("aid", id);
        map.put("org_id", userInfo.getOrg_id());
        OkhttpUtil.okHttpPost(Api.GETORGCOMMANDER, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    switch (type) {
                        case "1":
                            listPost1.addAll(JSON.parseArray(JSON.parseObject(response).getString("data"), AllPersonBean.class));
                            for (int i = 0; i < listRoster.size(); i++) {
                                for (int j = 0; j < listPost1.size(); j++) {
                                    if (listRoster.get(i).getUSERID().equals(listPost1.get(j).getID())) {
                                        AllPersonBean allPersonBean = listPost1.get(j);
                                        listRandom1.add(allPersonBean);
                                        listPost1.remove(allPersonBean);
                                    }
                                }

                            }
                            listPost.clear();
                            listPost.addAll(listPost1);
                            commonAdapterPost.notifyDataSetChanged();
                            listRandom.clear();
                            listRandom.addAll(listRandom1);
                            commonAdapterRandom.notifyDataSetChanged();
                            break;
                        case "2":
                            listPost2.addAll(JSON.parseArray(JSON.parseObject(response).getString("data"), AllPersonBean.class));
                            for (int i = 0; i < listRoster.size(); i++) {
                                for (int j = 0; j < listPost2.size(); j++) {
                                    if (listRoster.get(i).getUSERID().equals(listPost2.get(j).getID())) {
                                        AllPersonBean allPersonBean = listPost2.get(j);
                                        listRandom2.add(allPersonBean);
                                        listPost2.remove(allPersonBean);
                                    }
                                }
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    public void commitData() {
        List<AllPersonBean> listCommit = new ArrayList<>();
        listCommit.addAll(listRandom1);
        listCommit.addAll(listRandom2);
        for (int i = 0; i < listCommit.size(); i++) {
            listCommit.get(i).setNO(i + "");
        }
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("org_id", userInfo.getOrg_id());
        map.put("persons", Tool.transformLowerCase(JSON.toJSONString(listCommit)));
        OkhttpUtil.okHttpPost(Api.SETPERSONFORASSESSMENT, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    EventBus.getDefault().post(new MessageEvent("刷新参赛人员列表"));
                    finish();
                }
            }
        });
    }
}
