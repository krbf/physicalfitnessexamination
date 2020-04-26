package com.example.physicalfitnessexamination.api.response

import com.example.physicalfitnessexamination.common.annotation.AllOpenAndNoArgAnnotation

/**
 * Created by chenzhiyuan On 2020/4/27
 */
@AllOpenAndNoArgAnnotation
data class PersonAssessList4Res(
        var ACHIEVEMENT: String?,
        var CREATETIME: String?,
        var NAME: String,
        var ORGNAME: String?,
        var PHOTO: String?,
        var USERNAME: String?
)