package com.example.physicalfitnessexamination.api.request

/**
 * 人员列表请求体
 * Created by chenzhiyuan On 2020/4/15
 *
 * @param org_id 机构id，通过此id查询本级及下级单位
 * @param type “类型” 为数组形式  1-支队 2-大队 3-中队 ；传null-返回全部
 * @param hierarchy_restriction 0--只查询下级一层信息(用作级联查询)；1--查询到底
 */
data class GetOrgForAssessmentReq(val org_id: String, val type: String, val hierarchy_restriction: Int) : FTRequest()