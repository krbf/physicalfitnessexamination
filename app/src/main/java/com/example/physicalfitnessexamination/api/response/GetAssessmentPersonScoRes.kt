package com.example.physicalfitnessexamination.api.response

import com.example.physicalfitnessexamination.common.annotation.AllOpenAndNoArgAnnotation

/**
 * Created by chenzhiyuan On 2020/5/26
 */
@AllOpenAndNoArgAnnotation
data class GetAssessmentPersonScoRes(
        var RANK: String?,
        var SCORE: String?
)