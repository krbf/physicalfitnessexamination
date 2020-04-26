package com.example.physicalfitnessexamination.bean;

import java.util.List;

/**
 * 我的成绩实体类
 */
public class MyAchievementBean {
    private String remark;
    private List<MyAchievement> list;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<MyAchievement> getList() {
        return list;
    }

    public void setList(List<MyAchievement> list) {
        this.list = list;
    }

    public class MyAchievement{
        private String ID;
        private String NAME;//项目名
        private String ATYPE;
        private String ZAC;//支队记录
        private String WAC;//我的最好成绩
        private String WSCORE;//单项积分
        private String ZSCORE;//支队平均积分
        private String DENSE_RANK;//支队排名

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
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

        public String getZAC() {
            return ZAC;
        }

        public void setZAC(String ZAC) {
            this.ZAC = ZAC;
        }

        public String getWAC() {
            return WAC;
        }

        public void setWAC(String WAC) {
            this.WAC = WAC;
        }

        public String getWSCORE() {
            return WSCORE;
        }

        public void setWSCORE(String WSCORE) {
            this.WSCORE = WSCORE;
        }

        public String getZSCORE() {
            return ZSCORE;
        }

        public void setZSCORE(String ZSCORE) {
            this.ZSCORE = ZSCORE;
        }

        public String getDENSE_RANK() {
            return DENSE_RANK;
        }

        public void setDENSE_RANK(String DENSE_RANK) {
            this.DENSE_RANK = DENSE_RANK;
        }
    }
}
