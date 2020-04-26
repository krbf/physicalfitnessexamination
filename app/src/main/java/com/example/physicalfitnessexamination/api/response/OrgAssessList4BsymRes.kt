package com.example.physicalfitnessexamination.api.response

import com.example.physicalfitnessexamination.common.annotation.AllOpenAndNoArgAnnotation

@AllOpenAndNoArgAnnotation
data class OrgAssessList4BsymRes(
        var CREATETIME: String,
        var ID: String,
        var NAME: String,
        var ORG_NAME: String?,
        var SCORE: Int = 0
)