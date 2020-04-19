package com.example.physicalfitnessexamination.view.excel

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.api.response.OrgRes
import com.example.physicalfitnessexamination.bean.KbiTimeCfgType2Bean
import kotlinx.android.synthetic.main.v_kbi_time_config2.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 时间安排（到站）View
 * Created by chenzhiyuan On 2020/4/17
 */
class KbiTimeConfig2View : LinearLayout {

    companion object {
        val sdf = SimpleDateFormat("MM月dd日", Locale.CHINESE)
        val isMorningArray = arrayOf("上午", "下午")
    }

    private var bean: KbiTimeCfgType2Bean? = null
        set(value) {
            field = value

            tv_date.text = sdf.format(value?.date)
            initSp()
        }

    private var orgResList: ArrayList<OrgRes>? = null

    private fun initSp() {
        val isMorning = bean?.isMorning
        sp_isMorning.setSpinner(isMorningArray,
                onCheckListener = object : SpinnerParentView.OnCheckListener<String> {
                    override fun onConfirmAndChangeListener(view: SpinnerParentView<String>, selectBeanList: List<String>) {
                        bean?.isMorning = selectBeanList.isNotEmpty() && selectBeanList.first() == "上午"
                    }
                },
                isRadio = true,
                defaultIndex = if (isMorning != null) {
                    mutableSetOf(if (isMorning) 0 else 1)
                } else emptySet())

        run {
            var defaultIndex = -1
            if (bean?.org1 != null) {
                val indexBean = orgResList?.find {
                    it.ORG_ID == bean?.org1?.ORG_ID
                }
                defaultIndex = orgResList?.indexOf(indexBean) ?: -1
            }
            sp_group1.setSpinner(orgResList?.toTypedArray() ?: emptyArray(),
                    listener = object : SpinnerParentView.OnGetStrListener<OrgRes> {
                        override fun getStr(bean: OrgRes): String {
                            return bean.ORG_NAME ?: ""
                        }
                    },
                    onCheckListener = object : SpinnerParentView.OnCheckListener<OrgRes> {
                        override fun onConfirmAndChangeListener(view: SpinnerParentView<OrgRes>, selectBeanList: List<OrgRes>) {
                            if (selectBeanList.isNotEmpty()) {
                                bean?.org1 = selectBeanList.first()
                            }
                        }
                    },
                    isRadio = true,
                    defaultIndex = if (defaultIndex != -1) {
                        mutableSetOf(defaultIndex)
                    } else emptySet())
        }

        run {
            var defaultIndex = -1
            if (bean?.org2 != null) {
                val indexBean = orgResList?.find {
                    it.ORG_ID == bean?.org2?.ORG_ID
                }
                defaultIndex = orgResList?.indexOf(indexBean) ?: -1
            }
            sp_group2.setSpinner(orgResList?.toTypedArray() ?: emptyArray(),
                    listener = object : SpinnerParentView.OnGetStrListener<OrgRes> {
                        override fun getStr(bean: OrgRes): String {
                            return bean.ORG_NAME ?: ""
                        }
                    },
                    onCheckListener = object : SpinnerParentView.OnCheckListener<OrgRes> {
                        override fun onConfirmAndChangeListener(view: SpinnerParentView<OrgRes>, selectBeanList: List<OrgRes>) {
                            if (selectBeanList.isNotEmpty()) {
                                bean?.org2 = selectBeanList.first()
                            }
                        }
                    },
                    isRadio = true,
                    defaultIndex = if (defaultIndex != -1) {
                        mutableSetOf(defaultIndex)
                    } else emptySet())
        }

        run {
            var defaultIndex = -1
            if (bean?.org3 != null) {
                val indexBean = orgResList?.find {
                    it.ORG_ID == bean?.org3?.ORG_ID
                }
                defaultIndex = orgResList?.indexOf(indexBean) ?: -1
            }
            sp_group3.setSpinner(orgResList?.toTypedArray() ?: emptyArray(),
                    listener = object : SpinnerParentView.OnGetStrListener<OrgRes> {
                        override fun getStr(bean: OrgRes): String {
                            return bean.ORG_NAME ?: ""
                        }
                    },
                    onCheckListener = object : SpinnerParentView.OnCheckListener<OrgRes> {
                        override fun onConfirmAndChangeListener(view: SpinnerParentView<OrgRes>, selectBeanList: List<OrgRes>) {
                            if (selectBeanList.isNotEmpty()) {
                                bean?.org3 = selectBeanList.first()
                            }
                        }
                    },
                    isRadio = true,
                    defaultIndex = if (defaultIndex != -1) {
                        mutableSetOf(defaultIndex)
                    } else emptySet())
        }
    }

    private var onRemoveListener: OnRemoveListener<KbiTimeCfgType2Bean>? = null

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
        LayoutInflater.from(context).inflate(R.layout.v_kbi_time_config2, this)
        orientation = VERTICAL

        iv_remove.setOnClickListener {
            bean?.let { it1 -> onRemoveListener?.onRemove(it1) }
        }

        sp_isMorning.let {
            it.setSimpleUiMode(true)
            it.setName("时间")
        }

        sp_group1.let {
            it.setSimpleUiMode(true)
            it.setName("选择考核组")
        }

        sp_group2.let {
            it.setSimpleUiMode(true)
            it.setName("选择考核组")
        }

        sp_group3.let {
            it.setSimpleUiMode(true)
            it.setName("选择考核组")
        }
    }

    fun setData(bean: KbiTimeCfgType2Bean, orgResList: ArrayList<OrgRes>, onRemoveListener: OnRemoveListener<KbiTimeCfgType2Bean>) {
        this.orgResList = orgResList
        this.bean = bean
        this.onRemoveListener = onRemoveListener
    }

}