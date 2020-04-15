package com.example.physicalfitnessexamination.page.kbi

import com.example.physicalfitnessexamination.api.request.SaveAssessmentReq
import com.example.physicalfitnessexamination.bean.CreateKbiDataBean

/**
 * 创建考核数据管理类
 * Created by chenzhiyuan On 2020/4/14
 */
object CreateKbiDataManager {
    /**
     * 数据记录类
     */
    var kbiBean: CreateKbiDataBean? = null

//    /**
//     * 接口请求体
//     */
//    private var request: SaveAssessmentReq? = null

    /**
     * 创建新的kbi实体对象
     */
    fun createNewKbi() {
        kbiBean = CreateKbiDataBean()
    }

    /**
     * 清除kbi实体
     */
    fun clearKbi() {
        kbiBean = null
    }

    fun getFinalReq(): SaveAssessmentReq {
        TODO("具体的数据到接口要求的转换实现")
        return SaveAssessmentReq()
    }
}