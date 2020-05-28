package com.example.physicalfitnessexamination.bean

import android.databinding.ObservableField
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.base.MyApplication
import com.example.physicalfitnessexamination.page.kbi.CreateKbiDataManager

/**
 * 单个训练项目的创建人员人数
 * Created by chenzhiyuan On 2020/5/23
 */
class KbiProjectPersonCountBean {

    //项目id
    var sid: String = ""

    //参考单位
    var type: String = ""

    //是否是消防员
    var isXiaoFangYuan = false

    //显示参考单位集合
    var orgList: Array<out String>? = null
        set(value) {
            field = value
            val orgArray = MyApplication.getContext().resources.getStringArray(R.array.joinEvaOrg)
            when (value?.size) {
                0 -> {
                    ctVisible1.set(false)
                    ctVisible2.set(false)
                    ctVisible3.set(false)
                    ctVisible4.set(false)
                }
                1 -> {
                    //参考单位
                    val orgStr = value.first()

                    when (orgStr) {
                        "支队机关" -> {
                            ctVisible1.set(true)
                            ctVisible2.set(true)
                            ctVisible3.set(false)
                            ctVisible4.set(false)
                            type1Name.set("支队领导")
                            type2Name.set("支队指挥员")
                        }
                        "大队机关" -> {
                            ctVisible1.set(true)
                            ctVisible2.set(true)
                            ctVisible3.set(false)
                            ctVisible4.set(false)
                            type1Name.set("大队领导")
                            type2Name.set("大队指挥员")
                        }
                        "应急消防站" -> {
                            ctVisible1.set(true)
                            ctVisible2.set(false)
                            ctVisible3.set(false)
                            ctVisible4.set(false)
                            type1Name.set(if (!isXiaoFangYuan) "消防站指挥员" else "消防站消防员")
                        }
                    }
                }
                else -> {
                    ctVisible1.set(true)
                    ctVisible2.set(true)
                    ctVisible3.set(true)
                    ctVisible4.set(true)

                    value?.forEachIndexed { index, s ->
                        when (index) {
                            0 -> {
                                when (s) {
                                    orgArray[0] -> {
                                        //支队机关
                                        type1Name.set("支队领导")
                                        type2Name.set("支队指挥员")
                                    }
                                    orgArray[1] -> {
                                        //大队机关
                                        type1Name.set("大队领导")
                                        type2Name.set("大队指挥员")
                                    }
                                    orgArray[2] -> {
                                        //应急消防站
                                        type1Name.set("消防站指挥员")
                                        type2Name.set("消防站消防员")
                                    }
                                }
                            }
                            1 -> {
                                when (s) {
                                    orgArray[0] -> {
                                        //支队机关
                                        type3Name.set("支队领导")
                                        type4Name.set("支队指挥员")
                                    }
                                    orgArray[1] -> {
                                        //大队机关
                                        type3Name.set("大队领导")
                                        type4Name.set("大队指挥员")
                                    }
                                    orgArray[2] -> {
                                        //应急消防站
                                        type3Name.set("消防站指挥员")
                                        type4Name.set("消防站消防员")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    //项目名称
    val proName = ObservableField<String>()

    val type1Name = ObservableField("")
    val type2Name = ObservableField("")
    val type3Name = ObservableField("")
    val type4Name = ObservableField("")

    val type1PerCount = ObservableField("0")
    val type2PerCount = ObservableField("0")
    val type3PerCount = ObservableField("0")
    val type4PerCount = ObservableField("0")

    val ctVisible1 = ObservableField(false)
    val ctVisible2 = ObservableField(false)
    val ctVisible3 = ObservableField(false)
    val ctVisible4 = ObservableField(false)

    fun getSidList(): List<CreateKbiDataManager.PointsDiff> {
        val list = mutableListOf<CreateKbiDataManager.PointsDiff>()

        if (ctVisible1.get() == true) {
            list.add(CreateKbiDataManager.PointsDiff(sid, type1Name.get() ?: "",
                    type1PerCount.get() ?: "0"))
        }
        if (ctVisible2.get() == true) {
            list.add(CreateKbiDataManager.PointsDiff(sid, type2Name.get() ?: "",
                    type2PerCount.get() ?: "0"))
        }
        if (ctVisible3.get() == true) {
            list.add(CreateKbiDataManager.PointsDiff(sid, type3Name.get() ?: "",
                    type3PerCount.get() ?: "0"))
        }
        if (ctVisible4.get() == true) {
            list.add(CreateKbiDataManager.PointsDiff(sid, type4Name.get() ?: "",
                    type4PerCount.get() ?: "0"))
        }

        return list
    }
}