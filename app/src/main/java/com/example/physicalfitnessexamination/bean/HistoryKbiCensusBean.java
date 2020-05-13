package com.example.physicalfitnessexamination.bean;
/**
 * 历史考核列表统计实体类
 */
public class HistoryKbiCensusBean {
    private String ZKHS;//总考核
    private String ZDKHS;//支队考核
    private String DDKHS;//大队考核
    private String XFZKHS;//消防站考核
    private String year;//年份

    public String getZKHS() {
        return ZKHS;
    }

    public void setZKHS(String ZKHS) {
        this.ZKHS = ZKHS;
    }

    public String getZDKHS() {
        return ZDKHS;
    }

    public void setZDKHS(String ZDKHS) {
        this.ZDKHS = ZDKHS;
    }

    public String getDDKHS() {
        return DDKHS;
    }

    public void setDDKHS(String DDKHS) {
        this.DDKHS = DDKHS;
    }

    public String getXFZKHS() {
        return XFZKHS;
    }

    public void setXFZKHS(String XFZKHS) {
        this.XFZKHS = XFZKHS;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
