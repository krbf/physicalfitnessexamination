package com.example.physicalfitnessexamination.view

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.activity.UserManager
import com.example.physicalfitnessexamination.api.RequestManager
import com.example.physicalfitnessexamination.api.callback.JsonCallback
import com.example.physicalfitnessexamination.api.request.GetOrgCommanderReq
import com.example.physicalfitnessexamination.api.response.ApiResponse
import com.example.physicalfitnessexamination.bean.PersonBean
import com.example.physicalfitnessexamination.view.excel.LinearRosterItemView
import com.lzy.okgo.model.Response
import kotlinx.android.synthetic.main.fragment_roster.*

/**
 * 人员花名册DialogFragment
 * Created by chenzhiyuan On 2020/4/15
 */
class RosterDialogFragment : DialogFragment() {

    companion object {
        const val TYPE_KEY = "type_key"
        const val ARRAY_SELECT_KEY = "array_select"

        /**
         * 实例化方法
         * @param type 类型 null-全部 1-干部 2-战士
         */
        @JvmStatic
        fun newInstance(type: Int?, selectList: ArrayList<PersonBean> = ArrayList(), listener: OnCheckListener) =
                RosterDialogFragment().apply {
                    arguments = Bundle().apply {
                        type?.let { putInt(TYPE_KEY, it) }
                        putParcelableArrayList(ARRAY_SELECT_KEY, selectList)
                    }
                    setCheckListener(listener)
                }
    }


    /**
     * 机构id
     */
    private val orgId by lazy {
        UserManager.getInstance().getUserInfo(context).org_id
    }

    /**
     * 类型 null-全部 1-干部 2-战士
     */
    private var type: Int? = null

    /**
     * 已选中的人选
     */
    private var selectList: ArrayList<PersonBean> = ArrayList()

    private val adapter by lazy {
        RosterAdapter()
    }

    private var checkListener: OnCheckListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getInt(TYPE_KEY)
            selectList.addAll(it.getParcelableArrayList<PersonBean>(ARRAY_SELECT_KEY))
        }
    }

    override fun onResume() {
        super.onResume()
        dialog?.let {
            it.window?.setBackgroundDrawable(null)
            val width: Int = (context?.resources?.displayMetrics?.widthPixels?.times(0.9))?.toInt()
                    ?: WindowManager.LayoutParams.MATCH_PARENT
            it.window?.setLayout(width, WindowManager.LayoutParams.MATCH_PARENT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.let {
            //去除标题栏
            it.requestWindowFeature(Window.FEATURE_NO_TITLE)

            val window = it.window
            window?.attributes?.let { lp ->
                lp.gravity = Gravity.END //底部
                lp.width = WindowManager.LayoutParams.MATCH_PARENT
                lp.windowAnimations = R.style.R2LdialogAnimation
                window.attributes = lp
            }
        }
        return inflater.inflate(R.layout.fragment_roster, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_yes.setOnClickListener {
            checkListener?.checkOver(selectList)
            dismiss()
        }
        tv_cancel.setOnClickListener {
            dismiss()
        }
        ll_title.setListener(object : LinearRosterItemView.OnRosterItemListener {
            override fun onClickAll() {
                super.onClickAll()
                if (selectList.size == adapter.list.size) {
                    selectList.clear()
                } else {
                    selectList.clear()
                    selectList.addAll(adapter.list)
                }
                adapter.notifyDataSetChanged()
            }
        })

        rv_roster.let {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
        }

        getOrgCommander()
    }

    /**
     * 请求人员列表
     */
    private fun getOrgCommander() {
        RequestManager.getOrgCommander(context,
                GetOrgCommanderReq(orgId, type),
                object : JsonCallback<ApiResponse<List<PersonBean>>, List<PersonBean>>() {
                    override fun onSuccess(response: Response<ApiResponse<List<PersonBean>>>?) {
                        response?.body()?.data?.let { personBeanList ->
                            adapter.setData(personBeanList, selectList)
                        }
                    }
                })
    }

    fun setCheckListener(listener: OnCheckListener) {
        this.checkListener = listener
    }

    interface OnCheckListener {
        fun checkOver(list: ArrayList<PersonBean>)
    }

    class RosterAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        val list = ArrayList<PersonBean>()

        private var selectList: ArrayList<PersonBean>? = null

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
            return ItemViewHolder(LinearRosterItemView(p0.context))
        }

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
            if (p0 is ItemViewHolder) {
                p0.v.let { rosterView ->
                    val samePer = selectList?.find { selectBean ->
                        return@find list[p1].ID == selectBean.ID
                    }
                    rosterView.setData(p1, list[p1], samePer != null)
                    rosterView.setBackgroundColor(
                            ContextCompat.getColor(rosterView.context,
                                    if (p1 % 2 == 0) R.color._B7CFF3 else R.color._CBD7F4)
                    )
                    rosterView.setListener(object : LinearRosterItemView.OnRosterItemListener {
                        override fun onCheckedChangeListener(isChecked: Boolean, entity: PersonBean) {
                            super.onCheckedChangeListener(isChecked, entity)
                            if (isChecked) {
                                if (selectList?.find { f ->
                                            f.ID == entity.ID
                                        } == null) {
                                    selectList?.add(entity)
                                }
                            } else {
                                val cancelCheckPer = selectList?.find { selectBean ->
                                    return@find entity.ID == selectBean.ID
                                }
                                selectList?.remove(cancelCheckPer)
                            }
                        }
                    })
                }
            }
        }

        fun setData(list: List<PersonBean>, selectList: ArrayList<PersonBean>?) {
            this.list.addAll(list)
            this.selectList = selectList
            notifyDataSetChanged()
        }

        class ItemViewHolder(val view: LinearRosterItemView) : RecyclerView.ViewHolder(view) {
            val v = view.apply {
                val lp = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
                layoutParams = lp
            }
        }
    }
}