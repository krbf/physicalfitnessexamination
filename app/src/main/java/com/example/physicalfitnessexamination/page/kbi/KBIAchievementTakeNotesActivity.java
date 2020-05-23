package com.example.physicalfitnessexamination.page.kbi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.physicalfitnessexamination.Constants;
import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.app.Api;
import com.example.physicalfitnessexamination.base.MyBaseActivity;
import com.example.physicalfitnessexamination.bean.ClauseBean;
import com.example.physicalfitnessexamination.bean.MessageEvent;
import com.example.physicalfitnessexamination.bean.PersonAchievementBean;
import com.example.physicalfitnessexamination.common.adapter.CommonAdapter;
import com.czy.module_common.glide.ImageLoaderUtils;
import com.czy.module_common.okhttp.CallBackUtil;
import com.czy.module_common.okhttp.OkhttpUtil;
import com.example.physicalfitnessexamination.util.SportKeyBoardUtil;
import com.czy.module_common.utils.Tool;
import com.example.physicalfitnessexamination.view.DMDialog;
import com.example.physicalfitnessexamination.view.SportKeyBoardView;
import com.example.physicalfitnessexamination.view.dialog.MessageDialog;
import com.example.physicalfitnessexamination.view.excel.SpinnerParentView;
import com.example.physicalfitnessexamination.viewholder.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;

/**
 * 考核成绩记录表
 */
