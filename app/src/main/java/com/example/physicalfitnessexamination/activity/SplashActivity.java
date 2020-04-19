package com.example.physicalfitnessexamination.activity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.physicalfitnessexamination.R;
import com.example.physicalfitnessexamination.page.Main2Activity;

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
                    Main2Activity.startInstant(SplashActivity.this);
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
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//                finish();
//            }
//        }, 3000);
    }
}
