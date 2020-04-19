package com.example.physicalfitnessexamination.app;

import com.example.physicalfitnessexamination.Constants;

public class Api {
    public static final String LOGIN = Constants.IP + "android/user/loginForSY";//登录接口
    public static final String BUILTKBILIST = Constants.IP + "assessment/getAssessmentsList";//已建考核列表
    public static final String GETCREATAORG = Constants.IP + "assessment/getCreateOrgForAssessment";//获取考核计划参赛机构列表
    public static final String GETASSESSMENTPERSONLIST = Constants.IP + "assessment/getAssessmentsPersonList";//参考人员列表
    public static final String GETASSESSMENTINFO = Constants.IP + "assessment/getAssessmentInfo";//获取考核计划基本信息
    public static final String GETORGCOMMANDER = Constants.IP + "assessment/getOrgCommander";//机构全部人员列表 1-领导 2-普通
    public static final String SETPERSONFORASSESSMENT = Constants.IP + "assessment/setPersonForAssessment";//人员报名接口
    public static final String GETLEAVEPERSONFORASSESSMENT = Constants.IP + "assessment/getLeavePersonForAssessment";//已请假人员列表
    public static final String SETLEAVEPERSONFORASSESSMENT = Constants.IP + "assessment/setLeavePersonForAssessment";//上报请假人员接口
    public static final String GETLEAVEPERSONINFOFORASSESSMENT = Constants.IP + "assessment/getLeavePersonInfoForAssessment";//获取请假人员详细信息
    public static final String GETSUBJECTFORASSESSMENT = Constants.IP + "assessment/getSubjectForAssessment";//成绩录入-项目列表
}