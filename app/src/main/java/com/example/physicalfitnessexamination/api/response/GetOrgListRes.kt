package com.example.physicalfitnessexamination.api.response

import com.example.physicalfitnessexamination.common.annotation.AllOpenAndNoArgAnnotation

@AllOpenAndNoArgAnnotation
data class GetOrgListRes(
        var ORG_ID: String,
        var ORG_NAME: String
)