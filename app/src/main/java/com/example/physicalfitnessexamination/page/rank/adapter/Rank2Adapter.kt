package com.example.physicalfitnessexamination.page.rank.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.api.response.OrgAssessList4BsymRes
import kotlinx.android.synthetic.main.v_rank_org_item.view.*

/**
 * Created by chenzhiyuan On 2020/4/26
 */
class Rank2Adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val dataList = mutableListOf<OrgAssessList4BsymRes>()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(
                LayoutInflater.from(p0.context).inflate(R.layout.v_rank_org_item, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        if (p0 is ItemViewHolder) {
            dataList[p1].let { bean ->
                p0.tvOrgName.text = bean.ORG_NAME
                p0.tvScore.text = bean.SCORE.toString()
                p0.tvKbiName.text = bean.NAME
                p0.tvTime.text = bean.CREATETIME
            }
        }
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvOrgName: TextView = view.tv_orgName
        val tvScore: TextView = view.tv_score
        val tvKbiName: TextView = view.tv_kbiName
        val tvTime: TextView = view.tv_time
    }
}