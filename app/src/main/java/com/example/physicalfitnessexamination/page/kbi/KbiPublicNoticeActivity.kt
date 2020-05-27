package com.example.physicalfitnessexamination.page.kbi

import android.content.Context
import android.content.Intent
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.Toast
import com.czy.module_common.utils.JacksonMapper
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.api.RequestManager
import com.example.physicalfitnessexamination.api.response.SaveAssessmentRes
import com.example.physicalfitnessexamination.base.ActivityCollector
import com.example.physicalfitnessexamination.base.MyBaseActivity
import com.example.physicalfitnessexamination.util.snack
import com.example.physicalfitnessexamination.util.spannableBold
import com.example.physicalfitnessexamination.view.dialog.MessageDialog
import com.example.physicalfitnessexamination.view.excel.KbiProjectPersonCountView
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import com.lzy.okgo.request.base.Request
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_kbi_public_notice.*
import kotlinx.android.synthetic.main.v_toolbar.*
import java.text.SimpleDateFormat
import java.util.*

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

        val sdf = SimpleDateFormat("MM月dd日", Locale.CHINESE)
    }

    /**
     * 人员选取方式是否是 普考
     */
    private val isCommonTest by lazy {
        CreateKbiDataManager.kbiBean?.personType == resources.getStringArray(R.array.perSelect)[0]
    }

    /**
     * 等待Dialog
     */
    private val loadingDialog: MessageDialog by lazy {
        MessageDialog.newInstance("创建考核中，请稍等……")
    }

    override fun initLayout(): Int = R.layout.activity_kbi_public_notice

    override fun initView() {
        tv_title.text = "新建考核-公告发布"
        iv_right.setOnClickListener(this)
        tv_createFinish.setOnClickListener(this)

        run {
            var timeStr = ""
            //公告预设文案
            CreateKbiDataManager.kbiBean?.timeArrangementA?.let {
                if (it.isNotEmpty()) {
                    timeStr = sdf.format(it.first().date.time)
                }
            }
            CreateKbiDataManager.kbiBean?.timeArrangementB?.let {
                if (it.isNotEmpty()) {
                    timeStr = sdf.format(it.first().date)
                }
            }
            edt_publicNotice.setText("${CreateKbiDataManager.kbiBean?.name}将于${timeStr}进行，请各单位核对本单位名单，不能参考人员，请在已建考核中备注区注明原因，拍取相关照片资料备案")
        }

        if (isCommonTest) {
            //普考
            v_dividerLine2.visibility = View.GONE
            tv_PerRequest.visibility = View.GONE
            cl_personRequest.visibility = View.GONE
            group1.visibility = View.GONE
            group2.visibility = View.GONE
            group3.visibility = View.GONE
        } else if (CreateKbiDataManager.kbiBean?.type == resources.getStringArray(R.array.evaMode)[0]) {
            //考核方式为 "业务竞赛" 的场合
            v_dividerLine2.visibility = View.VISIBLE
            tv_PerRequest.visibility = View.VISIBLE
            cl_personRequest.visibility = View.GONE

            //"业务竞赛"-"参考单位"
            val orgType = CreateKbiDataManager.kbiBean?.orgType?.first()
            val nameArray = when (orgType) {
                resources.getStringArray(R.array.joinEvaOrg)[0] -> {
                    resources.getStringArray(R.array.managePosition)
                }
                resources.getStringArray(R.array.joinEvaOrg)[1] -> {
                    resources.getStringArray(R.array.manageBrigadePosition)
                }
                else -> {
                    resources.getStringArray(R.array.commPosition)
                }
            }

            CreateKbiDataManager.kbiBean?.objPart1?.forEach { prj ->
                ll_content.addView(
                        KbiProjectPersonCountView(context).apply {
                            binding.bean?.proName?.set(prj.NAME)
                            binding.bean?.sid = prj.ID
                            binding.bean?.type1Name?.set(nameArray[0])
                            binding.bean?.type2Name?.set(nameArray[1])
                        }
                )
            }

            CreateKbiDataManager.kbiBean?.objPart2?.forEach { prj ->
                ll_content.addView(
                        KbiProjectPersonCountView(context).apply {
                            binding.bean?.proName?.set(prj.NAME)
                            binding.bean?.sid = prj.ID
                            binding.bean?.type1Name?.set(nameArray[0])
                            binding.bean?.type2Name?.set(nameArray[1])
                        }
                )
            }

            CreateKbiDataManager.kbiBean?.objPart3?.forEach { prj ->
                ll_content.addView(
                        KbiProjectPersonCountView(context).apply {
                            binding.bean?.proName?.set(prj.NAME)
                            binding.bean?.sid = prj.ID
                            binding.bean?.type1Name?.set(nameArray[0])
                        }
                )
            }

            CreateKbiDataManager.kbiBean?.objPart4?.forEach { prj ->
                ll_content.addView(
                        KbiProjectPersonCountView(context).apply {
                            binding.bean?.proName?.set(prj.NAME)
                            binding.bean?.sid = prj.ID
                            binding.bean?.type1Name?.set(nameArray[1])
                        }
                )
            }
        } else {
            v_dividerLine2.visibility = View.VISIBLE
            tv_PerRequest.visibility = View.VISIBLE
            cl_personRequest.visibility = View.VISIBLE

            group1.visibility = View.GONE
            group2.visibility = View.GONE
            group3.visibility = View.GONE
            CreateKbiDataManager.kbiBean?.orgType?.let {
                group1.visibility = if (it.contains(resources.getStringArray(R.array.joinEvaOrg)[0])) View.VISIBLE else View.GONE
                group2.visibility = if (it.contains(resources.getStringArray(R.array.joinEvaOrg)[1])) View.VISIBLE else View.GONE
                group3.visibility = if (it.contains(resources.getStringArray(R.array.joinEvaOrg)[2])) View.VISIBLE else View.GONE
            }
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
                        override fun onStart(request: Request<String, out Request<Any, Request<*, *>>>?) {
                            super.onStart(request)
                            if (!loadingDialog.isVisible) {
                                loadingDialog.show(supportFragmentManager, "")
                            }
                        }

                        override fun onSuccess(response: Response<String>?) {
                            try {
                                response?.body()?.let {
                                    val res = JacksonMapper.mInstance.readValue(it, SaveAssessmentRes::class.java)

                                    if (res.success) {
                                        Toast.makeText(context, "创建成功", Toast.LENGTH_SHORT).show()

                                        BuiltKBIDetailActivity.startInstant(this@KbiPublicNoticeActivity, res.id);

                                        //关闭之前创建流程中的页面
                                        ActivityCollector.activitys.forEach { ac ->
                                            if (ac is CreateKBIActivity || ac is KbiOrgActivity
                                                    || ac is KbiTimeConfigActivity || ac is KbiPublicNoticeActivity) {
                                                if (!ac.isFinishing) {
                                                    ac.finish()
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

                        override fun onFinish() {
                            super.onFinish()
                            if (loadingDialog.isVisible) {
                                loadingDialog.dismiss()
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

        CreateKbiDataManager.kbiBean?.requirementPerson = null
        if (CreateKbiDataManager.kbiBean?.type == resources.getStringArray(R.array.evaMode)[0]) {
            //业务竞赛
            val list = mutableListOf<CreateKbiDataManager.PointsDiff>()
            for (childIndex in 0 until ll_content.childCount) {
                ll_content.getChildAt(childIndex).let { childV ->
                    if (childV is KbiProjectPersonCountView) {
                        childV.binding.bean?.let { bean ->
                            list.add(CreateKbiDataManager.PointsDiff(bean.sid,
                                    bean.type1Name.get() ?: "",
                                    bean.type1PerCount.get() ?: "0"))

                            if ((bean.type2Name.get() ?: "").isNotEmpty()) {
                                list.add(CreateKbiDataManager.PointsDiff(bean.sid,
                                        bean.type2Name.get() ?: "",
                                        bean.type2PerCount.get() ?: "0"))
                            }
                        }
                    }
                }
            }
            CreateKbiDataManager.kbiBean?.requirementPerson = JacksonMapper.mInstance.writeValueAsString(list)
        } else if (!isCommonTest) {
            val spb = SpannableStringBuilder()
            CreateKbiDataManager.kbiBean?.orgType?.let {
                if (it.contains(resources.getStringArray(R.array.joinEvaOrg)[0])) {
                    edt_tv_personReq1.text.trim().let { numStr ->
                        if (numStr.isEmpty()) {
                            tv_createFinish.snack("请输入 人员要求 内容")
                            return false
                        } else {
                            spb.append("支队领导:${numStr}人;")
                        }
                    }
                    edt_tv_personReq2.text.trim().let { numStr ->
                        if (numStr.isEmpty()) {
                            tv_createFinish.snack("请输入 人员要求 内容")
                            return false
                        } else {
                            spb.append("支队指挥员:${numStr}人;")
                        }
                    }
                }

                if (it.contains(resources.getStringArray(R.array.joinEvaOrg)[1])) {
                    edt_tv_personReq3.text.trim().let { numStr ->
                        if (numStr.isEmpty()) {
                            tv_createFinish.snack("请输入 人员要求 内容")
                            return false
                        } else {
                            spb.append("大队领导:${numStr}人;")
                        }
                    }
                    edt_tv_personReq4.text.trim().let { numStr ->
                        if (numStr.isEmpty()) {
                            tv_createFinish.snack("请输入 人员要求 内容")
                            return false
                        } else {
                            spb.append("大队指挥员:${numStr}人;")
                        }
                    }
                }

                if (it.contains(resources.getStringArray(R.array.joinEvaOrg)[2])) {
                    edt_tv_personReq5.text.trim().let { numStr ->
                        if (numStr.isEmpty()) {
                            tv_createFinish.snack("请输入 人员要求 内容")
                            return false
                        } else {
                            spb.append("消防站指挥员:${numStr}人;")
                        }
                    }
                    edt_tv_personReq6.text.trim().let { numStr ->
                        if (numStr.isEmpty()) {
                            tv_createFinish.snack("请输入 人员要求 内容")
                            return false
                        } else {
                            spb.append("消防站消防员:${numStr}人;")
                        }
                    }
                }
            }
            CreateKbiDataManager.kbiBean?.requirementPerson = spb.toString()
        }
        return true
    }
}
