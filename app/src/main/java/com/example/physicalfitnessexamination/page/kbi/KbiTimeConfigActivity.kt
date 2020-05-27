package com.example.physicalfitnessexamination.page.kbi

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.czy.module_common.utils.JacksonMapper
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.activity.UserManager
import com.example.physicalfitnessexamination.api.RequestManager
import com.example.physicalfitnessexamination.api.callback.JsonCallback
import com.example.physicalfitnessexamination.api.request.GetOrgForAssessmentReq
import com.example.physicalfitnessexamination.api.response.ApiResponse
import com.example.physicalfitnessexamination.api.response.OrgRes
import com.example.physicalfitnessexamination.api.response.SaveAssessmentRes
import com.example.physicalfitnessexamination.base.ActivityCollector
import com.example.physicalfitnessexamination.base.MyBaseActivity
import com.example.physicalfitnessexamination.bean.KbiTimeCfgBean
import com.example.physicalfitnessexamination.bean.KbiTimeCfgType1Bean
import com.example.physicalfitnessexamination.bean.KbiTimeCfgType2Bean
import com.example.physicalfitnessexamination.page.kbi.adapter.KbiTimeConfigAdapter
import com.example.physicalfitnessexamination.util.snack
import com.example.physicalfitnessexamination.view.dialog.MessageDialog
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import com.lzy.okgo.request.base.Request
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_kbi_time_config.*
import kotlinx.android.synthetic.main.v_toolbar.*

/**
 * 新建考核-时间安排页面
 */
class KbiTimeConfigActivity : MyBaseActivity(), View.OnClickListener {
    companion object {
        /**
         * 跳转方法
         * @param context 上下文
         */
        @JvmStatic
        fun startInstant(context: Context) {
            Intent(context, KbiTimeConfigActivity::class.java).let {
                context.startActivity(it)
            }
        }
    }

    /**
     * 是否是到站考核
     */
    private val isToPlaceEva by lazy {
        CreateKbiDataManager.kbiBean?.type == resources.getStringArray(R.array.evaMode)[2]
    }

    //时间安排
    private val timeTypeList = ArrayList<KbiTimeCfgBean>()

    private val adapter: KbiTimeConfigAdapter by lazy {
        KbiTimeConfigAdapter(isToPlaceEva)
    }

    /**
     * 等待Dialog
     */
    private val loadingDialog: MessageDialog by lazy {
        MessageDialog.newInstance("创建考核中，请稍等……")
    }

    override fun initLayout(): Int = R.layout.activity_kbi_time_config

    override fun initView() {
        tv_title.text = "新建考核-时间安排"
        iv_right.setOnClickListener(this)
        tv_next.setOnClickListener(this)

        rl_list.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapter.apply {
                timeTypeList = this@KbiTimeConfigActivity.timeTypeList
            }
        }

        tv_next.text = if (CreateKbiDataManager.kbiBean?.type == resources.getStringArray(R.array.evaMode)[3]) {
            "新建完成"
        } else {
            "下一步 公告发布"
        }
    }

    override fun initData() {
        var type = mutableListOf<Int>()

        CreateKbiDataManager.kbiBean?.orgType?.let {
            if (it.contains(resources.getStringArray(R.array.joinEvaOrg)[0])) {
                type.add(1)
            }
            if (it.contains(resources.getStringArray(R.array.joinEvaOrg)[1])) {
                type.add(2)
            }
            if (it.contains(resources.getStringArray(R.array.joinEvaOrg)[2])) {
                type.add(3)
            }
        }

        val typeStr = CreateKbiDataManager.getAppendStr(type, object : CreateKbiDataManager.GetStrListener<Int> {
            override fun getStr(bean: Int): String = bean.toString()
        }, ',')

        RequestManager.getOrgForAssessment(context,
                GetOrgForAssessmentReq(
                        UserManager.getInstance().getUserInfo(context)?.org_id ?: "",
                        typeStr,
                        1
                ),
                object : JsonCallback<ApiResponse<List<OrgRes>>, List<OrgRes>>() {
                    override fun onSuccess(response: Response<ApiResponse<List<OrgRes>>>?) {
                        response?.body()?.data?.let {
                            adapter.orgResList.addAll(it)
                        }
                    }
                })
    }

    override fun onClick(v: View?) {
        when (v) {
            iv_right -> finish()
            tv_next -> {
                if (checkParameter()) {
                    if (CreateKbiDataManager.kbiBean?.type == resources.getStringArray(R.array.evaMode)[3]) {
                        //直接创建考核 对于日常考核而言 这是最后一步
                        requestCreateKbi()
                    } else {
                        KbiPublicNoticeActivity.startInstant(context)
                    }
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

                                        BuiltKBIDetailActivity.startInstant(this@KbiTimeConfigActivity, res.id);

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
        if (timeTypeList.isNullOrEmpty()) {
            tv_next.snack("请添加一个考核时间")
            return false
        }

        if (!isToPlaceEva) {
            CreateKbiDataManager.kbiBean?.timeArrangementA = timeTypeList as List<KbiTimeCfgType1Bean>
            CreateKbiDataManager.kbiBean?.timeArrangementB = null
        } else {
            CreateKbiDataManager.kbiBean?.timeArrangementB = timeTypeList as List<KbiTimeCfgType2Bean>
            CreateKbiDataManager.kbiBean?.timeArrangementA = null
        }
        return true
    }
}
