package com.example.physicalfitnessexamination.bean

import com.example.physicalfitnessexamination.api.response.TestProjectRes

/**
 * 新建考核所有数据实体类
 * Created by chenzhiyuan On 2020/4/13
 */
class CreateKbiDataBean {
    /**
     * 考核名称
     */
    var name: String? = null

    /**
     * 参考单位
     */
    var orgType: List<String>? = null

    /**
     * 考核方式
     */
    var type: String? = null

    /**
     * 人员选取方式 0-普考 1-随机抽取 2-单位报名
     */
    var personType: String? = null

    /**
     * 成绩记取方式 0-名次积分 1-年龄积分 2-对抗淘汰
     */
    var achievenmentType: String? = null

    /**
     * 考核项目1
     * 机关男
     */
    var objPart1: List<TestProjectRes>? = null

    /**
     * 考核项目2
     * 机关女
     */
    var objPart2: List<TestProjectRes>? = null

    /**
     * 考核项目3
     * 消防站指挥员
     */
    var objPart3: List<TestProjectRes>? = null

    /**
     * 考核项目4
     * 消防站消防员
     */
    var objPart4: List<TestProjectRes>? = null

    /**
     * 考核组长
     */
    var groupLeader: List<PersonBean>? = null

    /**
     * 考核副组长
     */
    var deputyGroupLeader: List<PersonBean>? = null

    /**
     * 考核组员
     */
    var teamMember: List<PersonBean>? = null

    /**
     * 考核小组集合
     */
    var examiners: List<KbiOrgGroupBean>? = null

    /**
     * 时间安排:非到站形式
     */
    var timeArrangementA: List<KbiTimeCfgType1Bean>? = null

    /**
     * 时间安排:到站形式
     */
    var timeArrangementB: List<KbiTimeCfgType2Bean>? = null

    /**
     * 公告
     */
    var remark: String? = null

    /**
     * 人员要求
     */
    var requirementPerson: String? = null

}