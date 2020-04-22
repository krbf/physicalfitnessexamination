package com.example.physicalfitnessexamination.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 考核项目实体类
 */
public class ClauseBean implements Parcelable {
    private String GW;//岗位
    private List<Clause> SUBJECT;

    public ClauseBean() {
    }

    protected ClauseBean(Parcel in) {
        GW = in.readString();
        if (SUBJECT == null) {
            SUBJECT = new ArrayList<>();
        }
        in.readTypedList(SUBJECT, Clause.CREATOR);
    }

    public static final Creator<ClauseBean> CREATOR = new Creator<ClauseBean>() {
        @Override
        public ClauseBean createFromParcel(Parcel in) {
            return new ClauseBean(in);
        }

        @Override
        public ClauseBean[] newArray(int size) {
            return new ClauseBean[size];
        }
    };

    public String getGW() {
        return GW;
    }

    public void setGW(String GW) {
        this.GW = GW;
    }

    public List<Clause> getSUBJECT() {
        return SUBJECT;
    }

    public void setSUBJECT(List<Clause> SUBJECT) {
        this.SUBJECT = SUBJECT;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(GW);
        dest.writeTypedList(this.SUBJECT);
    }

    public static class Clause implements Parcelable {
        private String SID;
        private String TYPE;
        private String NAME;
        private String ATYPE;//0-时间计数 1-次数
        private String DW;//计量单位

        public Clause() {
        }

        protected Clause(Parcel in) {
            SID = in.readString();
            TYPE = in.readString();
            NAME = in.readString();
            ATYPE = in.readString();
            DW = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(SID);
            dest.writeString(TYPE);
            dest.writeString(NAME);
            dest.writeString(ATYPE);
            dest.writeString(DW);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Clause> CREATOR = new Creator<Clause>() {
            @Override
            public Clause createFromParcel(Parcel in) {
                return new Clause(in);
            }

            @Override
            public Clause[] newArray(int size) {
                return new Clause[size];
            }
        };

        public String getSID() {
            return SID;
        }

        public void setSID(String SID) {
            this.SID = SID;
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

        public String getATYPE() {
            return ATYPE;
        }

        public void setATYPE(String ATYPE) {
            this.ATYPE = ATYPE;
        }

        public String getDW() {
            return DW;
        }

        public void setDW(String DW) {
            this.DW = DW;
        }
    }

}
