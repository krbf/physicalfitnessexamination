package com.example.physicalfitnessexamination.bean;
/**
 * 考核计划基本信息实体类
 */
public class AssessmentInfoBean {
    private String ID;
    private String ORG_ID;
    private String TYPE;//0-日常考核 1-集中考核 2-业务竞赛 3-院校招生考核 4-到站考核 5-单位对抗
    private String NAME;//考核名称
    private String ORG_NAME;
    private String REMARK;//公告
    private String REQUIREMENT_PERSON;//人员要求
    private String ORG_TYPE;//参考单位0-支队机关 1-大队机关 2-应急消防站
    private String PERSON_TYPE;//人员选取方式0-普靠 1-随机抽取 2-单位报名
    private String ACHIEVEMENT_TYPE;//成绩记取方式 0-名次积分 1-年龄积分 2-对抗淘汰

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getORG_ID() {
        return ORG_ID;
    }

    public void setORG_ID(String ORG_ID) {
        this.ORG_ID = ORG_ID;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getORG_NAME() {
        return ORG_NAME;
    }

    public void setORG_NAME(String ORG_NAME) {
        this.ORG_NAME = ORG_NAME;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public String getREQUIREMENT_PERSON() {
        return REQUIREMENT_PERSON;
    }

    public void setREQUIREMENT_PERSON(String REQUIREMENT_PERSON) {
        this.REQUIREMENT_PERSON = REQUIREMENT_PERSON;
    }

    public String getORG_TYPE() {
        return ORG_TYPE;
    }

    public void setORG_TYPE(String ORG_TYPE) {
        this.ORG_TYPE = ORG_TYPE;
    }

    public String getPERSON_TYPE() {
        return PERSON_TYPE;
    }

    public void setPERSON_TYPE(String PERSON_TYPE) {
        this.PERSON_TYPE = PERSON_TYPE;
    }

    public String getACHIEVEMENT_TYPE() {
        return ACHIEVEMENT_TYPE;
    }

    public void setACHIEVEMENT_TYPE(String ACHIEVEMENT_TYPE) {
        this.ACHIEVEMENT_TYPE = ACHIEVEMENT_TYPE;
    }
}
