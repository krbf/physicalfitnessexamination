package com.example.physicalfitnessexamination.bean

import android.databinding.ObservableField

/**
 * 单个训练项目的创建人员人数
 * Created by chenzhiyuan On 2020/5/23
 */
class KbiProjectPersonCountBean {

    //项目id
    var sid: String = ""

    //项目名称
    val proName = ObservableField<String>()

    //人员类型1的名称
    val type1Name = ObservableField<String>()

    //人员类型2的名称
    val type2Name = ObservableField<String>()

    //人员类型1的人数
    val type1PerCount = ObservableField<String>()

    //人员类型2的人数
    val type2PerCount = ObservableField<String>()
}