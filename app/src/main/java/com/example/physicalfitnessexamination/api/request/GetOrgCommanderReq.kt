package com.example.physicalfitnessexamination.api.request

/**
 * 人员列表请求体
 * Created by chenzhiyuan On 2020/4/15
 */
data class GetOrgCommanderReq(val org_id: String, val type: Int?) : FTRequest()