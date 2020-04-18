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

import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.base.MyBaseActivity;

/**
 * 已建考核详情页
 */
public class BuiltKBIDetailActivity extends MyBaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageView imgRight;
    private WebView wvKbiDetail;
    private TextView tvModify;//修改
    private TextView tvAppend;//考核附件
    private TextView tvPlan;//考核方案
    private TextView tvOrganization;//考核组织
    private TextView tvTime;//时间安排
    private TextView tvRoster;//考核花名册
    private TextView tvAchievement;//考核成绩表
    private TextView tvName;//页面第一行名称显示
    private TextView tvWork;//考核实施
    private String id;//考核id

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
        Intent intent = new Intent(context, BuiltKBIDetailActivity.class);
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
        tvWork=findViewById(R.id.tv_work);
        tvWork.setOnClickListener(this::onClick);
    }

    @Override
    protected void initData() {
        tvTitle.setText("已建考核");
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
        wvKbiDetail.loadUrl("http://pic.sc.chinaz.com/files/pic/pic9/201910/zzpic20432.jpg");
        //设置在当前WebView继续加载网页
        wvKbiDetail.setWebViewClient(new MyWebViewClient());
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
                showToast("AAA");
                break;
            case R.id.tv_append:
                KBIAppendixActivity.startInstant(this, id);
                break;
            case R.id.tv_plan:
                tvName.setText("考核方案");
                tvWork.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_organization:
                tvName.setText("考核组织");
                tvWork.setVisibility(View.GONE);
                break;
            case R.id.tv_time:
                tvName.setText("时间安排");
                tvWork.setVisibility(View.GONE);
                break;
            case R.id.tv_roster:
                KBIRosterActivity.startInstant(this, id);
                break;
            case R.id.tv_achievement:
                KBIAchievementActivity.startInstant(this);
                break;
            case R.id.tv_work:
                showToast("考核实施");
                break;
            default:
                break;
        }
    }
}
