package com.example.physicalfitnessexamination.page

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.app.DialogFragment
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.czy.module_common.okhttp.CallBackUtil.CallBackString
import com.czy.module_common.okhttp.OkhttpUtil
import com.czy.module_common.utils.Tool
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.activity.LoginActivity
import com.example.physicalfitnessexamination.activity.UserManager
import com.example.physicalfitnessexamination.api.RequestManager
import com.example.physicalfitnessexamination.api.callback.JsonCallback
import com.example.physicalfitnessexamination.api.request.GetRemarkForAssessmentReq
import com.example.physicalfitnessexamination.api.response.ApiResponse
import com.example.physicalfitnessexamination.api.response.RemarkForAssessmentRes
import com.example.physicalfitnessexamination.app.Api
import com.example.physicalfitnessexamination.base.MyBaseActivity
import com.example.physicalfitnessexamination.interfaces.ViewHelper
import com.example.physicalfitnessexamination.ota.OTAManager
import com.example.physicalfitnessexamination.page.historyKbi.HistoryKbiActivity
import com.example.physicalfitnessexamination.page.kbi.KbiShuntActivity
import com.example.physicalfitnessexamination.page.myKbi.MyKbiActivity
import com.example.physicalfitnessexamination.page.rank.RankActivity
import com.example.physicalfitnessexamination.page.statistics.TrainingAnalysisActivity
import com.example.physicalfitnessexamination.page.trainFiles.TrainFilesActivity
import com.example.physicalfitnessexamination.util.dp2px
import com.example.physicalfitnessexamination.util.setPaddingBottom
import com.example.physicalfitnessexamination.view.DMDialog
import com.lzy.okgo.model.Progress
import com.lzy.okgo.model.Response
import com.lzy.okserver.download.DownloadListener
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Call
import java.io.File
import java.util.*
import kotlin.math.roundToInt


/**
 * 首页
 */
