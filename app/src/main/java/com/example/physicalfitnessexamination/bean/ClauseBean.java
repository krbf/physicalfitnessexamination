package com.example.physicalfitnessexamination.bean;

import java.util.List;

/**
 * 考核项目实体类
 */
public class ClauseBean {
    private String GW;//岗位
    private List<Clause> SUBJECT;

    public String getGW() {
        return GW;
    }

    public void setGW(String GW) {
        this.GW = GW;
    }

    public List<Clause> getSUBJECT() {
        return SUBJECT;
    }

    public void setSUBJECT(List<Clause> SUBJECT) {
        this.SUBJECT = SUBJECT;
    }

    public class Clause {
        private String SID;
        private String TYPE;
        private String NAME;
        private String ATYPE;//0-时间计数 1-次数
        private String DW;//计量单位

        public String getSID() {
            return SID;
        }

        public void setSID(String SID) {
            this.SID = SID;
        }

        public String getTYPE() {
            return TYPE;
        }

        public void setTYPE(String TYPE) {
            this.TYPE = TYPE;
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

        public String getDW() {
            return DW;
        }

        public void setDW(String DW) {
            this.DW = DW;
        }
    }

}
