package com.example.physicalfitnessexamination.page.rank.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.api.response.PersonAssessList4Res
import com.czy.module_common.glide.ImageLoaderUtils
import kotlinx.android.synthetic.main.v_rank_person_assess_item.view.*

/**
 * Created by chenzhiyuan On 2020/4/26
 */
class Rank1Adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val dataList = mutableListOf<PersonAssessList4Res>()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(
                LayoutInflater.from(p0.context).inflate(R.layout.v_rank_person_assess_item, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        if (p0 is ItemViewHolder) {
            dataList[p1].let { bean ->
                p0.tvProName.text = bean.NAME
                ImageLoaderUtils.display(p0.ivPic.context, p0.ivPic, bean.PHOTO)
                p0.tvScore.text = bean.ACHIEVEMENT
                p0.tvPersonName.text = bean.USERNAME
                p0.tvOrgName.text = "单位：" + bean.ORGNAME
                p0.tvRecordTime.text = "记录时间：" + bean.CREATETIME
            }
        }
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvProName = view.tv_proName
        val ivPic = view.iv_pic
        val tvScore = view.tv_score
        val tvPersonName = view.tv_personName
        val tvOrgName = view.tv_orgName
        val tvRecordTime = view.tv_recordTime
    }
}