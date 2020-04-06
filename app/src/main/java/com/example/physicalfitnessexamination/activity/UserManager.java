package com.example.physicalfitnessexamination.activity;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.physicalfitnessexamination.bean.UserInfo;


public class UserManager {
    private UserInfo userInfo=new UserInfo();
    private SharedPreferences userSharedPreferences;
    private static final String USER_SETTING = "user_setting";

    private UserManager() {

    }

    private static class HolderUserManager {
        private static final UserManager userManager = new UserManager();
    }

    public static UserManager getInstance() {
        return HolderUserManager.userManager;
    }


    public void saveUserInfo(Context context, UserInfo userInfo) {
        userSharedPreferences = context.getSharedPreferences(USER_SETTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSharedPreferences.edit();
        editor.putString("org_name", userInfo.getOrg_name());
        editor.putString("role_id", userInfo.getRole_id());
        editor.putString("department", userInfo.getDepartment());
        editor.putString("userid", userInfo.getUserid());
        editor.putString("city", userInfo.getCity());
        editor.putString("createtime",userInfo.getCreatetime());
        editor.putString("org_dimension",userInfo.getOrg_dimension());
        editor.putString("username",userInfo.getUsername());
        editor.putString("org_longitude",userInfo.getOrg_longitude());
        editor.putString("org_id_zd",userInfo.getOrg_id_zd());
        editor.putString("org_id",userInfo.getOrg_id());
        editor.putString("pwd",userInfo.getPwd());
        editor.putBoolean("isLogin",true);
        editor.commit();
    }

    public UserInfo getUserInfo(Context context) {
        userSharedPreferences = context.getSharedPreferences(USER_SETTING, Context.MODE_PRIVATE);
        userInfo.setOrg_name(userSharedPreferences.getString("org_name",""));
        userInfo.setRole_id(userSharedPreferences.getString("role_id",""));
        userInfo.setDepartment(userSharedPreferences.getString("department",""));
        userInfo.setUserid(userSharedPreferences.getString("userid",""));
        userInfo.setCity(userSharedPreferences.getString("city",""));
        userInfo.setCreatetime(userSharedPreferences.getString("createtime",""));
        userInfo.setOrg_dimension(userSharedPreferences.getString("org_dimension",""));
        userInfo.setUsername(userSharedPreferences.getString("username",""));
        userInfo.setOrg_longitude(userSharedPreferences.getString("org_longitude",""));
        userInfo.setOrg_id_zd(userSharedPreferences.getString("org_id_zd",""));
        userInfo.setOrg_id(userSharedPreferences.getString("org_id",""));
        userInfo.setPwd(userSharedPreferences.getString("pwd",""));
        userInfo.setLogin(userSharedPreferences.getBoolean("isLogin",false));
        return userInfo;
    }

    public void loginOut(Context context){
        userSharedPreferences = context.getSharedPreferences(USER_SETTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSharedPreferences.edit();
        editor.clear();
        editor.commit();

    }
}
