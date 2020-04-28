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
import android.view.MotionEvent
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
import com.lzy.okgo.model.Response
import kotlinx.android.synthetic.main.activity_main2.*


/**
 * 首页
 */
class Main2Activity : MyBaseActivity(), View.OnClickListener {

    companion object {
        /**
         * 跳转方法
         * @param context 上下文
         */
        @JvmStatic
        fun startInstant(context: Context) {
            Intent(context, Main2Activity::class.java).let {
                context.startActivity(it)
            }
        }
    }

    override fun initLayout(): Int = R.layout.activity_main2

    override fun initView() {
        btn_main1.setOnClickListener(this)
        btn_main2.setOnClickListener(this)
        btn_main3.setOnClickListener(this)
        btn_main4.setOnClickListener(this)
        btn_main5.setOnClickListener(this)
        btn_main6.setOnClickListener(this)
        iv_LoginOut.setOnClickListener(this)

        vFlip_notice.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_MOVE, MotionEvent.ACTION_DOWN -> {
                    if (vFlip_notice.isFlipping) {
                        vFlip_notice.stopFlipping()
                    }
                }
                else -> if (!vFlip_notice.isFlipping) {
                    vFlip_notice.startFlipping()
                    vFlip_notice.showNext()
                }
            }
            true
        }
    }

    override fun initData() {
        RequestManager.getRemarkForAssessment(this,
                GetRemarkForAssessmentReq(UserManager.getInstance().getUserInfo(this).org_id),
                object : JsonCallback<ApiResponse<List<RemarkForAssessmentRes>>, List<RemarkForAssessmentRes>>() {
                    override fun onSuccess(response: Response<ApiResponse<List<RemarkForAssessmentRes>>>?) {
                        response?.body()?.data?.let { list ->
                            val twoBeanList = mutableListOf<MutableList<RemarkForAssessmentRes>>()
                            list.forEachIndexed { index, res ->
                                when (index % 2) {
                                    0 -> {
                                        twoBeanList.add(
                                                mutableListOf<RemarkForAssessmentRes>().apply {
                                                    add(res)
                                                })
                                    }
                                    1 -> {
                                        twoBeanList[index / 2].add(res)
                                    }
                                }
                            }

                            twoBeanList.forEach { res ->
                                vFlip_notice.addView(
                                        LinearLayout(context).apply {
                                            orientation = LinearLayout.VERTICAL
                                            if (res.size > 0) {
                                                addView(
                                                        TextView(context).apply {
                                                            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
                                                            val span = SpannableStringBuilder("缩进" + res[0].REMARK).apply {
                                                                setSpan(ForegroundColorSpan(Color.TRANSPARENT), 0, 2,
                                                                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                                                            }
                                                            text = span
                                                        })
                                                addView(
                                                        TextView(context).apply {
                                                            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
                                                            text = "————" + res[0].CREATETIME
                                                        },
                                                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                                                            gravity = Gravity.END
                                                        }
                                                )
                                            }

                                            if (res.size > 1) {
                                                addView(
                                                        TextView(context).apply {
                                                            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
                                                            val span = SpannableStringBuilder("缩进" + res[1].REMARK).apply {
                                                                setSpan(ForegroundColorSpan(Color.TRANSPARENT), 0, 2,
                                                                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                                                            }
                                                            text = span
                                                        },
                                                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                                                            setMargins(0, 18.dp2px, 0, 0)
                                                        })
                                                addView(
                                                        TextView(context).apply {
                                                            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
                                                            text = "————" + res[1].CREATETIME
                                                        },
                                                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                                                            gravity = Gravity.END
                                                        }
                                                )
                                            }
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
                UserManager.getInstance().loginOut(context);
                LoginActivity.startInstant(this);
                finish();
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
