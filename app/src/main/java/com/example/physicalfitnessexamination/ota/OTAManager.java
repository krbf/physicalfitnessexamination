package com.example.physicalfitnessexamination.ota;

import android.os.Environment;

import com.example.physicalfitnessexamination.base.MyApplication;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;

import java.io.File;

/**
 * OTA升级管理类
 * Created by chenzhiyuan On 2020/5/10
 */
public class OTAManager {
    /**
     * OTA下载任务唯一标识
     */
    public final static String OTA_TAG = "ota_download_tag";
    private final static String TAG = "OTAManager";

    private static final OTAManager ourInstance = new OTAManager();

    private OTAManager() {
    }

    public static OTAManager getInstance() {
        return ourInstance;
    }

    /**
     * 下载安装包
     *
     * @param fileUrl 文件下载地址
     */
    void downloadApk(String fileUrl) {
        DownloadTask downloadTask = OkDownload.request(OTA_TAG, new GetRequest(fileUrl));
        downloadTask.folder(MyApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath());
        downloadTask.register(new DownloadListener(OTA_TAG) {
            @Override
            public void onStart(Progress progress) {
                // TODO: 2020/5/10 下载任务开始
            }

            @Override
            public void onProgress(Progress progress) {
                // TODO: 2020/5/10 下载进度回调 （0-1）
            }

            @Override
            public void onError(Progress progress) {
                // TODO: 2020/5/10 下载错误
            }

            @Override
            public void onFinish(File file, Progress progress) {
                // TODO: 2020/5/10 下载任务结束
            }

            @Override
            public void onRemove(Progress progress) {
                // TODO: 2020/5/10 下载任务被remove
            }
        }).save();

        downloadTask.start();
    }
}
