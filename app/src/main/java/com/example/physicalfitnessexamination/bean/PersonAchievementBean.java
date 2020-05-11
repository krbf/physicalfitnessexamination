package com.example.physicalfitnessexamination.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 人员名单（人员成绩列表）
 */
public class PersonAchievementBean implements Parcelable {
    private String USERID;
    private String USERNAME;
    private String NO;
    private String ORG_ID;
    private String ORG_NAME;
    private String GW;
    private String PHOTO;
    private String ACHIEVEMENT;

    public PersonAchievementBean(){

    }

    protected PersonAchievementBean(Parcel in) {
        USERID = in.readString();
        USERNAME = in.readString();
        NO = in.readString();
        ORG_ID = in.readString();
        ORG_NAME = in.readString();
        GW = in.readString();
        PHOTO = in.readString();
        ACHIEVEMENT = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(USERID);
        dest.writeString(USERNAME);
        dest.writeString(NO);
        dest.writeString(ORG_ID);
        dest.writeString(ORG_NAME);
        dest.writeString(GW);
        dest.writeString(PHOTO);
        dest.writeString(ACHIEVEMENT);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PersonAchievementBean> CREATOR = new Creator<PersonAchievementBean>() {
        @Override
        public PersonAchievementBean createFromParcel(Parcel in) {
            return new PersonAchievementBean(in);
        }

        @Override
        public PersonAchievementBean[] newArray(int size) {
            return new PersonAchievementBean[size];
        }
    };

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
