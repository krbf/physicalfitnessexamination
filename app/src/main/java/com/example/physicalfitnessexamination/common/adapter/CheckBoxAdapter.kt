package com.example.physicalfitnessexamination.common.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckedTextView
import com.example.physicalfitnessexamination.R

/**
 * CheckBox的适配器
 * Created by chenzhiyuan On 2020/4/11
 */
class CheckBoxAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var data: ArrayList<String>? = null

    /**
     * 选中的下标
     */
    private lateinit var selectSet: MutableSet<Int>

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return CheckViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.v_checktextview, p0, false) as CheckedTextView)
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        if (p0 is CheckViewHolder) {
            p0.checkBox.let {
                it.text = data?.get(p1) ?: ""
                it.isChecked = selectSet.contains(p1)
                it.setOnClickListener { v ->
                    (v as CheckedTextView).let { ct ->
                        ct.toggle()
                        if (ct.isChecked) {
                            selectSet.add(p1)
                        } else {
                            selectSet.remove(p1)
                        }
                    }

                }
            }
        }
    }

    fun setListData(data: ArrayList<String>, selectSet: MutableSet<Int>) {
        this.data = data
        this.selectSet = selectSet
        notifyDataSetChanged()
    }

    fun getSelectSet(): Set<Int> = selectSet

    fun getData(): ArrayList<String>? = data

    class CheckViewHolder(itemView: CheckedTextView) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckedTextView = itemView
    }
}