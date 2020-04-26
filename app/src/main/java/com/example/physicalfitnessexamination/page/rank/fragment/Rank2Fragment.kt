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
import com.example.physicalfitnessexamination.activity.UserManager
import com.example.physicalfitnessexamination.api.RequestManager
import com.example.physicalfitnessexamination.api.callback.JsonCallback
import com.example.physicalfitnessexamination.api.request.GetOrgAssessList4BsymReq
import com.example.physicalfitnessexamination.api.request.GetOrgListReq
import com.example.physicalfitnessexamination.api.response.ApiResponse
import com.example.physicalfitnessexamination.api.response.GetOrgListRes
import com.example.physicalfitnessexamination.api.response.OrgAssessList4BsymRes
import com.example.physicalfitnessexamination.page.rank.adapter.Rank2Adapter
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import kotlinx.android.synthetic.main.fragment_rank2.*

/**
 * 单位历次考核第一 fragment
 */
class Rank2Fragment : Fragment() {
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
                Rank2Fragment().apply {
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
        Rank2Adapter()
    }

    private var requestTag: GetOrgAssessList4BsymReq? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rank2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rcy_rankList.let { v ->
            v.layoutManager = GridLayoutManager(context, 4)
            v.adapter = adapter
        }

        sp_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //取消上一次的请求
                requestTag?.let { tag ->
                    OkGo.getInstance().cancelTag(tag)
                }

                orgList?.get(position)?.ORG_ID?.let { requestOrgAssessList4Bsym(it) }
            }
        }

        initData()
    }

    private fun requestOrgAssessList4Bsym(orgId: String) {
        requestTag = GetOrgAssessList4BsymReq(orgId)
        RequestManager.getOrgAssessList4Bsym(orgId, requestTag,
                object : JsonCallback<ApiResponse<List<OrgAssessList4BsymRes>>, List<OrgAssessList4BsymRes>>() {
                    override fun onSuccess(response: Response<ApiResponse<List<OrgAssessList4BsymRes>>>?) {
                        response?.body()?.data?.let { list ->
                            adapter.dataList.let { adapterData ->
                                adapterData.clear()
                                adapterData.addAll(list)
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }
                })
    }

    private var orgList: List<GetOrgListRes>? = null

    private fun initData() {
        RequestManager.getOrgList(this, GetOrgListReq(UserManager.getInstance().getUserInfo(context).org_id),
                object : JsonCallback<ApiResponse<List<GetOrgListRes>>, List<GetOrgListRes>>() {
                    override fun onSuccess(response: Response<ApiResponse<List<GetOrgListRes>>>?) {
                        response?.body()?.data?.let { list ->
                            orgList = list
                            sp_type.adapter = ArrayAdapter(context, R.layout.time_config_content_spinner_item,
                                    list.map { indexBean ->
                                        indexBean.ORG_NAME
                                    })
                        }
                    }
                })
    }
}
