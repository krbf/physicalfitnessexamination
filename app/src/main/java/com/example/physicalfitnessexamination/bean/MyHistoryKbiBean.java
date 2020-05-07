package com.example.physicalfitnessexamination.bean;

public class MyHistoryKbiBean {
    private String NAME;//组织单位
    private String DENSE_RANK;//排名
    private String CREATETIME;//考核时间
    private String ACHIEVE;//成绩

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getDENSE_RANK() {
        return DENSE_RANK;
    }

    public void setDENSE_RANK(String DENSE_RANK) {
        this.DENSE_RANK = DENSE_RANK;
    }

    public String getCREATETIME() {
        return CREATETIME;
    }

    public void setCREATETIME(String CREATETIME) {
        this.CREATETIME = CREATETIME;
    }

    public String getACHIEVE() {
        return ACHIEVE;
    }

    public void setACHIEVE(String ACHIEVE) {
        this.ACHIEVE = ACHIEVE;
    }
}
