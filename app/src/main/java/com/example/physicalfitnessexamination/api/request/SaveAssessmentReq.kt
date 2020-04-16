package com.example.physicalfitnessexamination.api.request

/**
 * 创建考核的api 请求体
 * Created by chenzhiyuan On 2020/4/14
 */
class SaveAssessmentReq : FTRequest() {
    /**
     * 登录人userid
     */
    var createUserid: String? = null

    /**
     * 登录人机构id
     */
    var org_id: String? = null

    /**
     * 0-日常考核 1-集中考核 2-业务竞赛 3-院校招生考核 4-到站考核 5-单位对抗
     */
    var type: Int? = null

    /**
     * 考核项目 sid-项目id type-0-机关男 1-机关女 2-消防站消防员 3-消防站指挥员
     * [{"sid":"deb9ae44f11e4a6d837777c73fd48","type":"0"},{"sid":"226d5696d053473e87f91fea381b4","type":"1"}]
     */
    var `object`: String? = null

    /**
     * 考核组人员 gid-考核组名称接口的获取的id(如果是到站考核，则第一组传“1”,第二组传"2")；
     * examiner_user中的type:0-组长 1-组员
     * [{"gid":"a88b29c09fc948b089d2902de8f85","examiner_user":[{"userid":"d12a741aa38547e4afb20c9b9e769e13","type":"0","username":"吴涛"},{"userid":"d12a741aa38547e4afb20c9b9e769e13","type":"1","username":"吴涛2"},{"userid":"d12a741aa38547e4afb20c9b9e769e13","type":"1","username":"吴涛1"}]},{"gid":"1","examiner_user":[{"userid":"d12a741aa38547e4afb20c9b9e769e13","type":"0","username":"吴涛"},{"userid":"d12a741aa38547e4afb20c9b9e769e13","type":"1","username":"吴涛2"},{"userid":"d12a741aa38547e4afb20c9b9e769e13","type":"1","username":"吴涛1"}]}]
     */
    var examiners: String? = null

    /**
     * 时间安排:非到站形式 content-内 容;gw-岗位(可能为“”);zjr-召集人;fly-发令员;jsy-计时员;sid-考核项目id(可能为“”)
     * [{"t_time":"06:45-07:00","content":"人员报到、装备检录","sid":"","gw":"","zjr":"王一","fly":"张三、李四","jsy":"王五","t_date":"04月13日"},{"t_time":"07:00-08:00","content":"绳索攀爬","sid":"226d5696d053473e87f91fea381b4","gw":"消防站指挥员","zjr":"王一","fly":"张三、李四","jsy":"王五","t_date":"04月13日"}]
     */
    var time_arrangement_a: String? = null

    /**
     * 时间安排:到站形式 glevel-组别，考核一组就传“1”
     * [{"t_time":"06:45-07:00","t_date":"04月13日","glevel":"1","org_name":"雨湖中队","org_id":"226d5696d053473e87f91fea381b4"},{"t_time":"06:45-07:00","t_date":"04月13日","glevel":"2","org_name":"特勤中队","org_id":"226d5696d053473e87f91fea381b4"},{"t_time":"07:00-08:00","t_date":"04月13日","glevel":"1","org_name":"雨湖大队","org_id":"226d5696d053473e87f91fea381b4"}]
     */
    var time_arrangement_b: String? = null

    /**
     * 考核名称
     */
    var name: String? = null

    /**
     * 组织单位
     */
    var org_name: String? = null

    /**
     * 参考单位(逗号分割) 0-支队机关 1-大队机关 2-应急消防站
     * 0,1
     */
    var org_type: String? = null

    /**
     * 人员选取方式 0-普考 1-随机抽取 2-单位报名
     */
    var person_type: Int? = null

    /**
     * 成绩记取方式 0-名次积分 1-年龄积分 2 -对抗淘汰
     */
    var achievenment_type: Int? = null

    /**
     * 公告
     */
    var remark: String? = null

    /**
     * 人员要求
     */
    var requirement_person: String? = null

    /**
     * 考核组员(逗号分割) ,到站考核没有值可不传这字段或null
     * d12a741aa38547e4afb20c9b9e769e12,d12a741aa38547e4afb20c9b9e769e16
     */
    var team_member: String? = null

    /**
     * 考核副组长(逗号分割) 到站考核没有值可不传这字段或null
     */
    var deputy_group_leader: String? = null

    /**
     * 考核组长(逗号分割) 到站考核没有值可不传这字段或null
     */
    var group_leader: String? = null
}