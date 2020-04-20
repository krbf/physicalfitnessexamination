package com.example.physicalfitnessexamination.page.kbi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.physicalfitnessexamination.Constants;
import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.activity.UserManager;
import com.example.physicalfitnessexamination.app.Api;
import com.example.physicalfitnessexamination.base.MyBaseActivity;
import com.example.physicalfitnessexamination.bean.AssessmentInfoBean;
import com.example.physicalfitnessexamination.bean.UserInfo;
import com.example.physicalfitnessexamination.okhttp.CallBackUtil;
import com.example.physicalfitnessexamination.okhttp.OkhttpUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 考核实施详情页
 */
public class KBIPutIntoEffectDetailActivity extends MyBaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageView imgRight;
    private WebView wvKbiDetail;
    private TextView tvModify;//考核中
    private TextView tvAppend;//考核附件
    private TextView tvPlan;//考核方案
    private TextView tvOrganization;//考核组织
    private TextView tvTime;//时间安排
    private TextView tvRoster;//考核花名册
    private TextView tvAchievement;//考核成绩表
    private TextView tvName;//页面第一行名称显示
    private TextView tvWork;//考核结束
    private String id;//考核id
    private AssessmentInfoBean assessmentInfoBean;
    private UserInfo userInfo;
    private boolean KBIPower = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 跳转方法
     *
     * @param context 上下文
     */
    public static void startInstant(Context context, String id) {
        Intent intent = new Intent(context, KBIPutIntoEffectDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_built_kbi_detail;
    }

    @Override
    protected void initView() {
        id = getIntent().getStringExtra("id");
        userInfo = UserManager.getInstance().getUserInfo(this);
        tvTitle = findViewById(R.id.tv_title);
        imgRight = findViewById(R.id.iv_right);
        imgRight.setOnClickListener(this::onClick);
        tvName = findViewById(R.id.tv_name);
        wvKbiDetail = findViewById(R.id.wv_view);
        tvModify = findViewById(R.id.tv_modify);
        tvModify.setOnClickListener(this::onClick);
        tvAppend = findViewById(R.id.tv_append);
        tvAppend.setOnClickListener(this::onClick);
        tvPlan = findViewById(R.id.tv_plan);
        tvPlan.setOnClickListener(this::onClick);
        tvOrganization = findViewById(R.id.tv_organization);
        tvOrganization.setOnClickListener(this::onClick);
        tvTime = findViewById(R.id.tv_time);
        tvTime.setOnClickListener(this::onClick);
        tvRoster = findViewById(R.id.tv_roster);
        tvRoster.setOnClickListener(this::onClick);
        tvAchievement = findViewById(R.id.tv_achievement);
        tvAchievement.setOnClickListener(this::onClick);
        tvWork = findViewById(R.id.tv_work);
        tvWork.setOnClickListener(this::onClick);
    }

    @Override
    protected void initData() {
        getAssessmentInfo();//获取考核基本信息
        tvModify.setText("考核中");
        tvWork.setText("考核结束");

        tvTitle.setText("考核实施");
        tvName.setText("考核方案");

        //加载本地html文件
        // mWVmhtml.loadUrl("file:///android_asset/hello.html");

        //设置JavaScrip
        wvKbiDetail.getSettings().setJavaScriptEnabled(true);
        //支持通过JS打开新窗口(允许JS弹窗)
        wvKbiDetail.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        wvKbiDetail.getSettings().setUseWideViewPort(true); //将图片调整到适合webview的大小
        wvKbiDetail.getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        wvKbiDetail.getSettings().setMediaPlaybackRequiresUserGesture(true);
        wvKbiDetail.getSettings().setAllowFileAccess(true);
        wvKbiDetail.getSettings().setPluginState(WebSettings.PluginState.ON);
        wvKbiDetail.getSettings().setDomStorageEnabled(true);

        wvKbiDetail.setBackgroundColor(Color.WHITE);
        //加载网络URL
        wvKbiDetail.loadUrl(Constants.IP + "assessment/assessmentInfoH5?id=" + id);
        //设置在当前WebView继续加载网页
        wvKbiDetail.setWebViewClient(new KBIPutIntoEffectDetailActivity.MyWebViewClient());
    }

    class MyWebViewClient extends WebViewClient {
        @Override  //WebView代表是当前的WebView
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            //表示在当前的WebView继续打开网页
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d("WebView", "开始访问网页");
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d("WebView", "访问网页结束");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && wvKbiDetail.canGoBack()) {
            wvKbiDetail.goBack();//返回上个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);//退出H5界面
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_right:
                finish();
                break;
            case R.id.tv_modify:
                showToast("考核中");
                break;
            case R.id.tv_append:
                KBIAppendixActivity.startInstant(this, id);
                break;
            case R.id.tv_plan:
                tvName.setText("考核方案");
                if (KBIPower) {
                    tvWork.setVisibility(View.VISIBLE);
                }
                wvKbiDetail.loadUrl(Constants.IP + "assessment/assessmentInfoH5?id=" + id);
                break;
            case R.id.tv_organization:
                tvName.setText("考核组织");
                tvWork.setVisibility(View.GONE);
                wvKbiDetail.loadUrl(Constants.IP + "assessment/assessmentExaminerH5?id=" + id);
                break;
            case R.id.tv_time:
                tvName.setText("时间安排");
                tvWork.setVisibility(View.GONE);
                wvKbiDetail.loadUrl(Constants.IP + "assessment/assessmentTimeH5?id=" + id);
                break;
            case R.id.tv_roster:
                KBIRosterActivity.startInstant(this, id);
                break;
            case R.id.tv_achievement:
                KBIAchievementActivity.startInstant(this, id, "2");
                break;
            case R.id.tv_work:
                showToast("考核结束");
                break;
            default:
                break;
        }
    }

    public void getAssessmentInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        OkhttpUtil.okHttpPost(Api.GETASSESSMENTINFO, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    assessmentInfoBean = JSON.parseObject(JSON.parseObject(response).getString("data"), AssessmentInfoBean.class);
                    if ("0".equals(assessmentInfoBean.getTYPE())) {
                        tvOrganization.setVisibility(View.GONE);//日常考核无考核组织
                    }
                    if (assessmentInfoBean.getGROUP_LEADER() == null) {
                        KBIPower = true;
                        tvWork.setVisibility(View.VISIBLE);
                    } else {
                        if (userInfo.getOrg_id().equals(assessmentInfoBean.getORG_ID())) {
                            String GROUP_LEADER = assessmentInfoBean.getGROUP_LEADER();
                            List<String> listGL = Arrays.asList(GROUP_LEADER.split(","));
                            for (int i = 0; i < listGL.size(); i++) {
                                if (userInfo.getUsername().equals(listGL.get(i))) {
                                    KBIPower = true;
                                    tvWork.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public void putIntoEffect() {
        Map<String, String> map = new HashMap<>();
        map.put("ID", id);
        OkhttpUtil.okHttpPost(Api.STARTASSESSMENT, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                String msg = JSON.parseObject(response).getString("msg");
                if (success) {

                } else {
                    showToast(msg);
                }
            }
        });
    }
}
