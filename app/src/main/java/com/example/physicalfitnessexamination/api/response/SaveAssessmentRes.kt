package com.example.physicalfitnessexamination.api.response

import com.example.physicalfitnessexamination.common.annotation.AllOpenAndNoArgAnnotation

/**
 * Created by chenzhiyuan On 2020/4/19
 */
@AllOpenAndNoArgAnnotation
data class SaveAssessmentRes(
        var id: String,
        var msg: String,
        var success: Boolean
)