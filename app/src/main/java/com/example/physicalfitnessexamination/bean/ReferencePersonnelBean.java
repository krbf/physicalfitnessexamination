package com.example.physicalfitnessexamination.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.physicalfitnessexamination.view.NoScrollViewPager;

/**
 * 参考人员实体类 +请假人员实体类 （STATUS无）
 */
public class ReferencePersonnelBean implements Parcelable {
    private String USERID;
    private String USERNAME;
    private String AGE;
    private String SEX;
    private String ZW;
    private String ORG_ID;
    private String STATUS;
    private String TYPE;
    private String ORG_NAME;
    private String GW;
    private String NO;
    private String id;
    private String photo;
    private String workphoto;

    public ReferencePersonnelBean() {

    }

    protected ReferencePersonnelBean(Parcel in) {
        USERID = in.readString();
        USERNAME = in.readString();
        AGE = in.readString();
        SEX = in.readString();
        ZW = in.readString();
        ORG_ID = in.readString();
        STATUS = in.readString();
        TYPE = in.readString();
        ORG_NAME = in.readString();
        GW = in.readString();
        NO = in.readString();
        id = in.readString();
        photo = in.readString();
        workphoto = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(USERID);
        dest.writeString(USERNAME);
        dest.writeString(AGE);
        dest.writeString(SEX);
        dest.writeString(ZW);
        dest.writeString(ORG_ID);
        dest.writeString(STATUS);
        dest.writeString(TYPE);
        dest.writeString(ORG_NAME);
        dest.writeString(GW);
        dest.writeString(NO);
        dest.writeString(id);
        dest.writeString(photo);
        dest.writeString(workphoto);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReferencePersonnelBean> CREATOR = new Creator<ReferencePersonnelBean>() {
        @Override
        public ReferencePersonnelBean createFromParcel(Parcel in) {
            return new ReferencePersonnelBean(in);
        }

        @Override
        public ReferencePersonnelBean[] newArray(int size) {
            return new ReferencePersonnelBean[size];
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

    public String getAGE() {
        return AGE;
    }

    public void setAGE(String AGE) {
        this.AGE = AGE;
    }

    public String getSEX() {
        return SEX;
    }

    public void setSEX(String SEX) {
        this.SEX = SEX;
    }

    public String getZW() {
        return ZW;
    }

    public void setZW(String ZW) {
        this.ZW = ZW;
    }

    public String getORG_ID() {
        return ORG_ID;
    }

    public void setORG_ID(String ORG_ID) {
        this.ORG_ID = ORG_ID;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
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

    public String getNO() {
        return NO;
    }

    public void setNO(String NO) {
        this.NO = NO;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getWorkphoto() {
        return workphoto;
    }

    public void setWorkphoto(String workphoto) {
        this.workphoto = workphoto;
    }
}
