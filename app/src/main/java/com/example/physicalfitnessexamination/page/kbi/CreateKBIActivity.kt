package com.example.physicalfitnessexamination.page.kbi

import android.content.Context
import android.content.Intent
import android.view.View
import com.example.physicalfitnessexamination.Constants
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.activity.UserManager
import com.example.physicalfitnessexamination.api.RequestManager
import com.example.physicalfitnessexamination.api.callback.JsonCallback
import com.example.physicalfitnessexamination.api.request.GetAssessmentObjectReq
import com.example.physicalfitnessexamination.api.response.ApiResponse
import com.example.physicalfitnessexamination.api.response.TestProjectRes
import com.example.physicalfitnessexamination.base.MyBaseActivity
import com.example.physicalfitnessexamination.util.snack
import com.example.physicalfitnessexamination.util.toast
import com.example.physicalfitnessexamination.view.excel.SpinnerParentView
import com.lzy.okgo.model.Response
import kotlinx.android.synthetic.main.activity_create_kbi.*
import kotlinx.android.synthetic.main.v_toolbar.*

/**
 * 新建考核
 */
class CreateKBIActivity : MyBaseActivity(), View.OnClickListener {

    companion object {
        const val CHOOSE_ORG_FIRST = "请先选择考核方式"

        /**
         * 跳转方法
         * @param context 上下文
         */
        @JvmStatic
        fun startInstant(context: Context) {
            Intent(context, CreateKBIActivity::class.java).let {
                context.startActivity(it)
            }
        }
    }

    override fun initLayout(): Int = R.layout.activity_create_kbi

    override fun initView() {
        tv_title.text = "新建考核"

        iv_right.setOnClickListener(this)
        tv_next.setOnClickListener(this)
    }

