package com.example.physicalfitnessexamination.api;

import com.example.physicalfitnessexamination.Constants;
import com.example.physicalfitnessexamination.api.request.FTRequest;
import com.example.physicalfitnessexamination.api.request.GetAssessmentGroupReq;
import com.example.physicalfitnessexamination.api.request.GetAssessmentObjectReq;
import com.example.physicalfitnessexamination.api.request.GetOrgAssessList4BsymReq;
import com.example.physicalfitnessexamination.api.request.GetOrgCommanderReq;
import com.example.physicalfitnessexamination.api.request.GetOrgForAssessmentReq;
import com.example.physicalfitnessexamination.api.request.GetOrgListReq;
import com.example.physicalfitnessexamination.api.request.GetPersonAssessList4BsymReq;
import com.example.physicalfitnessexamination.api.request.GetRemarkForAssessmentReq;
import com.example.physicalfitnessexamination.api.request.SaveAssessmentReq;
import com.example.physicalfitnessexamination.api.request.UploadFileReq;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.request.base.Request;

import java.io.File;
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
    /**
     * 服务器地址
     */
    public static final String HOST = Constants.IP + "/assessment";//服务器

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
     */
    public static void saveAssessment(Object tag, SaveAssessmentReq request, Callback callback) {
        setRequestAndCallBack(OkGo.post(HOST + "/saveAssessment").tag(tag)
                , request, callback);
    }

    /**
     * 获取机构层级列表
     */
    public static void getOrgList(Object tag, GetOrgListReq request, Callback callback) {
        setRequestAndCallBack(OkGo.post(HOST + "/getOrgList").tag(tag)
                , request, callback);
    }

    /**
     * 单位历次考核第一
     */
    public static void getOrgAssessList4Bsym(Object tag, GetOrgAssessList4BsymReq request, Callback callback) {
        setRequestAndCallBack(OkGo.post(HOST + "/getOrgAssessList4Bsym").tag(tag)
                , request, callback);
    }

    /**
     * 单项成绩记录
     */
    public static void getPersonAssessList4Bsym(Object tag, GetPersonAssessList4BsymReq request, Callback callback) {
        setRequestAndCallBack(OkGo.post(HOST + "/getPersonAssessList4Bsym").tag(tag)
                , request, callback);
    }

    /**
     * 主页公告
     */
    public static void getRemarkForAssessment(Object tag, GetRemarkForAssessmentReq request, Callback callback) {
        setRequestAndCallBack(OkGo.post(HOST + "/getRemarkForAssessment").tag(tag)
                , request, callback);
    }

    /**
     * 文件上传至服务器
     */
    public static void uploadFile(Object tag, UploadFileReq req, List<File> files, Callback callback) {
        OkGo.<String>post(Constants.IMAGE + "/img/uploads")
                .tag(tag)
                .isMultipart(true)
                .params("appName", req.getAppName())
                .params("fireId", req.getFireId())
                .addFileParams("file", files)
                .execute(callback);
    }
}
