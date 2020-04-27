package com.example.physicalfitnessexamination.api.response

import com.example.physicalfitnessexamination.common.annotation.AllOpenAndNoArgAnnotation

/**
 * Created by chenzhiyuan On 2020/4/27
 */
@AllOpenAndNoArgAnnotation
data class RemarkForAssessmentRes(
        var CREATETIME: String,
        var ORG_NAME: String,
        var REMARK: String
)