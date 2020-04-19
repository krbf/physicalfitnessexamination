package com.example.physicalfitnessexamination.bean;
/**
 * 人员名单（人员成绩列表）
 */
public class PersonAchievementBean {
    private String USERID;
    private String USERNAME;
    private String NO;
    private String ORG_ID;
    private String ORG_NAME;
    private String GW;
    private String PHOTO;
    private String ACHIEVEMENT;

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getNO() {
        return NO;
    }

    public void setNO(String NO) {
        this.NO = NO;
    }

    public String getORG_ID() {
        return ORG_ID;
    }

    public void setORG_ID(String ORG_ID) {
        this.ORG_ID = ORG_ID;
    }

    public String getORG_NAME() {
        return ORG_NAME;
    }

    public void setORG_NAME(String ORG_NAME) {
        this.ORG_NAME = ORG_NAME;
    }

    public String getGW() {
        return GW;
    }

    public void setGW(String GW) {
        this.GW = GW;
    }

    public String getPHOTO() {
        return PHOTO;
    }

    public void setPHOTO(String PHOTO) {
        this.PHOTO = PHOTO;
    }

    public String getACHIEVEMENT() {
        return ACHIEVEMENT;
    }

    public void setACHIEVEMENT(String ACHIEVEMENT) {
        this.ACHIEVEMENT = ACHIEVEMENT;
    }
}
