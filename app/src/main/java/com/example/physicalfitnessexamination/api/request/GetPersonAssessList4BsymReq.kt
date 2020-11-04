package com.example.physicalfitnessexamination.api.request

/**
 * @param org_id 组织id
 * @param type 1-机关 2-消防站
 * @param gw 岗位名称
 */
data class GetPersonAssessList4BsymReq(var type: Int, var gw: String, var org_id: String = "") : FTRequest()