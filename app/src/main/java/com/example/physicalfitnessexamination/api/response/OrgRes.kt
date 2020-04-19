package com.example.physicalfitnessexamination.api.response

import com.example.physicalfitnessexamination.common.annotation.AllOpenAndNoArgAnnotation

/**
 * 机构 对象
 * Created by chenzhiyuan On 2020/4/18
 */
@AllOpenAndNoArgAnnotation
data class OrgRes(
    var ORG_ID: String?,
    var ORG_NAME: String?
)