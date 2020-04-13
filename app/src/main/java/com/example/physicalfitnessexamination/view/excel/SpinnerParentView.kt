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
     * 选项是否是单选
     */
    private var isRadio: Boolean = false

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
//        // Load attributes
//        val a = context.obtainStyledAttributes(
//                attrs, R.styleable.TestView, defStyle, 0)
//        a.recycle()
        orientation = HORIZONTAL
        minimumHeight = resources.getDimensionPixelSize(R.dimen.excel_height)
        setBackgroundColor(ContextCompat.getColor(context, R.color._B7CFF3))

        LayoutInflater.from(context).inflate(R.layout.v_spinner_parent, this, true)

        setOnClickListener {
            if (chooseAble) {
                AlertDialog.Builder(context).let { builder ->
                    builder.setTitle(tv_name.text.toString())
                    builder.setView(
                            if (isRadio) {
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
//            removeViews(2, childCount - 2)
            warpLinearLayout.removeAllViews()
            selectSet.forEach { index ->
                warpLinearLayout.addView(TextView(context).apply {
                    setTextColor(ContextCompat.getColor(context, R.color._2C2C2C))
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
                    text = if (isRadio) {
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
        if (isRadio) {
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
                if (selectSet.isNotEmpty() && selectSet.size == 1) {
                    spinnerRadioGroup.check(spinnerRadioGroup.getChildAt(selectSet.first()).id)
                }
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
     * @param isRadio 是否是单选
     */
    fun <T> setSpinner(data: Array<T>,
                       listener: OnGetStrListener<T> = object : OnGetStrListener<T> {
                           override fun getStr(bean: T): String {
                               return if (bean is String) bean else ""
                           }
                       },
                       onCheckListener: OnCheckListener<T>? = null,
                       isRadio: Boolean = false) where T : Any {
        clear()
        this.data = data as Array<X>
        this.isRadio = isRadio
        this.onCheckListener = if (onCheckListener != null) onCheckListener as OnCheckListener<X> else null
        val dataStrList = ArrayList<String>()
        data.forEach { t ->
            dataStrList.add(listener.getStr(t as T))
        }
        prepareChoiceView(dataStrList)
    }

//    /**
//     * 设置选项
//     * @param data 选项集合
//     * @param isRadio 是否是单选
//     */
//    fun <T> setSpinner(data: Array<String>, onCheckListener: OnCheckListener<T>? = null, isRadio: Boolean = false)
//            where T : X {
//        try {
//            this.isRadio = isRadio
//            this.onCheckListener = onCheckListener as OnCheckListener<X>
//            val dataStrList = ArrayList<String>()
//            data.forEach { t ->
//                dataStrList.add(t)
//            }
//            prepareChoiceView(dataStrList)
//        } catch (e: Exception) {
//            Logger.e(e, "ss")
//        }
//    }

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