package com.example.physicalfitnessexamination.page.trainFiles;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.okhttp.CallBackUtil;
import com.example.physicalfitnessexamination.okhttp.OkhttpUtil;
import com.hanlyjiang.library.fileviewer.FileViewer;

import java.io.File;

import okhttp3.Call;

/**
 * 训练计划
 */
public class TrainingProgramFragment extends Fragment implements View.OnClickListener {
    private TextView tv_unOpen;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_training_program,null);
        tv_unOpen=view.findViewById(R.id.tv_unOpen);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tv_unOpen.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_unOpen:
                permission();
                break;
        }
    }
    public void permission() {
        int permissionWrite = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionWrite != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE, 1000);
        } else {
            downLoad();
        }
    }

    public void downLoad() {
        OkhttpUtil.okHttpDownloadFile("http://218.75.249.59:31010/hnxf/fire/fire/yadoc/22cd255b-6bd7-48e7-9e1d-3fc06a4e1e9b.doc", new CallBackUtil.CallBackFile(Environment.getExternalStorageDirectory() + File.separator + "体能考核" + File.separator, "123.doc") {
            @Override
            public void onFailure(Call call, Exception e) {
                //showToast("下载失败");
            }

            @Override
            public void onResponse(File response) {
                FileViewer.viewFile(getContext(), Environment.getExternalStorageDirectory() + File.separator + "体能考核" + File.separator + "123.doc");
            }

            @Override
            public void onProgress(float progress, long total) {
                super.onProgress(progress, total);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1000:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    downLoad();
                }
                break;
            default:
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
