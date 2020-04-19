package com.example.physicalfitnessexamination.view.excel

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import com.example.physicalfitnessexamination.Constants
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.activity.UserManager
import com.example.physicalfitnessexamination.api.response.TestProjectRes
import com.example.physicalfitnessexamination.bean.KbiTimeCfgType1Bean
import com.example.physicalfitnessexamination.bean.PersonBean
import com.example.physicalfitnessexamination.bean.TimeCfgType1DetailBean
import com.example.physicalfitnessexamination.page.kbi.CreateKbiDataManager
import com.example.physicalfitnessexamination.view.RosterDialogFragment
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.time_config_content.view.*
import kotlinx.android.synthetic.main.v_kbi_time_config1.view.*
import kotlinx.android.synthetic.main.v_kbi_time_config1_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * 时间安排（集中考核、竞赛）View
 * Created by chenzhiyuan On 2020/4/17
 */
class KbiTimeConfig1View : LinearLayout {

    companion object {
        val sdf = SimpleDateFormat("MM月dd日", Locale.CHINESE)
    }

    private var bean: KbiTimeCfgType1Bean? = null
        set(value) {
            field = value
            resetChildView()
            //日期
            tv_date.text = sdf.format(value?.date?.time)
        }
    private var onRemoveListener: OnRemoveListener<KbiTimeCfgType1Bean>? = null

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        LayoutInflater.from(context).inflate(R.layout.v_kbi_time_config1, this)

        dividerDrawable = ContextCompat.getDrawable(context, R.drawable.divider_hor)
        showDividers = SHOW_DIVIDER_MIDDLE + SHOW_DIVIDER_BEGINNING + SHOW_DIVIDER_END
        orientation = VERTICAL

        iv_removeDate.setOnClickListener {
            bean?.let { it1 -> onRemoveListener?.onRemove(it1) }
        }

        //添加时间段
        tv_addTimeQuantum.setOnClickListener {
            bean?.detailBean?.add(TimeCfgType1DetailBean())
            resetChildView()
        }
    }

    fun setData(bean: KbiTimeCfgType1Bean, onRemoveListener: OnRemoveListener<KbiTimeCfgType1Bean>) {
        this.bean = bean
        this.onRemoveListener = onRemoveListener
    }

    private fun resetChildView() {
        removeViews(2, childCount - 2)

        bean?.detailBean?.let { list ->
            for (eachBean in list) {
                addView(KbiTimeConfig1ItemView(context).apply {
                    setData(eachBean, object : OnRemoveListener<TimeCfgType1DetailBean> {
                        override fun onRemove(detailBean: TimeCfgType1DetailBean) {
                            list.remove(detailBean)

                            if (list.isEmpty()) {
                                this@KbiTimeConfig1View.iv_removeDate.performClick()
                            } else {
                                resetChildView()
                            }
                        }
                    })
                })
            }
        }
    }
}

class KbiTimeConfig1ItemView : LinearLayout {
    companion object {
        val sdf = SimpleDateFormat("HH:mm", Locale.CHINESE)
    }

    var bean: TimeCfgType1DetailBean? = null
        set(value) {
            field = value

            tv_convener.text = value?.convener?.let { appendLabel(it) }
            tv_starter.text = value?.starter?.let { appendLabel(it) }
            tv_timekeeper.text = value?.timekeeper?.let { appendLabel(it) }

            tv_timeQuantum.text = if (
                    value?.startTime != null && value.endTime != null
            ) "${sdf.format(value.startTime)}-${sdf.format(value.endTime)}" else ""

            sp_position.let { sp ->
                val defaultIndex = getPositionSpinnerData().indexOf(bean?.position)
                sp.setSpinner(getPositionSpinnerData(), onCheckListener = object : SpinnerParentView.OnCheckListener<String> {
                    override fun onConfirmAndChangeListener(view: SpinnerParentView<String>, selectBeanList: List<String>) {
                        if (selectBeanList.isNotEmpty()) {
                            bean?.position = selectBeanList.first()
                        }
                    }
                }, isRadio = true, defaultIndex = if (defaultIndex == -1) emptySet() else mutableSetOf(defaultIndex))
                sp.setName("选择岗位")
                sp.setSimpleUiMode(true)
            }

            tv_contentMsg.text = if (value?.assessment != null) {
                value.assessment?.NAME
            } else {
                value?.contentStr
            }
        }

    private var onRemoveListener: OnRemoveListener<TimeCfgType1DetailBean>? = null

    private fun getPositionSpinnerData(): Array<String> {
        var spinnerData: Array<String> = arrayOf("")
        when (UserManager.getInstance().getUserInfo(context).role_id) {
            Constants.RoleIDStr.MANAGE -> {
                spinnerData = resources.getStringArray(R.array.managePosition)
            }
            Constants.RoleIDStr.COMM -> {
                spinnerData = resources.getStringArray(R.array.commPosition)
            }
            Constants.RoleIDStr.MANAGE_CROPS -> {
                TODO("目前不会出现该角色 后期添加")
//                    spinnerData = resources.getStringArray(R.array.managePosition)
            }
            Constants.RoleIDStr.MANAGE_BRIGADE -> {
                spinnerData = resources.getStringArray(R.array.manageBrigadePosition)
            }
        }
        return spinnerData
    }

