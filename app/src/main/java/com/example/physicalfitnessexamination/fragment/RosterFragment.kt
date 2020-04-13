package com.example.physicalfitnessexamination.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.bean.RosterAndCheckBean
import com.example.physicalfitnessexamination.util.dp2px
import com.example.physicalfitnessexamination.view.excel.LinearHorView
import kotlinx.android.synthetic.main.fragment_roster.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * 花名册Fragment
 * @author 陈致远
 */
class RosterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val list by lazy {
        val array = ArrayList<RosterAndCheckBean>();
        for (i in 0 until 15) {
            array.add(RosterAndCheckBean().apply {
                id = "sss"
                name = "韩某某${i}韩某某韩某某韩某某韩某某韩某某"
                sex = if (i % 2 == 1) "男" else "女"
                age = i
                zw = null
                j_TYPE = "干部"
                orG_ID = "d7bafdd450b4b29b34f825902a92c0c"
                orG_NAME = "湘乡中队"
                type = "消防站"
                isChecked = false
            })
        }
        array
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RosterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                RosterFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    private val adapter by lazy {
        RosterAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ll_title.let {
            it.setTvColor(R.color.white)
            it.setBackColor(R.color.colorPrimary)
            it.setData("序号", "姓名", "性别", "年龄", "人员类型", "职务", "岗位", "单位")
            it.setEndView(Button(it.context).apply {
                //全选按钮
                text = "全选"
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 7F)
                setOnClickListener {
                    Toast.makeText(context, "quxuan", Toast.LENGTH_SHORT).show()
                    adapter.getData().forEach { bean ->
                        bean.isChecked = true
                    }
                    adapter.notifyDataSetChanged()
                }
            })
        }

        rv_roster.let {
            it.adapter = adapter.apply {
                setData(list)
            }
            it.layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_roster, container, false)
    }
}

class RosterAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * 数据集合
     */
    private var dataList = ArrayList<RosterAndCheckBean>()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(LinearHorView(p0.context))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        if (p0 is ItemViewHolder) {
            p0.view.let {
                it.setBackColor(if (p1 % 2 == 0) R.color._CBD7F4 else R.color._E6ECFA)
                it.setData((p1 + 1).toString(), dataList[p1].name, dataList[p1].sex, dataList[p1].age.toString()
                        , dataList[p1].j_TYPE, dataList[p1].type, dataList[p1].orG_NAME)
                it.setEndView(CheckBox(it.context).apply {
                    setOnCheckedChangeListener { _, isChecked ->
                        dataList[p1].isChecked = isChecked
                    }
                })
                it.setEndData(dataList[p1].isChecked)
            }
        }
    }

    fun getData(): ArrayList<RosterAndCheckBean> {
        return dataList
    }

    fun setData(list: ArrayList<RosterAndCheckBean>) {
        dataList = list
        notifyDataSetChanged()
    }

    class ItemViewHolder(itemView: LinearHorView) : RecyclerView.ViewHolder(itemView) {
        val view = itemView.apply {
            layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, 30.dp2px)
        }
    }
}
