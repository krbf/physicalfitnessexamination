package com.czy.module_common.utils

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature

/**
 * Jackson 工具单例
 * Created by chenzhiyuan On 2018/12/27
 */
object JacksonMapper {
    val mInstance: ObjectMapper by lazy {
        ObjectMapper()
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)//如果是空对象的时候,不抛异常
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)//反序列化的时候如果多了其他属性,不抛出异常
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)//大小写脱敏  这是由于接口不规范导致的 一般不应该脱敏
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
    }
}