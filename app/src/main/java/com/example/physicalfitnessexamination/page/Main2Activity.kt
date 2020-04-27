package com.example.physicalfitnessexamination.page

import android.content.Context
import android.content.Intent
import android.util.TypedValue
import android.view.View
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
import com.example.physicalfitnessexamination.page.trainFiles.TrainFilesActivity
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
    }

    override fun initData() {
        RequestManager.getRemarkForAssessment(this,
                GetRemarkForAssessmentReq(UserManager.getInstance().getUserInfo(this).org_id),
                object : JsonCallback<ApiResponse<List<RemarkForAssessmentRes>>, List<RemarkForAssessmentRes>>() {
                    override fun onSuccess(response: Response<ApiResponse<List<RemarkForAssessmentRes>>>?) {
                        response?.body()?.data?.let { list ->
                            list.forEach { res ->
                                vFlip_notice.addView(
                                        TextView(context).apply {
                                            setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
                                            text = res.REMARK
                                        })
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
}
