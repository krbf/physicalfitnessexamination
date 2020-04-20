package com.example.physicalfitnessexamination.page.kbi

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.api.RequestManager
import com.example.physicalfitnessexamination.api.response.SaveAssessmentRes
import com.example.physicalfitnessexamination.base.ActivityCollector
import com.example.physicalfitnessexamination.base.MyBaseActivity
import com.example.physicalfitnessexamination.util.JacksonMapper
import com.example.physicalfitnessexamination.util.snack
import com.example.physicalfitnessexamination.util.spannableBold
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_kbi_public_notice.*
import kotlinx.android.synthetic.main.v_toolbar.*

/**
 * 新建考核-公告发布
 */
class KbiPublicNoticeActivity : MyBaseActivity(), View.OnClickListener {
    companion object {
        /**
         * 跳转方法
         * @param context 上下文
         */
        @JvmStatic
        fun startInstant(context: Context) {
            Intent(context, KbiPublicNoticeActivity::class.java).let {
                context.startActivity(it)
            }
        }
    }

    /**
     * 人员选取方式是否是 普考
     */
    private val isCommonTest by lazy {
        CreateKbiDataManager.kbiBean?.personType == resources.getStringArray(R.array.perSelect)[0]
    }

    override fun initLayout(): Int = R.layout.activity_kbi_public_notice

    override fun initView() {
        tv_title.text = "新建考核-公告发布"
        iv_right.setOnClickListener(this)
        tv_createFinish.setOnClickListener(this)

        if (isCommonTest) {
            //普考
            v_dividerLine2.visibility = View.GONE
            tv_PerRequest.visibility = View.GONE
            edt_PerRequest.visibility = View.GONE
        } else {
            v_dividerLine2.visibility = View.VISIBLE
            tv_PerRequest.visibility = View.VISIBLE
            edt_PerRequest.visibility = View.VISIBLE
        }
    }

    override fun initData() {
        val spanStr = "人员选取方式：" + CreateKbiDataManager.kbiBean?.personType
        tv_personPickType.text = spannableBold(spanStr, 0, spanStr.length)
    }

    override fun onClick(v: View?) {
        when (v) {
            iv_right -> finish()
            tv_createFinish -> {
                if (checkParameter()) {
                    requestCreateKbi()
                }
            }
        }
    }

    private fun requestCreateKbi() {
        try {
            RequestManager.saveAssessment(context, CreateKbiDataManager.getFinalReq(context),
                    object : StringCallback() {
                        override fun onSuccess(response: Response<String>?) {
                            try {
                                response?.body()?.let {
                                    val res = JacksonMapper.mInstance.readValue(it, SaveAssessmentRes::class.java)

                                    if (res.success) {
                                        Toast.makeText(context, "创建成功", Toast.LENGTH_SHORT).show()

                                        BuiltKBIDetailActivity.startInstant(this@KbiPublicNoticeActivity, res.id);

                                        //关闭之前创建流程中的页面
                                        ActivityCollector.activitys.forEach {
                                            if (it is CreateKBIActivity || it is KbiOrgActivity
                                                    || it is KbiTimeConfigActivity || it is KbiPublicNoticeActivity) {
                                                if (!it.isFinishing) {
                                                    it.finish()
                                                }
                                            }
                                        }
                                    } else {
                                        Toast.makeText(context, "创建失败", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } catch (e: Exception) {
                                Logger.e(e, "StringCallback()")
                            }
                        }
                    })
        } catch (e: Exception) {
            Logger.e(e, "requestCreateKbi()")
        }
    }

    private fun checkParameter(): Boolean {
        edt_publicNotice.text.trim().toString().let { str ->
            if (str.isEmpty()) {
                tv_createFinish.snack("请输入 公告 内容")
                return false
            } else {
                CreateKbiDataManager.kbiBean?.remark = str
            }
        }

        if (!isCommonTest) {
            edt_PerRequest.text.trim().toString().let { str ->
                if (str.isEmpty()) {
                    tv_createFinish.snack("请输入 人员要求 内容")
                    return false
                } else {
                    CreateKbiDataManager.kbiBean?.requirementPerson = str
                }
            }
        }

        return true
    }
}
