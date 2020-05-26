package com.example.physicalfitnessexamination.api.request

/**
 * Created by chenzhiyuan On 2020/5/26
 *
 * @param gw 岗位
 * @param aid 计划
 * @param sid 项目id
 */
data class GetAssessmentPersonScoReq(val gw: String, val aid: String, val sid: String) : FTRequest()