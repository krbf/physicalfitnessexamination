package com.example.physicalfitnessexamination.api.request

/**
 * 获取考核项目列表 请求体
 * Created by chenzhiyuan On 2020/4/12
 *
 * @param type 0-消防站 1-机关
 * @param org_id 登录人机构id
 */
data class GetAssessmentObjectReq(val type: Int, val org_id: String) : FTRequest()