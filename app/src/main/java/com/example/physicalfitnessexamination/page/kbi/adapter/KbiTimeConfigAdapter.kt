package com.example.physicalfitnessexamination.page.kbi.adapter

import android.app.DatePickerDialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.api.response.OrgRes
import com.example.physicalfitnessexamination.bean.KbiTimeCfgBean
import com.example.physicalfitnessexamination.bean.KbiTimeCfgType1Bean
import com.example.physicalfitnessexamination.bean.KbiTimeCfgType2Bean
import com.example.physicalfitnessexamination.bean.TimeCfgType1DetailBean
import com.example.physicalfitnessexamination.view.excel.KbiTimeConfig1View
import com.example.physicalfitnessexamination.view.excel.KbiTimeConfig2View
import com.example.physicalfitnessexamination.view.excel.OnRemoveListener
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.v_kbi_org_group_add.view.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * 新建考核-时间安排 list适配器
 * Created by chenzhiyuan On 2020/4/17
 *
 * @param isToPlaceEva 是否是到站考核
 */
class KbiTimeConfigAdapter(private val isToPlaceEva: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val DATE_TYPE = 0
        const val TABLE_HEAD_TYPE = 1
        const val ADD_DATE_TYPE = 2
    }

    //数据源
    lateinit var timeTypeList: ArrayList<KbiTimeCfgBean>

    //大队集合
    var orgResList: ArrayList<OrgRes> = ArrayList<OrgRes>()

    private var datePickerDialog: DatePickerDialog? = null

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            itemCount - 1 -> ADD_DATE_TYPE
            0 -> if (isToPlaceEva) TABLE_HEAD_TYPE else DATE_TYPE
            else -> DATE_TYPE
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return when (p1) {
            DATE_TYPE -> DateViewHolder(
                    if (isToPlaceEva) KbiTimeConfig2View(p0.context) else KbiTimeConfig1View(p0.context)
            )
            ADD_DATE_TYPE -> AddDateViewHolder(
                    LayoutInflater.from(p0.context).inflate(R.layout.v_kbi_org_group_add, p0, false)
            )
            else -> TableHeadViewHolder(
                    LayoutInflater.from(p0.context).inflate(R.layout.v_kbi_time_config2_head, p0, false)
            )
        }
    }

    override fun getItemCount(): Int = timeTypeList.size + 1 + (if (isToPlaceEva) 1 else 0)

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        when (p0) {
            is DateViewHolder -> {
                if (!isToPlaceEva) {
                    (timeTypeList[p1] as KbiTimeCfgType1Bean).let { tb ->
                        p0.v1?.setData(tb, object : OnRemoveListener<KbiTimeCfgType1Bean> {
                            override fun onRemove(detailBean: KbiTimeCfgType1Bean) {
                                timeTypeList.remove(detailBean)
                                notifyItemRemoved(p1)
                                notifyItemRangeChanged(p1, itemCount - p1)
                            }
                        })
                    }
                } else {
                    (timeTypeList[p1 - 1] as KbiTimeCfgType2Bean).let { tb ->
                        p0.v2?.setData(tb, orgResList, object : OnRemoveListener<KbiTimeCfgType2Bean> {
                            override fun onRemove(detailBean: KbiTimeCfgType2Bean) {
                                timeTypeList.remove(detailBean)
                                notifyItemRemoved(p1)
                                notifyItemRangeChanged(p1, itemCount - p1)
                            }
                        })
                    }
                }
            }
            is AddDateViewHolder -> {
                p0.addDateBtn.text = "+  添加日期"
                p0.addDateBtn.setOnClickListener {
                    getDatePickerDialog(p0.addDateBtn.context).show()
                }
            }
        }
    }

    /**
     * 日期选择Dialog
     */
    private fun getDatePickerDialog(mContext: Context): DatePickerDialog {
        if (datePickerDialog == null) {
            val cal = Calendar.getInstance()
            datePickerDialog = DatePickerDialog(mContext, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert,
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        try {
                            if (isToPlaceEva) {
                                timeTypeList.add(KbiTimeCfgType2Bean(
                                        Calendar.getInstance().apply {
                                            set(year, month, dayOfMonth)
                                        }.time
                                ))
                            } else {
                                timeTypeList.add(KbiTimeCfgType1Bean(
                                        Calendar.getInstance().apply {
                                            set(year, month, dayOfMonth)
                                        },
                                        ArrayList()).apply {
                                    detailBean.add(TimeCfgType1DetailBean())
                                })
                            }
                            notifyItemInserted(itemCount - 1)
                            notifyItemChanged(itemCount)
                        } catch (e: Exception) {
                            Logger.e(e, "insert")
                        }
                    }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE))
        }
        return datePickerDialog as DatePickerDialog
    }

    class DateViewHolder(view: ViewGroup) : RecyclerView.ViewHolder(view) {
        var v1: KbiTimeConfig1View? = null
        var v2: KbiTimeConfig2View? = null

        init {
            if (view is KbiTimeConfig1View) {
                v1 = view.apply {
                    val lp = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
                    layoutParams = lp
                }
            } else if (view is KbiTimeConfig2View) {
                v2 = view.apply {
                    val lp = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
                    layoutParams = lp
                }
            }
        }
    }

    class AddDateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val addDateBtn: TextView = view.tv_addGroup
    }

    class TableHeadViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val head: View = view
    }
}