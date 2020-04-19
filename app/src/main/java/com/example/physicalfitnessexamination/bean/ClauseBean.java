package com.example.physicalfitnessexamination.bean;
/**
 * 考核项目实体类
 */
public class ClauseBean {
    private String SID;
    private String NAME;
    private String TYPE;
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

    public String[] getGW() {
        return GW;
    }

    public void setGW(String[] GW) {
        this.GW = GW;
    }
}
