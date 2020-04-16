package com.example.physicalfitnessexamination.bean

/**
 * 考核组
 * Created by chenzhiyuan On 2020/4/13
 */
open class KbiOrgGroupBean {
    /**
     * 组长
     */
    var leaderList: ArrayList<PersonBean> = ArrayList()
        private set

    /**
     * 成员
     */
    var memberList: ArrayList<PersonBean> = ArrayList()
        private set
}