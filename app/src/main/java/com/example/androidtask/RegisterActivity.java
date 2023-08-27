package com.example.androidtask;


import android.app.Activity;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidtask.network.RetrofitClient;
import com.example.androidtask.network.service.PhotoService;
import com.example.androidtask.response.BaseResponse;
import com.example.androidtask.response.User;
import com.example.androidtask.util.CodeUtils;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private Boolean bPwdSwitch = false;
    private EditText etPwd;
    private EditText etPwd2;

    private EditText etAccount;
    private EditText etVerify;
    private ImageView ivPwdSwitch, ivPwdSwitch2, ivCode;

    private final String UserName = "^[a-z0-9A-Z]+$";
    private Button btRegister;
    private CodeUtils codeutils;


    private final PhotoService photoService = RetrofitClient.getInstance().getService(PhotoService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);

        etAccount = findViewById(R.id.et_account);
        etPwd = findViewById(R.id.et_pwd);
        etPwd2 = findViewById(R.id.et_pwd2);
        ivPwdSwitch = findViewById(R.id.iv_pwd_switch);
        ivPwdSwitch2 = findViewById(R.id.iv_pwd_switch2);
        etVerify = findViewById(R.id.et_verify);
        btRegister = findViewById(R.id.bt_resgiter);
        ivCode = findViewById(R.id.iv_code);

        codeutils = CodeUtils.getInstance();
        ivCode.setImageBitmap(codeutils.createBitmap());

        btRegister.setOnClickListener(this);
        ivPwdSwitch.setOnClickListener(this);
        ivPwdSwitch2.setOnClickListener(this);
        ivCode.setOnClickListener(this);
        etVerify.setOnFocusChangeListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bt_resgiter) {
            if (etAccount.toString().trim().equals("")) {
                Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Pattern.matches(UserName, etPwd.getText().toString().toLowerCase())) {
                Toast.makeText(this, "密码由字母和数字组成", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!codeutils.getCode().equals(etVerify.getText().toString().toLowerCase())) {
                Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show();
            } else {
                photoService.userRegister(new User(etPwd.getText().toString(), etAccount.getText().toString())).enqueue(new Callback<BaseResponse<Object>>() {
                    @Override
                    public void onResponse(@NonNull Call<BaseResponse<Object>> call, @NonNull Response<BaseResponse<Object>> response) {
                        if (response.body() != null) {
                            if (response.body().getCode() == 200) {
                                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putString(LoginActivity.USER_NAME, etAccount.getText().toString());
                                bundle.putString(LoginActivity.USER_PWD, etPwd.getText().toString());
                                intent.putExtras(bundle);
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            } else if (response.body().getCode() == 500) {
                                Toast.makeText(RegisterActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<BaseResponse<Object>> call, @NonNull Throwable t) {
                        Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
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
        } else if (view.getId() == R.id.iv_code) {
            ivCode.setImageBitmap(codeutils.createBitmap());
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            String con = etPwd.getText().toString();
            String con2 = etPwd2.getText().toString();
            if (!con.equals(con2)) {
                etPwd2.requestFocus();
                Toast.makeText(this, "两次输入密码不相同", Toast.LENGTH_SHORT).show();
            }
        }
    }
}