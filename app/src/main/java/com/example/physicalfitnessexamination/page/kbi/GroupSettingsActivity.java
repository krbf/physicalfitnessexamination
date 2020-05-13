package com.example.physicalfitnessexamination.page.kbi;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.czy.module_common.okhttp.CallBackUtil;
import com.czy.module_common.okhttp.OkhttpUtil;
import com.czy.module_common.utils.Tool;
import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.app.Api;
import com.example.physicalfitnessexamination.base.MyBaseActivity;
import com.example.physicalfitnessexamination.bean.GroupCommitBean;
import com.example.physicalfitnessexamination.bean.GroupSettingsBean;
import com.example.physicalfitnessexamination.bean.MessageEvent;
import com.example.physicalfitnessexamination.bean.PersonAchievementBean;
import com.example.physicalfitnessexamination.common.adapter.CommonAdapter;
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

public class GroupSettingsActivity extends MyBaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageView imgRight;
    private TextView tvNumber;
    private SpinnerParentView spvGroup;
    private ListView lvGroup;
    private CommonAdapter<GroupSettingsBean> commonAdapter;
    private List<GroupSettingsBean> listGroup = new ArrayList<>();
    private List<String> listGroupNumber = new ArrayList<>();
    private List<PersonAchievementBean> listPersonAchievement = new ArrayList<>();
    private TextView tvCommit;
    private int personNumber;
    private String id;//考核id
    private String gw;//岗位
    private String sid;//项目id

    /**
     * 跳转方法
     *
     * @param context 上下文
     */
    public static void startInstant(Context context, int number, List<PersonAchievementBean> listPersonAchievement, String id, String gw, String sid) {
        Intent intent = new Intent(context, GroupSettingsActivity.class);
        intent.putExtra("number", number);
        intent.putParcelableArrayListExtra("listPersonAchievement", (ArrayList<? extends Parcelable>) listPersonAchievement);
        intent.putExtra("id", id);
        intent.putExtra("gw", gw);
        intent.putExtra("sid", sid);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_group_settings;
    }

    @Override
    protected void initView() {
        personNumber = getIntent().getIntExtra("number", 0);
        listPersonAchievement = getIntent().getParcelableArrayListExtra("listPersonAchievement");
        id = getIntent().getStringExtra("id");
        gw = getIntent().getStringExtra("gw");
        sid = getIntent().getStringExtra("sid");
        tvTitle = findViewById(R.id.tv_title);
        imgRight = findViewById(R.id.iv_right);
        imgRight.setOnClickListener(this::onClick);
        tvNumber = findViewById(R.id.tv_number);
        spvGroup = findViewById(R.id.spv_group);
        lvGroup = findViewById(R.id.lv_group);
        tvCommit = findViewById(R.id.tv_commit);
        tvCommit.setOnClickListener(this::onClick);
    }

    @Override
    protected void initData() {
        tvTitle.setText("组别设置");
        tvNumber.setText("总人数：" + personNumber);
        spvGroup.setName("分组");
        for (int i = 1; i < personNumber + 1; i++) {
            listGroupNumber.add(i + "");
        }
        HashSet<Integer> defSet = new HashSet<>();
//        defSet.add(0);
        spvGroup.setSpinner(listGroupNumber.toArray(), new SpinnerParentView.OnGetStrListener() {
            @NotNull
            @Override
            public String getStr(Object bean) {
                return bean.toString();
            }
        }, new SpinnerParentView.OnCheckListener() {
            @Override
            public void onConfirmAndChangeListener(@NotNull SpinnerParentView view, @NotNull List selectBeanList) {
                int number = Integer.parseInt(selectBeanList.get(0).toString());
                int result = personNumber / number;
                int remainder = personNumber % number;
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
                    groupSettingsBean.setPersonNumber(groupSettingsBean.getEndOrder() - groupSettingsBean.getStartOrder() + 1);
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
            case R.id.tv_commit:
                GroupCommitBean groupCommitBean = new GroupCommitBean();
                groupCommitBean.setAid(id);
                groupCommitBean.setSid(sid);
                groupCommitBean.setGw(gw);
                List<ArrayList<String>> arrayListList = new ArrayList<>();
                Random random = new Random();
                for (int i = 0; i < listGroup.size(); i++) {
                    HashSet<Integer> hashSet = new HashSet<>();
                    ArrayList<String> arrayList = new ArrayList<>();
                    List<PersonAchievementBean> list = new ArrayList<>();
                    do {
                        hashSet.add(random.nextInt(listPersonAchievement.size()));
                    } while (hashSet.size() != listGroup.get(i).getPersonNumber());
                    for (Integer index : hashSet) {
                        PersonAchievementBean personAchievementBean = listPersonAchievement.get(index);
                        list.add(personAchievementBean);
                        arrayList.add(personAchievementBean.getUSERID());
                    }
                    listPersonAchievement.removeAll(list);
                    arrayListList.add(arrayList);
                }
                groupCommitBean.setPlist(arrayListList);
                if (groupCommitBean.getPlist().size() == 0) {
                    showToast("分组后才可以进行提交");
                } else {
                    commitGroup(groupCommitBean);
                }
                break;
            default:
                break;
        }
    }

    public void commitGroup(GroupCommitBean groupCommitBean) {
        Map<String, String> map = new HashMap<>();
        map.put("personTeamList", Tool.transformLowerCase(JSON.toJSONString(groupCommitBean)));
        OkhttpUtil.okHttpPost(Api.SETPERSONTEAMFORASSESSMENT, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    showToast("分组成功");
                    EventBus.getDefault().post(new MessageEvent("分组刷新"));
                    finish();
                }else {
                    showToast("分组失败");
                }
            }
        });
    }
}
