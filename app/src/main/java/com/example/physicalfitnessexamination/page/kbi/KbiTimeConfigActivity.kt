package com.example.physicalfitnessexamination.page.kbi

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.activity.UserManager
import com.example.physicalfitnessexamination.api.RequestManager
import com.example.physicalfitnessexamination.api.callback.JsonCallback
import com.example.physicalfitnessexamination.api.request.GetOrgForAssessmentReq
import com.example.physicalfitnessexamination.api.response.ApiResponse
import com.example.physicalfitnessexamination.api.response.OrgRes
import com.example.physicalfitnessexamination.base.MyBaseActivity
import com.example.physicalfitnessexamination.bean.KbiTimeCfgBean
import com.example.physicalfitnessexamination.bean.KbiTimeCfgType1Bean
import com.example.physicalfitnessexamination.bean.KbiTimeCfgType2Bean
import com.example.physicalfitnessexamination.page.kbi.adapter.KbiTimeConfigAdapter
import com.example.physicalfitnessexamination.util.snack
import com.lzy.okgo.model.Response
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
                    KbiPublicNoticeActivity.startInstant(context)
                }
            }
        }
    }

    private fun checkParameter(): Boolean {
        timeTypeList.forEach { cfgBean ->
            when (cfgBean) {
                is KbiTimeCfgType1Bean -> {
                    cfgBean.detailBean.forEach { cfgDetailBean ->
                        if (cfgDetailBean.startTime == null || cfgDetailBean.endTime == null) {
                            tv_next.snack("时间不可为空")
                            return false
                        }

                        if (cfgDetailBean.contentStr.isNullOrEmpty() && cfgDetailBean.assessment == null) {
                            tv_next.snack("内容不可为空")
                            return false
                        }

                        if (cfgDetailBean.position == null) {
                            tv_next.snack("岗位不可为空")
                            return false
                        }

                        if (cfgDetailBean.convener.isEmpty()) {
                            tv_next.snack("请选择召集人")
                            return false
                        }

                        if (cfgDetailBean.starter.isEmpty()) {
                            tv_next.snack("请选择发令员")
                            return false
                        }

                        if (cfgDetailBean.timekeeper.isEmpty()) {
                            tv_next.snack("请选择计时员")
                            return false
                        }
                    }
                }
                is KbiTimeCfgType2Bean -> {
                    if (cfgBean.isMorning == null) {
                        tv_next.snack("请选择时间")
                        return false
                    }

                    if (cfgBean.org1 == null && cfgBean.org2 == null && cfgBean.org3 == null) {
                        tv_next.snack("请选择考核组")
                        return false
                    }
                }
            }
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
