package com.example.physicalfitnessexamination.bean;

/**
 * 分组设置实体类
 */
public class GropSettingsBean {
    private String Name;
    private String startNumber;
    private String endNumber;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStartNumber() {
        if (startNumber == null) {
            return "";
        }
        return startNumber;
    }

    public void setStartNumber(String startNumber) {
        this.startNumber = startNumber;
    }

    public String getEndNumber() {
        if (endNumber == null) {
            return "";
        }
        return endNumber;
    }

    public void setEndNumber(String endNumber) {
        this.endNumber = endNumber;
    }
}
