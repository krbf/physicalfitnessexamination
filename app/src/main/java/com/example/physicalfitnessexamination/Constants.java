package com.example.physicalfitnessexamination;

public class Constants {
    //    public static final String HOST = "http://218.75.249.59:31010";//湖南
    public static final String HOST = "http://114.55.167.62:8080";//测试服务器
    public static final String PROJECT_NAME = "/hnxf";
    public static final String IP = HOST + PROJECT_NAME + "/";
    public static final String UNITID = "70717bcfa05d4042b75294a4d4a786c8";
    //    public static final String IMAGE = "http://192.168.101.94:8080";
    public static final String IMAGE = "http://114.55.167.62:8089";


    public static class RoleIDStr {
        /**
         * 指挥中心（支队）
         * [机关]
         */
        public static final String MANAGE = "manage";
        /**
         * 基层中队
         * [消防站]
         */
        public static final String COMM = "comm";
        /**
         * 指挥中心（总队）
         * [机关]
         */
        public static final String MANAGE_CROPS = "manageCrops";
        /**
         * 指挥中心（大队）
         * [机关]
         */
        public static final String MANAGE_BRIGADE = "manageBrigade";
    }
}
