package com.example.physicalfitnessexamination.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 组别提交实体类
 */
public class GroupCommitBean {
    private String aid;
    private String sid;
    private String gw;
    private List<ArrayList<String>> plist;

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getGw() {
        return gw;
    }

    public void setGw(String gw) {
        this.gw = gw;
    }

    public List<ArrayList<String>> getPlist() {
        return plist;
    }

    public void setPlist(List<ArrayList<String>> plist) {
        this.plist = plist;
    }
}
