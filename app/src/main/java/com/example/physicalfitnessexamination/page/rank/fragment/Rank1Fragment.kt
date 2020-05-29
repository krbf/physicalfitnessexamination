package com.example.physicalfitnessexamination.page.rank.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.physicalfitnessexamination.Constants
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.api.RequestManager
import com.example.physicalfitnessexamination.api.callback.JsonCallback
import com.example.physicalfitnessexamination.api.request.GetPersonAssessList4BsymReq
import com.example.physicalfitnessexamination.api.response.ApiResponse
import com.example.physicalfitnessexamination.api.response.PersonAssessList4Res
import com.example.physicalfitnessexamination.bean.UnitBean
import com.example.physicalfitnessexamination.page.rank.adapter.Rank1Adapter
import com.example.physicalfitnessexamination.util.ShowUnitView
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import kotlinx.android.synthetic.main.fragment_rank1.*

/**
 * 单项成绩记录 fragment
 */
class Rank1Fragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    companion object {

        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Rank1Fragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                Rank1Fragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }

        private val typeStrArray = mutableListOf<String>()
        private val typeStrArray1 = arrayListOf("机关", "消防站")
        private val typeStrArray2 = arrayListOf("消防站")

        private val gwStrArray = mutableListOf<String>()
        private val gwStrArray1 = arrayListOf("")
        private val gwStrArray2 = arrayListOf("", "消防站指挥员", "消防站消防员")

    }

    private val adapter2: ArrayAdapter<String> by lazy {
        ArrayAdapter(context, R.layout.time_config_content_spinner_item,
                typeStrArray)
    }

    private val adapter3: ArrayAdapter<String> by lazy {
        ArrayAdapter(context, R.layout.time_config_content_spinner_item,
                gwStrArray)
    }

    /**
     * 页面适配器
     */
    private val adapter by lazy {
        Rank1Adapter()
    }

    private val requestTag = GetPersonAssessList4BsymReq(1, "", Constants.UNITID)

    //机构选择器
    private val showUnitView: ShowUnitView by lazy {
        ShowUnitView(context, { unitBean ->
            initListener(unitBean)
        }, true)
    }

    private fun initListener(unitBean: UnitBean) {
        this.unitBean = unitBean
        if (unitBean.name.endsWith("消防站")) {
            //消防站
            adapter2.let { ad ->
                ad.clear()
                typeStrArray.clear()
                typeStrArray.addAll(typeStrArray2)
                ad.notifyDataSetChanged()
            }
        } else {
            //机关
            adapter2.let { ad ->
                ad.clear()
                typeStrArray.clear()
                typeStrArray.addAll(typeStrArray1)
                ad.notifyDataSetChanged()
            }
        }
        tv_chooseOrg.text = unitBean.name
        requestTag.let { req ->
            req.org_id = unitBean.id
        }
        initData()
    }

    private var unitBean: UnitBean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rank1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rcy_rankList.let { v ->
            v.layoutManager = GridLayoutManager(context, 5)
            v.adapter = adapter
        }
        sp_type2.adapter = adapter2
        sp_type3.adapter = adapter3

        sp_type2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (view is TextView) {
                    var type = 1
                    when (view.text) {
                        "机关" -> {
                            adapter3.let { ad ->
                                ad.clear()
                                gwStrArray.clear()
                                gwStrArray.addAll(gwStrArray1)
                                ad.notifyDataSetChanged()
                            }
                            sp_type3.visibility = View.GONE
                            tv_type3.visibility = View.GONE
                            type = 1
                        }
                        "消防站" -> {
                            adapter3.let { ad ->
                                ad.clear()
                                gwStrArray.clear()
                                gwStrArray.addAll(gwStrArray2)
                                ad.notifyDataSetChanged()
                            }
                            sp_type3.visibility = View.VISIBLE
                            tv_type3.visibility = View.VISIBLE
                            type = 2
                        }
                    }
                    requestTag.let { req ->
                        req.org_id = unitBean?.id ?: Constants.UNITID
                        req.type = type
                        req.gw = ""
                    }
                    initData()
                }
            }
        }

        sp_type3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (view is TextView) {
                    requestTag.let { req ->
                        req.org_id = unitBean?.id ?: Constants.UNITID
                        req.type = 2
                        req.gw = view.text.toString()
                    }
                    initData()
                }
            }
        }

        tv_chooseOrg.setOnClickListener {
            showUnitView.showUnitView()
        }

        initListener(UnitBean().apply {
            id = Constants.UNITID
            name = "湘潭支队"
        })
    }

    /**
     * 请求api
     * @param orgId 组织id
     * @param type 1-支队 2-大队 3-消防站
     * @param gw 岗位名称
     */
    private fun initData() {
        OkGo.getInstance().cancelTag(requestTag)
        RequestManager.getPersonAssessList4Bsym(requestTag, requestTag,
                object : JsonCallback<ApiResponse<List<PersonAssessList4Res>>, List<PersonAssessList4Res>>() {
                    override fun onSuccess(response: Response<ApiResponse<List<PersonAssessList4Res>>>?) {
                        response?.body()?.data?.let { list ->
                            adapter.dataList.clear()
                            adapter.dataList.addAll(list)
                            adapter.notifyDataSetChanged()
                        }
                    }
                })
    }
}
