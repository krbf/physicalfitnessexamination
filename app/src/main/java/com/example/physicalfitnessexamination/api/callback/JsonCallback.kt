package com.example.physicalfitnessexamination.api.callback

import com.example.physicalfitnessexamination.api.response.ApiResponse
import com.example.physicalfitnessexamination.base.MyApplication
import com.czy.module_common.utils.JacksonMapper
import com.example.physicalfitnessexamination.util.toast
import com.lzy.okgo.callback.AbsCallback
import com.lzy.okgo.model.Response
import com.orhanobut.logger.Logger
import java.lang.reflect.ParameterizedType

/**
 * 请求返回json处理回调
 * Created by chenzhiyuan On 2018/12/28
 */
abstract class JsonCallback<T, R> : AbsCallback<T>() where T : ApiResponse<R>, R : Any {

    val type by lazy {
        (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
    }

//    private var code: Int? = null

    /**
     * 拿到响应后，将数据转换成需要的格式，子线程中执行，可以是耗时操作
     *
     * @param response 需要转换的对象
     * @return 转换后的结果
     * @throws Exception 转换过程发生的异常
     */
    override fun convertResponse(response: okhttp3.Response?): T {
        response?.body()?.string()?.let {
            Logger.d(it)

            val apiResponse = JacksonMapper.mInstance.readValue<T>(
                    it,
                    JacksonMapper.mInstance.typeFactory.constructType(type)
            )

            when {
                !apiResponse.success -> throw Exception("错误信息:${apiResponse.msg}")
                else -> return apiResponse
            }
        } ?: throw Exception("信息异常")
    }

    override fun onError(response: Response<T>?) {
        response?.exception?.let {
            Logger.e(response.exception.message.toString())
            MyApplication.getContext().toast(response.exception.message.toString())
        }

//        code?.let {
//            when (it) {
//                //没有登录的场合
//                1000 -> {
//                    LoginActivity.startInstant(MyApplication.getContext())
//                    //清理本地数据库
//                    DbManager.daoSession.userTableBeanDao.deleteAll()
//                }
//            }
//        }

    }
}
