package com.example.physicalfitnessexamination.bean

import com.example.physicalfitnessexamination.api.response.OrgRes
import com.example.physicalfitnessexamination.api.response.TestProjectRes
import java.util.*
import kotlin.collections.ArrayList

/**
 * 时间安排 数据模型
 * Created by chenzhiyuan On 2020/4/17
 */
open class KbiTimeCfgBean

/**
 * 集中考核、竞赛考核的时间安排数据模型
 */
data class KbiTimeCfgType1Bean(
        var date: Calendar,//日期
        val detailBean: ArrayList<TimeCfgType1DetailBean> = ArrayList()//具体时间段
) : KbiTimeCfgBean()

/**
 * 集中考核、竞赛考核具体时间段数据模型
 */
data class TimeCfgType1DetailBean(
        var startTime: Date? = null,//开始时间
        var endTime: Date? = null,//结束时间
        var contentStr: String? = null,//内容（输入的）
        var assessment: TestProjectRes? = null,//内容 （选择了考核项目）
        var position: String? = null,//岗位
        var convener: ArrayList<PersonBean> = ArrayList(),//召集人
        var starter: ArrayList<PersonBean> = ArrayList(),//发令员
        var timekeeper: ArrayList<PersonBean> = ArrayList()//计时员
)

/**
 * 到站考核的时间安排数据模型
 */
data class KbiTimeCfgType2Bean(
        var date: Date,//日期
        var isMorning: Boolean? = null,//是否是上午
        var org1: OrgRes? = null,//考核组1
        var org2: OrgRes? = null,//考核组2
        var org3: OrgRes? = null//考核组3
) : KbiTimeCfgBean()
