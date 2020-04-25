package com.example.physicalfitnessexamination.page.kbi

import android.content.Context
import android.text.SpannableStringBuilder
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.activity.UserManager
import com.example.physicalfitnessexamination.api.request.SaveAssessmentReq
import com.example.physicalfitnessexamination.bean.CreateKbiDataBean
import com.example.physicalfitnessexamination.bean.KbiOrgGroupIndexBean
import com.example.physicalfitnessexamination.bean.KbiOrgGroupTypeBean
import com.example.physicalfitnessexamination.bean.PersonBean
import com.example.physicalfitnessexamination.common.annotation.AllOpenAndNoArgAnnotation
import com.example.physicalfitnessexamination.util.JacksonMapper
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * 创建考核数据管理类
 * Created by chenzhiyuan On 2020/4/14
 */
object CreateKbiDataManager {
    private val sdf_date = SimpleDateFormat("yyyy年MM月dd日", Locale.CHINESE)
    private val sdf_hour = SimpleDateFormat("HH:mm", Locale.CHINESE)

    /**
     * 数据记录类
     */
    var kbiBean: CreateKbiDataBean? = null

//    /**
//     * 接口请求体
//     */
//    private var request: SaveAssessmentReq? = null

    /**
     * 创建新的kbi实体对象
     */
    fun createNewKbi() {
        kbiBean = CreateKbiDataBean()
    }

    /**
     * 清除kbi实体
     */
    fun clearKbi() {
        kbiBean = null
    }

    fun getFinalReq(context: Context): SaveAssessmentReq {
        return SaveAssessmentReq().apply {
            createUserid = UserManager.getInstance().getUserInfo(context).userid
            org_id = UserManager.getInstance().getUserInfo(context).org_id

            //0-日常考核 1-集中考核 2-业务竞赛 3-院校招生考核 4-到站考核 5-单位对抗
            type = when (kbiBean?.type) {
                context.resources.getStringArray(R.array.evaMode)[0] -> 2
                context.resources.getStringArray(R.array.evaMode)[1] -> 1
                context.resources.getStringArray(R.array.evaMode)[2] -> 4
                context.resources.getStringArray(R.array.evaMode)[3] -> 0
                else -> -1
            }

            run {
                //考核项目 sid-项目id type-0-机关男 1-机关女 2-消防站消防员 3-消防站指挥员
                val beanList = ArrayList<ObjectFormatBeanItem>()
                kbiBean?.objPart1?.forEach {
                    beanList.add(ObjectFormatBeanItem(it.ID, "0"))
                }
                kbiBean?.objPart2?.forEach {
                    beanList.add(ObjectFormatBeanItem(it.ID, "1"))
                }
                kbiBean?.objPart3?.forEach {
                    beanList.add(ObjectFormatBeanItem(it.ID, "3"))
                }
                kbiBean?.objPart4?.forEach {
                    beanList.add(ObjectFormatBeanItem(it.ID, "2"))
                }
                `object` = JacksonMapper.mInstance.writeValueAsString(beanList)
            }

            run {
                //考核组人员
                val beanList = ArrayList<ExaminerItem>()
                kbiBean?.examiners?.forEach {
                    val userList = ArrayList<ExaminerUser>()
                    it.leaderList.forEach { pBean ->
                        userList.add(ExaminerUser(
                                "0", pBean.ID ?: "", pBean.NAME ?: ""
                        ))
                    }
                    it.memberList.forEach { pBean ->
                        userList.add(ExaminerUser(
                                "1", pBean.ID ?: "", pBean.NAME ?: ""
                        ))
                    }

                    when (it) {
                        is KbiOrgGroupTypeBean -> {
                            beanList.add(ExaminerItem(
                                    userList, it.groupType.ID ?: ""
                            ))
                        }
                        is KbiOrgGroupIndexBean -> {
                            beanList.add(ExaminerItem(
                                    userList, it.index.toString()
                            ))
                        }
                    }
                }
                examiners = JacksonMapper.mInstance.writeValueAsString(beanList)
            }


            run {
                /*
                时间安排
                 */
                run {
                    //非到站
                    val list = ArrayList<TimeTypeA>()
                    kbiBean?.timeArrangementA?.forEach { a ->
                        a.detailBean.forEach { b ->
                            val zjr = getAppendStr(b.convener, object : GetStrListener<PersonBean> {
                                override fun getStr(bean: PersonBean): String = bean.NAME ?: ""
                            }, '、')
                            val fly = getAppendStr(b.starter, object : GetStrListener<PersonBean> {
                                override fun getStr(bean: PersonBean): String = bean.NAME ?: ""
                            }, '、')
                            val jsy = getAppendStr(b.timekeeper, object : GetStrListener<PersonBean> {
                                override fun getStr(bean: PersonBean): String = bean.NAME ?: ""
                            }, '、')

                            list.add(TimeTypeA(
                                    t_date = sdf_date.format(a.date.time),
                                    t_time = if (b.startTime != null && b.endTime != null) "${sdf_hour.format(b.startTime)}-${sdf_hour.format(b.endTime)}" else "",
                                    content = b.assessment?.NAME ?: b.contentStr ?: "",
                                    sid = b.assessment?.ID ?: "",
                                    gw = b.position ?: "",
                                    zjr = zjr,
                                    fly = fly,
                                    jsy = jsy
                            ))
                        }
                    }

                    time_arrangement_a = JacksonMapper.mInstance.writeValueAsString(list)
                }

                run {
                    //到站考核
                    val list = ArrayList<TimeTypeB>()

                    kbiBean?.timeArrangementB?.forEach { a ->
                        val gList = ArrayList<Glevel>()
                        a.org1?.let { org ->
                            gList.add(Glevel(org.ORG_ID ?: "", org.ORG_NAME ?: ""))
                        }
                        a.org2?.let { org ->
                            gList.add(Glevel(org.ORG_ID ?: "", org.ORG_NAME ?: ""))
                        }
                        a.org3?.let { org ->
                            gList.add(Glevel(org.ORG_ID ?: "", org.ORG_NAME ?: ""))
                        }

                        list.add(TimeTypeB(
                                t_date = sdf_date.format(a.date),
                                t_time = if (a.isMorning != null) {
                                    if (a.isMorning!!) {
                                        "上午"
                                    } else {
                                        "下午"
                                    }
                                } else "",
                                glevel = gList
                        ))
                    }
                    time_arrangement_b = JacksonMapper.mInstance.writeValueAsString(list)
                }
            }

            name = kbiBean?.name

            org_name = UserManager.getInstance().getUserInfo(context).org_name

            run {
                /*
                参考单位
                 */
                org_type = getAppendStr(kbiBean?.orgType, object : GetStrListener<String> {
                    override fun getStr(bean: String): String {
                        return when (bean) {
                            context.resources.getStringArray(R.array.joinEvaOrg)[0] -> "0"
                            context.resources.getStringArray(R.array.joinEvaOrg)[1] -> "1"
                            context.resources.getStringArray(R.array.joinEvaOrg)[2] -> "2"
                            else -> "-1"
                        }
                    }
                }, ',')
            }

            //人员选取方式
            person_type = when (kbiBean?.personType) {
                context.resources.getStringArray(R.array.perSelect)[0] -> 0
                context.resources.getStringArray(R.array.perSelect)[1] -> 1
                context.resources.getStringArray(R.array.perSelect)[2] -> 2
                else -> -1
            }

            //成绩记取
            achievenment_type = when (kbiBean?.achievenmentType) {
                context.resources.getStringArray(R.array.resultsRemember)[0] -> 0
                context.resources.getStringArray(R.array.resultsRemember)[1] -> 1
                context.resources.getStringArray(R.array.resultsRemember)[2] -> 2
                else -> -1
            }

            //公告
            remark = kbiBean?.remark

            //人员要求
            requirement_person = kbiBean?.requirementPerson

            //考核组员
            team_member = getAppendStr(kbiBean?.teamMember, object : GetStrListener<PersonBean> {
                override fun getStr(bean: PersonBean): String = bean.ID ?: ""
            }, ',')

            //考核组副组长
            deputy_group_leader = getAppendStr(kbiBean?.deputyGroupLeader, object : GetStrListener<PersonBean> {
                override fun getStr(bean: PersonBean): String = bean.ID ?: ""
            }, ',')

            //考核组组长
            group_leader = getAppendStr(kbiBean?.groupLeader, object : GetStrListener<PersonBean> {
                override fun getStr(bean: PersonBean): String = bean.ID ?: ""
            }, ',')


        }
    }

