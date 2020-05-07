package com.example.physicalfitnessexamination.bean;

/**
 * 分组设置实体类
 */
public class GroupSettingsBean {
    private String Name;
    private int startOrder;
    private int endOrder;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getStartOrder() {
        return startOrder;
    }

    public void setStartOrder(int startOrder) {
        this.startOrder = startOrder;
    }

    public int getEndOrder() {
        return endOrder;
    }

    public void setEndOrder(int endOrder) {
        this.endOrder = endOrder;
    }
}
