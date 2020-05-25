package com.example.physicalfitnessexamination.activity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.czy.module_common.okhttp.CallBackUtil;
import com.czy.module_common.okhttp.OkhttpUtil;
import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.app.Api;
import com.example.physicalfitnessexamination.page.MainActivity;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (!this.isTaskRoot()) {
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                finish();
                return;
            }
        }
        unload();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LottieAnimationView lottieAnimationView = new LottieAnimationView(this);
        lottieAnimationView.setAnimation("exercise.json");
        lottieAnimationView.setRepeatCount(3);
        lottieAnimationView.playAnimation();
        addContentView(lottieAnimationView, params);
        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                lottieAnimationView.cancelAnimation();
                if (UserManager.getInstance().getUserInfo(SplashActivity.this).isLogin()) {
//                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    MainActivity.startInstant(SplashActivity.this);
                    finish();
                } else {
                    LoginActivity.startInstant(SplashActivity.this);
                    finish();

                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void unload() {
        Map<String, String> map = new HashMap<>();
        //map.put("org_id", org_id);
        OkhttpUtil.okHttpPost(Api.GETORGSCORESUMLIST4BSYM, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
//                boolean success = JSON.parseObject(response).getBoolean("success");
//                if (success) {
//                    list.clear();
//                    list.addAll(JSON.parseArray(JSON.parseObject(response).getString("data"), AllAroundBean.class));
//                    commonAdapter.notifyDataSetChanged();
//                }
            }
        });
    }
}
