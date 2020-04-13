package com.example.physicalfitnessexamination.api.response

import com.example.physicalfitnessexamination.common.annotation.AllOpenAndNoArgAnnotation

/**
 * Created by chenzhiyuan On 2020/4/12
 */
@AllOpenAndNoArgAnnotation
data class TestProjectRes(
        /*
          "ID": "a7f9a89469344c9881b5965c719ba",
          "NAME": "男子3000m",
          "SEX": "0"
         */
        var ID: String,
        var NAME: String,
        var SEX: String?
)

