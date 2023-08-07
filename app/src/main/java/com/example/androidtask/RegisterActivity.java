package com.example.androidtask;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private Boolean bPwdSwitch = false;
    private EditText etPwd;
    private EditText etPwd2;

    private EditText  etAccount;
    private EditText etVerify;
    private ImageView ivPwdSwitch;
    private ImageView ivPwdSwitch2;

    private String num = "^1(3[0-9]|5[0-3,5-9]|7[1-3,5-8]|8[0-9])\\d{8}$";
    private Button btRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);

        etAccount  = findViewById(R.id.et_account);
        etPwd = findViewById(R.id.et_pwd);
        etPwd2 = findViewById(R.id.et_pwd2);
        ivPwdSwitch = findViewById(R.id.iv_pwd_switch);
        ivPwdSwitch2 = findViewById(R.id.iv_pwd_switch2);
        etVerify = findViewById(R.id.et_verify);
        btRegister = findViewById(R.id.bt_resgiter);


        btRegister.setOnClickListener(this);
        ivPwdSwitch.setOnClickListener(this);
        ivPwdSwitch2.setOnClickListener(this);
        etVerify.setOnFocusChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bt_resgiter) {
            if(!Pattern.matches(num,etAccount.getText().toString())){
                Toast.makeText(this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(RegisterActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
        } else if (view.getId() == R.id.iv_pwd_switch || view.getId() == R.id.iv_pwd_switch2) {
            int context = String.valueOf(etPwd.getText()).length();
            int context2 = String.valueOf(etPwd2.getText()).length();

            bPwdSwitch = !bPwdSwitch;
            if (bPwdSwitch) {
                ivPwdSwitch.setImageResource(R.drawable.baseline_visibility_24);
                ivPwdSwitch2.setImageResource(R.drawable.baseline_visibility_24);
                etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                etPwd2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                ivPwdSwitch.setImageResource(R.drawable.baseline_visibility_off_24);
                ivPwdSwitch2.setImageResource(R.drawable.baseline_visibility_off_24);
                etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                etPwd2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                etPwd.setTypeface(Typeface.DEFAULT);
                etPwd2.setTypeface(Typeface.DEFAULT);
            }
            etPwd.setSelection(context);
            etPwd2.setSelection(context2);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus){
            String con = etPwd.getText().toString();
            String con2 = etPwd2.getText().toString();
            if (!con.equals(con2)){
                etPwd2.requestFocus();
                Toast.makeText(this, "两次输入密码不相同", Toast.LENGTH_SHORT).show();
            }
        }
    }
}