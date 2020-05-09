package com.example.physicalfitnessexamination.page

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AlertDialog
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.activity.LoginActivity
import com.example.physicalfitnessexamination.activity.UserManager
import com.example.physicalfitnessexamination.api.RequestManager
import com.example.physicalfitnessexamination.api.callback.JsonCallback
import com.example.physicalfitnessexamination.api.request.GetRemarkForAssessmentReq
import com.example.physicalfitnessexamination.api.response.ApiResponse
import com.example.physicalfitnessexamination.api.response.RemarkForAssessmentRes
import com.example.physicalfitnessexamination.base.MyBaseActivity
import com.example.physicalfitnessexamination.page.historyKbi.HistoryKbiActivity
import com.example.physicalfitnessexamination.page.kbi.KbiShuntActivity
import com.example.physicalfitnessexamination.page.myKbi.MyKbiActivity
import com.example.physicalfitnessexamination.page.rank.RankActivity
import com.example.physicalfitnessexamination.page.statistics.TrainingAnalysisActivity
import com.example.physicalfitnessexamination.page.trainFiles.TrainFilesActivity
import com.example.physicalfitnessexamination.util.dp2px
import com.example.physicalfitnessexamination.util.setPaddingBottom
import com.lzy.okgo.model.Response
import kotlinx.android.synthetic.main.activity_main.*


/**
 * 首页
 */
class MainActivity : MyBaseActivity(), View.OnClickListener {

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
}
