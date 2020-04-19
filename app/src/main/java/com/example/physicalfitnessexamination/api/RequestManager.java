package com.example.physicalfitnessexamination.api;

import com.example.physicalfitnessexamination.api.request.FTRequest;
import com.example.physicalfitnessexamination.api.request.GetAssessmentGroupReq;
import com.example.physicalfitnessexamination.api.request.GetAssessmentObjectReq;
import com.example.physicalfitnessexamination.api.request.GetOrgCommanderReq;
import com.example.physicalfitnessexamination.api.request.GetOrgForAssessmentReq;
import com.example.physicalfitnessexamination.api.request.SaveAssessmentReq;
import com.example.physicalfitnessexamination.view.RosterDialogFragment;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.request.base.Request;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 接口统一调用类
 *
 * @author chenzhiyuan
 */
public class RequestManager {
    /*
    服务器地址
     */
    public static final String HOST = "http://114.55.167.62:8080/hnxf/assessment";//服务器

    private static void setRequestAndCallBack(Request hasParamsable, FTRequest request, Callback callback) {
        for (Field field : getFields(request.getClass())) {
            String name = field.getName();
            field.setAccessible(true);
            try {
                Object value = field.get(request);
                if (value != null) {
                    if (value instanceof BigDecimal) {
                        value = ((BigDecimal) value).toPlainString();
                    }
                    hasParamsable.params(name, value.toString());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

//        //头参
//        UserTableBean bean = DbManager.INSTANCE.getDaoSession().getUserTableBeanDao().load(Constant.INSTANCE.getCurrent_UserTableId());
//        if (bean != null) {
//            hasParamsable.headers(TOKEN, bean.getToken());
//        }

        hasParamsable.execute(callback);
    }

    /**
     * 获取class中所有 field
     *
     * @param clazz 类
     * @return field集合
     */
    private static List<Field> getFields(Class clazz) {
        List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
        if (null != clazz.getSuperclass() && clazz.getSuperclass() != Object.class) {
            if (clazz.getSuperclass().getDeclaredFields().length > 0) {
                fields.addAll(getFields(clazz.getSuperclass()));
                return fields;
            }
        }
        return fields;
    }

    /**
     * 获取考核项目列表
     *
     * @param tag      tag
     * @param request  请求体
     * @param callback 回调
     */
    public static void getAssessmentObject(Object tag, GetAssessmentObjectReq request, Callback callback) {
        setRequestAndCallBack(OkGo.post(HOST + "/getAssessmentObject").tag(tag)
                , request, callback);
    }

    /**
     * 获取考核组名称
     *
     * @param tag      tag
     * @param request  请求体
     * @param callback 回调
     */
    public static void getAssessmentGroup(Object tag, GetAssessmentGroupReq request, Callback callback) {
        setRequestAndCallBack(OkGo.post(HOST + "/getAssessmentGroup").tag(tag)
                , request, callback);
    }

    /**
     * 获取人员列表
     *
     * @param tag      tag
     * @param request  请求体
     * @param callback 回调
     */
    public static void getOrgCommander(Object tag, GetOrgCommanderReq request, Callback callback) {
        setRequestAndCallBack(OkGo.post(HOST + "/getOrgCommander").tag(tag)
                , request, callback);
    }

    /**
     * 获取本级机构及下级机构列表
     */
    public static void getOrgForAssessment(Object tag, GetOrgForAssessmentReq request, Callback callback) {
        setRequestAndCallBack(OkGo.post(HOST + "/getOrgForAssessment").tag(tag)
                , request, callback);
    }

    /**
     * 新增考核计划
     *
     * @param tag      tag
     * @param request  请求体
     * @param callback 回调
     */
    public static void saveAssessment(Object tag, SaveAssessmentReq request, Callback callback) {
        setRequestAndCallBack(OkGo.post(HOST + "/saveAssessment").tag(tag)
                , request, callback);
    }
}
