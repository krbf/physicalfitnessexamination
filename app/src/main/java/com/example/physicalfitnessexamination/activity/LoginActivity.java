package com.example.physicalfitnessexamination.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.app.Api;
import com.example.physicalfitnessexamination.bean.UserInfo;
import com.czy.module_common.okhttp.CallBackUtil;
import com.czy.module_common.okhttp.OkhttpUtil;
import com.example.physicalfitnessexamination.page.MainActivity;
import com.czy.module_common.utils.MD5;
import com.czy.module_common.utils.Tool;
import com.example.physicalfitnessexamination.view.dialog.MessageDialog;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_user_name;//用户名
    private EditText et_password;//密码
    private Button btn_login;//登录
    private String mDeviceId;
    private MessageDialog messageDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setSteepStatusBar(true);
        setScreenRoate(true);
        super.onCreate(savedInstanceState);
        setActivityContentView(R.layout.activity_login);
        getToolBar().setTitle("登录页面");
        getToolBar().setLeftVisible(false);
        getToolBar().setRightImgVislble(false);
        messageDialog = MessageDialog.newInstance("登录中，请稍等……");
    }

    /**
     * 跳转方法
     *
     * @param context 上下文
     */
    public static void startInstant(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
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
                    if (!messageDialog.isVisible()) {
                        messageDialog.show(getSupportFragmentManager(), "");
                    }
                }
                break;
            default:
                break;
        }
    }

    public boolean checkLogin() {
        if (Tool.isEmpty(et_user_name.getText().toString())) {
            toast(this, "请填写用户名!");
            return false;
        } else if (Tool.isEmpty(et_password.getText().toString())) {
            toast(this, "请填写密码!");
            return false;
        }
        return true;
    }

    public void login() {
        Map<String, String> map = new HashMap<>();
        map.put("pwd", et_password.getText().toString());
        map.put("pwd_md5", MD5.md5Decode(et_password.getText().toString()));
        map.put("userid", et_user_name.getText().toString());
        map.put("flag","1");
        if (mDeviceId == null) {
            map.put("imei", "");
        } else {
            map.put("imei", mDeviceId);
        }
        Map<String, String> heaherMap = new HashMap<>();
        heaherMap.put("content-type", "application/json");
        OkhttpUtil.okHttpPost(Api.LOGIN, map, heaherMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                if (messageDialog.isVisible()) {
                    messageDialog.dismiss();
                }
                boolean success = JSON.parseObject(response).getBoolean("success");
                if (success) {
                    String result = JSON.parseObject(response).getString("userinfo");
                    UserInfo userInfo = JSON.parseObject(result, UserInfo.class);
                    UserManager.getInstance().saveUserInfo(LoginActivity.this, userInfo);
                    MainActivity.startInstant(LoginActivity.this);
                    finish();
                } else {
                    String msg = JSON.parseObject(response).getString("msg");
                    toast(LoginActivity.this, msg);
                }
            }
        });
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
