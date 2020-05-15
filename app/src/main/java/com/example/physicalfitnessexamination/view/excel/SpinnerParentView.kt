package com.example.physicalfitnessexamination.view.excel

import android.app.AlertDialog
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.common.adapter.CheckBoxAdapter
import com.example.physicalfitnessexamination.util.dp2px
import com.example.physicalfitnessexamination.util.snack
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.v_spinner_parent.view.*

/**
 * 选取组件
 * Created by chenzhiyuan On 2020/4/8
 */
class SpinnerParentView<X> : LinearLayout {

    /**
     * 默认背景色
     */
    var defaultBkColor = R.color._B7CFF3
        set(value) {
            field = value
            setBackgroundColor(ContextCompat.getColor(context, value))
        }

    /**
     * 选项是否是单选
     */
    private var isSingleSelection: Boolean = false

    /**
     * 已选中的下标
     */
    private val selectSet: MutableSet<Int> by lazy {
        mutableSetOf<Int>()
    }

    /**
     * 数据总集合
     */
    private var data: Array<X>? = null

    private val spinnerAdapter: CheckBoxAdapter by lazy {
        CheckBoxAdapter()
    }

    private val spinnerRadioGroup: RadioGroup by lazy {
        RadioGroup(context).apply {
            orientation = VERTICAL
            setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    -1 -> {
                        selectSet.clear()
                        group.clearCheck()
                    }
                    else -> {
                        for (index in 0 until group.childCount) {
                            if (group.getChildAt(index).id == checkedId) {
                                selectSet.clear()
                                selectSet.add(index)
                                break
                            }
                        }
                    }
                }
            }
        }
    }

    private var onCheckListener: OnCheckListener<X>? = null

    /**
     * 控件是否可选择
     */
    var chooseAble: Boolean = true

    /**
     * 不可选择原因
     */
    var unableChooseMsg: String = UNABLE_MSG

    /**
     * 是否在数据集合有且仅有一条的时候,默认选中该数据
     */
    var defaultDisplayWhenJustOne = false

    companion object {
        private const val UNABLE_MSG = "暂不可选择"
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
        orientation = HORIZONTAL
        minimumHeight = resources.getDimensionPixelSize(R.dimen.excel_height)

        defaultBkColor = R.color._B7CFF3

        LayoutInflater.from(context).inflate(R.layout.v_spinner_parent, this, true)

        setOnClickListener {
            if (chooseAble) {
                AlertDialog.Builder(context).let { builder ->
                    builder.setTitle(tv_name.text.toString())
                    builder.setView(
                            if (isSingleSelection) {
                                ScrollView(context).apply {
                                    if (spinnerRadioGroup.parent is ViewGroup) {
                                        (spinnerRadioGroup.parent as ViewGroup).removeView(spinnerRadioGroup)
                                    }
                                    addView(spinnerRadioGroup)
                                }
                            } else {
                                RecyclerView(builder.context).apply {
                                    layoutManager = LinearLayoutManager(builder.context)
                                    adapter = spinnerAdapter
                                }
                            }
                    )
                    builder.setNegativeButton("取消", null)
                    builder.setPositiveButton("确定") { dialog, which ->
                        resetSelectView()
                        onCheckListener?.onConfirmAndChangeListener(this, getSelectList())
                    }
                    builder.create().show()
                }
            } else {
                snack(unableChooseMsg)
            }
        }
    }

    private fun resetSelectView() {
        try {
            warpLinearLayout.removeAllViews()
            selectSet.forEach { index ->
                warpLinearLayout.addView(TextView(context).apply {
                    setTextColor(ContextCompat.getColor(context, R.color._2C2C2C))
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
                    text = if (isSingleSelection) {
                        spinnerRadioGroup.findViewById<RadioButton>(spinnerRadioGroup.checkedRadioButtonId).text.toString()
                    } else {
                        spinnerAdapter.getData()?.get(index)
                    }
                    setPadding(10.dp2px, 0, 10.dp2px, 0)
                    gravity = Gravity.CENTER_VERTICAL
                }, LayoutParams(LayoutParams.WRAP_CONTENT, resources.getDimensionPixelSize(R.dimen.excel_height)))
            }
        } catch (e: Exception) {
            Logger.e(e, "resetSelectView()")
        }
    }

    private fun prepareChoiceView(dataStrList: ArrayList<String>) {
        if (isSingleSelection) {
            spinnerRadioGroup.removeAllViews()
            dataStrList.forEach { choiceStr ->
                spinnerRadioGroup.addView(
                        RadioButton(context).apply {
                            text = choiceStr
                            setTextColor(ContextCompat.getColor(context, R.color.black))
                            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
                            setPadding(18.dp2px, 5.dp2px, 18.dp2px, 5.dp2px)
                        }
                        , RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT)
                )
            }
            if (selectSet.isNotEmpty() && selectSet.size == 1) {
                spinnerRadioGroup.check(spinnerRadioGroup.getChildAt(selectSet.first()).id)
            }
        } else {
            spinnerAdapter.setListData(dataStrList, selectSet)
        }
    }

    /**
     * 设置标题
     */
    fun setName(name: String) {
        tv_name.text = name
    }

    /**
     * 设置选项
     * @param data 选项集合
     * @param listener 获取String监听方法
     * @param onCheckListener 点击【确定】时触发的回调
     * @param isRadio 是否是单选
     * @param defaultIndex 默认选中的下标集合
     */
    fun <T> setSpinner(data: Array<T>,
                       listener: OnGetStrListener<T> = object : OnGetStrListener<T> {
                           override fun getStr(bean: T): String {
                               return if (bean is String) bean else ""
                           }
                       },
                       onCheckListener: OnCheckListener<T>? = null,
                       isRadio: Boolean = false,
                       defaultIndex: Set<Int> = mutableSetOf()) where T : Any {
        clear()
        this.data = data as Array<X>
        this.isSingleSelection = isRadio

        selectSet.clear()
        if (defaultDisplayWhenJustOne && isSingleSelection && data.size == 1) {
            selectSet.add(0)
        } else {
            if (isRadio && defaultIndex.isNotEmpty()) {
                selectSet.add(defaultIndex.first())
            } else {
                selectSet.addAll(defaultIndex)
            }
        }

        this.onCheckListener = if (onCheckListener != null) onCheckListener as OnCheckListener<X> else null
        val dataStrList = ArrayList<String>()
        data.forEach { t ->
            dataStrList.add(listener.getStr(t as T))
        }
        prepareChoiceView(dataStrList)
        resetSelectView()
    }

    /**
     * 是否设置为简洁UI模式
     * @param isSimpleUiMode 简洁UI模式（什么都不显示）
     */
    fun setSimpleUiMode(isSimpleUiMode: Boolean) {
        if (isSimpleUiMode) {
            tv_name.visibility = View.GONE
            iv_downArrow.visibility = View.GONE
        } else {
            tv_name.visibility = View.VISIBLE
            iv_downArrow.visibility = View.VISIBLE
        }
    }

    /**
     * 设置控件是否可选择
     * @param chooseAble 是否
     * @param unableMsg 不可选择提示文案
     */
    fun setChooseAble(chooseAble: Boolean, unableMsg: String = UNABLE_MSG) {
        this.chooseAble = chooseAble
        this.unableChooseMsg = unableMsg
    }

    /**
     * 清除当前选项
     */
    fun clear() {
        selectSet.clear()
        resetSelectView()
    }

    /**
     * 获取选中的数据集合
     */
    fun getSelectList(): List<X> {
        val list = ArrayList<X>()
        selectSet.forEach { index ->
            data?.get(index)?.let { list.add(it) }
        }

        return list
    }

    interface OnGetStrListener<T> {
        fun getStr(bean: T): String
    }

    interface OnCheckListener<T> {
        fun onConfirmAndChangeListener(view: SpinnerParentView<T>, selectBeanList: List<T>)
    }
}