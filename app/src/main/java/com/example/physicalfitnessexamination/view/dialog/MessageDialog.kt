package com.example.physicalfitnessexamination.view.dialog

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.view.RosterDialogFragment
import kotlinx.android.synthetic.main.dialog_auth_code_loading.*

/**
 * 验证码获取中\登录成功 提示弹框
 * Created by chenzhiyuan On 2019/3/7
 */
class MessageDialog : DialogFragment() {

    companion object {
        const val MSG_KEY = "msg_key"

        /**
         * 实例化方法
         * @param msg 文案
         */
        @JvmStatic
        fun newInstance(msg: String? = "加载中……") =
                MessageDialog().apply {
                    arguments = Bundle().apply {
                        msg?.let { putString(MSG_KEY, it) }
                    }
                }
    }

    private var msgStr = ""

    private val rotateAnimation by lazy {
        RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
                .apply {
                    this.duration = 1200
                    this.repeatCount = Animation.INFINITE
                }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_auth_code_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_loading_circle.setImageResource(R.mipmap.ic_loading_circle)
        iv_loading_circle.startAnimation(rotateAnimation)
        tv_msg.text = msgStr
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        msgStr = arguments?.getString(MSG_KEY).toString()
        setStyle(STYLE_NORMAL, R.style.Dialog_FullScreen)
        isCancelable = false
    }

    override fun onStart() {
        super.onStart()
        dialog!!.window?.setBackgroundDrawableResource(R.color.colorTransparency)
    }
}