package com.example.physicalfitnessexamination.bean;

/**
 * 花名册数据集合
 * Created by chenzhiyuan On 2020/4/9
 */
public class RosterAndCheckBean extends RosterBean{

//    private RosterBean bean;

    private boolean isChecked;
//
//    public RosterBean getBean() {
//        return bean;
//    }
//
//    public void setBean(RosterBean bean) {
//        this.bean = bean;
//    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
