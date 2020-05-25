package com.example.physicalfitnessexamination.view.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.SparseIntArray
import android.view.*
import android.widget.LinearLayout
import com.czy.module_common.utils.JacksonMapper
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.api.RequestManager
import com.example.physicalfitnessexamination.api.request.SetPersonScoreForAssessmentReq
import com.example.physicalfitnessexamination.common.annotation.AllOpenAndNoArgAnnotation
import com.example.physicalfitnessexamination.util.displayHeight
import com.example.physicalfitnessexamination.util.displayWidth
import com.example.physicalfitnessexamination.util.toast
import com.example.physicalfitnessexamination.view.PointsDifferView
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import kotlinx.android.synthetic.main.dialog_points_differ_setting.*
import kotlinx.android.synthetic.main.v_points_differ_setting_item.view.*

/**
 * 设置积分分差设置Dialog
 * Created by chenzhiyuan On 2020/5/24
 */
class PointsDifferSettingDialog() : DialogFragment() {

    /**
     * 实例化方法
     * @param id 考核id
     * @param gw 岗位
     * @param projectId
     * @param personCount
     */
    @SuppressLint("ValidFragment")
    constructor(id: String, gw: String, projectId: String, personCount: Int) : this() {
        this.id = id
        this.gw = gw
        this.projectId = projectId
        this.personCount = personCount
    }

    private lateinit var id: String
    private lateinit var gw: String
    private lateinit var projectId: String
    private var personCount: Int = 0

    //积分分差 <endIndex,score>
    private val scoreSet = SparseIntArray()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_points_differ_setting, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pd_point.maxCount = personCount
        pd_point.onPointsDifferListener = object : PointsDifferView.OnPointsDifferListener {
            override fun onChange(divisionList: List<PointsDifferView.PointBean>) {
                ll_pointContent.removeAllViews()
                scoreSet.clear()

                divisionList.forEach {
                    scoreSet.put(it.indexCode, 0)
                }
                scoreSet.put(pd_point.maxCount, 0)

                var startIndex = 1
                for (index in 0 until scoreSet.size()) {
                    ll_pointContent.addView(
                            this@PointsDifferSettingDialog.context?.let { con ->
                                ItemView(con).apply {

                                    addTextChangedListener("$startIndex - ${scoreSet.keyAt(index)}名分差：", object : TextWatcher {
                                        override fun afterTextChanged(s: Editable?) {
                                            val score = try {
                                                s.toString().toInt()
                                            } catch (e: NumberFormatException) {
                                                0
                                            }
                                            scoreSet.put(scoreSet.keyAt(index), score)
                                        }

                                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                                        }

                                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                        }
                                    })
                                }
                            }
                    )
                    startIndex = scoreSet.keyAt(index)
                }
            }
        }

        tv_cancel.setOnClickListener {
            dismiss()
        }
        tv_check.setOnClickListener {
            if (et_score.text.toString().trim().isEmpty()) {
                context.toast("请输出最高分")
                return@setOnClickListener
            }

            //接口所需参数
            val plists = mutableListOf<Plist>()
            //总分
            val totalScore: Int = et_score.text.toString().toInt()
            //总人数
            personCount
            //积分分差
            scoreSet
            //与第一名相差的分值
            var cutScore = 0

            for (rank in 1..personCount) {
                val plist = Plist(rank.toString(), (totalScore - cutScore).toString())
                plists.add(plist)
                for (setIndex in 0 until scoreSet.size()) {
                    val endIndex = scoreSet.keyAt(setIndex)
                    if (rank < endIndex) {
                        //找到当前名次所属的分差 左开右闭
                        cutScore += scoreSet[endIndex]
                        break
                    }
                }
            }

            val bean = PointsDifferBean(id, gw, plists, projectId)

            RequestManager.setPersonScoreForAssessment(context,
                    SetPersonScoreForAssessmentReq(JacksonMapper.mInstance.writeValueAsString(bean)),
                    object : StringCallback() {
                        override fun onSuccess(response: Response<String>?) {
                            val bean = JacksonMapper.mInstance.readValue(response?.body(), ResBean::class.java)
                            if (bean?.success == true) {
                                context.toast("积分分差设置成功")
                                dismiss()
                            } else {
                                context.toast("积分分差设置失败，${bean.msg}")
                            }
                        }
                    }
            )
        }
    }

    override fun onStart() {
        super.onStart()
        dialog.window?.let { window ->
//            window.setDimAmount()
            window.setGravity(Gravity.CENTER)

            val params = window.attributes
            params.width = context?.displayWidth?.times(0.7)?.toInt()
                    ?: WindowManager.LayoutParams.WRAP_CONTENT
            params.height = context?.displayHeight?.times(0.9)?.toInt()
                    ?: WindowManager.LayoutParams.WRAP_CONTENT
            window.attributes = params
        }
    }

    class ItemView : LinearLayout {

        constructor(context: Context) : super(context) {
            init(null, 0)
        }

        constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
            init(attrs, 0)
        }

        constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
            init(attrs, defStyle)
        }

        private fun init(attrs: AttributeSet?, defStyle: Int) {
            LayoutInflater.from(context).inflate(R.layout.v_points_differ_setting_item, this)
        }

        fun getScoreDiffer(): Int = ed_score.text.toString().toInt()

        fun addTextChangedListener(durationStr: String, textWatcher: TextWatcher) {
            tvDuration.text = durationStr
            ed_score.addTextChangedListener(textWatcher)
        }
    }

    data class PointsDifferBean(
            var aid: String?,
            var gw: String?,
            var plist: List<Plist>?,
            var sid: String?
    )

    data class Plist(
            var rank: String?,
            var score: String?
    )

    @AllOpenAndNoArgAnnotation
    data class ResBean(
            var msg: String?,
            var success: Boolean?
    )
}