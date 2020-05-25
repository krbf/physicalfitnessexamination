package com.example.physicalfitnessexamination.view.excel

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.bean.KbiProjectPersonCountBean
import com.example.physicalfitnessexamination.databinding.VProPersonCountBinding

/**
 * 单项考核人员人数设置组件
 * Created by chenzhiyuan On 2020/5/23
 */
class KbiProjectPersonCountView : ConstraintLayout {

    lateinit var binding: VProPersonCountBinding
    private lateinit var bean: KbiProjectPersonCountBean

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
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.v_pro_person_count, this, true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            focusable = FOCUSABLE
        }
        isFocusableInTouchMode = true

        bean = KbiProjectPersonCountBean()

        binding.bean = bean
    }
}