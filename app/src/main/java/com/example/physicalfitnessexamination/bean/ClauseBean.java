package com.example.physicalfitnessexamination.bean;

/**
 * 考核项目实体类
 */
public class ClauseBean {
    private String SID;
    private String NAME;
    private String TYPE;
    private String ATYPE;//0-时间计数 1-次数
    private String DW;
    private String[] GW;

    public String getSID() {
        return SID;
    }

    public void setSID(String SID) {
        this.SID = SID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
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

    public String[] getGW() {
        return GW;
    }

    public void setGW(String[] GW) {
        this.GW = GW;
    }
}
