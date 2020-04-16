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
     * 参考单位(逗号分割) 0-支队机关 1-大队机关 2-应急消防站
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
     * 机关男 或 消防站指挥员
     */
    var objPart1: List<TestProjectRes>? = null

    /**
     * 考核项目2
     * 机关女 或 消防站消防员
     */
    var objPart2: List<TestProjectRes>? = null

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

//    /**
//     * 时间安排:非到站形式 content-内 容;gw-岗位(可能为“”);zjr-召集人;fly-发令员;jsy-计时员;sid-考核项目id(可能为“”)
//     * [{"t_time":"06:45-07:00","content":"人员报到、装备检录","sid":"","gw":"","zjr":"王一","fly":"张三、李四","jsy":"王五","t_date":"04月13日"},{"t_time":"07:00-08:00","content":"绳索攀爬","sid":"226d5696d053473e87f91fea381b4","gw":"消防站指挥员","zjr":"王一","fly":"张三、李四","jsy":"王五","t_date":"04月13日"}]
//     */
//    var timeArrangementA: String? = null
//
//    /**
//     * 时间安排:到站形式 glevel-组别，考核一组就传“1”
//     * [{"t_time":"06:45-07:00","t_date":"04月13日","glevel":"1","org_name":"雨湖中队","org_id":"226d5696d053473e87f91fea381b4"},{"t_time":"06:45-07:00","t_date":"04月13日","glevel":"2","org_name":"特勤中队","org_id":"226d5696d053473e87f91fea381b4"},{"t_time":"07:00-08:00","t_date":"04月13日","glevel":"1","org_name":"雨湖大队","org_id":"226d5696d053473e87f91fea381b4"}]
//     */
//    var timeArrangementB: String? = null

    /**
     * 公告
     */
    var remark: String? = null

    /**
     * 人员要求
     */
    var requirementPerson: String? = null

}