public class KBIAchievementTakeNotesActivity extends MyBaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageView imgRight;
    private TextView tvClause;//项目显示
    private TextView tvPost;//岗位显示
    private TextView tvGroupSetting;//组别设置
    private String id;//考核id
    private ClauseBean.Clause clause;//项目实体类
    private String GW;//岗位
    private ListView lvAchievementTakeNotes;
    private CommonAdapter<PersonAchievementBean> commonAdapter;
    private List<PersonAchievementBean> list = new ArrayList<>();
    private List<PersonAchievementBean> listAll = new ArrayList<>();
    private OptionsPickerView pvNoLinkOptions;//条件选择器
    private ArrayList<String> food = new ArrayList<>();
    private ArrayList<String> clothes = new ArrayList<>();
    private ArrayList<String> computer = new ArrayList<>();
    private SpinnerParentView spvGroup;//组别
    private int teamCount;//组数
    private String currentGroup = null;//当前组数
    private String flag;//1-已建考核 2-考核实施 3-历史考核
    private TextView tvAchievement;//成绩列
    private MessageDialog messageDialog;

    /**
     * 跳转方法
     *
     * @param context 上下文
     */
    public static void startInstant(Context context, String id, ClauseBean.Clause clause, String GW, String flag) {
        Intent intent = new Intent(context, KBIAchievementTakeNotesActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("clause", (Parcelable) clause);
        intent.putExtra("GW", GW);
        intent.putExtra("flag", flag);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        messageDialog = MessageDialog.newInstance("加载中，请稍等……");
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_kbi_achievement_take_notes;
    }

    @Override
    protected void initView() {
        id = getIntent().getStringExtra("id");
        clause = getIntent().getParcelableExtra("clause");
        GW = getIntent().getStringExtra("GW");
        flag = getIntent().getStringExtra("flag");
        tvTitle = findViewById(R.id.tv_title);
        imgRight = findViewById(R.id.iv_right);
        imgRight.setOnClickListener(this::onClick);
        lvAchievementTakeNotes = findViewById(R.id.lv_achievement_take_notes);
        tvClause = findViewById(R.id.tv_clause);
        tvPost = findViewById(R.id.tv_post);
        tvGroupSetting = findViewById(R.id.tv_group_setting);
        tvGroupSetting.setOnClickListener(this::onClick);
        spvGroup = findViewById(R.id.spv_group);
        tvAchievement = findViewById(R.id.tv_achievement);
    }

    @Override
    protected void initData() {
        if ("2".equals(flag)) {
            tvGroupSetting.setVisibility(View.GONE);
        }
        if ("1".equals(flag)) {
            tvAchievement.setVisibility(View.GONE);
        }
        tvTitle.setText("成绩记录表");
        tvClause.setText(clause.getNAME());
        tvPost.setText(GW);
        spvGroup.setName("组别");
        getGroup();
        getAllData();
        commonAdapter = new CommonAdapter<PersonAchievementBean>(this, R.layout.item_kbi_achievement_takes_notes, list) {
            @Override
            public void convert(ViewHolder viewHolder, PersonAchievementBean s) {
                viewHolder.setText(R.id.tv_order, viewHolder.getPosition() + 1 + "");
                viewHolder.setText(R.id.tv_number, s.getNO());
                viewHolder.setText(R.id.tv_name, s.getUSERNAME());
                viewHolder.setText(R.id.tv_unit, s.getORG_NAME());
                viewHolder.setText(R.id.tv_post, s.getGW());
                viewHolder.setText(R.id.tv_achievement, s.getACHIEVEMENT());
                if ("1".equals(flag)) {
                    viewHolder.getView(R.id.tv_achievement).setVisibility(View.GONE);
                }
                viewHolder.getView(R.id.tv_achievement).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DMDialog.builder(KBIAchievementTakeNotesActivity.this, R.layout.dialog_achievement_input)
                                .onDialogInitListener((helper, dialog) ->
                                {
                                    helper.setText(R.id.tv_name, s.getUSERNAME());
                                    helper.setText(R.id.tv_unit, s.getORG_NAME());
                                    ImageLoaderUtils.display(KBIAchievementTakeNotesActivity.this, helper.getView(R.id.img_photo), Constants.IP + s.getPHOTO());
                                    if ("1".equals(clause.getATYPE())) {
                                        helper.setText(R.id.tv_measurement, clause.getDW());
                                    }
//                                    else {
//                                        helper.setText(R.id.tv_measurement, "格式00’00”00");
//                                    }
                                    EditText edtAchievement = helper.getView(R.id.edt_achievement);

                                    int flag;
                                    if ("1".equals(clause.getATYPE())) {//次数
                                        flag = 0;
                                        edtAchievement.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                            }

                                            @Override
                                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                                            }

                                            @Override
                                            public void afterTextChanged(Editable s) {
                                                if (s.toString().length() == 1 && s.toString().equals("0")) {
                                                    showToast("输入不合法");
                                                    edtAchievement.setText("");
                                                }

                                            }
                                        });
                                    } else {
                                        flag = 1;
                                    }

                                    final SportKeyBoardUtil[] sportKeyBoardUtil = new SportKeyBoardUtil[1];

                                    SportKeyBoardView keyboardView = helper.getView(R.id.ky_keyboard);
                                    LinearLayout ky_keyboard_parent = helper.getView(R.id.ky_keyboard_parent);
                                    sportKeyBoardUtil[0] = new SportKeyBoardUtil(ky_keyboard_parent, keyboardView, edtAchievement, flag);
                                    edtAchievement.setOnTouchListener(new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {
                                            if (sportKeyBoardUtil[0] == null) {
                                                sportKeyBoardUtil[0] = new SportKeyBoardUtil(ky_keyboard_parent, keyboardView, edtAchievement, flag);
                                            }
                                            sportKeyBoardUtil[0].showKeyboard();
                                            return false;
                                        }
                                    });

                                    helper.setOnClickListener(R.id.tv_ok, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            switch (clause.getATYPE()) {
                                                case "0"://时间计数
                                                    String result = edtAchievement.getText().toString();

                                                    // 首先要编译正则规则形式
                                                    Pattern p = Pattern.compile("([0-5][0-9]’[0-5][0-9]”[0-9][0-9])|([0-5][0-9]’[0-5][0-9])" +
                                                            "|([0-5][0-9]”[0-9][0-9])|([0-9]’[0-5][0-9]”[0-9][0-9])" +
                                                            "|([0-9]’[0-9]”[0-9][0-9])|([0-9]’[0-5][0-9])|([0-9]’[0-9])|([0-9]”[0-9][0-9])");
                                                    // 将正则进行匹配
                                                    Matcher m = p.matcher(result);
                                                    // 进行判断
                                                    boolean b = m.matches();
                                                    if (!b) {
                                                        showToast("输入不合法");
                                                        return;
                                                    }
                                                    String[] re = result.split("’|”");
                                                    int achievement = 0;
                                                    if (re.length == 3) {
                                                        achievement = Integer.parseInt(re[0]) * 60 * 1000 + Integer.parseInt(re[1]) * 1000 + Integer.parseInt(re[2]) * 10;
                                                    } else if (re.length == 2) {
                                                        if (result.contains("”")) {
                                                            achievement = Integer.parseInt(re[0]) * 1000 + Integer.parseInt(re[1]) * 10;
                                                        } else {
                                                            achievement = Integer.parseInt(re[0]) * 60 * 1000 + Integer.parseInt(re[1]) * 1000;
                                                        }
                                                    } else if (re.length == 1) {
                                                        if (result.contains("”")) {
                                                            achievement = Integer.parseInt(re[0]) * 1000;
                                                        } else {
                                                            if (result.contains("’")) {
                                                                achievement = Integer.parseInt(re[0]) * 60 * 1000;
                                                            } else {
                                                                showToast("请输入单位");
                                                                return;
                                                            }
                                                        }
                                                    }
                                                    submission(s.getUSERID(), String.valueOf(achievement));
                                                    break;
                                                case "1"://次数
                                                    if (Tool.isEmpty(edtAchievement.getText().toString())) {
                                                        showToast("请输入成绩");
                                                        return;
                                                    }
                                                    submission(s.getUSERID(), edtAchievement.getText().toString());
                                                    break;
                                            }
                                            dialog.dismiss();
                                        }
                                    });
                                })
                                .setGravity(Gravity.CENTER)
                                .show();
                    }
                });
            }
        };
        lvAchievementTakeNotes.setAdapter(commonAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_right:
                finish();
                break;
            case R.id.tv_group_setting:
                if (listAll.size() < 1) {
                    showToast("无参考人员无法进行组别设置");
                } else {
                    GroupSettingsActivity.startInstant(this, listAll.size(), listAll, id, GW, clause.getSID());
                }
                break;
            default:
                break;
        }
    }

    public void getData(String teamno) {//获取每组人员
        if (!messageDialog.isVisible()) {
            messageDialog.show(getSupportFragmentManager(), "");
        }
        Map<String, String> map = new HashMap<>();
        map.put("aid", id);
        map.put("sid", clause.getSID());
        map.put("gw", GW);
        map.put("type", clause.getTYPE());
        map.put("teamno", teamno);
        map.put("personAll", "false");
        OkhttpUtil.okHttpPost(Api.GETPERSONACHIEVEMENT4ASSESS, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                if (messageDialog.isVisible()) {
                    messageDialog.dismiss();
                }
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    list.clear();
                    list.addAll(JSON.parseArray(JSON.parseObject(response).getString("data"), PersonAchievementBean.class));
                    commonAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void getAllData() {//获取每组人员
        Map<String, String> map = new HashMap<>();
        map.put("aid", id);
        map.put("sid", clause.getSID());
        map.put("gw", GW);
        map.put("type", clause.getTYPE());
        map.put("teamno", null);
        map.put("personAll", "true");
        OkhttpUtil.okHttpPost(Api.GETPERSONACHIEVEMENT4ASSESS, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    listAll.clear();
                    listAll.addAll(JSON.parseArray(JSON.parseObject(response).getString("data"), PersonAchievementBean.class));
                }
            }
        });
    }

    public void getGroup() {
        Map<String, String> map = new HashMap<>();
        map.put("aid", id);
        map.put("gw", GW);
        map.put("sid", clause.getSID());
        OkhttpUtil.okHttpPost(Api.GETPERSONTEAMNO, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    teamCount = JSON.parseObject(response).getInteger("data");
                    HashSet<Integer> defSet = new HashSet();
                    defSet.add(0);
                    List<String> list = new ArrayList<>();
                    for (int i = 1; i < teamCount + 1; i++) {
                        list.add(i + "");
                    }
                    spvGroup.setSpinner(list.toArray(), new SpinnerParentView.OnGetStrListener() {
                        @NotNull
                        @Override
                        public String getStr(Object bean) {
                            return bean.toString();
                        }
                    }, new SpinnerParentView.OnCheckListener() {
                        @Override
                        public void onConfirmAndChangeListener(@NotNull SpinnerParentView view, @NotNull List selectBeanList) {
                            currentGroup = selectBeanList.get(0).toString();
                            getData(currentGroup);
                        }
                    }, true, defSet);
                    getData(spvGroup.getSelectList().get(0).toString());
                }
            }
        });
    }

    public void submission(String userid, String achievement) {
        Map<String, String> map = new HashMap<>();
        map.put("aid", id);
        map.put("sid", clause.getSID());
        map.put("userid", userid);
        map.put("achievement", achievement);
        map.put("type", clause.getATYPE());
        OkhttpUtil.okHttpPost(Api.SETPERSONACHIEVEMENT4ASSESS, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    list.clear();
                    getData(currentGroup);
                }
            }
        });
    }


    private void initNoLinkOptionsPicker(EditText editText) {// 不联动的多级选项
        getNoLinkData();
        pvNoLinkOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {

                String str = "food:" + food.get(options1)
                        + "\nclothes:" + clothes.get(options2)
                        + "\ncomputer:" + computer.get(options3);
                editText.setText(str);
            }
        })
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;
                    }
                })
                .setItemVisibleCount(5)
                //.setSelectOptions(0, 1, 1)
                .build();
        pvNoLinkOptions.setNPicker(food, clothes, computer);
        pvNoLinkOptions.setSelectOptions(0, 1, 1);
        pvNoLinkOptions.show();
    }

    private void getNoLinkData() {
        food.add("KFC");
        food.add("MacDonald");
        food.add("Pizza hut");

        clothes.add("Nike");
        clothes.add("Adidas");
        clothes.add("Armani");

        computer.add("ASUS");
        computer.add("Lenovo");
        computer.add("Apple");
        computer.add("HP");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        switch (messageEvent.getMessage()) {
            case "分组刷新":
                getGroup();
                break;
            default:
                break;
        }
    }
}