    /**
     * 数组以某种字符为间隔 拼接成字符串
     * @param lists 数据集合
     * @param getStrListener 获取需显示的字符回调方法
     * @param splitChar 间隔字符
     */
    fun <T> getAppendStr(lists: List<T>?, getStrListener: GetStrListener<T>, splitChar: Char): String {
        var msg = ""

        lists?.let { s ->
            val str = SpannableStringBuilder()
            s.forEach {
                str.append(getStrListener.getStr(it))
                str.append(splitChar)
            }

            if (s.isNotEmpty()) {
                msg = str.substring(0, str.length - 1)
            }
        }

        return msg
    }

    interface GetStrListener<T> {
        fun getStr(bean: T): String
    }

    @AllOpenAndNoArgAnnotation
    data class ObjectFormatBeanItem(
            //考核项目 sid-项目id type-0-机关男 1-机关女 2-消防站消防员 3-消防站指挥员
            var sid: String,
            var type: String
    )

    data class ExaminerItem(
            var examiner_user: List<ExaminerUser>,
            var gid: String
    )

    data class ExaminerUser(
            var type: String,
            var userid: String,
            var username: String
    )

    data class TimeTypeA(
            var content: String,
            var fly: String,
            var gw: String,
            var jsy: String,
            var sid: String,
            var t_date: String,
            var t_time: String,
            var zjr: String
    )

    data class TimeTypeB(
            var glevel: List<Glevel>,
            var t_date: String,
            var t_time: String
    )

    data class Glevel(
            var org_id: String,
            var org_name: String
    )
}