package com.example.physicalfitnessexamination.page.rank.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.api.RequestManager
import com.example.physicalfitnessexamination.api.callback.JsonCallback
import com.example.physicalfitnessexamination.api.request.GetPersonAssessList4BsymReq
import com.example.physicalfitnessexamination.api.response.ApiResponse
import com.example.physicalfitnessexamination.api.response.PersonAssessList4Res
import com.example.physicalfitnessexamination.page.rank.adapter.Rank1Adapter
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
    }

    /**
     * 页面适配器
     */
    private val adapter by lazy {
        Rank1Adapter()
    }

    private var requestTag: GetPersonAssessList4BsymReq? = null

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
        sp_type1.adapter = ArrayAdapter(context, R.layout.time_config_content_spinner_item,
                arrayOf("支队", "大队", "消防站"))
        sp_type1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sp_type2.adapter = ArrayAdapter(context, R.layout.time_config_content_spinner_item,
                        when (position) {
                            0 -> {
                                resources.getStringArray(R.array.managePosition)
                            }
                            1 -> {
                                resources.getStringArray(R.array.manageBrigadePosition)
                            }
                            else -> {
                                resources.getStringArray(R.array.commPosition)
                            }
                        })
            }
        }

        sp_type2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //取消上一次的请求
                requestTag?.let { tag ->
                    OkGo.getInstance().cancelTag(tag)
                }

                initData(sp_type1.selectedItemPosition + 1, (sp_type2.adapter as ArrayAdapter<String>).getItem(position))
            }
        }
    }

    private fun initData(type: Int, gw: String) {
        requestTag = GetPersonAssessList4BsymReq(type, gw)
        RequestManager.getPersonAssessList4Bsym(this, requestTag,
                object : JsonCallback<ApiResponse<List<PersonAssessList4Res>>, List<PersonAssessList4Res>>() {
                    override fun onSuccess(response: Response<ApiResponse<List<PersonAssessList4Res>>>?) {
                        response?.body()?.data?.let { list ->
                            adapter.dataList.addAll(list)
                            adapter.notifyDataSetChanged()
                        }
                    }
                })
    }
}
