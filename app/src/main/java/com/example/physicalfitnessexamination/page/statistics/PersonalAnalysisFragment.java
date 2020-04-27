package com.example.physicalfitnessexamination.page.statistics;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.physicalfitnessexamination.Constants;
import com.example.physicalfitnessexamination.R;

public class PersonalAnalysisFragment extends Fragment {
    private WebView wvKbiGather;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_analysis, null);
        wvKbiGather = view.findViewById(R.id.wv_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //加载本地html文件
        // mWVmhtml.loadUrl("file:///android_asset/hello.html");

        //设置JavaScrip
        wvKbiGather.getSettings().setJavaScriptEnabled(true);
        //支持通过JS打开新窗口(允许JS弹窗)
        wvKbiGather.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        wvKbiGather.getSettings().setUseWideViewPort(true); //将图片调整到适合webview的大小
        wvKbiGather.getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        wvKbiGather.getSettings().setMediaPlaybackRequiresUserGesture(true);
        wvKbiGather.getSettings().setAllowFileAccess(true);
        wvKbiGather.getSettings().setPluginState(WebSettings.PluginState.ON);
        wvKbiGather.getSettings().setDomStorageEnabled(true);

        wvKbiGather.setBackgroundColor(Color.WHITE);
        //加载网络URL
        wvKbiGather.loadUrl(Constants.IP + "assessment/assessmentGrfxH5");
        //设置在当前WebView继续加载网页
        wvKbiGather.setWebViewClient(new MyWebViewClient());
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
}
