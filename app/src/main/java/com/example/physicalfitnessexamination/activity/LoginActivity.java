package com.example.physicalfitnessexamination.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.app.Api;
import com.example.physicalfitnessexamination.app.Constants;
import com.example.physicalfitnessexamination.bean.UserInfo;
import com.example.physicalfitnessexamination.okhttp.CallBackUtil;
import com.example.physicalfitnessexamination.okhttp.OkhttpUtil;
import com.example.physicalfitnessexamination.util.MD5;
import com.example.physicalfitnessexamination.util.Tool;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_user_name;//用户名
    private EditText et_password;//密码
    private Button btn_login;//登录
    private String mDeviceId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setSteepStatusBar(true);
        setScreenRoate(true);
        super.onCreate(savedInstanceState);
        setActivityContentView(R.layout.activity_login);
        getToolBar().setTitle("登录页面");
        getToolBar().setLeftVisible(false);
        getToolBar().setRightVislble(false);
    }

    @Override
    public void initView() {
        et_user_name = findViewById(R.id.et_user_name);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
    }

    @Override
    public void initBind() {
        btn_login.setOnClickListener(this::onClick);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void initData() {
        setHint("用户名", et_user_name);
        setHint("密码", et_password);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1000);
        } else {
            TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
            mDeviceId = tm.getDeviceId();
        }

    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (checkLogin()) {
                    login();
                }
                break;
            default:
                break;
        }
    }


    public void setHint(String name, EditText editText) {
        // 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString(name);
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(12, true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置hint
        editText.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
    }


    public void login() {
        Map<String, String> map = new HashMap<>();
        map.put("pwd", et_password.getText().toString());
        map.put("pwd_md5", MD5.md5Decode(et_password.getText().toString()));
        map.put("userid", et_user_name.getText().toString());
        map.put("imei", mDeviceId);
        Map<String, String> heaherMap = new HashMap<>();
        heaherMap.put("content-type", "application/json");
        OkhttpUtil.okHttpGet(Api.LOGIN, map, heaherMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    UserInfo userInfo=JSON.parseObject(response,UserInfo.class);
                    UserManager.getInstance().saveUserInfo(LoginActivity.this,userInfo);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    String msg = JSON.parseObject(response).getString("msg");
                    toast(LoginActivity.this, msg);
                }
            }
        });
    }

    public boolean checkLogin() {
        if (Tool.isEmpty(et_user_name.getText().toString())) {
            toast(this, "请填写用户名");
            return false;
        } else if (Tool.isEmpty(et_password.getText().toString())) {
            toast(this, "请填写密码");
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1000:
                if (permissions[0].equals(Manifest.permission.READ_PHONE_STATE)) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        mDeviceId = tm.getDeviceId();
                    }
                }
                break;
            default:
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
