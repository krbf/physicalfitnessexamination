package com.example.physicalfitnessexamination.view.excel

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.activity.UserManager
import com.example.physicalfitnessexamination.api.response.AssessmentGroupRes
import com.example.physicalfitnessexamination.bean.KbiOrgGroupBean
import com.example.physicalfitnessexamination.bean.KbiOrgGroupIndexBean
import com.example.physicalfitnessexamination.bean.KbiOrgGroupTypeBean
import com.example.physicalfitnessexamination.bean.PersonBean
import com.example.physicalfitnessexamination.view.RosterDialogFragment
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.v_kbi_org_group_item.view.*

/**
 * 考核组织-小组Item组件
 * Created by chenzhiyuan On 2020/4/15
 */
class KbiOrgSmallGroupView : LinearLayout {

    private lateinit var bean: KbiOrgGroupBean

    /**
     * 组次小组当前所在组次
     */
    private var position: Int? = null

    /**
     * 分类小组可选类别集合
     */
    private var assessments: List<AssessmentGroupRes>? = null

    private var onListener: OnSmallGroupListener? = null

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
            val dialogFragment = RosterDialogFragment.newInstance(
                    UserManager.getInstance().getUserInfo(context).org_id, 1,
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
            val dialogFragment = RosterDialogFragment.newInstance(
                    UserManager.getInstance().getUserInfo(context).org_id, 1,
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

        iv_remove.setOnClickListener {
            onListener?.onRemoveClick(bean)
        }
    }

    private fun setData(bean: KbiOrgGroupBean, onListener: OnSmallGroupListener) {
        try {
            this.bean = bean
            this.onListener = onListener

            //组别/组次
            when (bean) {
                is KbiOrgGroupTypeBean -> {
                    sp_group.visibility = View.VISIBLE
                    tv_gIndex.visibility = View.GONE

                    assessments?.let { aList ->
                        //选中的选项下标
                        var defaultIndex: Int? = null
                        if (bean.groupType != null) {
                            for (i in aList.indices) {
                                if (aList[i].ID == bean.groupType.ID) {
                                    defaultIndex = i
                                    break
                                }
                            }
                        }
                        sp_group.setSpinner(aList.toTypedArray(),
                                object : SpinnerParentView.OnGetStrListener<AssessmentGroupRes> {
                                    override fun getStr(bean: AssessmentGroupRes): String {
                                        return bean.NAME ?: ""
                                    }
                                },
                                object : SpinnerParentView.OnCheckListener<AssessmentGroupRes> {
                                    override fun onConfirmAndChangeListener(view: SpinnerParentView<AssessmentGroupRes>, selectBeanList: List<AssessmentGroupRes>) {
                                        if (selectBeanList.isNotEmpty()) {
                                            bean.groupType = selectBeanList.first()
                                        }
                                    }
                                }, isRadio = true, defaultIndex = if (defaultIndex == null) mutableSetOf() else mutableSetOf(defaultIndex))
                    }

                }
                is KbiOrgGroupIndexBean -> {
                    sp_group.visibility = View.GONE
                    tv_gIndex.visibility = View.VISIBLE

                    bean.index = (position ?: 0 + 1)
                    tv_gIndex.text = "第${bean.index}组"
                }
            }

            //组长
            tv_gLeader.text = appendLabel(bean.leaderList)

            //成员
            tv_groupMember.text = appendLabel(bean.memberList)
        } catch (e: Exception) {
            Logger.e(e, "GroupView")
        }
    }

    /**
     * 设置小组
     */
    fun setData(bean: KbiOrgGroupTypeBean, assessments: List<AssessmentGroupRes>, onListener: OnSmallGroupListener) {
        this.assessments = assessments
        setData(bean, onListener)
    }

    /**
     * 设置小组
     */
    fun setData(bean: KbiOrgGroupIndexBean, position: Int, onListener: OnSmallGroupListener) {
        this.position = position
        setData(bean, onListener)
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

    interface OnSmallGroupListener {
        fun onRemoveClick(bean: KbiOrgGroupBean)
    }
}