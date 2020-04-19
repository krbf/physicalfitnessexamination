package com.example.physicalfitnessexamination.page.kbi

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.api.RequestManager
import com.example.physicalfitnessexamination.api.callback.JsonCallback
import com.example.physicalfitnessexamination.api.request.GetAssessmentGroupReq
import com.example.physicalfitnessexamination.api.response.ApiResponse
import com.example.physicalfitnessexamination.api.response.AssessmentGroupRes
import com.example.physicalfitnessexamination.base.MyBaseActivity
import com.example.physicalfitnessexamination.bean.KbiOrgGroupBean
import com.example.physicalfitnessexamination.bean.KbiOrgGroupTypeBean
import com.example.physicalfitnessexamination.bean.PersonBean
import com.example.physicalfitnessexamination.page.kbi.adapter.KbiOrgAdapter
import com.example.physicalfitnessexamination.util.dp2px
import com.example.physicalfitnessexamination.util.snack
import com.lzy.okgo.model.Response
import kotlinx.android.synthetic.main.activity_kbi_org.*
import kotlinx.android.synthetic.main.v_toolbar.*

/**
 * 考核组织页面
 */
class KbiOrgActivity : MyBaseActivity(), View.OnClickListener {

    companion object {
        /**
         * 跳转方法
         * @param context 上下文
         */
        @JvmStatic
        fun startInstant(context: Context) {
            Intent(context, KbiOrgActivity::class.java).let {
                context.startActivity(it)
            }
        }
    }

    //<editor-fold desc="数据集合">
    //组长
    private val groLeader = ArrayList<PersonBean>()
    //副组长
    private val depGroLeader = ArrayList<PersonBean>()
    //成员
    private val member = ArrayList<PersonBean>()
    //小组结合
    private val smallGroups = ArrayList<KbiOrgGroupBean>()
    //</editor-fold>

    //<editor-fold desc="RecyclerView相关">
    /**
     * 考核组织适配器
     */
    private val adapter: KbiOrgAdapter by lazy {
        KbiOrgAdapter(CreateKbiDataManager.kbiBean?.type == resources.getStringArray(R.array.evaMode)[2]).apply {
            setDataList(groLeader, depGroLeader, member, smallGroups)
        }
    }
    private val itemDecoration by lazy {
        object : RecyclerView.ItemDecoration() {
            val paint = Paint().apply {
                color = ContextCompat.getColor(context, R.color.white)
            }

            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.top = resources.getDimensionPixelSize(R.dimen.excel_dividerSize)
            }

            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                super.onDraw(c, parent, state)
                val childCount = parent.childCount
                val manager = parent.layoutManager
                for (i in 0 until childCount) {
                    val child = parent.getChildAt(i)
                    manager?.getLeftDecorationWidth(child)
                    c.drawRect(0F, child.top.toFloat() - 1.dp2px, parent.right.toFloat(), child.top.toFloat(), paint)
                }
            }
        }
    }
    //</editor-fold>

    override fun initLayout(): Int = R.layout.activity_kbi_org

    override fun initView() {
        tv_title.text = "新建考核 — 考核组织"
        iv_right.setOnClickListener(this)
        tv_next.setOnClickListener(this)

        rcv_content.let {
            it.layoutManager = LinearLayoutManager(context)
            it.addItemDecoration(itemDecoration)
            it.adapter = adapter
        }
    }

    override fun initData() {
        if (CreateKbiDataManager.kbiBean?.type == resources.getStringArray(R.array.evaMode)[0]
                || CreateKbiDataManager.kbiBean?.type == resources.getStringArray(R.array.evaMode)[1]) {
            //集中考核 或 业务竞赛 请求项目组数据
            RequestManager.getAssessmentGroup(context, GetAssessmentGroupReq(),
                    object : JsonCallback<ApiResponse<List<AssessmentGroupRes>>, List<AssessmentGroupRes>>() {
                        override fun onSuccess(response: Response<ApiResponse<List<AssessmentGroupRes>>>?) {
                            response?.body()?.data?.let {
                                adapter.setAssessments(it)
                            }
                        }

                        override fun onError(response: Response<ApiResponse<List<AssessmentGroupRes>>>?) {
                            super.onError(response)
                            rcv_content.snack("获取考核小组数据失败")
                        }
                    })
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            iv_right -> finish()
            tv_next -> {
                if (checkParameter()) {
                    KbiTimeConfigActivity.startInstant(context)
                }
            }
        }
    }

    /**
     * 验证当前页面数据合法性
     */
    private fun checkParameter(): Boolean {
        //组长
        if (groLeader.isEmpty()) {
            tv_next.snack("请确认总组长人选")
            return false
        } else {
            CreateKbiDataManager.kbiBean?.groupLeader = groLeader
        }

        //副组长
        if (depGroLeader.isEmpty()) {
            tv_next.snack("请确认副总组长人选")
            return false
        } else {
            CreateKbiDataManager.kbiBean?.deputyGroupLeader = depGroLeader
        }

        //成员
        if (member.isEmpty()) {
            tv_next.snack("请确认总组成员人选")
            return false
        } else {
            CreateKbiDataManager.kbiBean?.teamMember = member
        }

        //小组
        val map = HashMap<String, Int>()
        for (bean in smallGroups) {
            if (bean.leaderList.isEmpty()) {
                tv_next.snack("考核小组组长不可为空")
                return false
            }

            if (bean.memberList.isEmpty()) {
                tv_next.snack("考核小组成员不可为空")
                return false
            }

            //检查是否有相同类型的考核组
            if (bean is KbiOrgGroupTypeBean) {
                if (bean.groupType == null) {
                    tv_next.snack("考核小组类型不可为空")
                    return false
                }

                if (map[bean.groupType.ID] == null) {
                    map[bean.groupType.ID ?: ""] = 1
                } else {
                    tv_next.snack("不可存在多个相同类型的考核小组")
                    return false
                }
            }
        }
        CreateKbiDataManager.kbiBean?.examiners = smallGroups

        return true
    }
}