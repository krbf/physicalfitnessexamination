package com.example.physicalfitnessexamination.api.request

/**
 * @param org_id 当前登录人org_id
 */
data class GetOrgListReq(val org_id: String) : FTRequest()