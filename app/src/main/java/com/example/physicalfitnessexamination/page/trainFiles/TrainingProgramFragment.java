package com.example.physicalfitnessexamination.page.trainFiles;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.physicalfitnessexamination.Constants;
import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.app.Api;
import com.example.physicalfitnessexamination.bean.FilesBean;
import com.example.physicalfitnessexamination.common.adapter.CommonAdapter;
import com.czy.module_common.okhttp.CallBackUtil;
import com.czy.module_common.okhttp.OkhttpUtil;
import com.example.physicalfitnessexamination.view.dialog.MessageDialog;
import com.example.physicalfitnessexamination.viewholder.ViewHolder;
import com.hanlyjiang.library.fileviewer.FileViewer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 训练计划
 */
public class TrainingProgramFragment extends Fragment implements View.OnClickListener {
    private ListView lvFile;
    private CommonAdapter<FilesBean> commonAdapter;
    private List<FilesBean> list = new ArrayList<>();
    private MessageDialog messageDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageDialog = MessageDialog.newInstance("文件加载中，请稍等……");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_training_program, null);
        lvFile = view.findViewById(R.id.lv_file);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        commonAdapter = new CommonAdapter<FilesBean>(getContext(), R.layout.item_training_program, list) {
            @Override
            public void convert(ViewHolder viewHolder, FilesBean filesBean) {
                viewHolder.setText(R.id.tv_name, filesBean.getNAME());
                viewHolder.setText(R.id.tv_data, filesBean.getCREATETIME());
                viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!messageDialog.isVisible()) {
                            messageDialog.show(getFragmentManager(), "");
                        }
                        String fileName = filesBean.getPATH().substring(filesBean.getPATH().lastIndexOf("/") + 1);
                        downLoad(Constants.IP + filesBean.getPATH(), fileName);
                    }
                });
            }
        };
        lvFile.setAdapter(commonAdapter);
        getData("1");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }


    public void downLoad(String url, String fileName) {
        OkhttpUtil.okHttpDownloadFile(url, new CallBackUtil.CallBackFile(Environment.getExternalStorageDirectory() + File.separator + "体能考核" + File.separator, fileName) {
            @Override
            public void onFailure(Call call, Exception e) {
                if (messageDialog.isVisible()) {
                    messageDialog.dismiss();
                }
            }

            @Override
            public void onResponse(File response) {
                if (messageDialog.isVisible()) {
                    messageDialog.dismiss();
                }
                FileViewer.viewFile(getContext(), Environment.getExternalStorageDirectory() + File.separator + "体能考核" + File.separator + fileName);
            }

            @Override
            public void onProgress(float progress, long total) {
                super.onProgress(progress, total);
            }
        });
    }


    public void getData(String type) {
        Map<String, String> map = new HashMap<>();
        map.put("type", type);//1-训练计划 2-考核通报 3-考核通知 4-规范文件
        OkhttpUtil.okHttpPost(Api.GETFILELIST, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    list.addAll(JSON.parseArray(JSON.parseObject(response).getString("data"), FilesBean.class));
                    commonAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
