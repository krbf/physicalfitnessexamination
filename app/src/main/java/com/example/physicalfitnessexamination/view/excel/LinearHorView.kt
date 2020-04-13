package com.example.physicalfitnessexamination.view.excel

import android.content.Context
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.util.dp2px

/**
 * 横向标题
 * Created by chenzhiyuan On 2020/4/8
 */
class LinearHorView : LinearLayout {

    /**
     * 末尾的View
     */
    private var endView: View? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
//        LayoutInflater.from(context).inflate(R.layout.v_linearhor, this)

        setBackColor(R.color.colorAccent)
        setEachWeight()
        showDividers = SHOW_DIVIDER_MIDDLE
        dividerDrawable = resources.getDrawable(R.drawable.divider_ver, null)
    }

    /**
     * 设置子View宽度比重
     * @param eachWeight
     */
    fun setEachWeight(eachWeight: Array<Int> = arrayOf(1, 2, 2, 2, 2, 2, 2, 5, 2)) {
        removeAllViews()
        for (i in eachWeight.indices) {
            addView(
                    TextView(context).apply {
                        setPadding(5.dp2px, 0, 0, 0)
                        setTextColor(ContextCompat.getColor(context, R.color.black))
                        maxLines = 1
                        ellipsize = TextUtils.TruncateAt.END
                    },
                    LayoutParams(0, LayoutParams.WRAP_CONTENT).apply {
                        gravity = Gravity.CENTER
                        weight = eachWeight[i].toFloat()
                    }
            )
        }
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

    /**
     * 设置背景颜色
     * @param color 颜色
     */
    fun setBackColor(@ColorRes color: Int) {
        setBackgroundColor(ContextCompat.getColor(context, color))
    }

    /**
     * 设置EndView
     * @param endView 最末端的View
     */
    fun setEndView(endView: View) {
        if (childCount > 0) {
            val oldEndView = getChildAt(childCount - 1)
            removeView(oldEndView)
            //维持weight
            addView(endView, oldEndView.layoutParams)
            this.endView = endView
        }
    }

    /**
     * 设置数据集合
     * @param str 文本集合
     */
    fun setData(vararg str: String) {
        for (index in str.indices) {
            if (index < childCount && getChildAt(index) is TextView) {
                (getChildAt(index) as TextView).text = str[index]
            }
        }
    }

    fun setEndData(isCheck: Boolean) {
        if (endView is CheckBox) {
            (endView as CheckBox).isChecked = isCheck
        }
    }
}