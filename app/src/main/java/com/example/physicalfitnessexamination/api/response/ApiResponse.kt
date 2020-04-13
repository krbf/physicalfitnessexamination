package com.example.physicalfitnessexamination.api.response

class ApiResponse<T> {
    var success: Boolean = false
    var msg: String? = null
    var `data`: T? = null
}