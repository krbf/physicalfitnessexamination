package com.example.physicalfitnessexamination.bean;

import java.util.List;

/**
 * 业务竞赛按项目报名实体类
 */
public class ClauseEnrollBean {
    private String name;
    private String sid;
    private List<ClauseEnroll> requirment;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public List<ClauseEnroll> getRequirment() {
        return requirment;
    }

    public void setRequirment(List<ClauseEnroll> requirment) {
        this.requirment = requirment;
    }

    public class ClauseEnroll {
        private String GW;
        private String PSUM;

        public String getGW() {
            return GW;
        }

        public void setGW(String GW) {
            this.GW = GW;
        }

        public String getPSUM() {
            return PSUM;
        }

        public void setPSUM(String PSUM) {
            this.PSUM = PSUM;
        }
    }
}
