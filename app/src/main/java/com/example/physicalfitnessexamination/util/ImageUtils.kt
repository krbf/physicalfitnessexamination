package com.example.physicalfitnessexamination.util

import com.example.physicalfitnessexamination.Constants

/**
 * 图片加载共用方法集合
 * Created by chenzhiyuan On 2020/5/23
 */
object ImageUtils {
    /**
     * 获取图片长链接
     * @return 长链接
     */
    fun getCompleteUrl(shortPath: String): String = Constants.IP + shortPath
}