package com.example.androidtask;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class SplashActivity extends AppCompatActivity {

    private TextView countdownTextView, skipButton;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        countdownTextView = findViewById(R.id.countdown_text);
        skipButton = findViewById(R.id.skip_text);

        long countdownMillis = 4000;
        long intervalMillis = 1000;

        countDownTimer = new CountDownTimer(countdownMillis, intervalMillis) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                countdownTextView.setText(getString(R.string.seconds_remaining, secondsRemaining));
            }

            @Override
            public void onFinish() {
                countdownTextView.setText(getString(R.string.seconds_remaining, 0));
                jumpToMainActivity();
            }
        };

        // 启动倒计时
        countDownTimer.start();

        // 添加点击事件监听器，当用户点击跳过按钮时取消倒计时并执行跳过操作
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 取消倒计时
                countDownTimer.cancel();
                jumpToMainActivity();
            }
        });
    }

    private void jumpToMainActivity() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // 关闭当前 Splash Activity
    }
}