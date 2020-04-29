package com.example.physicalfitnessexamination.page.trainFiles;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.base.MyBaseActivity;
import com.hanlyjiang.library.fileviewer.tbs.TBSFileViewActivity;
import com.hanlyjiang.library.utils.FileViewerUtils;
import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;

/**
 * 文件查看页面
 */
public class FileLookActivity extends MyBaseActivity implements View.OnClickListener, TbsReaderView.ReaderCallback {
    private TextView tvTitle;
    private ImageView imgRight;
    public static final String FILE_PATH = "filePath";

    private static final String TAG = "TBSFileViewActivity";

    private TbsReaderView mTbsReaderView;
    private FrameLayout rootViewParent;

    private ViewGroup errorHandleLayout;
    private String filePath;

    /**
     * 跳转方法
     *
     * @param context 上下文
     */
    public static void viewFile(Context context, String localPath) {
        Intent intent = new Intent(context, TBSFileViewActivity.class);
        intent.putExtra(FILE_PATH, localPath);
        context.startActivity(intent);
    }

    public static String getFileName(String filePath) {
        if (filePath == null) {
            return "";
        }
        int lastSlashIndex = filePath.lastIndexOf("/") + 1;
        if (lastSlashIndex == -1) {
            return filePath;
        }
        int lastDotFromSlashIndex = filePath.indexOf(".", lastSlashIndex);
        if (lastDotFromSlashIndex == -1) {
            return filePath.substring(lastSlashIndex);
        }
        return filePath.substring(lastSlashIndex, lastDotFromSlashIndex);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_file_look;
    }

    @Override
    protected void initView() {
        tvTitle = findViewById(R.id.tv_title);
        imgRight = findViewById(R.id.iv_right);
        imgRight.setOnClickListener(this::onClick);
        rootViewParent = (FrameLayout) findViewById(com.artifex.mupdf.R.id.fl_rootview);
        errorHandleLayout = (ViewGroup) findViewById(com.artifex.mupdf.R.id.ll_error_handle);
    }

    @Override
    protected void initData() {
        tvTitle.setText("文件查看");
        initErrorHandleLayout(errorHandleLayout);

        filePath = handleIntent();
        if (TextUtils.isEmpty(filePath) || !new File(filePath).isFile()) {
            Toast.makeText(this, getString(com.artifex.mupdf.R.string.file_not_exist), Toast.LENGTH_SHORT).show();
            finish();
        }

        getSupportActionBar().setTitle(getString(com.artifex.mupdf.R.string.view_file) + getFileName(filePath));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTbsReaderView = new TbsReaderView(this, this);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        mTbsReaderView.setLayoutParams(layoutParams);
        rootViewParent.addView(mTbsReaderView);
        displayFile(filePath);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_right:
                finish();
                break;
        }
    }


    @Override
    public void onCallBackAction(Integer integer, Object long1, Object long2) {
        Log.d(TAG, "onCallBackAction " + integer + "," + long1 + "," + long2);
    }

    private void initErrorHandleLayout(ViewGroup errorHandleLayout) {
        findViewById(com.artifex.mupdf.R.id.btn_retry_with_tbs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFile(filePath);
            }
        });
        findViewById(com.artifex.mupdf.R.id.btn_view_with_other_app).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileViewerUtils.viewFile4_4(v.getContext(), filePath);
            }
        });
    }

    private String handleIntent() {
        if (getIntent() != null) {
            return getIntent().getStringExtra(FILE_PATH);
        }
        return null;
    }

    private void displayFile(String fileAbsPath) {
        Bundle bundle = new Bundle();
        bundle.putString("filePath", fileAbsPath);
        bundle.putString("tempPath", Environment.getExternalStorageDirectory().getPath());
        // preOpen 需要文件后缀名 用以判断是否支持
        boolean result = mTbsReaderView.preOpen(parseFormat(fileAbsPath), true);
        if (result) {
            mTbsReaderView.openFile(bundle);
            mTbsReaderView.setVisibility(View.VISIBLE);
            errorHandleLayout.setVisibility(View.GONE);
        } else {
            mTbsReaderView.setVisibility(View.GONE);
            errorHandleLayout.setVisibility(View.VISIBLE);
        }
    }

    private String parseFormat(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTbsReaderView.onStop();
    }
}
