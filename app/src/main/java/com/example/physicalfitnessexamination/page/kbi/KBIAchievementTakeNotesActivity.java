package com.example.physicalfitnessexamination.page.kbi;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.app.Api;
import com.example.physicalfitnessexamination.base.MyBaseActivity;
import com.example.physicalfitnessexamination.bean.ClauseBean;
import com.example.physicalfitnessexamination.bean.PersonAchievementBean;
import com.example.physicalfitnessexamination.common.adapter.CommonAdapter;
import com.example.physicalfitnessexamination.okhttp.CallBackUtil;
import com.example.physicalfitnessexamination.okhttp.OkhttpUtil;
import com.example.physicalfitnessexamination.util.SportKeyBoardUtil;
import com.example.physicalfitnessexamination.view.DMDialog;
import com.example.physicalfitnessexamination.view.SportKeyBoardView;
import com.example.physicalfitnessexamination.viewholder.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 考核成绩记录表
 */
public class KBIAchievementTakeNotesActivity extends MyBaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageView imgRight;
    private TextView tvClause;
    private String id;//考核id
    private ClauseBean.Clause clause;//项目实体类
    private String GW;//岗位
    private ListView lvAchievementTakeNotes;
    private CommonAdapter<PersonAchievementBean> commonAdapter;
    private List<PersonAchievementBean> list = new ArrayList<>();
    private OptionsPickerView pvNoLinkOptions;//条件选择器
    private ArrayList<String> food = new ArrayList<>();
    private ArrayList<String> clothes = new ArrayList<>();
    private ArrayList<String> computer = new ArrayList<>();

    /**
     * 跳转方法
     *
     * @param context 上下文
     */
    public static void startInstant(Context context, String id, ClauseBean.Clause clause, String GW) {
        Intent intent = new Intent(context, KBIAchievementTakeNotesActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("clause", (Parcelable) clause);
        intent.putExtra("GW", GW);
        context.startActivity(intent);
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
        tvTitle = findViewById(R.id.tv_title);
        imgRight = findViewById(R.id.iv_right);
        imgRight.setOnClickListener(this::onClick);
        lvAchievementTakeNotes = findViewById(R.id.lv_achievement_take_notes);
        tvClause = findViewById(R.id.tv_clause);
    }

    @Override
    protected void initData() {
        tvTitle.setText("成绩记录表");
        tvClause.setText(clause.getNAME());
        getData();
        commonAdapter = new CommonAdapter<PersonAchievementBean>(this, R.layout.item_kbi_achievement_takes_notes, list) {
            @Override
            public void convert(ViewHolder viewHolder, PersonAchievementBean s) {
                viewHolder.setText(R.id.tv_order, viewHolder.getPosition() + 1 + "");
                viewHolder.setText(R.id.tv_number, s.getNO());
                viewHolder.setText(R.id.tv_name, s.getUSERNAME());
                viewHolder.setText(R.id.tv_unit, s.getORG_NAME());
                viewHolder.setText(R.id.tv_post, s.getGW());
                viewHolder.setText(R.id.tv_achievement, s.getACHIEVEMENT());
                viewHolder.getView(R.id.tv_achievement).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DMDialog.builder(KBIAchievementTakeNotesActivity.this, R.layout.dialog_achievement_input)
                                .onDialogInitListener((helper, dialog) ->
                                {
                                    helper.setText(R.id.tv_name, s.getUSERNAME());
                                    helper.setText(R.id.tv_unit, s.getORG_NAME());
                                    if ("1".equals(clause.getATYPE())){
                                        helper.setText(R.id.tv_measurement, clause.getDW());
                                    }
                                    EditText edtAchievement = helper.getView(R.id.edt_achievement);

                                    final SportKeyBoardUtil[] sportKeyBoardUtil = new SportKeyBoardUtil[1];

                                    SportKeyBoardView keyboardView = helper.getView(R.id.ky_keyboard);
                                    LinearLayout ky_keyboard_parent = helper.getView(R.id.ky_keyboard_parent);
                                    sportKeyBoardUtil[0] = new SportKeyBoardUtil(ky_keyboard_parent, keyboardView, edtAchievement);
                                    edtAchievement.setOnTouchListener(new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {
                                            if (sportKeyBoardUtil[0] == null) {
                                                sportKeyBoardUtil[0] = new SportKeyBoardUtil(ky_keyboard_parent, keyboardView, edtAchievement);
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
                                                    String[] re = result.split("’|”");
                                                    int achievement = 0;
                                                    if (re.length==3){
                                                        achievement=Integer.parseInt(re[0])*60*1000+Integer.parseInt(re[1])*1000+Integer.parseInt(re[2])*10;
                                                    }else if (re.length==2){
                                                        if (result.contains("”")){
                                                            achievement=Integer.parseInt(re[0])*1000+Integer.parseInt(re[1])*10;
                                                        }else {
                                                            achievement=Integer.parseInt(re[0])*60*1000+Integer.parseInt(re[1])*1000;
                                                        }
                                                    }
                                                    submission(s.getUSERID(), String.valueOf(achievement));
                                                    break;
                                                case "1"://次数
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
            default:
                break;
        }
    }

    public void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("aid", id);
        map.put("sid", clause.getSID());
        map.put("gw", GW);
        map.put("type", clause.getTYPE());
        OkhttpUtil.okHttpPost(Api.GETPERSONACHIEVEMENT4ASSESS, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    list.addAll(JSON.parseArray(JSON.parseObject(response).getString("data"), PersonAchievementBean.class));
                    commonAdapter.notifyDataSetChanged();
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
        map.put("type", clause.getTYPE());
        OkhttpUtil.okHttpPost(Api.SETPERSONACHIEVEMENT4ASSESS, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    list.clear();
                    getData();
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
}
