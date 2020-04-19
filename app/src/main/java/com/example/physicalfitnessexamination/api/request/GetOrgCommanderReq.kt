package com.example.physicalfitnessexamination.api.request

/**
 * 人员列表请求体
 * Created by chenzhiyuan On 2020/4/15
 *
 * @param org_id 机构id，默认传登录人
 * @param type null-全部 1-干部 2-战士
 * @param aid 参考计划id，若传这个字段。则会剔除此次计划以请假人员
 */
data class GetOrgCommanderReq(val org_id: String, val type: Int?, val aid: String?) : FTRequest()