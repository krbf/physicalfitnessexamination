package com.example.physicalfitnessexamination.page.kbi.adapter

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.api.response.AssessmentGroupRes
import com.example.physicalfitnessexamination.bean.KbiOrgGroupBean
import com.example.physicalfitnessexamination.bean.KbiOrgGroupIndexBean
import com.example.physicalfitnessexamination.bean.KbiOrgGroupTypeBean
import com.example.physicalfitnessexamination.bean.PersonBean
import com.example.physicalfitnessexamination.util.snack
import com.example.physicalfitnessexamination.view.RosterDialogFragment
import com.example.physicalfitnessexamination.view.excel.KbiOrgSmallGroupView
import kotlinx.android.synthetic.main.v_kbi_org_group_add.view.*
import kotlinx.android.synthetic.main.v_kbi_org_head.view.*

/**
 * Created by chenzhiyuan On 2020/4/13
 * @param isToPlaceKbi 是否到站考核
 */
class KbiOrgAdapter(private val isToPlaceKbi: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val ITEM_TYPE_HEAD = 0
        const val ITEM_TYPE_GROUP = 1
        const val ITEM_TYPE_ADD_BTN = 2
    }

    //<editor-fold desc="数据集合">
    private lateinit var groLeader: ArrayList<PersonBean>//组长集合
    private lateinit var depGroLeader: ArrayList<PersonBean>//副组长集合
    private lateinit var member: ArrayList<PersonBean>//成员集合
    private lateinit var smallGroups: ArrayList<KbiOrgGroupBean>//小组集合

    /**
     * 可选考核组集合
     */
    private val assessments by lazy {
        ArrayList<AssessmentGroupRes>()
    }
    //</editor-fold>

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ITEM_TYPE_HEAD
            itemCount - 1 -> ITEM_TYPE_ADD_BTN
            else -> ITEM_TYPE_GROUP
        }
    }

    override fun getItemCount(): Int = smallGroups.size + 2 //头部、底部添加按钮

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return when (p1) {
            ITEM_TYPE_HEAD -> HeadViewHolder(
                    LayoutInflater.from(p0.context).inflate(R.layout.v_kbi_org_head, p0, false)
            )
            ITEM_TYPE_GROUP -> GroupViewHolder(KbiOrgSmallGroupView(p0.context))
            else -> AddBtnViewHolder(
                    LayoutInflater.from(p0.context).inflate(R.layout.v_kbi_org_group_add, p0, false)
            )
        }
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        when (p0) {
            is HeadViewHolder -> {
                //组长
                p0.groupLeaderStr.setOnClickListener {
                    val dialogFragment = RosterDialogFragment.newInstance(1,
                            selectList = groLeader,
                            listener = object : RosterDialogFragment.OnCheckListener {
                                override fun checkOver(list: ArrayList<PersonBean>) {
                                    groLeader.clear()
                                    groLeader.addAll(list)
                                    p0.groupLeaderStr.text = appendLabel(list)
                                }
                            })
                    dialogFragment.show((p0.groupLeaderStr.context as AppCompatActivity).supportFragmentManager, "")
                }
                //副组长
                p0.duGroupLeaderStr.setOnClickListener {
                    val dialogFragment = RosterDialogFragment.newInstance(1,
                            selectList = depGroLeader,
                            listener = object : RosterDialogFragment.OnCheckListener {
                                override fun checkOver(list: ArrayList<PersonBean>) {
                                    depGroLeader.clear()
                                    depGroLeader.addAll(list)
                                    p0.duGroupLeaderStr.text = appendLabel(list)
                                }
                            })
                    dialogFragment.show((p0.duGroupLeaderStr.context as AppCompatActivity).supportFragmentManager, "")
                }
                //成员
                p0.memberStr.setOnClickListener {
                    val dialogFragment = RosterDialogFragment.newInstance(1,
                            selectList = member,
                            listener = object : RosterDialogFragment.OnCheckListener {
                                override fun checkOver(list: ArrayList<PersonBean>) {
                                    member.clear()
                                    member.addAll(list)
                                    p0.memberStr.text = appendLabel(list)
                                }
                            })
                    dialogFragment.show((p0.memberStr.context as AppCompatActivity).supportFragmentManager, "")
                }
            }
            is AddBtnViewHolder -> {
                p0.tv_addGroup.setOnClickListener {
                    if (!isToPlaceKbi && smallGroups.size >= assessments.size) {
                        p0.tv_addGroup.snack("无法添加更多考核小组")
                        return@setOnClickListener
                    }
                    smallGroups.add(if (isToPlaceKbi) {
                        KbiOrgGroupIndexBean()
                    } else {
                        KbiOrgGroupTypeBean()
                    })
                    notifyDataSetChanged()
                }
            }
            is GroupViewHolder -> {
                val listener = object : KbiOrgSmallGroupView.OnSmallGroupListener {
                    override fun onRemoveClick(bean: KbiOrgGroupBean) {
                        smallGroups.remove(bean)
                        notifyItemRemoved(p1)
                        notifyItemRangeChanged(p1, itemCount - p1)
                    }
                }
                smallGroups[p1 - 1].let { s ->
                    when (s) {
                        is KbiOrgGroupTypeBean -> p0.spinner.setData(s, assessments, listener)
                        is KbiOrgGroupIndexBean -> p0.spinner.setData(s, p1, listener)
                    }
                }
            }
        }
    }

    /**
     * 根据已选项拼接显示文案
     */
    private fun appendLabel(list: java.util.ArrayList<PersonBean>): String {
        var str = ""
        list.forEach { bean ->
            str += bean.NAME + " "
        }
        return str
    }

    /**
     * 设置可选项目组
     */
    fun setAssessments(list: List<AssessmentGroupRes>) {
        assessments.clear()
        assessments.addAll(list)
    }

    fun setDataList(groLeader: java.util.ArrayList<PersonBean>, depGroLeader: java.util.ArrayList<PersonBean>
                    , member: java.util.ArrayList<PersonBean>, smallGroups: java.util.ArrayList<KbiOrgGroupBean>) {
        this.groLeader = groLeader
        this.depGroLeader = depGroLeader
        this.member = member
        this.smallGroups = smallGroups
    }

    class HeadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val groupLeaderStr = itemView.tv_groupLeaderStr
        val duGroupLeaderStr = itemView.tv_duGroupLeaderStr
        val memberStr = itemView.tv_memberStr
    }

    class GroupViewHolder(itemView: KbiOrgSmallGroupView) : RecyclerView.ViewHolder(itemView) {
        val spinner = itemView.apply {
            val lp = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
            layoutParams = lp
        }
    }

    class AddBtnViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_addGroup = itemView.tv_addGroup
    }
}