    override fun initData() {

        val orgName = UserManager.getInstance().getUserInfo(context).org_name
//        val orgName = Constants.RoleIDStr.COMM

        //组织单位
        tv_orgUnitStr.text = orgName

        if (orgName == Constants.RoleIDStr.COMM) {
            //消防站
            tv_projectType.text = "消防站"
            spv_pj1.setName("指挥员")
            spv_pj2.setName("消防员")
        } else {
            //机关
            tv_projectType.text = "机关"
            spv_pj1.setName("男")
            spv_pj2.setName("女")
        }

        spv_evaOrg.let {
            it.setName("参考单位")
            it.setSpinner(resources.getStringArray(R.array.joinEvaOrg))
        }
        spv_evaWay.let {
            it.setName("考核方式")
            it.setSpinner(resources.getStringArray(R.array.evaMode), isRadio = true,
                    onCheckListener = object : SpinnerParentView.OnCheckListener<String> {
                        override fun onConfirmAndChangeListener(view: SpinnerParentView<String>, selectBeanList: List<String>) {
                            if (selectBeanList.isNotEmpty()) {
                                spv_perSelect.setChooseAble(true)
                                spv_scoreRecord.setChooseAble(true)

                                if (selectBeanList.size == 1) {
                                    when (selectBeanList.first()) {
                                        resources.getStringArray(R.array.evaMode)[0] -> {
                                            //业务竞赛
                                            spv_perSelect.setSpinner(
                                                    resources.getStringArray(R.array.perSelect).filter { str ->
                                                        return@filter str == resources.getStringArray(R.array.perSelect)[2]
                                                    }.toTypedArray(),
                                                    isRadio = true)
                                            spv_scoreRecord.setSpinner(
                                                    resources.getStringArray(R.array.resultsRemember).filter { str ->
                                                        return@filter str == resources.getStringArray(R.array.resultsRemember)[0]
                                                    }.toTypedArray(),
                                                    isRadio = true)
                                        }
                                        resources.getStringArray(R.array.evaMode)[1] -> {
                                            //集中考核
                                            spv_perSelect.setSpinner(
                                                    resources.getStringArray(R.array.perSelect).filter { str ->
                                                        return@filter (str == resources.getStringArray(R.array.perSelect)[1]
                                                                || str == resources.getStringArray(R.array.perSelect)[2])
                                                    }.toTypedArray(),
                                                    isRadio = true)
                                            spv_scoreRecord.setSpinner(
                                                    resources.getStringArray(R.array.resultsRemember).filter { str ->
                                                        return@filter str == resources.getStringArray(R.array.resultsRemember)[1]
                                                    }.toTypedArray(),
                                                    isRadio = true)
                                        }
                                        resources.getStringArray(R.array.evaMode)[2] -> {
                                            //到站考核
                                            spv_perSelect.setSpinner(
                                                    resources.getStringArray(R.array.perSelect),
                                                    isRadio = true)
                                            spv_scoreRecord.setSpinner(
                                                    resources.getStringArray(R.array.resultsRemember).filter { str ->
                                                        return@filter str == resources.getStringArray(R.array.resultsRemember)[1]
                                                    }.toTypedArray(),
                                                    isRadio = true)
                                        }
                                        resources.getStringArray(R.array.evaMode)[3] -> {
                                            //日常考核
                                            spv_perSelect.setSpinner(
                                                    resources.getStringArray(R.array.perSelect).filter { str ->
                                                        return@filter str == resources.getStringArray(R.array.perSelect)[0]
                                                    }.toTypedArray(),
                                                    isRadio = true)
                                            spv_scoreRecord.setSpinner(
                                                    resources.getStringArray(R.array.resultsRemember).filter { str ->
                                                        return@filter str == resources.getStringArray(R.array.resultsRemember)[1]
                                                    }.toTypedArray(),
                                                    isRadio = true)
                                        }
                                    }
                                }

                            } else {
                                spv_perSelect.clear()
                                spv_scoreRecord.clear()
                            }
                        }
                    })
        }
        spv_perSelect.let {
            it.setName("人员选取")
            it.setSpinner(resources.getStringArray(R.array.perSelect), isRadio = true)
            it.setChooseAble(false, CHOOSE_ORG_FIRST)
        }
        spv_scoreRecord.let {
            it.setName("成绩记录")
            it.setSpinner(resources.getStringArray(R.array.resultsRemember), isRadio = true)
            it.setChooseAble(false, CHOOSE_ORG_FIRST)
        }

        RequestManager.getAssessmentObject(context
                , GetAssessmentObjectReq(if (orgName == Constants.RoleIDStr.COMM) 0 else 1, UserManager.getInstance().getUserInfo(context).org_id)
                , object : JsonCallback<ApiResponse<List<TestProjectRes>>, List<TestProjectRes>>() {
            override fun onSuccess(response: Response<ApiResponse<List<TestProjectRes>>>?) {
                response?.body()?.data?.let {
                    if (orgName == Constants.RoleIDStr.COMM) {
                        //消防站
                        spv_pj1.setSpinner(it.toTypedArray(), object : SpinnerParentView.OnGetStrListener<TestProjectRes> {
                            override fun getStr(bean: TestProjectRes): String = bean.NAME
                        })
                        spv_pj2.setSpinner(it.toTypedArray(), object : SpinnerParentView.OnGetStrListener<TestProjectRes> {
                            override fun getStr(bean: TestProjectRes): String = bean.NAME
                        })
                    } else {
                        //机关
                        spv_pj1.setSpinner(
                                it.filter { bean ->
                                    bean.SEX == "0"
                                }.toTypedArray()
                                , object : SpinnerParentView.OnGetStrListener<TestProjectRes> {
                            override fun getStr(bean: TestProjectRes): String = bean.NAME
                        })
                        spv_pj2.setSpinner(it.filter { bean ->
                            bean.SEX == "1"
                        }.toTypedArray(), object : SpinnerParentView.OnGetStrListener<TestProjectRes> {
                            override fun getStr(bean: TestProjectRes): String = bean.NAME
                        })
                    }
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when (v) {
            iv_right -> finish()
            tv_next -> {
                if (checkParameter()) {
                    toast("下一步")
                }
            }
        }
    }

    /**
     * 确认跳转参数
     * @return 是否合法
     */
    private fun checkParameter(): Boolean {
        //考核名称
        if (edit_kbiName.text.trim().isEmpty()) {
            edit_kbiName.snack("请输入考核名称")
            return false
        }

        //参考单位
        if (spv_evaOrg.getSelectList().isEmpty()) {
            edit_kbiName.snack("请选择参考单位")
            return false
        }

        //考核方式
        if (spv_evaWay.getSelectList().isEmpty()) {
            edit_kbiName.snack("请选择考核方式")
            return false
        }

        //人员选取
        if (spv_perSelect.getSelectList().isEmpty()) {
            edit_kbiName.snack("请选择人员选取")
            return false
        }

        //成绩记取
        if (spv_scoreRecord.getSelectList().isEmpty()) {
            edit_kbiName.snack("请选择成绩记取")
            return false
        }

        return true
    }
}
