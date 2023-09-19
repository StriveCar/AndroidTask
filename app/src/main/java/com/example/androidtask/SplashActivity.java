package com.example.androidtask;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class SplashActivity extends AppCompatActivity {

    private TextView countdownTextView, skipButton;
    private CountDownTimer countDownTimer;

    private Handler handler = new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        countdownTextView = findViewById(R.id.countdown_text);
        skipButton = findViewById(R.id.skip_text);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.cyan));
        }

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

        countDownTimer.start();

        skipButton.setOnClickListener(v -> {
            countDownTimer.cancel();
            jumpToMainActivity();
        });
    }

    private void jumpToMainActivity() {
        LoadingDialog.getInstance(this).show();
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
//        handler.postDelayed(this::finish, 500);
    }
}