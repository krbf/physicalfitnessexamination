package com.example.physicalfitnessexamination.api.request

/**
 * @param type 1-支队 2-大队 3-消防站
 * @param gw 岗位名称
 */
data class GetPersonAssessList4BsymReq(val type: Int, val gw: String) : FTRequest()