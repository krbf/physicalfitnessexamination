package com.example.physicalfitnessexamination.view.excel

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.bean.KbiOrgGroupBean
import com.example.physicalfitnessexamination.bean.KbiOrgGroupIndexBean
import com.example.physicalfitnessexamination.bean.KbiOrgGroupTypeBean
import com.example.physicalfitnessexamination.bean.PersonBean
import com.example.physicalfitnessexamination.view.RosterDialogFragment
import kotlinx.android.synthetic.main.v_kbi_org_group_item.view.*

/**
 * 考核组织-小组Item组件
 * Created by chenzhiyuan On 2020/4/15
 */
class KbiOrgSmallGroupView : LinearLayout {

    private lateinit var bean: KbiOrgGroupBean

//    val iv_remove = itemView.iv_remove
//    val tv_group = itemView.tv_group
//    val tv_gLeader = itemView.tv_gLeader
//    val tv_groupMember = itemView.tv_groupMember


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
        LayoutInflater.from(context).inflate(R.layout.v_kbi_org_group_item, this, true)

        sp_group.setSimpleUiMode(true)

        tv_gLeader.setOnClickListener {
            val dialogFragment = RosterDialogFragment.newInstance(1,
                    selectList = bean.leaderList,
                    listener = object : RosterDialogFragment.OnCheckListener {
                        override fun checkOver(list: ArrayList<PersonBean>) {
                            bean.leaderList.clear()
                            bean.leaderList.addAll(list)
                            tv_gLeader.text = appendLabel(list)
                        }
                    })
            dialogFragment.show((context as AppCompatActivity).supportFragmentManager, "")
        }
        tv_groupMember.setOnClickListener {
            val dialogFragment = RosterDialogFragment.newInstance(1,
                    selectList = bean.memberList,
                    listener = object : RosterDialogFragment.OnCheckListener {
                        override fun checkOver(list: ArrayList<PersonBean>) {
                            bean.memberList.clear()
                            bean.memberList.addAll(list)
                            tv_groupMember.text = appendLabel(list)
                        }
                    })
            dialogFragment.show((context as AppCompatActivity).supportFragmentManager, "")
        }

    }

    fun setData(bean: KbiOrgGroupBean) {
        this.bean = bean

        //组别
        when (bean) {
            is KbiOrgGroupTypeBean -> {
//                sp_group.setSpinner()
            }
            is KbiOrgGroupIndexBean -> {
            }
        }

        //组长
        tv_gLeader.text = appendLabel(bean.leaderList)

        //成员
        tv_groupMember.text = appendLabel(bean.memberList)
    }

    /**
     * 根据已选项拼接显示文案
     */
    private fun appendLabel(list: List<PersonBean>): String {
        var str = ""
        list.forEach { bean ->
            str += bean.NAME + " "
        }
        return str
    }
}