    private fun getTimePick(isStartTime: Boolean): TimePickerDialog {
        val calendar = Calendar.getInstance()
        val dialog = TimePickerDialog(context, AlertDialog.THEME_HOLO_LIGHT,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    if (isStartTime) {
                        bean?.startTime = Calendar.getInstance().apply {
                            set(Calendar.HOUR_OF_DAY, hourOfDay)
                            set(Calendar.MINUTE, minute)
                        }.time
                        getTimePick(false).show()
                    } else {
                        bean?.endTime = Calendar.getInstance().apply {
                            set(Calendar.HOUR_OF_DAY, hourOfDay)
                            set(Calendar.MINUTE, minute)
                        }.time
                        bean = bean
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
        dialog.setMessage(if (isStartTime) "开始时间" else "结束时间")
        return dialog
    }

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        LayoutInflater.from(context).inflate(R.layout.v_kbi_time_config1_item, this)

        dividerDrawable = ContextCompat.getDrawable(context, R.drawable.divider_ver)
        showDividers = SHOW_DIVIDER_MIDDLE
        orientation = HORIZONTAL

        tv_removeTimeQuantum.setOnClickListener {
            bean?.let { it1 -> onRemoveListener?.onRemove(it1) }
        }

        tv_timeQuantum.setOnClickListener {
            getTimePick(true).show()
        }
        tv_contentMsg.setOnClickListener {
            val dialogView = KbiTimeConfig1ContentDialogView(context)
            AlertDialog.Builder(context)
                    .setMessage("内容")
                    .setView(dialogView)
                    .setPositiveButton("确定") { dialog, which ->
                        val selectBean = dialogView.getSelectItemPosition()
                        if (selectBean != null) {
                            bean?.assessment = selectBean
                            tv_contentMsg?.text = selectBean.NAME
                        } else {
                            bean?.assessment = null
                            dialogView.getEditText().let { str ->
                                bean?.contentStr = str
                                tv_contentMsg.text = str
                            }
                        }
                    }
                    .setNegativeButton("取消", null)
                    .create().show()
        }

        //召集人
        tv_convener.setOnClickListener {
            bean?.convener?.let { it1 ->
                RosterDialogFragment.newInstance(null, it1, object : RosterDialogFragment.OnCheckListener {
                    override fun checkOver(list: ArrayList<PersonBean>) {
                        bean?.convener = list
                        tv_convener.text = appendLabel(list)
                    }
                }).show((context as AppCompatActivity).supportFragmentManager, "")
            }
        }

        //发令员
        tv_starter.setOnClickListener {
            bean?.starter?.let { it1 ->
                RosterDialogFragment.newInstance(null, it1, object : RosterDialogFragment.OnCheckListener {
                    override fun checkOver(list: ArrayList<PersonBean>) {
                        bean?.starter = list
                        tv_starter.text = appendLabel(list)
                    }
                }).show((context as AppCompatActivity).supportFragmentManager, "")
            }
        }

        //计时员
        tv_timekeeper.setOnClickListener {
            bean?.timekeeper?.let { it1 ->
                RosterDialogFragment.newInstance(null, it1, object : RosterDialogFragment.OnCheckListener {
                    override fun checkOver(list: ArrayList<PersonBean>) {
                        bean?.timekeeper = list
                        bean = bean
                        tv_timekeeper.text = appendLabel(list)
                    }
                }).show((context as AppCompatActivity).supportFragmentManager, "")
            }
        }
    }

    fun setData(bean: TimeCfgType1DetailBean, onRemoveListener: OnRemoveListener<TimeCfgType1DetailBean>) {
        try {
            this.bean = bean
            this.onRemoveListener = onRemoveListener
        } catch (e: Exception) {
            Logger.e(e, "")
        }
    }

    /**
     * 根据已选项拼接显示文案
     */
    private fun appendLabel(list: ArrayList<PersonBean>): String {
        var str = ""
        list.forEach { bean ->
            str += bean.NAME + " "
        }
        return str
    }
}

class KbiTimeConfig1ContentDialogView : LinearLayout {

    val list: ArrayList<TestProjectRes> by lazy {
        ArrayList<TestProjectRes>().apply {
            //添加一个空值
            add(TestProjectRes("", "", ""))

            val map = mutableMapOf<String, TestProjectRes>()
            CreateKbiDataManager.kbiBean?.objPart1?.forEach {
                map[it.ID] = it
            }
            CreateKbiDataManager.kbiBean?.objPart2?.forEach {
                map[it.ID] = it
            }
            CreateKbiDataManager.kbiBean?.objPart3?.forEach {
                map[it.ID] = it
            }
            CreateKbiDataManager.kbiBean?.objPart4?.forEach {
                map[it.ID] = it
            }

            map.forEach {
                add(it.value)
            }
        }

    }

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        LayoutInflater.from(context).inflate(R.layout.time_config_content, this)
        spinner_test.adapter = ArrayAdapter(context, R.layout.time_config_content_spinner_item, list)
    }

    /**
     * 获取输入的文案
     */
    fun getEditText(): String = et_content.text.trim().toString()

    fun getSelectItemPosition(): TestProjectRes? = if (spinner_test.selectedItemPosition != 0) list[spinner_test.selectedItemPosition] else null

    interface OnItemSelectedListener<T> {
        fun onItemSelected(entity: T)
    }
}

interface OnRemoveListener<T> {
    fun onRemove(detailBean: T)
}