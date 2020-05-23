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
        tv_title.text = "新建考核 — 考核方案"

        iv_right.setOnClickListener(this)
        tv_next.setOnClickListener(this)

        group1.visibility = View.GONE
        group2.visibility = View.GONE
    }

    override fun initData() {
        CreateKbiDataManager.createNewKbi()

        val orgName = UserManager.getInstance().getUserInfo(context).org_name

        //组织单位
        tv_orgUnitStr.text = orgName

        //机关
        spv_pj1.setName("男")
        spv_pj2.setName("女")

        //消防站
        spv_pj3.setName("指挥员")
        spv_pj4.setName("消防员")

        spv_evaWay.let {
            it.setName("考核方式")
            it.defaultDisplayWhenJustOne = true
            it.setSpinner(
                    if (UserManager.getInstance().getUserInfo(context).role_id == Constants.RoleIDStr.COMM) {
                        arrayOf(resources.getStringArray(R.array.evaMode)[3])
                    } else {
                        resources.getStringArray(R.array.evaMode)
                    }
                    , isRadio = true,
                    onCheckListener = object : SpinnerParentView.OnCheckListener<String> {
                        override fun onConfirmAndChangeListener(view: SpinnerParentView<String>, selectBeanList: List<String>) {
                            if (selectBeanList.isNotEmpty()) {
                                spv_evaOrg.setChooseAble(true)
                                spv_perSelect.setChooseAble(true)
                                spv_scoreRecord.setChooseAble(true)

                                if (selectBeanList.size == 1) {
                                    when (selectBeanList.first()) {
                                        resources.getStringArray(R.array.evaMode)[0] -> {
                                            //业务竞赛
                                            spv_evaOrg.setSpinner(getJoinEvaOrg(),
                                                    onCheckListener = object : SpinnerParentView.OnCheckListener<String> {
                                                        override fun onConfirmAndChangeListener(view: SpinnerParentView<String>, selectBeanList: List<String>) {
                                                            initProVisible(selectBeanList)
                                                        }
                                                    }, isRadio = true)
                                            spv_evaOrg.isEnabled = true

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
                                            spv_evaOrg.setSpinner(getJoinEvaOrg(),
                                                    onCheckListener = object : SpinnerParentView.OnCheckListener<String> {
                                                        override fun onConfirmAndChangeListener(view: SpinnerParentView<String>, selectBeanList: List<String>) {
                                                            initProVisible(selectBeanList)
                                                        }
                                                    })
                                            spv_evaOrg.isEnabled = true

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
                                            spv_evaOrg.setSpinner(getJoinEvaOrg(),
                                                    onCheckListener = object : SpinnerParentView.OnCheckListener<String> {
                                                        override fun onConfirmAndChangeListener(view: SpinnerParentView<String>, selectBeanList: List<String>) {
                                                            initProVisible(selectBeanList)
                                                        }
                                                    })
                                            spv_evaOrg.isEnabled = true

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
                                            run {
                                                var dataList: Array<String> = arrayOf()
                                                when (UserManager.getInstance().getUserInfo(context).role_id) {
                                                    Constants.RoleIDStr.MANAGE -> {
                                                        //支队机关
                                                        dataList = arrayOf(resources.getStringArray(R.array.joinEvaOrg)[0])
                                                    }
                                                    Constants.RoleIDStr.COMM -> {
                                                        //消防站
                                                        dataList = arrayOf(resources.getStringArray(R.array.joinEvaOrg)[2])
                                                    }
                                                    Constants.RoleIDStr.MANAGE_BRIGADE -> {
                                                        //大队机关
                                                        dataList = arrayOf(resources.getStringArray(R.array.joinEvaOrg)[1])
                                                    }
                                                }
                                                spv_evaOrg.setSpinner(dataList,
                                                        onCheckListener = object : SpinnerParentView.OnCheckListener<String> {
                                                            override fun onConfirmAndChangeListener(view: SpinnerParentView<String>, selectBeanList: List<String>) {
                                                                initProVisible(selectBeanList)
                                                            }
                                                        }, defaultIndex = setOf(0))
                                                spv_evaOrg.isEnabled = false
                                                initProVisible(dataList.toList())
                                            }

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
        spv_evaOrg.let {
            it.setName("参考单位")
            it.setSpinner(getJoinEvaOrg(),
                    onCheckListener = object : SpinnerParentView.OnCheckListener<String> {
                        override fun onConfirmAndChangeListener(view: SpinnerParentView<String>, selectBeanList: List<String>) {
                            initProVisible(selectBeanList)
                        }
                    })
            it.setChooseAble(false, CHOOSE_ORG_FIRST)
        }
        spv_perSelect.let {
            it.setName("人员选取")
            it.defaultDisplayWhenJustOne = true
            it.setSpinner(resources.getStringArray(R.array.perSelect), isRadio = true)
            it.setChooseAble(false, CHOOSE_ORG_FIRST)
        }
        spv_scoreRecord.let {
            it.setName("成绩记录")
            it.defaultDisplayWhenJustOne = true
            it.setSpinner(resources.getStringArray(R.array.resultsRemember), isRadio = true)
            it.setChooseAble(false, CHOOSE_ORG_FIRST)
        }

        RequestManager.getAssessmentObject(context
                , GetAssessmentObjectReq(0, UserManager.getInstance().getUserInfo(context).org_id)
                , object : JsonCallback<ApiResponse<List<TestProjectRes>>, List<TestProjectRes>>() {
            override fun onSuccess(response: Response<ApiResponse<List<TestProjectRes>>>?) {
                response?.body()?.data?.let {
                    //消防站
                    spv_pj3.setSpinner(it.toTypedArray(), object : SpinnerParentView.OnGetStrListener<TestProjectRes> {
                        override fun getStr(bean: TestProjectRes): String = bean.NAME
                    })
                    spv_pj4.setSpinner(it.toTypedArray(), object : SpinnerParentView.OnGetStrListener<TestProjectRes> {
                        override fun getStr(bean: TestProjectRes): String = bean.NAME
                    })
                }
            }
        })

        RequestManager.getAssessmentObject(context
                , GetAssessmentObjectReq(1, UserManager.getInstance().getUserInfo(context).org_id)
                , object : JsonCallback<ApiResponse<List<TestProjectRes>>, List<TestProjectRes>>() {
            override fun onSuccess(response: Response<ApiResponse<List<TestProjectRes>>>?) {
                response?.body()?.data?.let {
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
        })
    }

    /**
     * 获取当前账号可选参考单位
     */
    private fun getJoinEvaOrg(): Array<String> {
        return when (UserManager.getInstance().getUserInfo(context).role_id) {
            Constants.RoleIDStr.MANAGE -> {
                resources.getStringArray(R.array.joinEvaOrg)
            }
            Constants.RoleIDStr.MANAGE_BRIGADE -> {
                arrayOf(resources.getStringArray(R.array.joinEvaOrg)[1], resources.getStringArray(R.array.joinEvaOrg)[2])
            }
            Constants.RoleIDStr.COMM -> {
                arrayOf(resources.getStringArray(R.array.joinEvaOrg)[2])
            }
            else -> emptyArray()
        }
    }

    /**
     * 初始化项目中 机关、消防站 的显示和隐藏
     * @param selectBeanList 当前选中的参考单位集合
     */
    private fun initProVisible(selectBeanList: List<String>) {
        group1.visibility = View.GONE
        group2.visibility = View.GONE
        if (selectBeanList.contains(resources.getStringArray(R.array.joinEvaOrg)[0])
                || selectBeanList.contains(resources.getStringArray(R.array.joinEvaOrg)[1])) {
            //机关
            group1.visibility = View.VISIBLE
        }

        if (selectBeanList.contains(resources.getStringArray(R.array.joinEvaOrg)[2])) {
            //消防站
            group2.visibility = View.VISIBLE
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            iv_right -> finish()
            tv_next -> {
                if (checkParameter()) {
                    if (CreateKbiDataManager.kbiBean?.type == resources.getStringArray(R.array.evaMode)[3]) {
                        KbiTimeConfigActivity.startInstant(context)
                    } else {
                        KbiOrgActivity.startInstant(context)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        CreateKbiDataManager.clearKbi()
        super.onDestroy()
    }

    /**
     * 确认跳转参数
     * @return 是否合法
     */
    private fun checkParameter(): Boolean {
        //考核名称
        edit_kbiName.text.trim().let { editStr ->
            if (editStr.isEmpty()) {
                edit_kbiName.snack("请输入考核名称")
                return false
            } else {
                CreateKbiDataManager.kbiBean?.name = editStr.toString()
            }
        }

        //参考单位
        spv_evaOrg.getSelectList().let { sl ->
            if (sl.isEmpty()) {
                edit_kbiName.snack("请选择参考单位")
                return false
            } else {
                CreateKbiDataManager.kbiBean?.orgType = sl as List<String>
            }
        }

        //考核方式
        spv_evaWay.getSelectList().let { sl ->
            if (sl.isEmpty()) {
                edit_kbiName.snack("请选择考核方式")
                return false
            } else {
                CreateKbiDataManager.kbiBean?.type = sl.first() as String
            }
        }

        //人员选取
        spv_perSelect.getSelectList().let { sl ->
            if (sl.isEmpty()) {
                edit_kbiName.snack("请选择人员选取")
                return false
            } else {
                CreateKbiDataManager.kbiBean?.personType = sl.first() as String
            }
        }

        //成绩记取
        spv_scoreRecord.getSelectList().let { sl ->
            if (sl.isEmpty()) {
                edit_kbiName.snack("请选择成绩记取")
                return false
            } else {
                CreateKbiDataManager.kbiBean?.achievenmentType = sl.first() as String
            }
        }

        //考核项目
        if (spv_pj1.getSelectList().isNullOrEmpty() && spv_pj2.getSelectList().isNullOrEmpty()
                && spv_pj3.getSelectList().isNullOrEmpty() && spv_pj4.getSelectList().isNullOrEmpty()) {
            edit_kbiName.snack("最少添加一项考核项目")
            return false
        }
        CreateKbiDataManager.kbiBean?.objPart1 = null
        CreateKbiDataManager.kbiBean?.objPart2 = null
        CreateKbiDataManager.kbiBean?.objPart3 = null
        CreateKbiDataManager.kbiBean?.objPart4 = null
        if (group1.visibility == View.VISIBLE) {
            CreateKbiDataManager.kbiBean?.objPart1 = spv_pj1.getSelectList() as List<TestProjectRes>
            CreateKbiDataManager.kbiBean?.objPart2 = spv_pj2.getSelectList() as List<TestProjectRes>
        }
        if (group2.visibility == View.VISIBLE) {
            CreateKbiDataManager.kbiBean?.objPart3 = spv_pj3.getSelectList() as List<TestProjectRes>
            CreateKbiDataManager.kbiBean?.objPart4 = spv_pj4.getSelectList() as List<TestProjectRes>
        }
        return true
    }
}
