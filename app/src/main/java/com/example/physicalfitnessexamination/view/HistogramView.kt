package com.example.physicalfitnessexamination.view

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.bean.UnitCreditsPkBean
import kotlinx.android.synthetic.main.v_histogram_item.view.*

/**
 * 柱状图 自定义动画控件
 * Created by chenzhiyuan On 2020/5/10
 */
class HistogramView : LinearLayout {

    /**
     * 数据集合
     */
    var dataList: MutableList<UnitCreditsPkBean> = mutableListOf()
        set(value) {
            field = value

            removeAllViews()

            var firstScore = 0
            value.forEach {bean->
                if (firstScore < bean.score){
                    firstScore = bean.score
                }
            }

            value.forEach {bean->
                addView(ItemView(context).apply {
                    orgName = bean.orG_NAME
                    maxExp = firstScore
                    factExp = bean.score
                })
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
        orientation = VERTICAL
        dataList = arrayListOf()
    }

    /**
     * 开始播放动画
     */
    fun startAnimation() {
        for (childIndex in 0 until childCount) {
            getChildAt(childIndex).let { cv ->
                if (cv is ItemView) {
                    cv.startAnimation()
                }
            }
        }
    }

    /**
     * 子ItemView
     */
    class ItemView : LinearLayout {

        companion object {
            /**
             * 默认倍数
             */
            const val DEF_MULTIPLE = 1000

            /**
             * 动画时长
             */
            const val VALUE_ANIMATOR_DURATION = 1500L
        }

        /**
         * 组织名称
         */
        var orgName: String = ""
            set(value) {
                field = value
                tv_orgName.text = value
            }

        /**
         * 进度条最大数值
         */
        var maxExp: Int = 0
            set(value) {
                field = value
                progressBar_exp.max = value * DEF_MULTIPLE
            }

        /**
         * 实际数值
         */
        var factExp: Int = 0
            set(value) {
                field = value
                annotation = ValueAnimator.ofInt(0, value * DEF_MULTIPLE).apply {
                    duration = VALUE_ANIMATOR_DURATION
                    addUpdateListener { an ->
                        exp = an.animatedValue as Int
                    }
                }
            }

        /**
         * 当前进度值
         */
        private var exp: Int = 0
            set(value) {
                field = value
                tv_currentExp.text = (value / DEF_MULTIPLE).toString()
                progressBar_exp.progress = value
            }

        private var annotation: ValueAnimator? = null


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
            LayoutInflater.from(context).inflate(R.layout.v_histogram_item, this, true)
        }

        /**
         * 开始动画
         */
        fun startAnimation() {
            annotation?.start()
        }
    }
}