class MainActivity : MyBaseActivity(), View.OnClickListener {
    private val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)

    companion object {
        /**
         * 跳转方法
         * @param context 上下文
         */
        @JvmStatic
        fun startInstant(context: Context) {
            Intent(context, MainActivity::class.java).let {
                context.startActivity(it)
            }
        }
    }

    override fun initLayout(): Int = R.layout.activity_main

    override fun initView() {
        btn_main1.setOnClickListener(this)
        btn_main2.setOnClickListener(this)
        btn_main3.setOnClickListener(this)
        btn_main4.setOnClickListener(this)
        btn_main5.setOnClickListener(this)
        btn_main6.setOnClickListener(this)
        iv_LoginOut.setOnClickListener(this)
        permission()
    }

    override fun initData() {
        RequestManager.getRemarkForAssessment(this,
                GetRemarkForAssessmentReq(UserManager.getInstance().getUserInfo(this).org_id),
                object : JsonCallback<ApiResponse<List<RemarkForAssessmentRes>>, List<RemarkForAssessmentRes>>() {
                    override fun onSuccess(response: Response<ApiResponse<List<RemarkForAssessmentRes>>>?) {
                        response?.body()?.data?.let { list ->
                            list.forEach { res ->
                                ll_notice.addView(
                                        TextView(context).apply {
                                            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
                                            val span = SpannableStringBuilder("缩进" + res.REMARK).apply {
                                                setSpan(ForegroundColorSpan(Color.TRANSPARENT), 0, 2,
                                                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                                            }
                                            text = span
                                            setPaddingBottom(15.dp2px)
                                        })
                                ll_notice.addView(
                                        TextView(context).apply {
                                            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
                                            text = "————" + res.CREATETIME
                                            setPaddingBottom(20.dp2px)
                                        },
                                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                                            gravity = Gravity.END
                                        }
                                )
                            }
                        }
                    }
                })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_main1 -> {
                //创建考核
                KbiShuntActivity.startInstant(this)
            }
            R.id.btn_main2 -> {
                //我的考核
                MyKbiActivity.startInstant(this)
            }
            R.id.btn_main3 -> {
                //统计分析
                TrainingAnalysisActivity.startInstant(this)
            }
            R.id.btn_main4 -> {
                //榜上有名
                RankActivity.startInstant(this)
            }
            R.id.btn_main5 -> {
                //训练文件
                TrainFilesActivity.startInstant(this)
            }
            R.id.btn_main6 -> {
                //历史考核
                HistoryKbiActivity.startInstant(this)
            }
            R.id.iv_LoginOut -> {
                AlertDialog.Builder(context)
                        .setMessage("确定退出当前登录账户吗？")
                        .setPositiveButton("确定") { _, _ ->
                            UserManager.getInstance().loginOut(context)
                            LoginActivity.startInstant(this)
                            finish()
                        }
                        .setNegativeButton("取消", null)
                        .create().show()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder(context)
                    .setMessage("确定退出当前应用吗？")
                    .setPositiveButton("确定") { dialog, which -> finish() }
                    .setNegativeButton("取消", null)
                    .create().show()
        }
        return true
    }

    private fun permission() {
        val permissionWrite = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissionWrite != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1000)
        } else {
            versionName()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        when (requestCode) {
            1000 -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                versionName()
            } else {
                //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true,它在用户选择"不再询问"的情况下返回false
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    DMDialog.builder(this, R.layout.dialog_hint)
                            .onDialogInitListener { helper: ViewHelper, dialog: DMDialog ->
                                helper.setText(R.id.tv_hint, "拒绝此权限将退出App！")
                                helper.setText(R.id.tv_cancel, "申请权限")
                                helper.setText(R.id.tv_ok, "退出App")
                                helper.setOnClickListener(R.id.tv_cancel) {
                                    ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1000)
                                    dialog.dismiss()
                                }
                                helper.setOnClickListener(R.id.tv_ok) {
                                    finish()
                                    dialog.dismiss()
                                }
                            }
                            .setGravity(Gravity.CENTER)
                            .setCancelable(false)
                            .show()
                } else {
                    startActivityForResult(Intent().apply {
                        action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                        data = Uri.fromParts("package", packageName, null)
                    }, 1)
                }

            }
            else -> {
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            val permissionWrite = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (permissionWrite != PackageManager.PERMISSION_GRANTED) {
                finish()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun versionName() {
        val map: MutableMap<String, String> = HashMap()
        OkhttpUtil.okHttpPost(Api.GETVERSIONNAME, map, object : CallBackString() {
            override fun onFailure(call: Call, e: Exception) {}
            override fun onResponse(response: String) {
                val success = JSON.parseObject(response).getString("success")
                if (success == "true") {
                    val versionName = JSON.parseObject(response).getString("versionName")
                    if (Tool.getAppVersionName(this@MainActivity) != versionName) {
                        val message = JSON.parseObject(response).getString("MSG")
                        DMDialog.builder(this@MainActivity, R.layout.dialog_version_upadte)
                                .onDialogInitListener { helper: ViewHelper, dialog: DMDialog ->
                                    helper.setText(R.id.tv_hint, message)
                                    helper.setText(R.id.tv_cancel, "退出App")
                                    helper.setText(R.id.tv_ok, "下载")
                                    val textView = helper.getView<TextView>(R.id.tv_progress)
                                    val progressBar = helper.getView<ProgressBar>(R.id.progress_bar)
                                    val linearLayout = helper.getView<LinearLayout>(R.id.lin_view)
                                    val linearLayout1 = helper.getView<LinearLayout>(R.id.lin_view1)
                                    helper.setOnClickListener(R.id.tv_cancel) {
                                        finish()
                                        dialog.dismiss()
                                    }
                                    helper.setOnClickListener(R.id.tv_ok) {
                                        linearLayout.visibility = View.GONE
                                        linearLayout1.visibility = View.VISIBLE
                                        downApk(progressBar, textView,dialog);
                                    }
                                }
                                .setGravity(Gravity.CENTER)
                                .setCancelable(false)
                                .show()
                    }
                }
            }
        })
    }

    private fun downApk(progressBar: ProgressBar, textView: TextView,dialogFragment: DialogFragment) {
        OTAManager.getInstance().downloadApk(Api.DOWNAPK, object : DownloadListener(OTAManager.OTA_TAG) {
            override fun onStart(progress: Progress) {
                // TODO: 2020/5/10 下载任务开始
            }

            override fun onProgress(progress: Progress) {
                // TODO: 2020/5/10 下载进度回调 （0-1）
                if (progress.currentSize < progress.totalSize) {
                    progressBar.progress = (progress.fraction * 100).roundToInt()
                    textView.text = (progress.fraction * 100).roundToInt().toString() + "%"
                } else {
                    textView.text = "100%"
                }
            }

            override fun onError(progress: Progress) {
                // TODO: 2020/5/10 下载错误
            }

            override fun onFinish(file: File, progress: Progress) {
                // TODO: 2020/5/10 下载任务结束
                installAPK(file)
                dialogFragment.dismiss()
            }

            override fun onRemove(progress: Progress) {
                // TODO: 2020/5/10 下载任务被remove
            }
        })
    }

    /**
     * @return
     * @Description 安装apk
     */
    private fun installAPK(savedFile: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 7.0+以上版本
            val apkUri = FileProvider.getUriForFile(this, "com.example.physicalfitnessexamination.fileprovider", savedFile)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
        } else {
            intent.setDataAndType(Uri.fromFile(savedFile), "application/vnd.android.package-archive")
        }
        startActivity(intent)
    }
}
