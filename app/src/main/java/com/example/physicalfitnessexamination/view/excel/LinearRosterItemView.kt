package com.example.physicalfitnessexamination.view.excel

import android.content.Context
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.bean.PersonBean
import kotlinx.android.synthetic.main.v_linear_roster_item.view.*

/**
 * 花名册Item组件
 * Created by chenzhiyuan On 2020/4/8
 */
class LinearRosterItemView : LinearLayout {

    private var listener: OnRosterItemListener? = null

    private var bean: PersonBean? = null

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
        LayoutInflater.from(context).inflate(R.layout.v_linear_roster_item, this, true)

        dividerDrawable = ContextCompat.getDrawable(context, R.drawable.divider_ver)
        showDividers = SHOW_DIVIDER_MIDDLE

        tv_checkAll.visibility = View.VISIBLE
        cb_check.visibility = View.GONE

        tv_checkAll.setOnClickListener {
            listener?.onClickAll()
        }
        cb_check.setOnCheckedChangeListener { buttonView, isChecked ->
            bean?.let {
                listener?.onCheckedChangeListener(isChecked, it)
            }
        }

        //序号
        tv_index.text = "序号"
        //姓名
        tv_perName.text = "姓名"
        //性别
        tv_perSex.text = "性别"
        //年龄
        tv_perAge.text = "年龄"
        //职务
        tv_perJob.text = "职务"
        //单位
        tv_perOrg.text = "单位"
    }

    /**
     * 设置子控件中所有TextView的字体颜色
     * @param color 字体颜色
     */
    fun setTvColor(@ColorRes color: Int = R.color.black) {
        for (i in 0 until childCount) {
            getChildAt(i).let {
                if (it is TextView) {
                    it.setTextColor(ContextCompat.getColor(context, color))
                }
            }
        }
    }

    fun setData(index: Int, bean: PersonBean, isChecked: Boolean) {
        tv_checkAll.visibility = View.GONE
        cb_check.visibility = View.VISIBLE

        this.bean = bean
        cb_check.isChecked = isChecked

        //序号
        tv_index.text = (index + 1).toString()
        //姓名
        tv_perName.text = bean.NAME
        //性别
        tv_perSex.text = bean.SEX
        //年龄
        tv_perAge.text = bean.AGE?.toString() ?: ""
        //职务
        tv_perJob.text = bean.ZW
        //单位
        tv_perOrg.text = bean.J_TYPE
    }

    fun setListener(listener: OnRosterItemListener) {
        this.listener = listener
    }

    interface OnRosterItemListener {
        /**
         * 点击【全选】的回调
         */
        fun onClickAll() {}

        /**
         * 当点击状态发生变化的回调
         */
        fun onCheckedChangeListener(isChecked: Boolean, entity: PersonBean) {}
    }
}